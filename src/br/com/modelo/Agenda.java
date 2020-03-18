package br.com.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import br.com.negocio.Controle;

public class Agenda {
	List<Pessoa> clientes = new ArrayList<Pessoa>();
	
	public void cadastrar() {
		String nome = "", tnum = "", gen = "";
		String cidade, bairro, rua, complemento, cnum;
		boolean genero;
		long nascms;
		System.out.println();
		Controle controle = new Controle();
		System.out.println("Itens com (*) são obrigatórios");
		
		// Nome
		while(nome.equals("")) {
			System.out.print("(*)Nome: ");
			nome = controle.texto();
		}
		// Telefone
		while(tnum.equals("")) {
			System.out.print("(*)Telefone: ");
			tnum = controle.texto();
		}
		// Data de Nascimento
		System.out.print("Data de Nascimento (aaaa-MM-dd): ");
		LocalDate nasc = LocalDate.parse(controle.texto().trim());
		nascms = Pessoa.toMillis(nasc);
		
		// Genero
		System.out.print("Genero(M/f): ");
		gen = controle.texto();
		genero = (gen.equals("")) || (gen.toUpperCase().startsWith("M"));
		
		// Endereço
		System.out.print("Cidade: ");
		cidade =  controle.texto();
		System.out.print("Bairro: ");
		bairro =  controle.texto();
		System.out.print("Rua: ");
		rua =  controle.texto();
		System.out.print("Numero: ");
		cnum = controle.texto();
		System.out.print("Complemento: ");
		complemento = controle.texto();
		
		Pessoa p = new Pessoa(nome, tnum, cidade, 
							bairro, rua, cnum, complemento,
							nascms, genero);
		if(clientes.isEmpty()) {
			//Caso esteja vazia insere no final
			this.clientes.add(p);
		}
		else if(p.getNome().compareToIgnoreCase( clientes.get(0).getNome() ) < 0 ) {
			// Caso não tenha um predecessor, insere no começo
			this.clientes.add(0, p);
		}
		else {
			// Busca binária por predecessor
			int l= 0, r = clientes.size() - 1, m;
			while(l < r) {
				m = (l + r + 1) / 2;
				String clt = clientes.get(m).getNome();
				int comp = nome.compareToIgnoreCase(clt);
				// remove todos que são maiores que m (inclusive)
				if( comp <= 0 ) {
					r = m - 1;
				}
				// remove todos os menores que m (exclisivo)
				else {
					l = m;
				}	
			}
			this.clientes.add(l+1, p);
		}
	}
	
	public void listar_clientes( boolean genero ) {
		for(int i = 0; i < clientes.size(); ++i) {
			Pessoa p = this.clientes.get(i);
			if( !(p.isGenero() ^ genero)  ) {
				System.out.printf("%d. %s\n", i, p.toString());
			}
		}
	}
	
	public void listar_clientes() {
		for(int i = 0; i < this.clientes.size(); ++i) {
			System.out.printf("%d. %s\n", i, clientes.get(i).toString());
		}
	}
	public int busca(String busca) {
		for(int i = 0; i < this.clientes.size(); ++i ) {
			Pessoa p = this.clientes.get(i);
			if( p.getNome().toLowerCase().startsWith(busca) ) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean editar(String busca) {
		int i = this.busca(busca);
		if (i < 0) {
			return false;
		}
		return this.editar(i);
		
	}
	
	public boolean editar(int i) {
		if (i < 0 || i >= this.clientes.size() ) {
			System.out.printf("%d nao e um id valido\n", i);
			return false;
		}
		Controle controle = new Controle();
		Pessoa p = this.clientes.get(i);
		
		String nome,
			   tel,
			   data,
			   gen;
		
		String cidade,
			   bairro,
			   rua,
			   numero,
			   complemento;
		
		System.out.println("Deixe em branco os campos que não precisam de alteração");
		
		// Nome
		System.out.println("Nome: " + p.getNome());
		System.out.print("Nome: ");
		nome = controle.texto();
		if(!nome.equals("")) {
			p.setNome(nome);
		}
		
		// Telefone
		System.out.println("Telefone: " + p.getTelefone() );
		System.out.print("Telefone: " );
		tel = controle.texto();
		if(!tel.equals("")) {
			p.setTelefone(tel);
		}
		
		// Data de nascimento
		System.out.println("Data de nascimento: " + p.getNasc() );
		System.out.print("Data de nascimento: ");
		data = controle.texto();
		if(!data.contentEquals("")) {
			p.setNasc(data);
		}
		
		// Genero
		System.out.println("Genero: " + p.getGenero() );
		System.out.print("Genero: ");
		gen = controle.texto().trim().toUpperCase();
		if(!gen.equals("")) {
			while(true) {
				if( gen.startsWith("M") ) {
					p.setGenero(true);
					break;
				}
				else if( gen.startsWith("F") ) {
					p.setGenero(false);
					break;
				}
				System.out.println("Insira uma opção valida");
			}
		}
		
		// Endereço
		System.out.println("Cidade: " + p.getEndereco().getCidade());
		System.out.print("Cidade: ");
		cidade = controle.texto();
		if(!cidade.equals("")) {
			cidade = p.getEndereco().getCidade();
		}
		System.out.println("Bairro: " + p.getEndereco().getBairro());
		System.out.print("Bairro: ");
		bairro = controle.texto();
		if(!bairro.equals("")) {
			bairro = p.getEndereco().getBairro();
		}
		System.out.println("Rua: " + p.getEndereco().getRua());
		System.out.print("Rua: ");
		rua = controle.texto();
		if(!rua.equals("")) {
			rua = p.getEndereco().getRua();
		}
		System.out.println("Numero: " + p.getEndereco().getNumero());
		System.out.print("Numero: ");
		numero = controle.texto();
		if(!numero.equals("")) {
			numero = p.getEndereco().getNumero();
		}
		System.out.println("Cidade: " + p.getEndereco().getComplemento());
		System.out.print("Complemento: ");
		complemento = controle.texto();
		if(!complemento.equals("")) {
			complemento = p.getEndereco().getComplemento();
		}
		
		return true;
	}
	
}
