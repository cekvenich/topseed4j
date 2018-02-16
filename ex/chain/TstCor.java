package chain;

import org.apache.chain.DefaultChainRouter;
import org.apache.chain.srv.AbsNRouter;
import org.apache.chain.srv.ChainPipe;
import org.info.util.Confd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 *
 * http://commons.apache.org/proper/commons-chain/cookbook.html
 *
 */
public class TstCor {
	static Confd P = Confd.INSTANCE;

	private final static Logger logger = LoggerFactory.getLogger(TstCor.class);

	public static EventLoopGroup _eg = new NioEventLoopGroup(200);

	public static void main(String[] args) throws Throwable {

		//public static final String CHAIN_PATH = "cmdRoot";

		new ChainPipe(new DefaultChainRouter("zcom.cmds")).srvBoot(8081, _eg);

		// now open http://localhost:8081/Blank
	}

	/**
	 * 
	If you want to make a chain class via String
	 */
	public static AbsNRouter _inst(String path) throws Throwable {
		Class<?> clazz = Class.forName(path.trim());
		AbsNRouter inst = (AbsNRouter) clazz.newInstance();
		logger.info(inst.getClass().getName());
		return inst;
	}

}
