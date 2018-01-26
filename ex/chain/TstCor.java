package chain;

import org.apache.chain.srv.ChainPipe;
import org.info.util.Confd;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 *
 * http://commons.apache.org/proper/commons-chain/cookbook.html
 *
 */
public class TstCor {
	static Confd P = Confd.INSTANCE;

	public static EventLoopGroup _eg = new NioEventLoopGroup(200);

	// lets you pick a factory/chain.
	final public static String DEFAULT_CHAIN_ROUTER = "defaultChainRouter";

	public static void main(String[] args) throws Throwable {

		String defaultChainRouter = P.getConf(DEFAULT_CHAIN_ROUTER);

		new ChainPipe(defaultChainRouter).srvBoot(8081, _eg);

		// now open http://localhost:8081/Blank
	}
}
