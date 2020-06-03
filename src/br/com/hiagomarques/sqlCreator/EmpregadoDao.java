package br.com.hiagomarques.sqlCreator;

import java.sql.Connection;

import br.com.hiagomarques.sqlCreator.entity.Empregado;

public class EmpregadoDao extends AbstractDAO<Empregado, Long>{

	public EmpregadoDao(Connection conn) {
		super(conn);
	}

}
