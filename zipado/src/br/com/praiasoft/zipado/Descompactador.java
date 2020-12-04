package br.com.praiasoft.zipado;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class Descompactador {
	
	private BitSet bitsTextoCompactado = new BitSet();
	private int tamanhoArvore = 0;
	private int posicao = 0;
	private int leitura = 0;
	private byte textoCompactado[];
	private int deslocamento =0;
	private int digitoVerificador;
	
	public String descompactarTexto( byte[] txtCompac) {
		textoCompactado = txtCompac;
		bitsTextoCompactado = BitSet.valueOf(textoCompactado);
		
        setDigitoVerificador(
        	convertByteArrayToInt(	
        		new byte[] {
        				textoCompactado[textoCompactado.length-4],
        				textoCompactado[textoCompactado.length-3],
        				textoCompactado[textoCompactado.length-2],
        				textoCompactado[textoCompactado.length-1],
        			}
        		)
        	);
		
		
		System.out.println("digito verificador(descompactador): " + getDigitoVerificador());
		
		int numSimbolosArvore = contaNumSimbolosArvore();
		
		System.out.printf("\nTamanho da arvore:  %dbits %dbytes", tamanhoArvore, tamanhoArvore/8+1);
		System.out.printf("\nNumeros de caracteres na arvore: %d", numSimbolosArvore);
		
		int tamanho = textoCompactado.length * 8 - Integer.BYTES; // +1-8;
		leitura = (tamanhoArvore/8+1)*8 + (numSimbolosArvore)*8 ;
		
		No raiz = montaArvore( );
		
		System.out.println("\n\n");
		imprimirArvore(raiz, 0);
		
		System.out.println();
		
		String resposta = "";		
		No noAtual = raiz;
		int bitsLidos = 0;
		for(int w=leitura; w<tamanho; w++) {

			
			if(noAtual.ehFolha()) {
				var simbolo = noAtual.getSimbolo();
//				System.out.print(simbolo);
				resposta += simbolo;
				noAtual = raiz;
			}
			if( bitsLidos == digitoVerificador )
			{
				break;
			}			

			var bit = bitsTextoCompactado.get(w);
			bitsLidos++;
			if(bit) {
//				System.out.print("1");
				noAtual = noAtual.getDireito();
			} else {
//				System.out.print("0");
				noAtual = noAtual.getEsquerdo();
			}
			
			if(noAtual == null) {
//				System.out.print("-");
				noAtual = raiz;
			}
		}
//		System.out.println();
		
		return resposta;
	}
	
	private int convertByteArrayToInt(byte[] intBytes){
	    ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
	    return byteBuffer.getInt();
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

	public int getDigitoVerificador() {
		return digitoVerificador;
	}

	public void setDigitoVerificador(int digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}
	
}
