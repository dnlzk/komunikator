package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Pakiet wysy�any w przypadku zako�czenia rozmowy przez kt�r�� ze stron */
public class RozmowaKoniec extends Pakiet{
		private static final long serialVersionUID = 1L;
		
		/** Konstruktor pakietu
		 * @param rozmowaDocelowa Referencja do rozmowy docelowej
		 * @param rozmowaZrodlowa Referencja do rozmowy �r�d�owej
		 */
		public RozmowaKoniec(RozmowaID rozmowaDocelowa, RozmowaID rozmowaZrodlowa)
		{
			nazwa = "Pakiet Rozmowa Koniec";
			nrid = NrIdPakietu.ROZMOWA_KONIEC;
			this.rozmowaDocelowa = rozmowaDocelowa;
			this.rozmowaZrodlowa = rozmowaZrodlowa;
		}
}
