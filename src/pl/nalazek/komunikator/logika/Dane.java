package pl.nalazek.komunikator.logika;

import java.io.IOException;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pojedyñcz¹ paczkê danych w programie */
public class Dane extends DanePytanie {
	private static final long serialVersionUID = 1L;
	private Plik plik;
	
	/** Konstruktor pakietu danych
	 * @param sciezkaPliku Sciezka pliku do wyslania
	 * @throws Wyjatek 
	 * @throws IOException 
	 * */
	public Dane(RozmowaID rozmowaZrodlowa, RozmowaID rozmowaDocelowa, String sciezkaPliku) throws IOException, Wyjatek 
	{
		nazwa = "Pakiet Danych";
		nrid = NrIdPakietu.DANE;
		this.rozmowaDocelowa = rozmowaDocelowa;
		this.rozmowaZrodlowa = rozmowaZrodlowa;
		plik = new Plik(sciezkaPliku);
		plikInfo = (PlikNaglowek)plik;
	}
	
	/** Zwaraca plik 
	 * @return Plik zawarty w paczce
	 */
	public Plik zwrocPlik()
	{
		return plik;
	}
	
}
	

