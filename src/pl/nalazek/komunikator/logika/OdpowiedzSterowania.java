package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca komunikat wysy�any przez Sterowanie do Modelu */
public class OdpowiedzSterowania {
	public Integer kod;
	public String sciezka;
	
	/** Konstruktor
	 * @param kod Kod jaki przyjmuje klasa
	 */
	public OdpowiedzSterowania(Integer kod)
	{
		this.kod = new Integer(kod);
	}
	
	/** Konstruktor
	 * @param kod Kod jaki przyjmuje klasa
	 * @param sciezka �cie�ka pliku wybrana przez u�ytkownika lokalnego. 
	 * Konstruktor u�ywany w przypadku u�ycia klasy OdpowiedzSterowania do wygenerowania odpowiedzi na ��danie danych
	 */
	public OdpowiedzSterowania(Integer kod, String sciezka)
	{
		this.kod =  new Integer(kod);
		this.sciezka = new String(sciezka);
	}
}
