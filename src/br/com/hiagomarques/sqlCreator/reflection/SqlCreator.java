package br.com.hiagomarques.sqlCreator.reflection;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.hiagomarques.sqlCreator.anotations.Id;

public class SqlCreator {

	public static String generateInsert(Class<? extends Object> reflected) {

		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		String query = "insert into " + arrayName[arrayName.length - 1];
		List<String> ids = Stream.of(reflected.getDeclaredFields()).filter(fil -> fil.getAnnotation(Id.class) != null)
				.map(Field::getName).collect(Collectors.toList());

		query += " (";
		for (int i = 0; i < reflected.getDeclaredFields().length; i++) {
			Field field = reflected.getDeclaredFields()[i];
			if (!ids.contains(field.getName())) {
				query += field.getName();
				if (i != reflected.getDeclaredFields().length - 1) {
					query += ", ";
				}
			}
		}

		query += ") values (";
		int forSize = reflected.getDeclaredFields().length - ids.size();
		for (int i = 0; i < forSize; i++) {
			query += "? ";
			if (i != forSize - 1) {
				query += ", ";
			}
		}
		query += ")";
		System.out.println(query);
		return query;
	}

	public static String generateUpdate(Class<? extends Object> reflected, String where) throws SQLException {
		String query = generateUpdate(reflected);
		if (where == null || where.trim().length() == 0) {
			throw new SQLException("Ta ficando doido bixo");
		}
		query = query + " where " + where;
		System.out.println(query);
		return query;
	}

	public static String generateUpdate(Class<? extends Object> reflected) throws SQLException {

		String[] arrayName = reflected.getName().replace(".", "#").split("#");

		String query = "update " + arrayName[arrayName.length - 1] + " set ";

		for (int i = 0; i < reflected.getDeclaredFields().length; i++) {
			Field field = reflected.getDeclaredFields()[i];
			query += field.getName() + " = ?";
			if (i != reflected.getDeclaredFields().length - 1) {
				query += ", ";
			}
		}
		return query;
	}

	public static String generateDelete(Class<? extends Object> reflected, String where) throws SQLException {
		String query = generateDelete(reflected);
		if (where == null || where.trim().length() == 0) {
			throw new SQLException("Ta ficando doido bixo");
		}
		query = query + " where " + where;
		System.out.println(query);
		return query;
	}

	public static String generateDelete(Class<? extends Object> reflected) throws SQLException {
		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		return "delete from " + arrayName[arrayName.length - 1];
	}

	public static String generateSelect(Class<? extends Object> reflected) {
		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		String query = "select * from " + arrayName[arrayName.length - 1];
		System.out.println(query);
		return query;
	}

}
