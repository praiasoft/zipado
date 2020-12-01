package br.com.praiasoft.zipado;

public class Prioridade {

	private int tamanho;
	private No nos[];

	public Prioridade(No[] nos) {
		super();
		this.nos = nos;
		construir();
	}
	
	public void inserir( No no ) {
		nos[tamanho++] = no;
		subir(tamanho-1);
	}
	
	public No remove( ) {
		No resp = nos[0];
		nos[0] = nos[tamanho-1];
		tamanho--;
		descer(0, tamanho-1);
		return resp;
	}
	
	public void construir() {
		tamanho = nos.length;
		System.out.println("construindo: tamanho("+tamanho+")");
		for(int w=1; w < tamanho; w++) {
			subir(w);
		}
	}
	
	private void subir( int i ) {
		int j = i/2;
		if( j >= 0 ) {
			if( nos[i].getFrequencia() < nos[j].getFrequencia()) {
				troca(i, j);
				subir( j );
			}
		}
	}
	
	private void descer( int i, int n ) {
		int j = i*2;
		if( j <= n ) {
			if( j < n ) {
				if(nos[j+1].getFrequencia() < nos[j].getFrequencia()) {
					j++;
				}
			} 
			if( nos[i].getFrequencia() > nos[j].getFrequencia() ) {
				troca(i, j);			
				descer(j,n);
			}
		}
	}
	
	private void troca(int x, int y) {
		No aux = nos[x];
		nos[x] = nos[y];
		nos[y] = aux;
	}

	@Override
	public String toString() {
		
		String nosStr = "";
		for( int w=0; w<tamanho; w++) {
			nosStr += nos[w]; // + "\n";  
		}
		
		return "Prioridade [tamanho=" + tamanho + ", nos=" + nosStr + "]";
	}
}
