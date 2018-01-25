package org.apache.chain.srv;

import org.info.net.DefaultPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Plugs into netty
 *
 */
public class ChainPipe extends DefaultPipe {

	private final static Logger logger = LoggerFactory.getLogger(ChainPipe.class);

	static AbsNRouter _ch;

	public ChainPipe(String router) throws Throwable {

		_ch = _instPath(router);
		handler(_ch);
	}// ()

	public static AbsNRouter _instPath(String path) throws Throwable {
		Class<?> clazz = Class.forName(path.trim());
		AbsNRouter inst = (AbsNRouter) clazz.newInstance();
		logger.info(inst.getClass().getName());
		return inst;
	}
}// class
