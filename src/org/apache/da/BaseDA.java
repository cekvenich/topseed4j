package org.apache.da;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.StatementConfiguration;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.info.util.Confd;
import org.info.util.U;

/**
 * Data base access. Should use lambda, BaaS, api, etc.
 */
@Deprecated
public enum BaseDA {
	INSTANCE;

	static Confd P = Confd.INSTANCE;
	static StatementConfiguration config = null;
	// is HA: to replicate writes to a read
	// db.
	// Or different priorities. Or failover
	// implementations.
	/**
	 * Could be configured as to timeout and max rows.
	 */
	static QueryRunner _run = new QueryRunner();
	static ExecutorService _es = Executors.newSingleThreadExecutor(); // for fastest async writes
	static AsyncQueryRunner _frun = new AsyncQueryRunner(_es);
	/**
	 * Pool
	 */
	public GenericObjectPool<Connection> DBConPool1; // pool1, ex reads
	public GenericObjectPool<Connection> DBConPool2; // pool2, ex writes. One use
	/**
	 * Register your comp *instances* here that may need cache to be invalidated
	 * from some other comp instances.
	 */
	public List<AbsCompDA> COMPS_REG = U.makeSyncedList();

	/**
	 * You must setup the pool before using DA
	 *
	 * @throws Throwable
	 */
	public void setPool1(AbsFactPool fact) throws Throwable {
		if (DBConPool1 != null)
			throw new Throwable("already configured");
		DBConPool1 = new GenericObjectPool(fact);
		DBConPool1.setConfig(fact.returnPoolConfig());
	}

	public void setPool2(AbsFactPool fact, DefaultPoolConfig config) throws Throwable {
		if (DBConPool2 != null)
			throw new Throwable("already configured");
		DBConPool2 = new GenericObjectPool(fact);
		DBConPool2.setConfig(config);
	}

	/**
	 * Select the pool
	 */
	public List<Map<String, Object>> read(GenericObjectPool pool, StringBuilder sql, Object... args) throws Throwable {
		Connection con = (Connection) pool.borrowObject();
		try {
			MapListHandler handler = new MapListHandler();
			List<List<Map<String, Object>>> res = _run.execute(con, sql.toString(), handler, args);

			if (res != null) {
				List<Map<String, Object>> res1 = res.get(0);
				return res1;// just the first results;
			}
			return null;
		} finally {
			pool.returnObject(con);// back in the ppol
		}
	}// ()

	/**
	 * Returns one/first row, Keys are case Insensitive
	 */
	public TreeMap<String, Object> readOne(GenericObjectPool pool, StringBuilder sql, Object... args) throws Throwable {
		Connection con = (Connection) pool.borrowObject();
		try {
			MapListHandler handler = new MapListHandler();
			List<List<Map<String, Object>>> res = _run.execute(con, sql.toString(), handler, args);

			if (res != null && res.size() > 0) {
				List<Map<String, Object>> res1 = res.get(0);// # of returns
				if (res1.size() > 0) {// rows
					TreeMap<String, Object> row = new TreeMap(String.CASE_INSENSITIVE_ORDER); // make it case
					// Insensitive key
					row.putAll(res1.get(0));// just the first row;
					return row;
				}
			}
			return null;
		} finally {
			pool.returnObject(con);// back in the ppol
		}
	}// ()

	/**
	 * Returns found rows
	 */
	public List<Map<String, Object>> read(StringBuilder sql, Object... args) throws Throwable {
		// Thread.sleep(1);// allow any simultaneous write to sneak ahead
		return read(DBConPool1, sql, args);
	}// ()

	/**
	 * CRUD. Slower than fast write. Returns PK if DB generated. (But you should be
	 * using GUID due to distributed DB and just treat this as void, so mostly
	 * ignore results here).
	 */
	public Map<String, Object> write(StringBuilder sql, Object... args) throws Throwable {
		Map<String, Object> ret = write(DBConPool1, sql, args);
		// Thread.yield();// allow a small break after write for the app.
		return ret;
	}// ()

	/**
	 * Select the pool. Some DB drivers return data, others don't.
	 */
	public Map<String, Object> write(GenericObjectPool pool, StringBuilder sql, Object... args) throws Throwable {
		Connection con = (Connection) pool.borrowObject();
		con.setAutoCommit(true);
		try {
			MapListHandler handler = new MapListHandler();
			List<Map<String, Object>> ret = _run.insert(con, sql.toString(), handler, args);// all 3 CRUD operations
			if (ret != null && ret.size() > 0) {
				Map<String, Object> ret1 = ret.get(0);
				return ret1;// just the first results;
			}
			return null;
		} finally {
			pool.returnObject(con);// back in the ppol
		}
	}// ()

	/**
	 * Faster CRUD. Lets you CRUD a few rows at once, by passing array of args for
	 * more than one row, for example when doing batch inserts or deletes. For
	 * example insert 10K or 100K rows at once before a disk write. Maybe set DB
	 * temp to RAM disk.
	 */
	public void write(GenericObjectPool pool, StringBuilder sql, Object[][] argsA) throws Throwable {
		Connection con = (Connection) pool.borrowObject();
		con.setAutoCommit(true);
		try {
			_run.batch(con, sql.toString(), argsA);
		} finally {
			pool.returnObject(con);// back in the ppol
		}
	}// ()

	/**
	 * Fastest CRUD writer due to single async thread.
	 *
	 * @param con
	 *            Should be a single connection you keep and never close dedicated
	 *            to fast writer. TODO: Make that single connection local to this
	 *            class.
	 */
	public void write(Connection con, StringBuilder sql, Object... args) throws Throwable {
		_frun.update(con, sql.toString(), args);
	}// ()

}
