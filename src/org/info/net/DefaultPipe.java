package org.info.net;

import org.info.util.Confd;
import org.info.util.U;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class DefaultPipe extends ChannelInitializer<SocketChannel> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultPipe.class);

	static Confd P = Confd.INSTANCE;

	static {
		logger.info("please wait");
	}

	protected AbsSHandler _hand;
	private int XOBJ_SIZE = 1024 * 200;// 200 kB per request as default max. Should be smaller

	public DefaultPipe(AbsSHandler proH) {
		_hand = proH;
	}

	/**
	 * You init to set handler
	 */
	public DefaultPipe() {

	}

	/**
	 * Set the netty handler
	 */
	public void handler(AbsSHandler proH) {
		_hand = proH;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		logger.debug("con");
		// @Sharable
		HttpObjectAggregator agg = new HttpObjectAggregator(XOBJ_SIZE, true);
		ChannelPipeline p = ch.pipeline();

		// new LoggingHandler(LogLevel.INFO);

		p.addLast(new HttpRequestDecoder());
		p.addLast(agg);
		p.addLast(new HttpResponseEncoder());
		p.addLast(new HttpContentCompressor());

		p.addLast(_hand);

	}// ()

	/**
	 * You need to specify the the threads group.
	 */
	public DefaultPipe srvBoot(int port, EventLoopGroup elg) throws Throwable {
		DefaultPipe pipeline = this;

		ServerBootstrap b = new ServerBootstrap();
		b.group(elg);

		b.option(ChannelOption.SO_BACKLOG, 2 * 1000).childOption(ChannelOption.SO_KEEPALIVE, false)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3 * 1000);

		b.childHandler(pipeline);
		b.channel(NioServerSocketChannel.class);

		Channel ch = b.bind(port).sync().channel();
		logger.info(U.getLocalHost() + " " + port);

		logger.info("started");
		ch.closeFuture().sync();
		return this;
	}// ()
}
