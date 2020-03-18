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
					
				case 5:
					// listar clientes
					System.out.println("");
					agenda.listar_clientes();
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
			controle.texto(); // Le o \n do ultimo input
			controle.texto(); // Espera pelo Enter
		}
	}

}
