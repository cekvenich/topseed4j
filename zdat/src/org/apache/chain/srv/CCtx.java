package org.apache.chain.srv;

import org.apache.commons.chain.impl.ContextBase;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 *
 * A map to communicate to the chain. Chain Context. Has req/resp. Else 'put'
 * and 'get'.
 *
 */
public class CCtx extends ContextBase {

	private static final long serialVersionUID = 1L;

	protected FullHttpRequest _req;

	protected FullHttpResponse _resp;

	public CCtx(FullHttpRequest req) {
		_req = req;
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
