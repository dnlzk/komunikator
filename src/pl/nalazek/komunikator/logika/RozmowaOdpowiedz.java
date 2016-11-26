package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca pakiet z opdowiedzi� na ��danie rozpocz�cia rozmowy */
public class RozmowaOdpowiedz extends PakietOdpowiedz {
	private static final long serialVersionUID = 1L;
	

	
	/** Konstruktor pakietu
	 * @param pytanie Referencja do pakietu pytaj�cego
	 * @param rozmowaZrodlowa Referencja do utworzonej rozmowy w odpowiedzi na pakiet pytaj�cy. Warto�� "null" oznacza brak zgody.
	 */
	public RozmowaOdpowiedz(RozmowaPytanie pytanie, RozmowaID rozmowaZrodlowa)
	{
		nazwa = "Pakiet Rozmowa Odpowiedz";
		nrid = NrIdPakietu.ROZMOWA_ODPOWIEDZ;
		rozmowaDocelowa = pytanie.zwrocIdRozmowyZrodlowej();
		this.rozmowaZrodlowa = rozmowaZrodlowa;
		if(rozmowaZrodlowa.zwrocIdRozmowy() != 0) kluczWeryf = pytanie.kluczWeryf / pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy();
		else kluczWeryf = -1;
	}
	

}
