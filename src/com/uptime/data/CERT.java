package com.uptime.data;

import java.io.File;

import org.info.util.FileU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class CERT {
	private static final Logger logger = LoggerFactory.getLogger(CERT.class);

	public static int CPORT = 9082;

	public static final String DOM_KEY = "dom";

	// commands:
	public static final String PING = "/ping";

	public static final String CERT_VALID_STATE = "/getCertValidState";
	public static final String REVOKE = "/revoke";
	public static final String CERT_CHAIN = "/CertChain";
	public static final String KEY_PFILE = "/KeyPFile";

	public static final String VAL_FILE = "/ValFile";
	public static final String DEL_VAL_FILE = "/DelValFile";

	public static final String STARTT_NEW_PROCESS = "/starttNewProcess";
	// end commands.

	// public static final String VALID_STRING = "OK";

	public static final String CERT_EP = "cert_ep";
	public static final String CFOLDER = "./zerts/";

	public static final String KEYF = ".key";
	public static final String CSRF = ".csr";

	public static final String CHAINF = "chain.crt";
	public static final String PEMF = ".pem";

	public static final String ACME = "well-known/acme-challenge";
	public static final String ACME_REVOKE = "known/acme-revoke";

	public static File mFile(String dom, String type) {
		String fn = CFOLDER + dom + type;
		logger.debug(fn);
		return new File(fn);
	}

	public static void deleteLFiles(String dom) {
		String fn;
		fn = CERT.CFOLDER + dom + CERT.CHAINF;
		FileU.del(fn);
		fn = CERT.CFOLDER + dom + CERT.PEMF;
		FileU.del(fn);
	}

	/*
	 * public static boolean certsExistsXX(String dom) { File keyCertChainFile =
	 * CERT.mFile(dom, CERT.CHAINF); File keyPFile = CERT.mFile(dom, CERT.PEMF);
	 * return (keyCertChainFile.isFile() & keyPFile.isFile()); }
	 */

	public static boolean isValidSSLFiles(String dom) {

		try {
			File keyCertChainFile = CERT.mFile(dom, CERT.CHAINF);
			File keyPFile = CERT.mFile(dom, CERT.PEMF);

			SslContext ssCtx1 = SslContextBuilder.forServer(keyCertChainFile, keyPFile).build();
			return true;
		} catch (Throwable e) {
			logger.info(e.getMessage() + dom);
			return false;
		}
	}// ()
}// class
