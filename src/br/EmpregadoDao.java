package br;

import java.sql.Connection;

import br.entity.Empregado;

public class EmpregadoDao extends AbstractDAO<Empregado>{

	public EmpregadoDao(Connection conn) {
		super(conn);
	}

}
