package br.com.praiasoft.zipado;

import java.io.IOException;

public class Principal {
	public static void main(String[] args) throws IOException {
		
		Compactador compactador = new Compactador(true);
		
		byte resultadoCompactado[] = compactador.compactarTexto("oratoroeuaroupadoreideroma", "texto-compactado.zipado");
		
	}
}
