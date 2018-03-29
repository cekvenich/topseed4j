package org.apache.da;

import java.util.List;
import java.util.Map;

import org.cache2k.Cache;

/**
 * Back end CRUD of a (web) component instance - but not business logic. DB
 * access should be mapped to the UI component and NOT to the E/R or tables.
 * Extend this for each UI component in your app. so that you can test the DB
 * access for that component. You are welcome to use composition. Should be
 * managed by a singleton - so cache and clean up are managed.

  Use BaaS
 */
@Deprecated
public abstract class AbsCompDA { // recommend 1:1 to component
	protected static final String CM = "?,";
	/**
	 * These should be read from config file/props
	 */
	protected static final String SEL_STAR = "select * from ";
	// friend list; or Item's comments - done async
	protected static final String INSERT = "INSERT into  ? ( ";
	protected static final String DESCRIBE_TABLE = "select column_name, data_type"
			+ " from INFORMATION_SCHEMA.COLUMNS where table_name = ?";
	// Get your results from cache that time decays. Key should have table and args
	public Cache<Object, List<Map<String, Object>>> CACHE0;
	public String CUSER; // the user of component, used for cache invalidation. Ex: invalidate Vic's
	// a private cache just for this component that cant be invalidated outside. For
	// example list of states
	protected Cache<Object, List<Map<String, Object>>> _cache1;
	protected BaseDA _dba; // database access

	/**
	 * Allows another component to invalidate the public cache.
	 */
	public void clearCache(Object... args) {
		CACHE0.clear();
	}

	/**
	 * Clean up if needed
	 */
	protected abstract void cleanUp() throws Throwable;

}
