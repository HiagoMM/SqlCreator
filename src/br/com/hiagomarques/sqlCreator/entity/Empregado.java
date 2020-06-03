package br.com.hiagomarques.sqlCreator.entity;

import br.com.hiagomarques.sqlCreator.anotations.Id;

public class Empregado {
	
	@Id
	private Long id;
	private String nome;
	private String sobrenome; 
	private Integer idade;
	private Integer matricula;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public Integer getIdade() {
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	public Integer getMatricula() {
		return matricula;
	}
	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}
	@Override
	public String toString() {
		return "Empregado [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", idade=" + idade
				+ ", matricula=" + matricula + "]";
	}
	

}
