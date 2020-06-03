package br.com.hiagomarques.sqlCreator.reflection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.hiagomarques.sqlCreator.util.Util;

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
		List<String> arr = Util.getNamesFromSql(sql);
		for (int i = 0; i < arr.size(); i++) {
			String key = arr.get(i);
			Object value = "null".equals(map.get(key)) ? null : map.get(key);
			prst.setObject(i + 1, value);
		}

		return prst;
	}


}
