package org.info.rpc;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class J {
	static JSONParser _parser = new JSONParser();

	static public String toJ(List lst) {
		JSONArray list = new JSONArray();
		list.addAll(lst);
		return list.toJSONString();
	}

	static public String toJ(Map m) {
		JSONObject obj = new JSONObject();
		obj.putAll(m);
		return obj.toJSONString();
	}

	static public Map toMap(String j) throws ParseException {
		Object obj = _parser.parse(j);
		return (JSONObject) obj;
	}

	static public List toLst(String j) throws ParseException {
		Object obj = _parser.parse(j);
		return (JSONArray) obj;
	}

}// class
