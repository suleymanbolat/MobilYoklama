package com.suleymanaybuke.mobilyoklama.object;

import java.io.Serializable;

public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String no;
	private String adi;
	private String soyadi;
	private String tc;
	private String sinif;
	private String fakulte;
	private String bolum;
	private String aktifDonem;
	private String fotograf;
//	private byte[] fotograf;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no.trim();
	}
	public String getAdi() {
		return adi;
	}
	public void setAdi(String adi) {
		this.adi = adi.trim();
	}
	public String getSoyadi() {
		return soyadi;
	}
	public void setSoyadi(String soyadi) {
		this.soyadi = soyadi.trim();
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc.trim();
	}
	public String getSinif() {
		return sinif;
	}
	public void setSinif(String sinif) {
		this.sinif = sinif.trim();
	}
	public String getFakulte() {
		return fakulte;
	}
	public void setFakulte(String fakulte) {
		this.fakulte = fakulte.trim();
	}
	public String getBolum() {
		return bolum;
	}
	public void setBolum(String bolum) {
		this.bolum = bolum.trim();
	}
	public String getAktifDonem() {
		return aktifDonem;
	}
	public void setAktifDonem(String aktifDonem) {
		this.aktifDonem = aktifDonem.trim();
	}
//	public byte[] getFotograf() {
//		return fotograf;
//	}
//	public void setFotograf(byte[] fotograf) {
//		this.fotograf = fotograf;
//	}
	public String getFotograf() {
		return fotograf;
	}
	public void setFotograf(String fotograf) {
		this.fotograf = fotograf;
	}
	

}
