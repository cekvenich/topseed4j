package org.info.net;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class NetU {

	// public static int TIMEOUT_S = 5 * 60;

	// public static EventLoopGroup MAIN_EVENTGROUP = new NioEventLoopGroup(800);

	public static final String HOST = "Host";

	public static FullHttpMessage makeEMsg(String msg, HttpResponseStatus code) {
		ByteBuf content = stringToBy(msg);
		DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code, content);

		HttpUtil.setKeepAlive(resp, false);
		return resp;
	}

	public static FullHttpMessage makeEMsg(ByteBuf content, HttpResponseStatus code) {
		DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code, content);
		HttpUtil.setKeepAlive(resp, false);
		return resp;
	}

	public static ByteBuf stringToBy(String str) {
		byte[] ba = str.getBytes(StandardCharsets.UTF_8);
		return Unpooled.wrappedBuffer(ba);
	}// ()

	public static String byToString(ByteBuf ba) {
		return ba.toString(StandardCharsets.UTF_8);
	}

	public static void chClose(Channel ch) {
		if (ch.isActive()) {
			ChannelFuture f = ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		} // fi
	}// ()

	public static void chClose(ChannelHandlerContext pch) {
		Channel ch = pch.channel();
		chClose(ch);
	}// ()

	public static String getHost(HttpRequest req) {
		HttpHeaders headers = req.headers();
		return headers.get(HOST);
	}

}// class
