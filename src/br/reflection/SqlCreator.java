package br.reflection;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class SqlCreator {
	
	public static<T> String generateInsert(T obj) {
		Class<? extends Object> reflected = obj.getClass();
		
		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		
		String query = "insert into " + arrayName[arrayName.length-1];

		query += " (";
		for(int i = 0; i < reflected.getDeclaredFields().length; i++){
			Field field = reflected.getDeclaredFields()[i];
			query += field.getName();
			if (i != reflected.getDeclaredFields().length -1) {
				query += ", ";
			}
		}
		
		query += ") values (";
		for (int i = 0; i < reflected.getDeclaredFields().length; i++) {
			query += "? ";
			if (i != reflected.getDeclaredFields().length -1) {
				query += ", ";
			}
		}
		query += ")";
		return query ;
	}
	
	public static<T> String generateUpdate(T obj, String where) throws SQLException {
		Class<? extends Object> reflected = obj.getClass();
		
		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		
		String query = "update " + arrayName[arrayName.length-1]+ " set ";

		for(int i = 0; i < reflected.getDeclaredFields().length; i++){
			Field field = reflected.getDeclaredFields()[i];
			query += field.getName() +" = ?";
			if (i != reflected.getDeclaredFields().length -1) {
				query += ", ";
			}
		}
		if( where == null || where.trim().length() == 0) {
			throw new SQLException("Ta ficando doido bixo");
		}
		return query + " where " + where;
	}
	public static<T> String generateDelete(T obj, String where) throws SQLException {
		Class<? extends Object> reflected = obj.getClass();
		String[] arrayName = reflected.getName().replace(".", "#").split("#");
		String query = "delete from " + arrayName[arrayName.length-1];
		if( where == null || where.trim().length() == 0) {
			throw new SQLException("Ta ficando doido bixo");
		}
		return query + " where " + where;
	}
}
