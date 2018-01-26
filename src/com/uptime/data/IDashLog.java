package com.uptime.data;

import java.util.Map;

public interface IDashLog {

	/**
	 * UTC request time at ms, DC EPip, Domain, User ISP, User Region/Geo Country +
	 * State, Url Path, Response Time, WAS_REJECTED_DUE_TO_LOAD, Response size,
	 * IsStaticCDBImg, (X) Optional User Header, (X) Returned Risk Index, (X) User
	 * IP, (X) User Org, (X) User Long/Lat, Indicated Internal Load, XX Internal NS
	 * ip
	 */
	public void qLog(Map args);

}
