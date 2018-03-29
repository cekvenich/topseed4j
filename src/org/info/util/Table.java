package org.info.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastTable;

public class Table extends FastTable<Map<String, Object>> {

	private static final long serialVersionUID = 1L;

	public Table() {
		super();
	}

	public Table(Table old) {
		super();
		_addAll(old);
	}

	public static int addUpColumnValues(List<Map<String, Object>> subSet, String col) {
		int total = 0;
		for (Map row : subSet) {
			int value = (Integer) row.get(col);
			total += value;
		}
		return total;
	}

	// concurrent add
	public void _addAll(List<Map<String, Object>> lst) {
		atomic().addAll(lst);
	}// ()

	public Set columnValuesSet(String col) throws Throwable {
		if (col == null)
			throw new Throwable("col can't be null");
		Set values = new HashSet();
		FastTable<Map<String, Object>> thiz = shared();
		for (Map row : thiz) {
			Object value = row.get(col);
			if (value == null)
				continue;
			values.add(value);
		}
		return values;
	}

	public List<Map<String, Object>> selectRowsWhereCol(String col, Object value) {
		List<Map<String, Object>> ret = new ArrayList();
		FastTable<Map<String, Object>> thiz = shared();
		for (Map row : thiz) {// scan
			Object val = row.get(col);
			if (val != value)
				continue; // next
			ret.add(row);
		}
		return ret;
	}

}// class
