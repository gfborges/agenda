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
					while(true) {
						System.out.print("Cliente que deseja editar (id/nome): ");
						if(controle.temInt()) {
							id = controle.opcao();
							break;
						}
						else if(controle.temTexto()){
							nome = controle.texto().toLowerCase();
							break;
						}else {
							System.out.println("Insira uma opção válida!");
						}
					}
					if(id >= 0) {
						agenda.editar(id);
					}
					else {
						agenda.editar(nome);
					}
					break;
				case 3:
					// remover cliente
					break;
				case 4:
					// Detalhar cliente
					while(true) {
						System.out.print("Buscar cliente por id ou nome: ");
						if(controle.temInt()) {
							id = controle.opcao();
							break;
						}
						else if(controle.temTexto()) {
							nome = controle.texto();
							break;
						}
						else {
							System.out.println("Insira uma opção válida! ");
						}
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
