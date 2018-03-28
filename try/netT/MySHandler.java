package netT;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.*;

import org.apache.chain.srv.Ctx;
import org.info.net.AbsSHandler;
import org.info.net.NetU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class MySHandler extends AbsSHandler {

	private static final Logger logger = LoggerFactory.getLogger(MySHandler.class);

	@Override
	protected FullHttpMessage handle(FullHttpRequest req, String domain) {
		logger.info(req.uri());
		DefaultFullHttpResponse resp = null;

		ByteBuf body = NetU.stringToBy("some body");

		resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
		return resp;
	}

	@Override
	protected boolean isStaticResource(Ctx ctx, String domain) {
		return false;
	}

}
