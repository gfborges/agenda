package br.com.modelo;

import java.util.List;
import br.com.negocio.Controle;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Agenda {
	List<Pessoa> clientes = new ArrayList<Pessoa>();
	
	public void cadastrar() {
		String nome = "",
		       tnum = "",
		       gen = "";
		String cidade,
		       bairro,
		       rua,
		       complemento,
		       cnum;
		boolean genero;
		long nascms;

		Controle controle = new Controle();
		System.out.println("\nItens com (*) são obrigatórios");

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
		LocalDate nasc = LocalDate.parse(controle.texto());
		nascms = nasc.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

		// Genero
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
		
		adicionar_ordenado(p);
	}

	private void adicionar_ordenado(Pessoa p) {
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
				int comp = p.getNome().compareToIgnoreCase(clt);
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
	
	public void listar_clientes() {
		for(int i = 0; i < this.clientes.size(); ++i) {
			System.out.printf("%d. %s\n", i, clientes.get(i).toString());
		}
	}

	public void exportar_csv() throws IOException {
		if(clientes.isEmpty()) {
			System.out.println("Lista de clientes vazia!");
			return;
		}

		Controle controle = new Controle();
		String nome_arquivo;

		do {
			System.out.print("Nome do arquivo para ser exportado: ");
			nome_arquivo = controle.texto();
		} while(nome_arquivo.isEmpty());

		FileWriter arquivo = new FileWriter(nome_arquivo);
		for(Pessoa p: clientes) {
			arquivo.write(
				p.getNome()     + ", " +
				p.getTelefone() + ", " +
				p.getGenero()   + ", " +
				p.getIdade()    + ", " +
				p.getEnderecoCSV()
			);
		}

		arquivo.close();
	}

}