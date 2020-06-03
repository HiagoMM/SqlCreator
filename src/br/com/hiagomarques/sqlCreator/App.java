package br.com.hiagomarques.sqlCreator;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.hiagomarques.sqlCreator.entity.Empregado;

public class App {

	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionManager.get();

		Empregado obj = new Empregado();
		obj.setId(1l);
		obj.setIdade(18);
		obj.setMatricula(1213);
		obj.setNome("Jujuba");
		obj.setSobrenome(":)");
		
		EmpregadoDao emp = new EmpregadoDao(conn);
		emp.save(obj);

//		SqlCreator.generateInsert(obj);
//		SqlCreator.generateDelete(obj, "nome = ?");
//		SqlCreator.generateSelect(obj);
//		StatementCreator<Empregado> stc = new StatementCreator<>(conn);
//		String sql = SqlCreator.generateDelete(Empregado.class, "id = 1");
//		stc.generateStatement(obj, sql).execute();
	}

}
