package br.com.negocio;

import java.util.Scanner;

public class Controle {
	public Scanner scanner;
	
	public Controle() {
		this.scanner = new Scanner(System.in);
	}
	
	public String texto() {
		String s = scanner.nextLine();
		return s;
	}
	
	public int opcao() {
		int i = scanner.nextInt();
		return i;
	}
	
	public void fim() throws Throwable {
		scanner.close();
	}
	
	public boolean temInt() {
		return scanner.hasNextInt();
	}
	public boolean temTexto() {
		return scanner.hasNextLine();
	}

}
