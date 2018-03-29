package zcom.cmds;

import org.apache.chain.srv.Ctx;
import org.apache.chain.srv.ICmd;
import org.info.net.NetU;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class BlankCmd implements ICmd {

	@Override
	public boolean exec(Ctx ctx) {
		ctx.httpRequest(); // here is the request

		// make a response
		ByteBuf body = NetU.stringToBy("some body");
		FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);

		// return
		ctx.httpResponse(resp);
		return false;
	}

}
