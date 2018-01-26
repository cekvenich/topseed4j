package com.uptime.data;

import java.util.Map;

/**
 * RPC
 */
public interface IDashNS {

	public static int MPORT = 9081;
	public static final String DASH_EP = "dash_ep";

	public static final String GET_LOAD_ALL = "/getLoadAll";

	/**
	 * Get load index for all the (NS) loads
	 */
	public Map<String, Object> getLoadNodes() throws Throwable;

}
