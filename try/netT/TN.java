package netT;

import org.info.net.DefaultPipe;
import org.info.util.Confd;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class TN {
	public static EventLoopGroup _eg = new NioEventLoopGroup(100);
	static Confd P = Confd.INSTANCE;

	public static void main(String[] args) throws Throwable {

		System.out.println(P.getConf());

		MySHandler h = new MySHandler();

		new DefaultPipe(h).srvBoot(8081, _eg);

	}

}
