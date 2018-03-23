package rpc;

import org.info.rpc.EMsg;
import org.info.util.Confd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	static Confd P = Confd.INSTANCE;

	public static void main(String[] args) throws Throwable {

		System.out.println(P.getConf());

		logger.info("JCL");

		EMsg m = new EMsg();
		m.error("k");
		System.out.println(m.ok());
		System.out.println(m);
		logger.info("", m);

	}

}
