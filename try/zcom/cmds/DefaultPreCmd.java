package zcom.cmds;

import java.util.Map;

import org.apache.chain.srv.Ctx;
import org.apache.chain.srv.ICmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This command goes first. Ex: think of this command as Tomcat Servlet, and
 * next one more business logic. Things like auth and sessions are here.
 */
public class DefaultPreCmd implements ICmd {
	private final static Logger logger = LoggerFactory.getLogger(DefaultPreCmd.class);

	protected Map<String, Object> _sessions;

	/**
	 * return true to stop
	 */
	@Override
	public boolean exec(Ctx ctx) {
		logger.info("oh hi");
		return false;
	}

}
