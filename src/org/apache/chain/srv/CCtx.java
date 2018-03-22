package org.apache.chain.srv;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.commons.chain.impl.ContextBase;
import org.info.rpc.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * A map to communicate to the chain. Chain Context. Has req/resp. Else 'put'
 * and 'get'.
 */
public class CCtx extends ContextBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContextBase.class);

	private static final long serialVersionUID = 1L;
	public int authRoleLevel;
	public String session;
	public boolean cache;
	protected FullHttpRequest _req;
	protected FullHttpResponse _resp;

	public CCtx(FullHttpRequest req) {
		_req = req;
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
}
