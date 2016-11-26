package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Zmienna enum reprezentuj�ca rodzaj przesy�anego pakietu */
public enum NrIdPakietu {
	
	PAKIET(100), WIADOMOSC(101), DANE(102), DANE_PYTANIE(105), DANE_ODPOWIEDZ(106), ROZMOWA_PYTANIE(110), 
	ROZMOWA_ODPOWIEDZ(111),	ROZMOWA_KONIEC(112);
	
	private int wartosc;

    private NrIdPakietu(int wartosc) {
    	this.wartosc = wartosc;
     }
    
    /** Zwraca rodzaj pakietu
     * @param x Liczba ca�kowita okre�laj�ca nr pakietu
     * @return Nazw� pakietu
     */
    public static NrIdPakietu fromInteger(int x)
    {
    	switch(x){
    	case 100: return PAKIET; 
    	case 101: return WIADOMOSC; 
    	case 102: return DANE; 
    	case 105: return DANE_PYTANIE; 
    	case 106: return DANE_ODPOWIEDZ;
    	case 110: return ROZMOWA_PYTANIE;
    	case 111: return ROZMOWA_ODPOWIEDZ;
    	case 112: return ROZMOWA_KONIEC;
    	default: return null;
    	}
    	
    }
    
    /** Zwraca warto�� klasy w postaci liczbowej */
    public int toInt()
    {
    	return wartosc;
    }

}
