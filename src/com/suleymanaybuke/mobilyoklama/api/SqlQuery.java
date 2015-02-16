package com.suleymanaybuke.mobilyoklama.api;

public interface SqlQuery {

	public static final String SELECT_ALL_EXAM = "SELECT s.id, d.adi as DersAdi  , t.adi as Turu , s.tarih as Tarihi , s.sinif as Sinifi from ders d , sinavTuru t, sinav s WHERE d.id = s.dersID and t.id=s.turID ";
	public static final String SELECT_ALL_STUDENT_DATA = " SELECT no , fotograf, ogrenci.adi , soyadi , tc , sinif , fakulte.adi as fakAdi, bolum.adi as bolumAdi, aktifDonem from ogrenci , fakulte , bolum where ogrenci.bolumID = bolum.id and ogrenci.fakID = fakulte.id and ogrenci.no = ";
//	public static final String SELECT_OGRENCI_SINAV = "select ogrenciID as ogrenci from sinavOgrenci where ogrenciID = %ogrenciNo% and sinavID = %sinavID%";
	public static final String SELECT_ALL_OGRENCI_SINAV = "select o.adi , o.soyadi , o.no , o.tc, o.sinif, o.aktifDonem, o.fotograf , f.adi as fakulteAdi , b.adi  as bolumAdi from sinavOgrenci so, ogrenci o, fakulte f, bolum b where sinavID = %sinavID% and so.ogrenciID = o.no and f.id = o.fakID and b.id = o.bolumID";
}
