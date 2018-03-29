package org.apache.chain;

import java.util.HashMap;
import java.util.Map;

import org.apache.chain.srv.Ctx;
import org.info.net.NetU;
import org.info.rpc.EMsg;
import org.info.rpc.J;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class AbsCMD {

	public boolean retError(String err, Ctx ctx) {
		Map ret = new HashMap();
		ret.put(EMsg.ERROR, err);
		String j = J.toJ(ret);
		ByteBuf body = NetU.stringToBy(j);
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);

		resp.headers().add("Access-Control-Allow-Origin", "*");

		ctx.httpResponse(resp);
		return true; // done

	}
}
