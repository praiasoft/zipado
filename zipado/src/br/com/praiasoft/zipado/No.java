package br.com.praiasoft.zipado;

public class No {
	private String simbolo;
	private long frequencia;
	
	private No esquerdo;
	private No direito;
	
	private String bits;
	
	public No(String simbolo) {
		super();
		this.simbolo = simbolo;
	}
	
	public No(No no) {
		simbolo = no.getSimbolo();
		frequencia = no.getFrequencia();
	}

	public No(No noEsquerdo, No noDireito) {
		esquerdo = noEsquerdo;
		direito = noDireito;
		frequencia = esquerdo.frequencia + direito.frequencia;
	}

	public void incrementaFrequencia() {
		frequencia++;
	}
	
	public boolean ehFolha() {
		return ((direito == null) && (esquerdo == null) );
	}
	
	public long getFrequencia() {
		return frequencia;
	}
	public void setFrequencia(long frequencia) {
		this.frequencia = frequencia;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	@Override
	public String toString() {
		return "No [simbolo=" + simbolo + ", frequencia=" + frequencia + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (frequencia ^ (frequencia >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		No other = (No) obj;
		if (frequencia != other.frequencia)
			return false;
		return true;
	}

	public No getEsquerdo() {
		return esquerdo;
	}

	public No getDireito() {
		return direito;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		this.bits = bits;
	}

	public void setEsquerdo(No esquerdo) {
		this.esquerdo = esquerdo;
	}

	public void setDireito(No direito) {
		this.direito = direito;
	}

}
