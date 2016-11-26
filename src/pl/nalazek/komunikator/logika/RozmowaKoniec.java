package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Pakiet wysy³any w przypadku zakoñczenia rozmowy przez któr¹œ ze stron */
public class RozmowaKoniec extends Pakiet{
		private static final long serialVersionUID = 1L;
		
		/** Konstruktor pakietu
		 * @param rozmowaDocelowa Referencja do rozmowy docelowej
		 * @param rozmowaZrodlowa Referencja do rozmowy Ÿród³owej
		 */
		public RozmowaKoniec(RozmowaID rozmowaDocelowa, RozmowaID rozmowaZrodlowa)
		{
			nazwa = "Pakiet Rozmowa Koniec";
			nrid = NrIdPakietu.ROZMOWA_KONIEC;
			this.rozmowaDocelowa = rozmowaDocelowa;
			this.rozmowaZrodlowa = rozmowaZrodlowa;
		}
}
