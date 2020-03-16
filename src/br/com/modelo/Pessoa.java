package br.com.modelo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class Pessoa {
	private String nome;
	private Telefone telefone;
	private Endereco endereco;
	private long nasc;
	private boolean genero;
	
	public Pessoa(String nome, Telefone telefone, Endereco endereco, long nascms, boolean genero) {
		super();
		this.nome = nome;
		this.telefone = telefone;
		this.endereco = endereco;
		this.nasc = nascms;
		this.genero = genero;
	}
	
	public Pessoa(String nome, String tnum, String cidade, String bairro, String rua, String cnum, String complemento, long nascms, boolean genero) {
		super();
		this.nome = nome;
		this.telefone = new Telefone(tnum);
		this.endereco = new Endereco(cidade, bairro, rua, cnum, complemento);
		this.nasc = nascms;
		this.genero = genero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Retorna o sexo do cliente
	 *<p>	
	 * Retorna o sexo do cliente em uma String:
	 * true : "Masculino"
	 * false : "Feminino"
	 *</p>
	 *	
	 * @return Sexo do cliente (T(M) / F(F))
	 */
	
	public String getTelefone() {
		return telefone.getNumero();
	}
	
	public String getGenero() {
		if(this.genero)
			return "Masculino";
		return "Feminino";
	}

	public void setGenero(boolean genero) {
		this.genero = genero;
	}
	
	public LocalDate getNasc() {
		LocalDate data = 
				Instant.ofEpochMilli(this.nasc).atZone(ZoneId.systemDefault()).toLocalDate();
		return data;
	}

	public void setNasc(long nasc) {
		this.nasc = nasc;
	}

	public int getIdade() {
		LocalDate dt = LocalDate.now();
		LocalDate localNasc = this.getNasc();
		Period diff = Period.between( localNasc, dt );
		int idade = diff.getYears();
		return idade;
	}
	
	public String info() {
		String data = this.getNasc().toString();
		String s = "Nome: " + this.nome + "\n" +
				   "Telefone: " + this.getTelefone() + "\n" +
				   "Data de Nascimento: " + data + "\n" +
				   "Genero: " + this.getGenero() + "\n" +
				   "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
		return s;
		
	}
	
	public String toString() {
		String s  = "Nome: " + this.nome + " Tel: " + this.getTelefone();
		return s;
		
	}
	
}
