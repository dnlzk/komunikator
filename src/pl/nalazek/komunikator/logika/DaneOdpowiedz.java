package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca pakiet z odpowiedzi� na ��danie danych */
public class DaneOdpowiedz extends PakietOdpowiedz {
	
	private static final long serialVersionUID = 1L;
	
	/** Konstruktor */
	DaneOdpowiedz()
	{
		nazwa = "Pakiet Danych Odpowiedz";
		nrid = NrIdPakietu.DANE_ODPOWIEDZ;
	}
	
	/** Konstruktor *
	 * @param pytanie Pytanie na kt�re ten pakiet b�dzi� odpowiada�
	 * @param odpowiedz Odpowied� na pytanie. Warto�� "true" oznacza zgod�, warto�� "false" brak zgody
	 */
	DaneOdpowiedz(DanePytanie pytanie, boolean odpowiedz)
	{
		this();
		rozmowaZrodlowa = pytanie.zwrocIdRozmowyDocelowej();
		rozmowaDocelowa = pytanie.zwrocIdRozmowyZrodlowej();
		if(odpowiedz)
		kluczWeryf = pytanie.kluczWeryf / pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy();
		else kluczWeryf = -1;
	}
	
}