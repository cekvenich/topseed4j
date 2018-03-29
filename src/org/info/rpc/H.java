package org.info.rpc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URLEncodedUtils;

public class H {

	public static final String OK = "OK";

	public static Map<String, String> getQS(final String uri_) {
		if (!uri_.contains("?"))
			return null;
		String uri = uri_.split("\\?")[1];
		List<NameValuePair> q = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
		Map<String, String> qs = q.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

		return qs;
	}

	public static String getPath(String url) {
		return url.split("\\?")[0];
	}

	public static String mapToQs(Map<String, Object> map) {
		if (map == null || map.size() < 1)
			return "";
		StringBuilder string = new StringBuilder();

		if (map.size() > 0) {
			string.append("?");
		}

		for (Entry<String, Object> entry : map.entrySet()) {
			string.append(entry.getKey());
			string.append("=");
			string.append(entry.getValue());
			string.append("&");
		}

		return string.toString();
	}

	public static byte[] getIncomingEnt(CloseableHttpResponse resp) throws Throwable {
		HttpEntity incomingEntity = resp.getEntity();

		if (incomingEntity != null) {
			byte[] ba = EntityUtils.toByteArray(incomingEntity);

			return ba;
		}
		return null;
	}// ()

	public static HttpEntity makeEnt(String str) throws Throwable {

		HttpEntity body = new StringEntity(str, ContentType.create("text/html", "UTF-8"));
		return body;
	}// ()

}// class
