package org.info.net;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import javolution.util.FastSet;

//should be @Sharable ?
public abstract class AbsSHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AbsSHandler.class);

	protected abstract FullHttpMessage handle(FullHttpRequest req, ChannelId id, String domain) throws Exception;

	protected static Set<ChannelId> _channels = new FastSet();

	
	@Override
	public void channelActive(ChannelHandlerContext pctx) throws Exception {
		//logger.info("" + _channels.size());
	}

	@Override
	public void channelInactive(ChannelHandlerContext pctx) {
		ChannelId id = pctx.channel().id();
		logger.debug("end session -" + id);
		_channels.remove(id);
		try {
			super.channelInactive(pctx);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	
	@Override
	public void channelRead(final ChannelHandlerContext pctx, final Object msg) {
		String domain;

		if (msg instanceof FullHttpRequest) {
			final FullHttpRequest req = (FullHttpRequest) msg;

			domain = NetU.getHost(req);
			logger.debug(domain);

			
			// sess
			ChannelId id = pctx.channel().id();
			if (!_channels.contains(id)) {
				_channels.add(id);
				logger.debug("new session +" + id);
			} // fi
			
			
			try {
				FullHttpMessage resp = handle(req, id, domain);

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
	public void exceptionCaught(ChannelHandlerContext pctx, Throwable e) {
		logger.warn("", e);
		NetU.chClose(pctx);
	}// ()

}
