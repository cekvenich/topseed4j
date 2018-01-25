package org.apache.da;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class DefaultPoolConfig extends GenericObjectPoolConfig {

	/**
	 * The lower the max, the faster it will be.
	 */
	public DefaultPoolConfig(int max) {
		setMaxTotal(max);
		setBlockWhenExhausted(true);
		setMaxWaitMillis(3000);
		setTestWhileIdle(true);
	}
}
