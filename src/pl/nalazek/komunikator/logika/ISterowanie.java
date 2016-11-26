package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Interfejs obs³uguj¹cy Sterowanie aplikacji */
public interface ISterowanie {
	 	
		/** Wysy³a ¿¹danie odebrania wiadomosci przez Sterowanie
		 * @param id Identyfikator rozmowy docelowej w programie
		 * @param wiadomosc Treœæ przesy³anej wiadomoœci
		 * @param ip Adres Ip nadawcy
		 * */ 
		void odbierzWiadomosc(String ip, RozmowaID id, String wiadomosc);
		 
		/** Wysy³a ¿¹danie odebrania pliku przez Sterowanie
		 * @param ip Adres Ip nadawcy
		 * @param id Identyfikator rozmowy w programie
		 * @param plik Nag³ówek przesy³anego pliku
		 * @return Kod odbioru: "0" odmowa, "1" zgoda, "-1" brak odpowiedzi, scie¿ka pliku
		 * */ 
	    OdpowiedzSterowania odbierzPlik(String ip, RozmowaID id, PlikNaglowek plik, StanWyslaniaPliku postep);

	    /** Wysy³a ¿¹danie odebrania rozmowy przez Sterowanie
		 * @param ip Adres Ip nadawcy
		 * @param rozmowaId Identyfikator rozmowy w programie
		 * @return Kod odbioru: "0" odmowa, "1" zgoda, "-1"
		 * */ 
	    OdpowiedzSterowania odbierzRozmowe(String ip, RozmowaID rozmowaId);

	    /** Wysy³a ¿¹danie odebrania zakoñczenia rozmowy przez Sterowanie
	     * @param ip Adres Ip nadawcy
	     * @param id Identyfikator rozmowy w programie
	     */
	    void odbierzZakonczenieRozmowy(String ip, RozmowaID id);
	    
	    /** Wysy³a ¿¹danie odebrania wyj¹tku przez Sterowanie
	     * @param opis Treœæ wyj¹tku
	     */
	    void odbierzWyjatek(String opis);

}
