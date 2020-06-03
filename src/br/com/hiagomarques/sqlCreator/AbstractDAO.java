package br.com.hiagomarques.sqlCreator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

import br.com.hiagomarques.sqlCreator.anotations.Id;
import br.com.hiagomarques.sqlCreator.reflection.SqlCreator;
import br.com.hiagomarques.sqlCreator.reflection.StatementCreator;

public abstract class AbstractDAO<T> {

	private Connection conn;

	public AbstractDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public void save(T obj) throws SQLException{
		StatementCreator<T> stg = new StatementCreator<T>(conn);
		String sql = "";
		if (isUpdate(obj)) {
			sql = SqlCreator.generateUpdate(obj.getClass(),"id = " + idValue(obj)) ;
		} else {
			sql = SqlCreator.generateInsert(obj.getClass());			
		}	
		PreparedStatement st = stg.generateStatement(obj, sql );
		st.execute();
	}

	private boolean isUpdate(T obj) {
		Object fieldValue = idValue(obj);
		return fieldValue != null;
	}

	private Object idValue(T obj) {
		Object fieldValue = null;
		try {
			Field field = Stream.of(obj.getClass().getDeclaredFields())
					.filter(fil -> fil.getAnnotation(Id.class) != null).findFirst().orElseThrow(() -> new RuntimeException("Id não encontrado"));
			field.setAccessible(true);
			fieldValue = field.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return fieldValue;
	}
}
