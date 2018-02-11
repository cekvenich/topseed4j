package org.info.net;

public class EPRecord {

	public static final String EP_HOST = "EP_HOST";
	public static final String EP_PORT = "EP_PORT";

	/**
	 * EP
	 */
	public String host;
	public int port;
	public String domain;

	public boolean _ok = false;

	public boolean _ok() {
		return _ok;
	}

	@Override
	public String toString() {
		return port + domain + host;
	}

}
