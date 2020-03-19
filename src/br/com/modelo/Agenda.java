package br.com.modelo;

import java.util.List;
import br.com.negocio.Controle;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

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
		do {
			System.out.print("(*) Nome: ");
			nome = controle.texto();
		} while(nome.equals(""));
		
		// Telefone
		do {
			System.out.print("(*) Telefone: ");
			tnum = controle.texto();
		} while(tnum.equals(""));

		// Data de Nascimento
		System.out.print("Data de Nascimento (aaaa-MM-dd): ");
		nascms = data_para_milisegundos(controle.texto());

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
	
	private static long data_para_milisegundos(String data) {
		LocalDate nasc = LocalDate.parse(data);
		return nasc.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
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
		} while(nome_arquivo.equals(""));

		FileWriter arquivo = new FileWriter(nome_arquivo);
		for(Pessoa p: clientes) {
			arquivo.write(
				p.getNome()     + ", " +
				p.getTelefone() + ", " +
				p.getGenero()   + ", " +
				p.getNasc()     + ", " +
				p.getEnderecoCSV()
			);
		}

		arquivo.close();
	}

	public void importar_csv()throws IOException {
		Controle controle = new Controle();

		// se a lista não estiver vazia, pergunta ao usuário se ele deseja apagar o conteúdo atual para importar o arquivo
		if(!clientes.isEmpty()) {
			System.out.println("Sua lista não está vazia, deseja apagar o conteúdo atual e importar o arquivo? (1 = sim) ");

			// adquire a resposta do usuário, se for diferente de 1(sim), a operação é cancelada e a sai do método
			int resposta = controle.opcao();
			controle.texto(); // lê o '\n'
			if(resposta != 1) {
				System.out.println("Cancelado importação do arquivo!");
				return;
			}

			clientes.clear(); // limpa a lista
		}

		String nome_arquivo;
		do {
			System.out.print("Nome do arquivo a ser importado: ");
			nome_arquivo = controle.texto();
		} while(nome_arquivo.equals(""));


		try {
			BufferedReader arquivo = new BufferedReader( new FileReader(nome_arquivo) );
			String linha = arquivo.readLine();

			// percorre cada linha do arquivo
			while(linha != null) {

				// faz parser de cada linha, separando as informações do cliente e colocando em um array de String
				String[] dados_cliente = linha.split(", ");
				boolean genero = dados_cliente[2].contentEquals("Masculino");
				long nascimento = data_para_milisegundos(dados_cliente[3]);

				// cria objeto Pessoa e adiciona na lista
				adicionar_ordenado(new Pessoa(
					dados_cliente[0], // nome
					dados_cliente[1], // telefone
					dados_cliente[4], // cidade
					dados_cliente[5], // bairro
					dados_cliente[6], // rua
					dados_cliente[7], // numero
					dados_cliente[8], // complemento
					nascimento,
					genero
				  )
				);

				linha = arquivo.readLine(); // próxima linha
			}

			arquivo.close();
		} catch (Exception e) {
			System.out.println("Não foi possível abrir o arquivo!");
		}
	}

}