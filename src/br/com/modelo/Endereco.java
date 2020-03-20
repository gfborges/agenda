package br.com.modelo;

public class Endereco {
	private String cidade;
	private String bairro;
	private String rua;
	private String numero;
	private String complemento;
	
	public Endereco(String cidade, String bairro, String rua, String numero, String complemento) {
		this.cidade = cidade;
		this.bairro = bairro;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}
	
	public String getBairro() {
		return bairro;
	}

	public String getRua() {
		return rua;
	}

	public String getNumero() {
		return numero;
	}

	public String getComplemento() {
		return complemento;
	}

}
