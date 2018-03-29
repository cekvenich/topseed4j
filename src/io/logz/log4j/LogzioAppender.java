package io.logz.log4j;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.info.util.Confd;
import org.info.util.U;

import com.google.common.base.Splitter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.logz.sender.LogzioSender;
import io.logz.sender.SenderStatusReporter;
import io.logz.sender.com.google.gson.JsonObject;
import io.logz.sender.exceptions.LogzioParameterErrorException;

/**
 * @author MarinaRazumovsky
 */

public class LogzioAppender extends AppenderSkeleton {
	private static final String TIMESTAMP = "@timestamp";
	private static final String LOGLEVEL = "loglevel";
	private static final String MESSAGE = "message";
	private static final String LOGGER = "logger";
	private static final String THREAD = "thread";
	private static final String EXCEPTION = "exception";
	private static final Set<String> reservedFields = new HashSet<>(
			Arrays.asList(new String[] { TIMESTAMP, LOGLEVEL, MESSAGE, LOGGER, THREAD, EXCEPTION }));
	static Confd P = Confd.INSTANCE;

	static {/// XXX

		System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		System.setProperty("NAME", U.getLocalHost());

		System.out.println("remote NS");

	}

	private ScheduledExecutorService scheduledExecutorService;
	private LogzioSender logzioSender;

	private String logzioToken;
	private String logzioType = "java";
	private int drainTimeoutSec = 3;
	private int fileSystemFullPercentThreshold = 98;
	private String bufferDir;
	private String logzioUrl;
	private int connectTimeout = 1 * 1000;
	private int socketTimeout = 1 * 1000;
	private boolean debug = false;
	private boolean addHostname = true;
	private int gcPersistedQueueFilesIntervalSeconds = 3;

	private Map<String, String> additionalFieldsMap = new HashMap<>();

	@Override
	public void activateOptions() {
		if (logzioToken == null) {
			LogLog.error("Logz.io Token is missing! Bailing out..");
			return;
		}
		if (!(fileSystemFullPercentThreshold >= 1 && fileSystemFullPercentThreshold <= 100)) {
			if (fileSystemFullPercentThreshold != -1) {
				LogLog.error("fileSystemFullPercentThreshold should be a number between 1 and 100, or -1");
				return;
			}
		}

		if (addHostname) {

			String hostname = U.getLocalHost(); // XXX
			additionalFieldsMap.put("hostname", hostname);
		}

		if (bufferDir != null) {
			File bufferFile = new File(bufferDir);
			if (bufferFile.exists()) {
				if (!bufferFile.canWrite()) {
					LogLog.error("We cant write to your bufferDir location: " + bufferFile.getAbsolutePath());
					return;
				}
			} else {
				if (!bufferFile.mkdirs()) {
					LogLog.error("We cant create your bufferDir location: " + bufferFile.getAbsolutePath());
					return;
				}
			}
		} else {
			bufferDir = System.getProperty("java.io.tmpdir") + File.separator + logzioType;
		}
		File bufferDirFile = new File(bufferDir, logzioType);

		try {
			scheduledExecutorService = Executors.newScheduledThreadPool(2,
					new ThreadFactoryBuilder().setDaemon(true).build());
			logzioSender = LogzioSender.getOrCreateSenderByType(logzioToken, logzioType, drainTimeoutSec,
					fileSystemFullPercentThreshold, bufferDirFile, logzioUrl, socketTimeout, connectTimeout, debug,
					new StatusReporter(), scheduledExecutorService, gcPersistedQueueFilesIntervalSeconds);
			logzioSender.start();
		} catch (LogzioParameterErrorException e) {
			LogLog.error("Some of the configuration parameters of logz.io is wrong: " + e.getMessage(), e);
			return;
		}
	}

	@Override
	protected void append(LoggingEvent event) {
		if (!event.getLoggerName().contains("io.logz.sender")) {
			logzioSender.send(formatMessageAsJson(event));
		}
	}

	private JsonObject formatMessageAsJson(LoggingEvent event) {
		JsonObject logMessage = new JsonObject();
		// Adding MDC first, as I dont want it to collide with any one of the following
		// fields
		Map properties = event.getProperties();
		if (!properties.isEmpty()) {
			properties.forEach((key, value) -> logMessage.addProperty(key.toString(), value.toString()));
		}

		logMessage.addProperty(TIMESTAMP, new Date(event.getTimeStamp()).toInstant().toString());
		logMessage.addProperty(LOGLEVEL, event.getLevel().toString());

		logMessage.addProperty(MESSAGE, event.getRenderedMessage());
		logMessage.addProperty(LOGGER, event.getLoggerName());
		logMessage.addProperty(THREAD, event.getThreadName());
		if (event.getThrowableInformation() != null) {
			ThrowableInformation throwable = event.getThrowableInformation();
			StringBuilder stringBulder = new StringBuilder();
			Arrays.asList(throwable.getThrowableStrRep()).forEach(ti -> stringBulder.append(ti).append("\n\t"));
			logMessage.addProperty(EXCEPTION, stringBulder.toString());
		}
		if (additionalFieldsMap != null) {
			additionalFieldsMap.forEach(logMessage::addProperty);
		}
		return logMessage;
	}

	@Override
	public void close() {
		if (logzioSender != null)
			logzioSender.stop();
		if (scheduledExecutorService != null)
			scheduledExecutorService.shutdownNow();

	}

	private String getValueFromSystemEnvironmentIfNeeded(String value) {
		if (value == null)
			return value;

		if (value.startsWith("$")) {

			String v = System.getProperty(value.replace("$", ""));
			// XXX return System.getenv(value.replace("$", ""));

		}
		return value;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	public void setLogzioToken(String logzioToken) {
		if (logzioToken != null) {
			logzioToken = getValueFromSystemEnvironmentIfNeeded(logzioToken);
		}
		this.logzioToken = logzioToken;
	}

	public void setLogzioType(String logzioType) {
		this.logzioType = logzioType;
	}

	public void setDrainTimeoutSec(int drainTimeoutSec) {
		this.drainTimeoutSec = drainTimeoutSec;
	}

	public void setFileSystemFullPercentThreshold(int fileSystemFullPercentThreshold) {
		this.fileSystemFullPercentThreshold = fileSystemFullPercentThreshold;
	}

	public void setBufferDir(String bufferDir) {
		this.bufferDir = bufferDir;
	}

	public void setLogzioUrl(String logzioUrl) {
		if (logzioUrl != null) {
			logzioUrl = getValueFromSystemEnvironmentIfNeeded(logzioUrl);
		}
		this.logzioUrl = logzioUrl;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setAddHostname(boolean addHostname) {
		this.addHostname = addHostname;
	}

	public void setAdditionalFields(String additionalFields) {
		if (additionalFields != null) {
			Splitter.on(';').omitEmptyStrings().withKeyValueSeparator('=').split(additionalFields).forEach((k, v) -> {
				if (reservedFields.contains(k)) {
					LogLog.warn("The field name '" + k
							+ "' defined in additionalFields configuration can't be used since it's a reserved field name. This field will not be added to the outgoing log messages");
				} else {
					String value = getValueFromSystemEnvironmentIfNeeded(v);
					if (value != null) {
						additionalFieldsMap.put(k, value);
					}
				}
			});
		}
	}

	public void setGcPersistedQueueFilesIntervalSeconds(int gcPersistedQueueFilesIntervalSeconds) {
		this.gcPersistedQueueFilesIntervalSeconds = gcPersistedQueueFilesIntervalSeconds;
	}

	class StatusReporter implements SenderStatusReporter {

		@Override
		public void error(String msg) {
			LogLog.error(msg);
		}

		@Override
		public void error(String msg, Throwable e) {
			LogLog.error(msg, e);
		}

		@Override
		public void warning(String msg) {
			LogLog.warn(msg);
		}

		@Override
		public void warning(String msg, Throwable e) {
			LogLog.warn(msg, e);
		}

		@Override
		public void info(String msg) {
			LogLog.debug(msg);
		}

		@Override
		public void info(String msg, Throwable e) {
			LogLog.debug(msg, e);
		}
	}
}
