package org.info.util;

import java.io.File;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DU {
	final static ZoneId _systemDefaultTZ = ZoneId.systemDefault();

	public static ZonedDateTime now() {
		return ZonedDateTime.now(ZoneOffset.UTC);
	}

	public static ZonedDateTime toZonedDateTime(Date utilDate) {
		if (utilDate == null) {
			return null;
		}
		return ZonedDateTime.ofInstant(utilDate.toInstant(), _systemDefaultTZ);
	}// ()

	public static long daysTimeDiff(ZonedDateTime d1, ZonedDateTime d2) {
		ChronoUnit unit = ChronoUnit.DAYS;
		return unit.between(d1, d2);
	}

	public static ZonedDateTime getFileDate(File f) {
		Date d = new Date(f.lastModified());
		return ZonedDateTime.ofInstant(d.toInstant(), _systemDefaultTZ);
	}

}
