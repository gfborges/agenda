package br.com.app;

import br.com.modelo.Agenda;
import br.com.negocio.Controle;
import br.com.negocio.Menu;

public class App {

	public static void main(String[] args) throws Throwable {
		int op = 10;
		Agenda agenda = new Agenda();
		Controle controle = new Controle();
		while(op != 0) {
			Menu.menu();
			op = controle.opcao();
			controle.texto(); // Le o \n do ultimo input
			switch(op) {
				case 0:
					System.exit(0);
				case 1:
					// cadastrar cliente
					agenda.cadastrar();				
					break;
				case 2:
					// editar cliente
					break;
				case 3:
					// remover cliente
					break;
				case 4:
					// Detalhar cliente
					System.out.print("Buscar cliente por id ou nome: ");
					while(true) {
						if(controle.temInt()) {
							int id = controle.opcao();
							
							break;
						}
						else if(controle.temTexto()) {
							String nome = controle.texto();
							
							break;
						}
						else {
							System.out.println("Insira uma opção válida! ");
						}
					}
					break;
				case 5:
					// listar clientes
					System.out.print("Escolha um genero para listar (ENTER para listar todos): ");
					String gen = controle.texto().toUpperCase();
					while( true ) {
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
						else {
							System.out.println("Insira uma opcao válida!");
						}
					}
					break;
				case 6:
					// relatório
					break;
					
				case 7:
					// importar / Exportar
					break;
				default:
					System.out.println("\nInsira uma opção valida!");
					break;
			}
			System.out.print("Pressione Enter para continuar...");
			controle.texto(); // Espera pelo Enter
		}
	}

}
