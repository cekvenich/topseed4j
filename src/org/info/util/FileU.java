package org.info.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileU {

	private static final Logger logger = LoggerFactory.getLogger(FileU.class);

	public static byte[] read(String fn) throws Throwable {
		Path path = Paths.get(fn);
		return Files.readAllBytes(path);
	}// ()

	public static byte[] read(File f) throws Throwable {
		Path path = f.toPath();
		return Files.readAllBytes(path);
	}// ()

	public static boolean write(byte[] ba, String fn) throws Throwable {
		try (FileOutputStream fos = new FileOutputStream(fn)) {
			fos.write(ba);
		}

		return true;
	}// ()

	public static void mkDir(String dn) throws Throwable {
		Path path = Paths.get(dn);
		// if directory exists?
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
	}// ()

	public static File getNewestFile(String dirn) {
		File dir = new File(dirn);
		if (!dir.exists())
			return null;
		Collection<File> filesC = FileUtils.listFiles(dir, null, true);
		List<File> files = new ArrayList<>(filesC);
		Collections.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		return files.get(0);
	}

	public static void makeFile(String folder, String name, String content) throws Throwable {
		FileU.mkDir(folder);
		FileWriter fw = new FileWriter(folder + name);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		fw.close();
	}

	public static List<String> dir(String folder) {
		List<String> results = new ArrayList<>();

		File[] files = new File(folder).listFiles();
		if (files == null || files.length < 1)
			return null;
		for (File file : files) {
			if (file.isFile()) {
				results.add(file.getName());
			}
		} // for
		return results;
	}// ()

	public static boolean del(String fn) {
		File f = new File(fn);
		return f.delete();
	}

	public static void delDir(String dn) throws Throwable {
		File dir = new File(dn);
		FileUtils.forceDelete(dir);
	}// ()

	public static void silentDelDir(String dn) {
		try {
			delDir(dn);
		} catch (Throwable e) {
			logger.info(e.getMessage());
		}
	}// ()

	public static String show(File f) throws FileNotFoundException {
		Scanner fileIn = new Scanner(f);
		StringBuilder ret = new StringBuilder();
		while (fileIn.hasNext()) {
			String s = fileIn.nextLine();
			ret.append(s);
		}
		fileIn.close();
		return ret.toString();
	}

}// class
