package org.info.rpc;

import java.util.Map;

import org.info.net.AbsSHandler;
import org.info.net.NetU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;

// should be @Sharable?
/**
 * Due to session id, it needs fixing
 *
 */
@Deprecated
public abstract class APIServiceHandler extends AbsSHandler {

	private static final Logger logger = LoggerFactory.getLogger(APIServiceHandler.class);

	@Override
	protected FullHttpMessage handle(FullHttpRequest req, ChannelId id, String domain) {
		HttpHeaders h = req.headers();
		String uri = req.uri();
		logger.debug(uri);
		String path = H.getPath(uri);
		Map<String, String> qs = H.getQS(uri);

		ByteBuf body = req.content();

		EMsg msg = _srv(path, qs, body, null);
		FullHttpMessage ret = msg.retNettyHTTP();

		HttpUtil.setContentLength(ret, body.readableBytes());

		return ret;
	}// ()

	/**
	 * Serve. Either request or return could be crypto, zip and cbor or JSON.
	 */
	public abstract EMsg _srv(String path, Map<String, String> qs, ByteBuf body, Map sheaders);

	/*
	@Override
	public void channelRead(final ChannelHandlerContext pctx, final Object msg) {
		String domain;

		if (msg instanceof FullHttpRequest) {
			final FullHttpRequest req = (FullHttpRequest) msg;

			domain = NetU.getHost(req);
			logger.debug(domain);

			try {
				FullHttpMessage resp = handle(req, domain);

				resp.headers().add("Connection", "close");

				ChannelFuture f = pctx.writeAndFlush(resp); // to browsers
				//
				 // if (!HttpUtil.isKeepAlive(req) || !HttpUtil.isKeepAlive(resp)) {
				 // NetU.chClose(pctx.channel()); }
				 //
				while (resp.refCnt() > 0)
					resp.release(resp.refCnt());

			} catch (Throwable e) {
				logger.warn("", e);
				NetU.chClose(pctx.channel());
			}
		} else {
			logger.error("no FullHttpRequest msg");
			FullHttpMessage resp = NetU.makeEMsg("not request", HttpResponseStatus.BAD_REQUEST);
			ChannelFuture f = pctx.writeAndFlush(resp);
			try {
				// f.sync();
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
			while (resp.refCnt() > 0)
				resp.release(resp.refCnt());
			NetU.chClose(pctx.channel());
		} // if else

	}// ()
	
	*/
}
