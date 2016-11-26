package pl.nalazek.komunikator.logika.logowanie;

import java.util.LinkedHashSet;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca u�ytkownika lokalnego */
public class UzytkownikLokalny extends Uzytkownicy {

	private static final long serialVersionUID = 1L;
	private LinkedHashSet<UzytkownikZdalny> kontakty;
	
	protected UzytkownikLokalny()
	{
		nrUzytkownika = licznik;
		kontakty = new LinkedHashSet<UzytkownikZdalny>();
	}
	
	/** Konstruktor tworz�cy nowego u�ytkownika lokalnego
	 * @param nazwa Nazwa u�ytkownika lokalnego
	 * @param haslo Has�o u�ytkownika lokalnego
	 */
	public UzytkownikLokalny(String nazwa, String haslo)
	{
		this();		
		this.nazwa = nazwa;
		uzytkownicyHasla.put(haslo+"/"+nazwa, this);
	}
	

	/** Zwraca nazw� u�ytkownika
	 * @return Nazwa u�ytkownika
	 */
	public String zwrocNazwe()
	{
		return nazwa;
	}
	
	/** Zwraca kopi� listy kontakt�w
	 * @return Lista kontakt�w w postaci zbioru klas UzytkownikZdalny
	 */
	LinkedHashSet<UzytkownikZdalny> zwrocListeKontaktow()
	{
		return kontakty;
	}
}
