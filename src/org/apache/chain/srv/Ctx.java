package org.apache.chain.srv;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.chain.impl.ContextBase;
import org.info.rpc.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * A map to communicate to the chain. Chain Context. Has req/resp. Else 'put'
 * and 'get'.
 */
public class Ctx extends ContextBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContextBase.class);

	private static final long serialVersionUID = 1L;
	public int authRoleLevel;
	public String session;
	public boolean cache;
	protected FullHttpRequest _req;
	protected FullHttpResponse _resp;
	public ChannelId id;
	public String mappedPath;
	

	public Ctx(FullHttpRequest req, ChannelId id_) {
		_req = req;
		id = id_;
	}

	public URI URI() throws URISyntaxException {
		URI uri = new URI(httpRequest().uri());
		return uri;
	}

	public Map getQS() {
		try {
			Map qs = H.getQS(URI().toString());
			return qs;
		} catch (URISyntaxException e) {
			LOGGER.warn(e.getMessage());
			return new HashMap();
		}
	}

	public FullHttpRequest httpRequest() {
		return _req;
	}

	public void httpResponse(FullHttpResponse resp) {
		_resp = resp;
	}

	public FullHttpResponse httpResponse() {
		return _resp;
	}
	
	/**
	 * Set response type
	 */
	public void setAsJson() {
		_resp.headers().set(HttpHeaderNames.CONTENT_TYPE,"application/json");
	}
	
	
	public String getPath() {
		String uri = _req.uri();
		String path = H.getPath(uri);
		return path;
	}

	public String mappedPath() {
		return mappedPath;
	}

	public void mappedPath(String mappedPath) {
		this.mappedPath = mappedPath;
	}


}
