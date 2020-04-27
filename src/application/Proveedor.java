package application;

import java.sql.Date;

public class Proveedor {
	private String cif;
	private String razSoc;
	private int regNot;
	private String segRespon;
	private float segImpo;
	private Date fechHomo;
	
	public Proveedor() {}

	public Proveedor(String cif, String razSoc, int regNot, String segRespon, float segImpo,Date fechHomo) {
		super();
		this.cif = cif;
		this.razSoc = razSoc;
		this.regNot = regNot;
		this.segRespon = segRespon;
		this.segImpo = segImpo;
		this.fechHomo = fechHomo;
	}

	
	
	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getRazSoc() {
		return razSoc;
	}

	public void setRazSoc(String razSoc) {
		this.razSoc = razSoc;
	}

	public int getRegNot() {
		return regNot;
	}

	public void setRegNot(int regNot) {
		this.regNot = regNot;
	}

	public String getSegRespon() {
		return segRespon;
	}

	public void setSegRespon(String segRespon) {
		this.segRespon = segRespon;
	}

	public float getSegImpo() {
		return segImpo;
	}

	public void setSegImpo(float segImpo) {
		this.segImpo = segImpo;
	}

	public Date getFechHomo() {
		return fechHomo;
	}

	public void setFechHomo(Date fechHomo) {
		this.fechHomo = fechHomo;
	}



}