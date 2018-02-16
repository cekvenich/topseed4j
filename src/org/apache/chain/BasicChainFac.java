package org.apache.chain;

import org.apache.chain.srv.CCtx;
import org.apache.chain.srv.ICmd;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.info.util.Confd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Loads command from classpath and makes a chain
 *
 */
public class BasicChainFac extends ChainBase {
	static Confd P = Confd.INSTANCE;

	private final static Logger logger = LoggerFactory.getLogger(BasicChainFac.class);

	public static ICmd _instCmd(String path, String path2) throws Throwable {
		Class<?> clazz = Class.forName(path.trim() + "." + path2.trim());
		ICmd inst = (ICmd) clazz.newInstance();
		logger.info(inst.getName());
		return inst;
	}

	public static final String CMD = "Cmd";

	/**
	 * Makes a new chain.
	 */
	public BasicChainFac(ICmd preCmd, String root, String path2) throws Throwable {
		ICmd cmd = _instCmd(root, path2 + CMD);
		this.addCommand(preCmd);
		this.addCommand(cmd);
	}

	/**
	 * Start execution of chain (takes over from netty)
	 */
	@Override
	public boolean execute(Context ctx) {

		CCtx nctx = (CCtx) ctx;
		try {
			return super.execute(nctx);
		} catch (Exception e) {
			logger.warn("cmd", e);
			return true;
		}
	}

}
