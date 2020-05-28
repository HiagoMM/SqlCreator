package br.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatementCreator<T> {

	private Connection conn;

	public StatementCreator(Connection conn) {
		this.conn = conn;
	}

	public PreparedStatement generateStatement(T obj, String sql) throws SQLException {
		PreparedStatement prst = conn.prepareStatement(sql);
		Class<? extends Object> reflected = obj.getClass();
		Field[] fields = reflected.getDeclaredFields();

		return fillStatement(sql, prst, fields,  Arrays.stream(reflected.getMethods())
				.filter(met -> met.getName().startsWith("get") && !met.getName().contains("Class")).map(met -> {
					try {
						return Arrays.asList(met.getName().substring(3).toLowerCase(), met.invoke(obj));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return Arrays.asList("","");
				}).collect(Collectors.toMap(arr -> arr.get(0), arr -> arr.get(1) == null ? "null" : arr.get(1))));


	}

	private PreparedStatement fillStatement(String sql, PreparedStatement prst,Field[] fields, Map<Object, Object> map) throws SQLException {
		
		List<String> arr = Arrays.asList(sql.substring(sql.indexOf("("), sql.indexOf(")")).replace("(","").replace(")","").split(",") );
		arr = arr.stream().map(v -> v.trim()).collect(Collectors.toList());
		System.out.println(arr);
		
		
		for (int i = 0; i < arr.size() ; i++  ) {
			String key = arr.get(i);
			String type = getType(fields, key).toLowerCase();
			System.out.println(type + " "+key  +" ");
			if ( type.contains("string")) {
				prst.setString(i+1,(String) map.get(key));
			}else if ( type.contains("int")) {
				prst.setInt(i+1,(Integer) map.get(key));
			}else if ( type.contains("double")) {
				prst.setDouble(i+1,(Double) map.get(key));
			}else if ( type.contains("long")) {
				prst.setLong(i+1,(Long) map.get(key));
			}
			
		}
		return prst;
	}
	
	private String getType(Field[] fields, String key) {
		return Stream.of(fields).filter(field ->  field.getName().equals(key)).findFirst().get().getType().getName();
	}

}
