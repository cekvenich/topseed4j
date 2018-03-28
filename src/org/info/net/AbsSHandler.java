package org.info.net;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;

import org.apache.chain.srv.Ctx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//should be @Sharable ?
public abstract class AbsSHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AbsSHandler.class);

	protected abstract FullHttpMessage handle(FullHttpRequest req, String domain) throws Exception;
	
	protected abstract boolean isStaticResource(Ctx ctx, String domain);

	@Override
	public void channelRead(final ChannelHandlerContext pctx, final Object msg) {
		String domain;

		if (msg instanceof FullHttpRequest) {
			final FullHttpRequest req = (FullHttpRequest) msg;

			domain = NetU.getHost(req);
			logger.debug(domain);

			try {
				FullHttpMessage resp = handle(req, domain);

				// resp.headers().remove("Connection");
				// resp.headers().add("Connection", "keep-alive");

				ChannelFuture f = pctx.writeAndFlush(resp); // to browsers
				// f.sync();
				if (!HttpUtil.isKeepAlive(req) || !HttpUtil.isKeepAlive(resp)) {
					NetU.chClose(pctx.channel());
				}

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

	@Override
	public void channelInactive(ChannelHandlerContext pctx) {
		logger.debug(pctx.name());
		NetU.chClose(pctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext pctx, Throwable e) {
		logger.warn("", e);
		NetU.chClose(pctx);
	}// ()

}
