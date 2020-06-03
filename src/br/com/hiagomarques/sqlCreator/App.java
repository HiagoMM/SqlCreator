package br.com.hiagomarques.sqlCreator;

import java.sql.Connection;

import br.com.hiagomarques.sqlCreator.entity.Empregado;

public class App {

	public static void main(String[] args) throws Exception {
		
		Connection conn = ConnectionManager.get();
		Empregado obj = new Empregado();
		
		obj.setIdade(19);
		obj.setMatricula(1213);
		obj.setNome("Hiago");
		obj.setSobrenome("Marques");
		
		EmpregadoDao emp = new EmpregadoDao(conn);
//		emp.save(obj);
		emp.delete(2l);
		System.out.println(emp.getAll());

//		SqlCreator.generateInsert(obj);
//		SqlCreator.generateDelete(obj, "nome = ?");
//		SqlCreator.generateSelect(obj);
//		StatementCreator<Empregado> stc = new StatementCreator<>(conn);
//		String sql = SqlCreator.generateDelete(Empregado.class, "id = 1");
//		stc.generateStatement(obj, sql).execute();
	}

}
