package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogS {
	private final static Logger logger = LoggerFactory.getLogger(LogS.class);

	public static void main(String[] args) throws Throwable {

		while (true) {
			Throwable t = new Throwable("oops");
			logger.info("something now? 22", t);
			System.out.print('.');
			Thread.sleep(400);
		}
	}

}
//