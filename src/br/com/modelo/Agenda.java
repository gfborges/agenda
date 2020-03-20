package br.com.modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import br.com.negocio.Controle;

public class Agenda  {
	List<Pessoa> clientes = new ArrayList<Pessoa>();

	public void cadastrar() {
		String nome = "",
		       tnum = "",
		       gen = "",
		       nascdt = "";
		String cidade,
		       bairro,
		       rua,
		       complemento,
		       cnum;
		boolean genero;
		long nascms = 0;
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
		nascdt = controle.texto();
		if(!nascdt.equals(""))
			nascms = data_para_milisegundos(nascdt);

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
	

	public void listar_clientes( boolean genero ) {
		for(int i = 0; i < clientes.size(); ++i) {
			Pessoa p = this.clientes.get(i);
			if( !(p.isGenero() ^ genero)  ) {
				System.out.printf("%d. %s\n", i, p.toString());
			}
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

	public int busca(String busca) {
		Controle controle = new Controle();
		for(int i = 0; i < this.clientes.size(); ++i ) {
			Pessoa p = this.clientes.get(i);
			if( p.getNome().toLowerCase().startsWith(busca) ) {
				System.out.println("Está procurando " + p.getNome() + "?(Y/n)");
				String confirmar = controle.texto().toLowerCase();
				if(confirmar.startsWith("y") || confirmar.equals(""))
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
