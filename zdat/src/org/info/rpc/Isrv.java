package org.info.rpc;

import java.util.Map;

import io.netty.buffer.ByteBuf;

public interface Isrv {

	/**
	 * Serve. Either request or return could be crypto, zip and cbor or JSON. Path
	 * should autoInst a Srv class
	 */
	public EMsg _srv(String path, Map<String, String> qs, ByteBuf body, Map sheaders);

	public void setMaxConcurent(int c);

	public void setTimeout(Integer timeOut);

}
