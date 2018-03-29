package org.apache.da.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.da.AbsFactPool;
import org.apache.da.DefaultPoolConfig;
import org.info.util.Confd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2DBFac1 extends AbsFactPool {
	private static final Logger logger = LoggerFactory.getLogger(H2DBFac1.class);
	static Confd P = Confd.INSTANCE;

	static {
		try {
			Class.forName("org.h2.Driver");// load driver
		} catch (ClassNotFoundException e) {
			logger.error("", e);
			System.exit(1);
		}
	}// s

	@Override
	public GenericObjectPoolConfig returnPoolConfig() {
		return new DefaultPoolConfig(4);
	}

	protected Connection _getCon() throws Exception {
		// TODO: Get from config props:
		String dbName = "jdbc:h2:~/db";// home dir

		Connection con = DriverManager.getConnection(dbName, "sa", "");
		return con;
	}

	@Override
	public PooledObject makeObject() throws Exception {
		DefaultPooledObject pobj = new DefaultPooledObject(_getCon());
		return pobj;
	}

	@Override
	public boolean validateObject(PooledObject pobj) {
		return true;
	}// ()

}
