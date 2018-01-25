package org.apache.chain.srv;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

/**
 * Interface COR N(netty) Command. Instances are shared, there is not a new one
 * pre connection or per request, so properties must be thread safe.
 */
public interface ICmd extends Command {

	/**
	 * return false to continue
	 */
	boolean exec(CCtx ctx) throws Exception;

	/**
	 * return false to continue. Don't change this method
	 */
	@Override
	public default boolean execute(Context ctx) throws Exception {
		CCtx nctx = (CCtx) ctx;
		return exec(nctx);
	}

	public default String getName() {
		return this.getClass().toString();
	}

}