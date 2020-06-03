package br.com.hiagomarques.sqlCreator.reflection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementCreator<T> {

	private Connection conn;

	public StatementCreator(Connection conn) {
		this.conn = conn;
	}

	public PreparedStatement generateStatement(T obj, String sql) throws SQLException {
		PreparedStatement prst = conn.prepareStatement(sql);
		Class<? extends Object> reflected = obj.getClass();

		return fillStatement(sql, prst, Arrays.stream(reflected.getMethods())
				.filter(met -> met.getName().startsWith("get") && !met.getName().contains("Class")).map(met -> {
					try {
						return Arrays.asList(met.getName().substring(3).toLowerCase(), met.invoke(obj));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return Arrays.asList("", "");
				}).collect(Collectors.toMap(arr -> arr.get(0), arr -> arr.get(1) == null ? "null" : arr.get(1))));

	}

	private PreparedStatement fillStatement(String sql, PreparedStatement prst, Map<Object, Object> map)
			throws SQLException {

		List<String> arr = new ArrayList<>();
		if (sql.toLowerCase().startsWith("insert")) {
			arr = Arrays.asList(
					sql.substring(sql.indexOf("("), sql.indexOf(")")).replace("(", "").replace(")", "").split(","));
		} else {
			int start = 0;
			do {
				start = sql.indexOf(" = ?", start + 1);
				String palavra = "";
				for (int i = start; i > 0; i--) {
					char letra = sql.charAt(i - 1);
					if (" ".equals(String.valueOf(letra))) {
						break;
					}
					palavra = letra + palavra;
				}
				if (start != -1) {
					arr.add(palavra);
				}

			} while (sql.indexOf(" = ?", start + 1) != -1);
		}
		arr = arr.stream().map(String::trim).collect(Collectors.toList());
		for (int i = 0; i < arr.size(); i++) {
			String key = arr.get(i);
			Object value = "null".equals(map.get(key)) ? null : map.get(key);
			prst.setObject(i + 1, value);
		}

		return prst;
	}

}
