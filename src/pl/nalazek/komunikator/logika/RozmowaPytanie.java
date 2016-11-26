package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca pakiet z pytaniem o rozpocz�cie rozmowy */
public class RozmowaPytanie extends PakietPytanie {
	private static final long serialVersionUID = 1L;

	/** Konstruktor pakietu
	 * @param rozmowaZrodlowa Numer id rozmowy �r�d�owej, kt�rej ma dotyczy� odpowied� 
	 */
	public RozmowaPytanie(RozmowaID rozmowaZrodlowa)
	{
		nazwa = "Pakiet Rozmowa Pytanie";
		nrid = NrIdPakietu.ROZMOWA_PYTANIE;
		this.rozmowaZrodlowa = rozmowaZrodlowa;

	}
	
}
