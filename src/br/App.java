package br;

import java.sql.Connection;
import java.sql.SQLException;

import br.entity.Empregado;

public class App {
	
	public static void main(String[] args) throws SQLException {
		Connection conn = ConnectionManager.get();
		
		Empregado obj = new Empregado();
		obj.setId(1l);
		obj.setIdade(14);
		obj.setMatricula(1232123);
		obj.setNome("Hiago");
		obj.setSobrenome("marques");
		
		EmpregadoDao empDao = new EmpregadoDao(conn);
		empDao.save(obj);
	}
		
}
