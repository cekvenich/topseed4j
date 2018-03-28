package org.apache.chain;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.chain.srv.AbsNRouter;
import org.apache.chain.srv.ICmd;
import org.info.net.NetU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Able to handle chain | cmd plugins
 */
@Sharable
public class DefaultChainRouter extends AbsNRouter {

	private final static Logger logger = LoggerFactory.getLogger(DefaultChainRouter.class);

	/**
	 * @param cmdRoot Where/what folder are commands
	 * @param preCmd  What is the first command/filter
	 */
	public DefaultChainRouter(String cmdRoot, ICmd preCmd) {
		super(cmdRoot, preCmd);
	}

	/**
	 * If you need a different factory, override this.
	 */
	@Override
	protected void addNewChain(String path) throws Throwable {
		BasicChainFac chain = new BasicChainFac(_preCmd, _cmdRoot, path);
		_chainRoutes.put(path, chain);
	}

	/**
	 * Handle resource requests
	 */
	@Override
	protected FullHttpMessage resource(FullHttpRequest req, String path) throws Exception {
		FullHttpMessage resp = NetU.makeEMsg("to do", HttpResponseStatus.BAD_REQUEST);
		return resp;
	}
}
