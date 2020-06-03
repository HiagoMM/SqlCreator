package br.com.hiagomarques.sqlCreator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

import br.com.hiagomarques.sqlCreator.anotations.Id;
import br.com.hiagomarques.sqlCreator.reflection.SqlCreator;
import br.com.hiagomarques.sqlCreator.reflection.StatementCreator;

public abstract class AbstractDAO<T extends Object, ID> {

	StatementCreator<T> stg;

	public AbstractDAO(Connection conn) {
		super();
		stg = new StatementCreator<T>(conn);
	}

	public void save(T obj) throws SQLException {
		String sql = "";
		if (isUpdate(obj)) {
			sql = SqlCreator.generateUpdate(obj.getClass(), "id = " + idValue(obj));
		} else {
			sql = SqlCreator.generateInsert(obj.getClass());
		}
		PreparedStatement st = stg.generateStatement(obj, sql);
		st.execute();
	}

	public void delete(ID id) throws SQLException {
		@SuppressWarnings("unchecked")
		Class<T> classOfT =  (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		T obj = null;
		try {
			obj = classOfT.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		stg.generateStatement(obj, SqlCreator.generateDelete(obj.getClass(), "id = " + id)).execute();
	}

	private boolean isUpdate(T obj) {
		Object fieldValue = idValue(obj);
		return fieldValue != null;
	}

	private Object idValue(T obj) {
		Object fieldValue = null;
		try {
			Field field = Stream.of(obj.getClass().getDeclaredFields())
					.filter(fil -> fil.getAnnotation(Id.class) != null).findFirst()
					.orElseThrow(() -> new RuntimeException("Id não encontrado"));
			field.setAccessible(true);
			fieldValue = field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldValue;
	}
}
