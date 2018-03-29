package org.info.rpc;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.info.net.NetU;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class EMsg {
	public static final String ERROR = "_ERROR";

	public static final String NPE = "NPE";
	public Map headers;// can be used to send headers, ex: auth related
	protected String _error;
	protected byte[] resp;

	public EMsg error(String er) throws Error {
		if (er == null || er.length() < 1)
			throw new Error(NPE);
		_error = er;
		return this;
	}

	public EMsg error(Throwable e) throws Error {
		_error = e.getMessage();
		return this;
	}

	public EMsg error(byte[] er) throws Error {
		if (er == null || er.length < 1)
			throw new Error(NPE);

		_error = new String(er, StandardCharsets.UTF_8);
		return this;
	}

	public String error() {
		return _error;
	}

	public ByteBuf erBB() {
		return NetU.stringToBy(_error);
	}

	public EMsg ba(byte[] ba) throws Throwable {
		if (ba == null || ba.length < 1)
			throw new Throwable(NPE);
		resp = ba;
		return this;
	}

	public byte[] ba() throws Throwable {
		return resp;
	}

	public ByteBuf toBB() {
		return Unpooled.wrappedBuffer(resp);
	}

	/**
	 * @return no error
	 */
	public boolean ok() {
		if (_error == null && resp != null && resp.length > 0)
			return true;
		return false;
	}

	public EMsg bool(Boolean r) throws Throwable {
		if (r == null)
			throw new Throwable(NPE);
		resp = new byte[] { (byte) (r ? 1 : 0) };
		return this;
	}

	public boolean bool() {
		byte b = resp[0];
		return b == 1;
	}

	public EMsg inti(Integer r) throws Throwable {
		if (r == null)
			throw new Throwable(NPE);
		resp = BigInteger.valueOf(r).toByteArray();
		return this;

	}

	public int inti() {
		return new BigInteger(resp).intValue();
	}

	public EMsg str(String str_) throws Throwable {
		resp = str_.getBytes(StandardCharsets.UTF_8);
		return this;
	}

	public String str() {
		return new String(resp, StandardCharsets.UTF_8);
	}

	public EMsg json(String str_) throws Throwable {
		return str(str_);
	}

	public String json() {
		return str();
	}

	@Override
	public String toString() {
		if (!ok())
			return null;
		String str = null;
		try {
			str = str();
		} catch (NullPointerException npe) {
		}
		if (str != null)
			return str;
		return _error;
	}

	public DefaultFullHttpResponse retNettyHTTP() {
		DefaultFullHttpResponse resp;
		if (ok())
			resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, toBB());
		else
			resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, erBB());

		return resp;
	}

}
