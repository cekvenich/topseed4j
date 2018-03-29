package org.apache.chain.res;

import org.apache.chain.BasicChainFac;
import org.apache.chain.DefaultChainRouter;
import org.apache.chain.srv.Ctx;
import org.apache.chain.srv.ICmd;
import org.info.net.NetU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Able to handle chain | cmd plugins
 */
@Sharable
public class ResourceChainRouter extends DefaultChainRouter {

	private final static Logger logger = LoggerFactory.getLogger(ResourceChainRouter.class);

	/**
	 * @param cmdRoot
	 *            Where/what folder are commands
	 * @param preCmd
	 *            What is the first command/filter
	 */
	public ResourceChainRouter(String cmdRoot, ICmd preCmd) {
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
	
	protected boolean isStaticResource(Ctx ctx, String domain) {
	
		String path = ctx.getPath();
		return path.indexOf('.') > -1;
	}
	
	
	protected FullHttpMessage resource(FullHttpRequest req, String path) throws Exception {
		FullHttpMessage resp = NetU.makeEMsg("to do", HttpResponseStatus.BAD_REQUEST);
		return resp;
	}

}
