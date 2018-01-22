package org.info.net;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;

import com.google.common.net.InetAddresses;

public class IPU {

	public static BigInteger ipv6ToBigInt(InetAddress inetAddress) {
		final byte[] bytes = inetAddress.getAddress();
		return new BigInteger(1, bytes);
	}

	public static InetAddress toIP(String adr) {
		InetAddress ip = InetAddresses.forString(adr);
		return ip;
	}

	public static BigInteger ipv4ToInt(InetAddress inetAddress) {
		return BigInteger.valueOf(InetAddresses.coerceToInteger(inetAddress));
	}// ()

	public static boolean is4(InetAddress inetAddress) {
		return (inetAddress instanceof Inet4Address);
	}

}
