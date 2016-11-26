package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca pakiet z wiadomo�ci� */
public class Wiadomosc extends Pakiet {

	private static final long serialVersionUID = 1L;
	private String tresc = "";
	
	/** Konstruktor wiadomo�ci 
	 * @param tresc Tre�� przesy�anej wiadomo�ci
	 * @param rozmowaZrodlowa Rozmowa �r�d�owa z kt�rej pochodzi wiadomo��
	 * */
	public Wiadomosc(String tresc, RozmowaID rozmowaZrodlowa) 
	{
		this.tresc = tresc;
		nazwa = "Pakiet Wiadomosc";
		nrid = NrIdPakietu.WIADOMOSC;
		this.rozmowaZrodlowa = rozmowaZrodlowa;
		
	}
	/** Zwaraca tekst wiadomo�ci */
	public String zwrocTekst()
	{
		return tresc;
	}
	
}
