package org.apache.chain;

import org.apache.chain.srv.AbsChainRouter;
import org.apache.chain.srv.ICmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;

/**
 * Able to handle chain | cmd plugins
 */
@Sharable
public class DefaultChainRouter extends AbsChainRouter {

	private final static Logger logger = LoggerFactory.getLogger(DefaultChainRouter.class);

	/**
	 * @param cmdRoot
	 *            Where/what folder are commands
	 * @param preCmd
	 *            What is the first command/filter
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

}
