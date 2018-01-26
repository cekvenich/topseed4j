package rpc;

import java.io.IOException;
import java.util.Map;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpRequestHandler;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.info.rpc.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SrvHand implements HttpRequestHandler {

	private static final Logger logger = LoggerFactory.getLogger(SrvHand.class);

	@Override
	public void handle(ClassicHttpRequest req, ClassicHttpResponse resp, HttpContext ctx)
			throws HttpException, IOException {
		String uri = req.getRequestUri();
		String path = H.getPath(uri);
		Map<String, String> qs = H.getQS(uri);

		logger.info("", qs);
		logger.info(path);

		resp.setCode(HttpStatus.SC_OK);

	}

}
