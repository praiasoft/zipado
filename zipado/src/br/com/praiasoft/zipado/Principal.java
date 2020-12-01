package br.com.praiasoft.zipado;

import java.util.Scanner;

public class Principal {
	
	static No nos[] = new No[256];
	
	public static void main(String[] args) {
		
		No nosEncontrados[];
		Prioridade prioridade;
		int numeroDeCaracteresEncontrados = 0;
		
		for(int w = 0; w < 256; w++) {
			nos[w] = new No( (byte) w );
		}
		
		String texto;
		try(var entrada = new Scanner(System.in)) {
			System.out.println("Entre com a frase para ser compactada:");
			// texto = entrada.nextLine();
			// texto = "yyyyyytttttrrrreeewwqpppppppp";
			texto = "rodrigooliveiradejesussssssssss000000";
		}
		
		for(int simbolo: texto.getBytes()) {
			if(nos[simbolo].getFrequencia() == 0 ) {
				numeroDeCaracteresEncontrados++;
			}
			nos[simbolo].incrementaFrequencia();
		}
		
		System.out.println("Número de caracteres encontrados: " + numeroDeCaracteresEncontrados );
		nosEncontrados = new No[numeroDeCaracteresEncontrados];
		int contador = 0;
		for(int w = 0; w < 256; w++) {
			if(nos[w].getFrequencia() > 0) {
				nosEncontrados[contador++] = new No(nos[w]);
				System.out.println(nos[w]);				
			}
		}
		
		prioridade = new Prioridade(nosEncontrados);
		
		System.out.println(prioridade);
		
		for(int w=0; w < numeroDeCaracteresEncontrados-1; w++) {
			
			No noEsquerdo = prioridade.remove();
			No noDireito = prioridade.remove();
			
			No novo = new No( noEsquerdo, noDireito );
			prioridade.inserir(novo);
		}
		
		System.out.println(prioridade);
		
		No raiz = prioridade.remove();
		
		geraSequenciaDeBits(raiz,0, "");
		
		System.out.println(texto);
		
		long totalBits = 0;
		for(int simbolo: texto.getBytes()) {
			System.out.print( nos[simbolo].getBits() +" ");
			totalBits += nos[simbolo].getBits().length();
		}
			
		System.out.println(
				"\n\nTotal de bytes compactados: " + (texto.length() - totalBits/8));
						
		
	}
	
	public static void geraSequenciaDeBits(No no, int nivel, String bits) {

		nivel++;			

		if(no != null) {
			geraSequenciaDeBits(no.getEsquerdo(), nivel+1, bits+"0");
			
			no.setBits(bits);
			
			for(int w = 0; w<nivel; w++) {
				System.out.print("--");			
			}
			System.out.printf("%c%d-%s\n",
					no.getSimbolo() == 0 ? '*' : no.getSimbolo(),
					no.getFrequencia(),
					no.getSimbolo() == 0 ? "" : no.getBits()
				);
			
			nos[no.getSimbolo()] = no;
			
			geraSequenciaDeBits(no.getDireito(), nivel+1, bits+"1");
		}
		
	}
}
