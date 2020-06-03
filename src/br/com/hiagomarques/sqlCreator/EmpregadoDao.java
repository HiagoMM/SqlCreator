package br.com.hiagomarques.sqlCreator;

import java.sql.Connection;

import br.com.hiagomarques.sqlCreator.entity.Empregado;

public class EmpregadoDao extends AbstractDAO<Empregado>{

	public EmpregadoDao(Connection conn) {
		super(conn);
	}

}
