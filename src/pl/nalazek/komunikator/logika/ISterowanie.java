package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Interfejs obs�uguj�cy Sterowanie aplikacji */
public interface ISterowanie {
	 	
		/** Wysy�a ��danie odebrania wiadomosci przez Sterowanie
		 * @param id Identyfikator rozmowy docelowej w programie
		 * @param wiadomosc Tre�� przesy�anej wiadomo�ci
		 * @param ip Adres Ip nadawcy
		 * */ 
		void odbierzWiadomosc(String ip, RozmowaID id, String wiadomosc);
		 
		/** Wysy�a ��danie odebrania pliku przez Sterowanie
		 * @param ip Adres Ip nadawcy
		 * @param id Identyfikator rozmowy w programie
		 * @param plik Nag��wek przesy�anego pliku
		 * @return Kod odbioru: "0" odmowa, "1" zgoda, "-1" brak odpowiedzi, scie�ka pliku
		 * */ 
	    OdpowiedzSterowania odbierzPlik(String ip, RozmowaID id, PlikNaglowek plik, StanWyslaniaPliku postep);

	    /** Wysy�a ��danie odebrania rozmowy przez Sterowanie
		 * @param ip Adres Ip nadawcy
		 * @param rozmowaId Identyfikator rozmowy w programie
		 * @return Kod odbioru: "0" odmowa, "1" zgoda, "-1"
		 * */ 
	    OdpowiedzSterowania odbierzRozmowe(String ip, RozmowaID rozmowaId);

	    /** Wysy�a ��danie odebrania zako�czenia rozmowy przez Sterowanie
	     * @param ip Adres Ip nadawcy
	     * @param id Identyfikator rozmowy w programie
	     */
	    void odbierzZakonczenieRozmowy(String ip, RozmowaID id);
	    
	    /** Wysy�a ��danie odebrania wyj�tku przez Sterowanie
	     * @param opis Tre�� wyj�tku
	     */
	    void odbierzWyjatek(String opis);

}
