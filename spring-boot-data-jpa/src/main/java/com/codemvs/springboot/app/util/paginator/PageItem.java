package com.codemvs.springboot.app.util.paginator;

public class PageItem {

	private int numero; 
	private boolean acutal;
	public PageItem(int numero, boolean acutal) {		
		this.numero = numero;
		this.acutal = acutal;
	}
	public int getNumero() {
		return numero;
	}
	public boolean isAcutal() {
		return acutal;
	}
	
	
}
