package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pakiet z odpowiedzi¹ na ¿¹danie danych */
public class DaneOdpowiedz extends PakietOdpowiedz {
	
	private static final long serialVersionUID = 1L;
	
	/** Konstruktor */
	DaneOdpowiedz()
	{
		nazwa = "Pakiet Danych Odpowiedz";
		nrid = NrIdPakietu.DANE_ODPOWIEDZ;
	}
	
	/** Konstruktor *
	 * @param pytanie Pytanie na które ten pakiet bêdziê odpowiada³
	 * @param odpowiedz OdpowiedŸ na pytanie. Wartoœæ "true" oznacza zgodê, wartoœæ "false" brak zgody
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