package it.polito.tdp.extflightdelays.model;

public class CoppiaId {
	
	int idPartenza;
	int idArrivo;
	int AVGDistanza;
	
	public CoppiaId(int idPartenza, int idArrivo, int AVGDistanza) {
		super();
		this.idPartenza = idPartenza;
		this.idArrivo = idArrivo;
		this.AVGDistanza = AVGDistanza;
	}
	
	public int getIdPartenza() {
		return idPartenza;
	}
	public void setIdPartenza(int idPartenza) {
		this.idPartenza = idPartenza;
	}
	public int getIdArrivo() {
		return idArrivo;
	}
	public void setIdArrivo(int idArrivo) {
		this.idArrivo = idArrivo;
	}

	public int getAVGDistanza() {
		return AVGDistanza;
	}

	public void setAVGDistanza(int aVGDistanza) {
		AVGDistanza = aVGDistanza;
	}
	
	
	
	

}
