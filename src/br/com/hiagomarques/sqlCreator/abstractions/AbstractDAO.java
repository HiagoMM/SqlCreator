package br.com.hiagomarques.sqlCreator.abstractions;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import br.com.hiagomarques.sqlCreator.reflection.SqlCreator;
import br.com.hiagomarques.sqlCreator.reflection.StatementCreator;
import br.com.hiagomarques.sqlCreator.util.Util;

public abstract class AbstractDAO<T extends Object, ID> {

	private StatementCreator<T> stg;

	public AbstractDAO(Connection conn) {
		super();
		stg = new StatementCreator<T>(conn);
	}

	public List<T> findAll() throws Exception {
		T obj = getObj();
		String sql = SqlCreator.generateSelect(obj.getClass());
		PreparedStatement st = stg.generateStatement(obj, sql);
		ResultSet result = st.executeQuery();
		List<String> fields = Util.getAllNamesFromClass(obj.getClass());
		return Util.resultSetFiler(obj, result, fields);
	}
	public T findById(ID id) throws Exception{
		T obj = getObj();
		Field idField = Util.idField(obj);
		String sql = SqlCreator.generateSelect(obj.getClass(), idField.getName() + " = " + id) ;
		PreparedStatement st = stg.generateStatement(obj, sql);
		ResultSet result = st.executeQuery();
		List<String> fields = Util.getAllNamesFromClass(obj.getClass());
		try {
			return Util.resultSetFiler(obj, result, fields).get(0);
		} catch(Exception e) {
			throw new RuntimeException(idField.getName()+" não encontrado");
		}
	}

	public T save(T obj) throws Exception {
		String sql = "";
		Field filId = Util.idField(obj);
		if (isUpdate(obj)) {
			sql = SqlCreator.generateUpdate(obj.getClass(), filId.getName() + " = " + filId.get(obj));
		} else {
			sql = SqlCreator.generateInsert(obj.getClass());
		}
		PreparedStatement st = stg.generateStatement(obj, sql);
		st.execute();
		T result = Util.resultSetFiler(obj, st.getGeneratedKeys(), Arrays.asList(filId.getName())).get(0);
		return Util.copyFields(result, obj, obj.getClass());
	}

	public void delete(ID id) throws Exception {
		T obj = getObj();
		Field filId = Util.idField(obj);
		stg.generateStatement(obj, SqlCreator.generateDelete(obj.getClass(), filId.getName()+" = " + id)).execute();
	}

	private T getObj() {
		@SuppressWarnings("unchecked")
		Class<T> classOfT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		T obj = null;
		try {
			obj = classOfT.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	private boolean isUpdate(T obj) throws Exception {
		Object fieldValue = Util.idField(obj).get(obj);
		return fieldValue != null;
	}


}
