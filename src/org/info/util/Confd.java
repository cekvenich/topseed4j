package org.info.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * Confd.INSTANCE.method();
 */
public enum Confd implements IDaemon {

	INSTANCE;

	private final Logger logger = LoggerFactory.getLogger(Confd.class);

	private final InternalLogger _nlog = InternalLoggerFactory.getInstance(Confd.class);

	ScheduledExecutorService _esd = Confd.esLow(4);

	protected static int TIME = 5; // seconds to reload
	protected static String FILE = "conf";

	public static final String DIR = System.getProperty("user.dir");

	protected static Map<String, String> _conf = new HashMap();

	@Override
	public void _start() {
		try {
			run();
			logger.info("i", getConf());
			logger.info(Confd.class.getPackage().getImplementationVersion());

			// setDaemon
			_esd.scheduleWithFixedDelay(this, TIME, TIME, TimeUnit.SECONDS);
		} catch (Throwable e) {
			logger.error("exited", e);
			System.exit(0);
		}
	}

	@Override
	public void run() {
		try {
			reloadConf();
			also();
		} catch (Throwable e) {
			logger.error("error", e);
		}
	}

	/**
	 * Extend and *also* on an interval
	 */
	protected void also() {

	}

	protected void reloadConf() throws Throwable {
		InputStream input = new FileInputStream(DIR + "/" + FILE + ".props");

		Map props = new Properties();
		((Properties) props).load(input);
		input.close();
		_conf = props; // atomic
	}

	public Map<String, String> getConf() throws Throwable {
		return _conf;
	}

	/**
	 * No noise
	 */
	public String getString(String key) {
		String s = null;
		try {
			s = getConf().get(key);
		} catch (Throwable e) {
		}

		if (s != null)
			return s.trim();
		else
			return null;
	}

	public String getConf(String key) throws Throwable {
		String s = getConf().get(key);
		if (null == s)
			throw new Throwable("not found " + key);
		return s.trim();
	}

	public int getConfI(String key) throws Throwable {
		String pro = getConf(key);
		return Integer.parseInt(pro);
	}

	public String getConf(String key, int n) throws Throwable {
		key = key + UNDERLINE + n;
		return getConf(key);
	}

	public static final char UNDERLINE = '_';

	public int getConfI(String key, int n) throws Throwable {
		key = key + UNDERLINE + n;
		Integer i = getConfI(key);
		return i;
	}

	public String getConf(String key, String n) throws Throwable {
		return getConf(key + UNDERLINE + n);
	}

	public int getConfI(String key, String n) throws Throwable {
		key = key + UNDERLINE + n;
		Integer i = getConfI(key);
		return i;
	}

	public List<String> getConfList(String key) throws Throwable {
		String[] csv = getConf().get(key).split(",");
		List<String> list = new ArrayList<>(Arrays.asList(csv));
		if (list == null || list.size() < 1)
			throw new Throwable("not found " + key);
		return list;
	}

	public boolean getConfBool(String key) throws Throwable {
		String pro = getConf(key);
		return Boolean.parseBoolean(pro);
	}

	public String getPID() {
		String pid = ManagementFactory.getRuntimeMXBean().getName();
		return pid;
	}

	protected static String _localHost;

	public static synchronized String getLocalHost() {
		if (_localHost != null)
			return _localHost;

		String h = null;
		try {
			final DatagramSocket socket = new DatagramSocket();

			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			h = socket.getLocalAddress().getHostAddress();
			socket.close();
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
		_localHost = h;
		return _localHost;
	}

	public static Runtime RT = Runtime.getRuntime();

	/**
	 * Force GC
	 *
	 * @return
	 */
	public synchronized long fgc() {
		long ramB = RT.totalMemory() - RT.freeMemory();

		Object obj = new Object();
		WeakReference<Object> wref = new WeakReference<>(obj);
		obj = null;
		while (wref.get() != null) {
			try {
				Thread.sleep(0, 1);
			} catch (InterruptedException e) {
			}
			System.gc();
		}
		long ramA = RT.totalMemory() - RT.freeMemory();

		return ramB - ramA;
	}

	/**
	 * Make a low priority thread
	 */
	public static Thread low(Runnable r, int pri) {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		// t.setDaemon(true);
		t.setPriority(pri);
		return t;
	}

	protected static ThreadFactory _lowThreads(int pri) {
		return new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return low(r, pri);
			}
		};
	}

	/**
	 * java daemon threads
	 *
	 * @return
	 */
	public static ScheduledExecutorService esLow(int pri) {
		return Executors.newSingleThreadScheduledExecutor(_lowThreads(pri));
	}

	static {
		INSTANCE._start();
	}

}