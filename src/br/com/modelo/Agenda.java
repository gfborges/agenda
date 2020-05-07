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
import br.com.negocio.Menu;

public class Agenda  {
	private List<Pessoa> clientes = new ArrayList<Pessoa>();
	private String[] produtos = {"Shampoo",
								 "Condicionador",
								 "Pasta de dente",
								 "Enxaguante bucal",
								 "Maquiagem",
								 "Alicate cortador de unha",
								 "Tinta para cabelo",
								 "Descolorante de cabelo"};
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
		do {
			System.out.print("Data de Nascimento (aaaa-MM-dd): ");
			nascdt = controle.texto();
			if(nascdt.equals(""))
				break;
			
			nascms = data_para_milisegundos(nascdt);
		} while(nascms == 0);

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
		System.out.println(p.info());
		System.out.print("Deseja confirmar o cadastro(S/n)? ");
		if( !controle.texto().toLowerCase().startsWith("n") ) {
			adicionar_ordenado(p);
			System.out.println("Cadastro efetuado");
		}
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
		this.perguntar_detalhar();
	}

	private static long data_para_milisegundos(String data) {
		LocalDate nasc;
		try {
			nasc = LocalDate.parse(data);
		}
		catch(Exception e){
			return 0;
		}
		
		return nasc.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

	}
	
	public void listar_clientes() {
		for(int i = 0; i < this.clientes.size(); ++i) {
			System.out.printf("%d. %s\n", i, clientes.get(i).toString());
		}
		System.out.print("Deseja detalhar um dos clientes (Y/n)?");
		if(Menu.confirmar()) {
			Controle  controle = new Controle();
			System.out.print("id do cliente:");
			int id = controle.opcao();
			this.detalhar(id);
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
				p.getEnderecoCSV() + "\n"
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
		for(int i = 0; i < this.clientes.size(); ++i ) {
			Pessoa p = this.clientes.get(i);
			if( p.getNome().toLowerCase().startsWith(busca) ) {
				System.out.println("Está procurando " + p.getNome() + "?(Y/n)");
				if(Menu.confirmar())
					return i;
			}
		}
		System.out.println("Cliente no encontrado");
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
		cidade = p.getEndereco().getCidade();
		if(!cidade.equals(""))
			System.out.println("Cidade: " + cidade);
		System.out.print("Cidade: ");
		cidade = controle.texto();
		if(!cidade.equals("")) {
			p.getEndereco().setCidade(cidade);
		}
		
		bairro = p.getEndereco().getBairro();
		if(!bairro.equals(""))
			System.out.println("Bairro: " + bairro);
		System.out.print("Bairro: ");
		bairro = controle.texto();
		if(!bairro.equals("")) {
			p.getEndereco().setBairro(bairro);
		}
		
		rua = p.getEndereco().getRua();
		if(!rua.equals(""))
			System.out.println("Rua: " + rua);
		System.out.print("Rua: ");
		rua = controle.texto();
		if(!rua.equals("")) {
			p.getEndereco().setRua(rua);
		}
		
		numero = p.getEndereco().getNumero();
		if(!numero.equals(""))
			System.out.println("Numero: " + numero);
		System.out.print("Numero: ");
		numero = controle.texto();
		if(!numero.equals("")) {
			p.getEndereco().setNumero(numero);
		}
		
		complemento = p.getEndereco().getComplemento();
		if(!complemento.equals(""))
			System.out.println("Complemento: " + complemento);
		System.out.print("Complemento: ");
		complemento = controle.texto();
		if(!complemento.equals("")) {
			p.getEndereco().setComplemento(complemento);
		}
		
		remover(i);
		adicionar_ordenado(p);
		return true;
	}
	
	public void remover(int i) {
		this.clientes.remove(i);
	}
	public void remover(String nome) {
		int i = busca(nome);
		if(i >=0 && i < this.clientes.size()  )
			remover(i);
		else
			System.out.println("Cliente nao encontrado");
	}
	
	public void listar_produtos() {
		System.out.println("\n+---------- PRODUTOS ----------+");
		for(int i = 0; i < this.produtos.length; ++i) {
			int x = 25 - this.produtos[i].length();
		    String s = "%"+x+"s|\n";
			System.out.printf(  "| [%d] %s", i, this.produtos[i]);
			System.out.printf(s, " ");
		}
		System.out.println("+------------------------------+");
	}
	
	public void nova_compra(String cliente,  int produto, int quantidade) {
		int i = this.busca(cliente);
		nova_compra(i, produto, quantidade);
	};
	
	public void nova_compra(int i,  int produto, int quantidade) {
		Pessoa p = this.clientes.get(i);
		p.nova_compra(produto , quantidade);	
		System.out.printf("x%d %s adicionados no historico de compras de %s\n",
						   quantidade,
						   this.produtos[produto],
						   p.getNome());
	};
	
	public void input_nova_compra(int id) {
		Controle controle = new Controle();
		// Detalhes da compra
		this.listar_produtos();
		System.out.print("Selecione um produto para adicionar: ");
		int produto = controle.opcao();
		System.out.print("Quantidade comprada: ");
		int quantidade = controle.opcao();
		
		this.nova_compra(id, produto, quantidade);
		
		System.out.print("Deseja adicionar a compra de outro produto nesse usuário (Y/n)?");
		if (Menu.confirmar()) {
			input_nova_compra(id);
		}
		
	}
	
	public void input_nova_compra(String nome) {
		int id = this.busca(nome);
		if(id >= 0) {
			this.input_nova_compra(id);
		}
	}
	
	public void perguntar_detalhar() {
		System.out.print("Deseja detalhar um dos clientes (Y/n)?");
		if(Menu.confirmar()) {
			Controle  controle = new Controle();
			int id  = -1;
			boolean aviso = false;
			do {
				System.out.print("id do cliente:");
				id = controle.opcao();
				if(aviso) {
					System.out.println("Não existe um cliente com esse id");
					aviso = true;
				}
			}while(id < 0  || id >= this.clientes.size());
			this.detalhar(id);
		}
	}
	
	public void detalhar(int id) {
		Pessoa p = this.clientes.get(id);
		System.out.println(p.info());
		for(int i = 0; i < this.produtos.length; ++i) {
			System.out.printf(" [%d] %25s x%d\n", i, this.produtos[i], p.getHist_produtos(i));
		}
	}
	
	public void relatorio() {
		double idade_media = 0, idade_mediaM = 0, idade_mediaF = 0;
		int prod_moda = 0, prod_modaM = 0, prod_modaF = 0;
		int[] prods = new int[8];
		int[] prodsM  = new int[8];
		int[] prodsF = new int[8];
		double size = (double) this.clientes.size(), qM = 0;
		for(Pessoa p : this.clientes) {
			// Media da idade
			idade_media += p.getIdade() / size;
			if(p.isGenero()) {
				// Masculino
				idade_mediaM += p.getIdade();
				qM++;
			}
			else {
				// Feminino
				idade_mediaF += p.getIdade();
			}
			
			// Moda dos produtos
			int[] prod = p.getHist_produtos();
			for(int i = 0; i < prod.length; ++i) {
				prods[i] += prod[i];
				if(p.isGenero()) {
					// Masculino
					prodsM[i] += prod[i];
				}else {
					// Feminino
					prodsF[i] += prod[i];
				}
			}
		}
		idade_mediaM = idade_mediaM / qM;
		idade_mediaF = idade_mediaF / (size - qM);
		// Calculando modas
		for(int i = 1; i < prods.length; ++i) {
			if(prods[i] > prods[prod_moda])
				prod_moda = i;
			if(prodsM[i] > prodsM[prod_modaM])
				prod_modaM = i;
			if(prodsF[i] > prodsF[prod_modaF])
				prod_modaF = i;
		}
		
		// imprimindo estatisticas
		
			// Medias
		System.out.println("A media de idade e de " + idade_media);
		System.out.println("A media de idade entre os homens e de " + idade_mediaM);
		System.out.println("A media de idade entre as mulheres e de " + idade_mediaF);
			// Modas
		System.out.println("O produto mais vendido e " + this.produtos[prod_moda]);
		System.out.println("O produto mais vendido entre homens e " + this.produtos[prod_modaM]);
		System.out.println("O produto mais vendido entre mulheres e " + this.produtos[prod_modaF]);
	}
}
