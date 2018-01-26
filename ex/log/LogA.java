package log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogA {
	// test apache log needed for pug
	private static final Log log = LogFactory.getLog(LogA.class);

	public static void main(String[] args) {
		log.info("yes?");
	}

}
