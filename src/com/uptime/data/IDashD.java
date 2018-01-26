package com.uptime.data;

import java.util.List;
import java.util.Map;

/**
 * RPC
 */
public interface IDashD {

	public static int MPORT = 9081;
	public static final String DASH_EP = "dash_ep";

	// commands:
	public static final String PING = "/ping";
	public static final String GET_DASH_ASSI_LOAD = "/getDashAssiLoad";
	public static final String INS_NODE_LOAD_INFO = "/insertNodeLoadInfo";

	// public static final String DOM = "dom";

	/**
	 * node, domain, connections, current latency
	 */
	public void sendNodeLoadInfo(String node, Map<String, Integer> nodeInfo);

	/**
	 *
	 * Assign load
	 */
	public Map<String, Object> getDashAssiLoad(String node, List<String> doms) throws Throwable;

}
