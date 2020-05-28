package br;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.reflection.SqlCreator;
import br.reflection.StatementCreator;

public abstract class AbstractDAO<T> {

	private Connection conn;
	
	public AbstractDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public void save(T obj) throws SQLException {
		StatementCreator<T> stg = new StatementCreator<T>(conn);
		PreparedStatement st = stg.generateStatement(obj, SqlCreator.generateInsert(obj));
		st.execute();
	}
}
