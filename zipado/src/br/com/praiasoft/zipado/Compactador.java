package br.com.praiasoft.zipado;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Compactador {
	
	private boolean depurar = true;		
	
	private Map<String, No> nosEncontrados = new HashMap<String, No>();
	
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
		
		long totalBits = 0;
		String bitsCompactados = "";
		for(String simbolo: texto.split("")) {
			bitsCompactados += nosEncontrados.get(simbolo).getBits() +" ";
			totalBits += nosEncontrados.get(simbolo).getBits().length();
		}
		
		System.out.println(bitsCompactados);
		
		var arvoreCompactada = compactaArvore(raiz);
		var simbolosOrdenados = simbolosOrdenados(raiz);
		
		System.out.printf("\nArvore compactada: %s-%s", arvoreCompactada, simbolosOrdenados);
		
		
		System.out.println(
				"\n\nTotal de bytes compactados: " + (texto.length() - totalBits/8));
		
		byte[] array = String.format("%s\n%s\n%s\n%s",texto, arvoreCompactada, simbolosOrdenados, bitsCompactados).getBytes();
		File file = new File(nomeArquqivo + ".txt");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(array);
		fos.flush();
		fos.close();
		
		return array;
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
