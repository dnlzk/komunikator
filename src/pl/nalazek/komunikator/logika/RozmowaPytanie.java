package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pakiet z pytaniem o rozpoczêcie rozmowy */
public class RozmowaPytanie extends PakietPytanie {
	private static final long serialVersionUID = 1L;

	/** Konstruktor pakietu
	 * @param rozmowaZrodlowa Numer id rozmowy Ÿród³owej, której ma dotyczyæ odpowiedŸ 
	 */
	public RozmowaPytanie(RozmowaID rozmowaZrodlowa)
	{
		nazwa = "Pakiet Rozmowa Pytanie";
		nrid = NrIdPakietu.ROZMOWA_PYTANIE;
		this.rozmowaZrodlowa = rozmowaZrodlowa;

	}
	
}
