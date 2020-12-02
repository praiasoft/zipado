package br.com.praiasoft.zipado;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Compactador {
	
	private boolean depurar = true;	
	private Map<String, No> nosEncontrados = new HashMap<String, No>();
	private BitSet bitsCompactados = new BitSet();
	private BitSet arvoreCompactada = new BitSet();
	
	public Compactador(boolean depurar) {
		super();
		this.depurar = depurar;
	}

	public byte[] compactarTexto( String texto, String nomeArquqivo ) throws IOException {
				
		Prioridade prioridade;
		int numeroDeCaracteresEncontrados = 0;
		
		for(String simbolo: texto.split("")) {
			No no = nosEncontrados.get(simbolo);
			if( no == null ) {
				no = new No(simbolo);
				nosEncontrados.put(simbolo, no);				
				numeroDeCaracteresEncontrados++;
			}
			no.incrementaFrequencia();
		}
		
		System.out.println("Número de caracteres encontrados: " + numeroDeCaracteresEncontrados );

		prioridade = new Prioridade( (No[]) nosEncontrados.values().toArray( new No[nosEncontrados.size()] ) );
		
		System.out.println(prioridade);
		
		for(int w=0; w < numeroDeCaracteresEncontrados-1; w++) {
			
			No noEsquerdo = prioridade.remove();
			No noDireito = prioridade.remove();
			
			No novo = new No( noEsquerdo, noDireito );
			prioridade.inserir(novo);
		}
		
		No raiz = prioridade.remove();
		
		geraSequenciaDeBits(raiz,0, "");
		
		System.out.println(texto);
		
		int totalBits = 0;
		String bitsCompactadosStr = "";
		for(String simbolo: texto.split("")) {
			
			String bitsSimbolo = nosEncontrados.get(simbolo).getBits();
			for(byte s: bitsSimbolo.getBytes()) {
				if(s == '1') {
					bitsCompactados.set(totalBits);
				}
				totalBits++;
			}
			bitsCompactadosStr += bitsSimbolo +" ";
		}
		

		System.out.println(bitsCompactadosStr);
		
		var simbolosOrdenados = simbolosOrdenados(raiz);
		var arvoreCompactadaStr = compactaArvore(raiz);
		
		var arvoreCompactadaBytes = arvoreCompactadaStr.getBytes();
		for(int w=0; w < arvoreCompactadaStr.length(); w++) {
			if(arvoreCompactadaBytes[w] == '1') {
				arvoreCompactada.set(w);
			}
		}
		
		System.out.printf("\nArvore compactada: %s-%s", arvoreCompactadaStr, simbolosOrdenados);
		
		byte[] arrayTxt = String.format("%s\n%s\n%s\n%s",texto, arvoreCompactadaStr, simbolosOrdenados, bitsCompactadosStr).getBytes();
		File fileTxt = new File(nomeArquqivo + ".txt");
		FileOutputStream fosTxt = new FileOutputStream(fileTxt);
		fosTxt.write(arrayTxt);
		fosTxt.flush();
		fosTxt.close();
		
		ByteArrayOutputStream  bytesArquivoCompactado = new ByteArrayOutputStream();
		bytesArquivoCompactado.write(arvoreCompactada.toByteArray());
		bytesArquivoCompactado.write(simbolosOrdenados.getBytes());
		bytesArquivoCompactado.write(bitsCompactados.toByteArray());
		
		System.out.println("\n\nTamanho texto original: " + texto.length());
		System.out.println("Tamanho arquivo compactado: " + bytesArquivoCompactado.size());
		
		File file = new File(nomeArquqivo);
		FileOutputStream fos = new FileOutputStream(file);
		
		fos.write(bytesArquivoCompactado.toByteArray());
		fos.flush();
		fos.close();
		
		return bytesArquivoCompactado.toByteArray();
	}
	
	private String simbolosOrdenados(No no) {
		
		if(no != null) {
			var esquerdo = simbolosOrdenados(no.getEsquerdo());
			var direito = simbolosOrdenados(no.getDireito());
			
			if(esquerdo.isEmpty() && direito.isEmpty()) {
				return String.format("%s", no.getSimbolo());
			} 
			return esquerdo + direito;
		}
		return "";
	}

	public void geraSequenciaDeBits(No no, int nivel, String bits) {

		nivel++;			

		if(no != null) {
			geraSequenciaDeBits(no.getEsquerdo(), nivel+1, bits+"0");
			
			no.setBits(bits);
			
			for(int w = 0; w<nivel; w++) {
				System.out.print("--");			
			}
			System.out.printf("%s%d-%s\n",
					no.getSimbolo() == null ? '*' : no.getSimbolo(),
					no.getFrequencia(),
					no.getSimbolo() == null ? "" : no.getBits()
				);
			geraSequenciaDeBits(no.getDireito(), nivel+1, bits+"1");
		}
	}
	
	public String compactaArvore(No no) {
		
		if(no != null) {
			var esquerdo = compactaArvore(no.getEsquerdo());
			var direito = compactaArvore(no.getDireito());
			
			if(esquerdo.isEmpty() && direito.isEmpty()) {
				// return String.format("%c", no.getSimbolo());
				return "1";
			} 
			return "0" + esquerdo + direito;
		}
		return "";
	}
}
