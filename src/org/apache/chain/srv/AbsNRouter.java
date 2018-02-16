package org.apache.chain.srv;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.chain.BasicChainFac;
import org.info.net.AbsSHandler;
import org.info.net.NetU;
import org.info.rpc.H;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 *
 * Able to handle chain | cmd plugins
 *
 */
@Sharable
public abstract class AbsNRouter extends AbsSHandler {

	private final static Logger logger = LoggerFactory.getLogger(AbsNRouter.class);

	protected String _cmdRoot;
	protected ICmd _preCmd;

	public AbsNRouter(String cmdRoot, ICmd preCmd) {
		_cmdRoot = cmdRoot;
		_preCmd = preCmd;
	}

	/**
	 * Stores each route
	 */
	protected Map<String, BasicChainFac> _chainRoutes = new ConcurrentHashMap();

	protected String INDEX = "index";

	/**
	 * Netty comes here
	 */
	@Override
	protected FullHttpMessage handle(FullHttpRequest req, String domain) {
		CCtx ctx = new CCtx(req);

		HttpHeaders h = req.headers();
		String uri = req.uri();
		String path = H.getPath(uri);

		if (path.indexOf('.') > -1) {// static resource, no a command
			return resource(req, path);
		}

		path = path.substring(1);
		if (path.length() < 1)
			path = INDEX;

		path = path.substring(0, 1).toUpperCase() + path.substring(1);
		logger.info(path);

		if (!_chainRoutes.containsKey(path)) {
			try {
				addNewChain(path);
			} catch (Throwable e) {
				logger.warn("nroute", e.getMessage() + path);
				FullHttpMessage resp = NetU.makeEMsg("path not found " + e.getMessage(), HttpResponseStatus.BAD_REQUEST);
				return resp;
			}
		}
		BasicChainFac chain = _chainRoutes.get(path);
		chain.execute(ctx);

		return ctx.httpResponse();
	}

	protected abstract FullHttpMessage resource(FullHttpRequest req, String path);

	/**
	 *
	 * If you need a different factory, override this.
	 */
	protected abstract void addNewChain(String path) throws Throwable;

	public static ByteBuf toStringBody(String path) throws Throwable {
		String str = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
		return NetU.stringToBy(str);
	}

	public static ByteBuf toByteArrayBody(String path) throws Throwable {
		byte[] ba = Files.readAllBytes(Paths.get(path));
		return Unpooled.wrappedBuffer(ba);
	}
}
