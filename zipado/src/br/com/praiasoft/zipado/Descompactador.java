package br.com.praiasoft.zipado;

import java.util.BitSet;

public class Descompactador {
	
	private BitSet bitsTextoCompactado = new BitSet();
	private int tamanhoArvore = 0;
	private int posicao = 0;
	private int leitura = 0;
	private byte textoCompactado[];
	private int deslocamento =0;
	
	public String descompactarTexto( byte[] txtCompac) {
		textoCompactado = txtCompac;
		bitsTextoCompactado = BitSet.valueOf(textoCompactado);
		
		int numSimbolosArvore = contaNumSimbolosArvore();
		
		System.out.printf("\nTamanho da arvore:  %dbits %dbytes", tamanhoArvore, tamanhoArvore/8+1);
		System.out.printf("\nNumeros de caracteres na arvore: %d", numSimbolosArvore);
		
		int tamanho = textoCompactado.length * 8+1;
		leitura = (tamanhoArvore/8+1)*8 + (numSimbolosArvore)*8;
		
		No raiz = montaArvore( );
		
		System.out.println("\n\n");
		imprimirArvore(raiz, 0);
		
		System.out.println();
		
		String resposta = "";		
		No noAtual = raiz;
		for(int w=leitura; w<tamanho; w++) {
			
			if(noAtual.ehFolha()) {
				var simbolo = noAtual.getSimbolo();
	
				resposta += simbolo;
				noAtual = raiz;

			}

			var bit = bitsTextoCompactado.get(w);
			if(bit) {
				noAtual = noAtual.getDireito();
			} else {
				noAtual = noAtual.getEsquerdo();
			}
			
			if(noAtual == null) {
				noAtual = raiz;
			}
		}
		
		return resposta;
	}
	
	private No montaArvore( ) {
		
		boolean bit = bitsTextoCompactado.get( posicao++ );
		
		if(bit) {
			deslocamento++;
			return new No(String.format("%c", textoCompactado[ deslocamento + tamanhoArvore/8]));
		} else {
			return new No(montaArvore(), montaArvore()); 
		}
	}

	private int contaNumSimbolosArvore( ) {
	
		boolean bit = bitsTextoCompactado.get( tamanhoArvore++ );
		
		return bit ? 1 : contaNumSimbolosArvore() + contaNumSimbolosArvore();
	}
	
	private void imprimirArvore(No no, int nivel) {

		nivel++;			

		if(no != null) {
			imprimirArvore(no.getEsquerdo(), nivel+1);
			
			for(int w = 0; w<nivel; w++) {
				System.out.print("--");			
			}
			System.out.printf("%s\n",
					no.getSimbolo() == null ? '*' : no.getSimbolo()
				);
			imprimirArvore(no.getDireito(), nivel+1);
		}
	}
	
}
