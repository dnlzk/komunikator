package pl.nalazek.komunikator.logika.logowanie;

import java.util.LinkedHashSet;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentująca użytkownika lokalnego */
public class UzytkownikLokalny extends Uzytkownicy {

	private static final long serialVersionUID = 1L;
	private LinkedHashSet<UzytkownikZdalny> kontakty;
	
	protected UzytkownikLokalny()
	{
		nrUzytkownika = licznik;
		kontakty = new LinkedHashSet<UzytkownikZdalny>();
	}
	
	/** Konstruktor tworzący nowego użytkownika lokalnego
	 * @param nazwa Nazwa użytkownika lokalnego
	 * @param haslo Hasło użytkownika lokalnego
	 */
	public UzytkownikLokalny(String nazwa, String haslo)
	{
		this();		
		this.nazwa = nazwa;
		uzytkownicyHasla.put(haslo+"/"+nazwa, this);
	}
	

	/** Zwraca nazwę użytkownika
	 * @return Nazwa użytkownika
	 */
	public String zwrocNazwe()
	{
		return nazwa;
	}
	
	/** Zwraca kopię listy kontaktów
	 * @return Lista kontaktów w postaci zbioru klas UzytkownikZdalny
	 */
	LinkedHashSet<UzytkownikZdalny> zwrocListeKontaktow()
	{
		return kontakty;
	}
}
