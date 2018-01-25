package org.info.rpc;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class J {

	static Gson GSON = new Gson();

	public static String listToJSON(List ls) {
		return GSON.toJson(ls);
	}

	public static List jsonToList(String js) {
		return GSON.fromJson(js, List.class);
	}

	public static String mapToJSON(Map info) {
		return GSON.toJson(info);
	}

	public static Map jsonToMap(String js) {
		return GSON.fromJson(js, Map.class);
	}// ()

	public static int double2Int(Object d) throws Throwable {
		return Double.valueOf(d.toString()).intValue();
	}
}// class
