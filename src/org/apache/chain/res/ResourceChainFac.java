package org.apache.chain.res;

import org.apache.chain.BasicChainFac;
import org.apache.chain.srv.ICmd;

/**
 * Loads command from classpath and makes a chain
 */
public class ResourceChainFac extends BasicChainFac {

	public ResourceChainFac(ICmd preCmd, String root, String path2) throws Throwable {
		super(preCmd, root, path2);
	}
	
}
