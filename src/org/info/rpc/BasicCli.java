package org.info.rpc;

import java.util.Map;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consider singleton manager
 */
public class BasicCli {

	protected static final String HTTP = "http://";
	private static final Logger logger = LoggerFactory.getLogger(BasicCli.class);
	// Create a connection manager with custom configuration.
	protected PoolingHttpClientConnectionManager _connManager = new PoolingHttpClientConnectionManager();
	// Create socket configuration
	protected SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
	protected RequestConfig _requestConfig = RequestConfig.custom().setConnectionRequestTimeout(Timeout.ofSeconds(1))
			.build();
	protected String _ep;

	public BasicCli(String ep, int port) {
		_ep = HTTP + ep + ":" + port;
		logger.info(_ep);
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		_connManager.setDefaultSocketConfig(socketConfig);

		HttpHost host = new HttpHost(ep, port);

		_connManager.setMaxPerRoute(new HttpRoute(host), 20);

		_connManager.setMaxTotal(40);

	}

	public EMsg call(String path, Map<String, Object> qs, HttpEntity body) throws Throwable {
		String requestUri = path + H.mapToQs(qs);
		return _call(requestUri, body);
	}

	public EMsg _call(String requestUri, HttpEntity requestBod) throws Throwable {
		logger.debug(requestUri);

		requestUri = _ep + requestUri;
		EMsg msg = new EMsg();

		// Create an HttpClient with the given custom dependencies and configuration.
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(_connManager)
				.setDefaultRequestConfig(_requestConfig).build();

		HttpPost httppost = new HttpPost(requestUri);
		httppost.setEntity(requestBod);

		HttpClientContext context = HttpClientContext.create();

		logger.debug("", httppost.getUri());
		CloseableHttpResponse response = httpclient.execute(httppost);

		int sc = response.getCode();
		if (sc == 200) {
			HttpEntity ent = response.getEntity();
			msg.ba(EntityUtils.toByteArray(ent));
			EntityUtils.consumeQuietly(ent);
		} else {
			HttpEntity ent = response.getEntity();
			msg.error(EntityUtils.toByteArray(ent));
			EntityUtils.consumeQuietly(ent);
		}
		response.close();
		// httpclient.close();

		return msg;
	}// ()

}
