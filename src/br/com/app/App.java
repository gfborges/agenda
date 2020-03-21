package br.com.app;

import java.io.IOException;

import br.com.modelo.Agenda;
import br.com.negocio.Controle;
import br.com.negocio.Menu;

public class App {

	public static void main(String[] args) throws Throwable {
		int op = 10;
		Agenda agenda = new Agenda();
		Controle controle = new Controle();
		while(op != 0) {
			int id = -1;
			String nome = "";
			Menu.menu();
			op = controle.opcao();
			switch(op) {
				case 0:
					System.exit(0);
				case 1:
					// cadastrar cliente
					agenda.cadastrar();				
					break;
				case 2:
					// editar cliente
					do {
						System.out.print("Cliente que deseja editar (id/nome): ");
						if(controle.temInt()) {
							id = controle.opcao();
						}
						else if(controle.temTexto()){
							nome = controle.texto().toLowerCase();
						}else {
							System.out.println("Insira uma opção válida!");
						}
					} while(id < 0 && nome.equals(""));
					if(id >= 0) {
						agenda.editar(id);
					}
					else {
						agenda.editar(nome);
					}
					break;
				case 3:
					// remover cliente
					do{
						System.out.print("Cliente que deseja remover (id/nome): ");
						if(controle.temInt()) {
							id = controle.opcao();
						}
						else if(controle.temTexto()){
							nome = controle.texto().toLowerCase();
						}else {
							System.out.println("Insira uma opção válida!");
						}
					}while(id < 0 || nome.equals(""));
					if(id >= 0) {
						agenda.remover(id);
					}
					else {
						agenda.remover(nome);
					}
					break;
				case 4:
					// Nova compra
					
					// Procurar cliente
					do {
						System.out.print("Procure pelo cliente que fez a compra (id/nome): ");
						if(controle.temInt()) {
							id = controle.opcao();
						}
						else if(controle.temTexto()){
							nome = controle.texto().toLowerCase();
						}else {
							System.out.println("Insira uma opção válida!");
						}
					} while(id < 0 && nome.equals(""));
					if(id >= 0) {
						agenda.input_nova_compra(id);
					}
					else {
						agenda.input_nova_compra(nome);
					}
					
					break;
				case 5:
					// listar clientes
					while( true ) {
						System.out.print("Escolha um genero para listar (ENTER para listar todos): ");
						String gen = controle.texto().toUpperCase();
						if(gen.startsWith("M")) {
							agenda.listar_clientes(true);
							 break;
						}
						else if(gen.startsWith("F")) {
							agenda.listar_clientes(false);
							break;
						}
						else if(gen.equals("")){
							agenda.listar_clientes();
							break;
						}
						System.out.println("Insira uma opcao válida!");
						
					}
					break;
				case 6:
					// relatório
					agenda.relatorio();
					break;
				case 7:
					try {
						agenda.exportar_csv();
					} catch (IOException e) {
						System.out.println("Ocorreu um erro ao exportar para arquivo! Erro: " + e.getMessage());
					}
					break;
				case 8:
					agenda.importar_csv();
					break;
				default:
					System.out.println("\nInsira uma opção valida!");
					break;
			}
			System.out.print("\nPressione Enter para continuar...");
			controle.texto(); // Le o \n do ultimo input

		}
	}

}
