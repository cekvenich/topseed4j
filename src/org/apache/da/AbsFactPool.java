package org.apache.da;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory No op. implemented

Use BaaS
 */
@Deprecated
public abstract class AbsFactPool implements PooledObjectFactory {

	private static final Logger logger = LoggerFactory.getLogger(AbsFactPool.class);

	public abstract GenericObjectPoolConfig returnPoolConfig();

	@Override
	public void activateObject(PooledObject arg0) throws Exception {
	}

	@Override
	public void destroyObject(PooledObject arg0) throws Exception {
		logger.debug("d");
	}

	@Override
	public abstract PooledObject makeObject() throws Exception;

	@Override
	public void passivateObject(PooledObject arg0) throws Exception {
		logger.debug("p");
	}

	@Override
	public abstract boolean validateObject(PooledObject arg0);

}// class
