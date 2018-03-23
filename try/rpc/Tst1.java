package rpc;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.info.rpc.BasicCli;
import org.info.rpc.EMsg;
import org.info.util.Confd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tst1 {
	private static final Logger logger = LoggerFactory.getLogger(Tst1.class);
	public static int PORT = 8081;
	static Confd P = Confd.INSTANCE;

	public static void main(String[] args) throws Throwable {

		Thread.sleep(1 * 1000);

		BasicCli cli = new BasicCli("localhost", PORT);
		HttpEntity ent = new StringEntity("oh hi");
		EMsg b1 = cli.call("/write?cn=aaa.crt&b=c", null, ent);
		EMsg b2 = cli.call("/write", null, ent);

		logger.info("", b1);
		logger.info("", b2);
		EMsg b3 = cli.call("/", null, ent);
		logger.info("", b3);

		System.exit(-1);
	}// ()

}// class
