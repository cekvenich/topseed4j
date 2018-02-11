package org.info.rpc;

import java.util.Map;

import org.apache.hc.core5.http.HttpEntity;

public interface Irpc {

	/**
	 * Return a response. Either request or return could be crypto, zip and cbor or
	 * JSON. Other: consider timeout. Path should autoInst a Srv class
	 */
	public EMsg call(String path, Map<String, Object> qs, HttpEntity body, Map sheaders) throws Throwable;

	public void setMaxConcurent(int c);

	public void setTimeout(Integer timeOut);
}
