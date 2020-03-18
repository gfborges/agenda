package br.com.modelo;

import java.util.List;
import br.com.negocio.Controle;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

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
		while(nome.equals("")) {
			System.out.print("(*)Nome: ");
			nome = controle.texto();
		}
		
		while(tnum.equals("")) {
			System.out.print("(*)Telefone: ");
			tnum = controle.texto();
		}
		// Data de Nascimento
		System.out.print("Data de Nascimento (aaaa-MM-dd): ");
		LocalDate nasc = LocalDate.parse(controle.texto());
		nascms = nasc.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		System.out.print("Genero(M/f): ");
		gen = controle.texto();
		genero = (gen.equals("")) || (gen.charAt(0) == 'M');
		
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
		int l= 0, r = clientes.size() - 1, m;
		char ch = p.getNome().charAt(0), ch2;
		
		while(l < r) {
			m = (l + r + 1) / 2;
			ch2 = nome.charAt(0);
			if( ch >= ch2 ) {
				r = m - 1;
			}
			else {
				l = m;
			}	
		}
		this.clientes.add(l+1, p);
		
	}
	
	public void listar_clientes() {
		for(int i = 0; i < this.clientes.size(); ++i) {
			System.out.printf("%d. %s\n", i, clientes.get(i).toString());
		}
		
	}
	
}
