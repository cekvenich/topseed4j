package org.apache.chain.res;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Use wedge when you can.
 */

public class MimeMAP {
	public static final Map<String, String> MIME_MAP = new HashMap<>();

	static {
		MIME_MAP.put("css", "text/css");
		MIME_MAP.put("html", "text/html");
		MIME_MAP.put("js", "application/javascript");
		MIME_MAP.put("json", "application/json");
		MIME_MAP.put("md", "text/plain");
		MIME_MAP.put("mf", "text/cache-manifest");
		MIME_MAP.put("svg", "image/svg+xml");
		MIME_MAP.put("txt", "text/plain");
		MIME_MAP.put("xml", "application/xml");

		MIME_MAP.put("gif", "image/gif");
		MIME_MAP.put("ico", "image/x-icon");
		MIME_MAP.put("jpeg", "image/jpeg");
		MIME_MAP.put("jpg", "image/jpeg");
		MIME_MAP.put("mp4", "video/mp4");
		MIME_MAP.put("pdf", "application/pdf");
		MIME_MAP.put("png", "image/png");
		MIME_MAP.put("zip", "application/zip");

	}

	;
}
