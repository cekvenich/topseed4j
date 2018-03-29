package org.apache.chain.srv;

import org.info.net.DefaultPipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plugs into netty
 */
public class ChainPipe extends DefaultPipe {

	private final static Logger logger = LoggerFactory.getLogger(ChainPipe.class);

	static AbsChainRouter _ch;

	public ChainPipe(AbsChainRouter router) throws Throwable {

		_ch = router;
		handler(_ch);
	}// ()

}// class
