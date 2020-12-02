package br.com.praiasoft.zipado;

import java.io.IOException;

public class Principal {
	public static void main(String[] args) throws IOException {
		
		Compactador compactador = new Compactador(true);
		Descompactador descompactador = new Descompactador();
		
		byte resultadoCompactado[] = compactador.compactarTexto("o rato roeu a roupa do rei de roma", "texto-compactado.zipado");

		System.out.println("--------------------------------------------");
		
		String texto = descompactador.descompactarTexto(resultadoCompactado);
		System.out.println(texto);
		
	}
}
