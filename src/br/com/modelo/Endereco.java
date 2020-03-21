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
	
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String info() {
		String s= "";
		if(!this.cidade.isEmpty())
			s += "Cidade: " + this.cidade + "\n";
		if(!this.bairro.isEmpty())
			s += "Bairro: " + this.bairro + "\n";
		if(!this.rua.isEmpty())
			s += "Rua: " + this.rua + "\n";
		if(!this.numero.isEmpty())
			s += "Numero: " + this.numero + "\n";
		if(!this.complemento.isEmpty())
			s += "Complemento: " + this.complemento + "\n";
		return s;
	}

}
