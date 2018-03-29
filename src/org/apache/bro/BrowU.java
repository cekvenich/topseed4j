package org.apache.bro;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrowU {

	public static final String VIDEO = "video";
	public static final String IMG = "img";
	public static final String SRC = "src";
	public static final String STYLE = "style";
	public static final String SCRIPT = "script";
	public static final String LINK = "link";
	static SystemDefaultDnsResolver _dns = new SystemDefaultDnsResolver();

	public static InetAddress lookup(String domain) throws Throwable {
		InetAddress address = InetAddress.getByName(domain);
		return address;
	}// ()

	public static InetAddress getHostIP2(URL url) throws Throwable {
		InetAddress[] ips = _dns.resolve(url.getHost());
		if (ips == null || ips.length < 1)
			return null;
		return ips[0];
	}

	public static InetAddress getHostIP(URL url) throws Throwable {
		String domain = url.getHost();
		return lookup(domain);
	}

	/**
	 * Return String or ba[]
	 */
	public static Object get(URL url, boolean isString) throws Throwable {
		CloseableHttpClient client = HttpClients.custom().build();
		try {

			HttpUriRequestBase req = new HttpGet(url.toURI());
			CloseableHttpResponse resp = client.execute(req);

			HttpEntity content = resp.getEntity();
			if (isString) {
				String str = EntityUtils.toString(content);
				EntityUtils.consume(content);
				return str;
			} // it's not a string
			byte[] ba = EntityUtils.toByteArray(content);
			EntityUtils.consume(content);
			return ba;
		} finally {
			client.close();
		}
	}// ()

	public List<Element> accessJS(Element doc) {
		List<Element> lst = new ArrayList();
		Elements scripts = doc.getElementsByTag(SCRIPT);
		for (Element el : scripts) {
			String src = el.attr(SRC);// could download each w/ http core
			if (src == null || src.length() < 3)
				continue;
			lst.add(el);
		} // for
		return lst;
	}

	public List<Element> accessStyles(Element doc) {
		List<Element> lst = new ArrayList();
		Elements styles = doc.getElementsByTag(LINK);
		for (Element el : styles) {
			String src = el.attr(SRC);// could download each w/ http core
			if (src == null || src.length() < 3)
				continue;
			lst.add(el);
		} // for
		return lst;
	}// ()

	public List<Element> accessAssets(Element doc) {
		List<Element> lst = new ArrayList();
		Elements imgs = doc.getElementsByTag(IMG);
		for (Element el : imgs) {
			String src = el.attr(SRC);// could download each w/ http core
			if (src == null || src.length() < 3)
				continue;
			lst.add(el);
		} // for
		return lst;
	}// ()
}
