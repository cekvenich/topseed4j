package netT;

import org.info.net.AbsSHandler;
import org.info.net.NetU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

@Sharable
public class MySHandler extends AbsSHandler {

	private static final Logger logger = LoggerFactory.getLogger(MySHandler.class);

	@Override
	protected FullHttpMessage handle(FullHttpRequest req, ChannelId id, String domain) {
		logger.info(req.uri());
		DefaultFullHttpResponse resp = null;

		ByteBuf body = NetU.stringToBy("some body");

		resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
		return resp;
	}

}
