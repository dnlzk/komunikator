package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pakiet z wiadomoœci¹ */
public class Wiadomosc extends Pakiet {

	private static final long serialVersionUID = 1L;
	private String tresc = "";
	
	/** Konstruktor wiadomoœci 
	 * @param tresc Treœæ przesy³anej wiadomoœci
	 * @param rozmowaZrodlowa Rozmowa Ÿród³owa z której pochodzi wiadomoœæ
	 * */
	public Wiadomosc(String tresc, RozmowaID rozmowaZrodlowa) 
	{
		this.tresc = tresc;
		nazwa = "Pakiet Wiadomosc";
		nrid = NrIdPakietu.WIADOMOSC;
		this.rozmowaZrodlowa = rozmowaZrodlowa;
		
	}
	/** Zwaraca tekst wiadomoœci */
	public String zwrocTekst()
	{
		return tresc;
	}
	
}
