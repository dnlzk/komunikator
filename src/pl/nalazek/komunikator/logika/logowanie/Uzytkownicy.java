package pl.nalazek.komunikator.logika.logowanie;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca u�ytkownik�w w programie */
public class Uzytkownicy implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int licznik;
	protected Integer nrUzytkownika = 0;
	protected String nazwa;
	static protected volatile HashMap<String,UzytkownikLokalny> uzytkownicyHasla;
	private HashMap<String,UzytkownikLokalny> uzytkownicyHaslaSer;
	
	/** Konstruktor */
	public Uzytkownicy()
	{
		if(uzytkownicyHasla == null) 
			{
			uzytkownicyHasla = new HashMap<String,UzytkownikLokalny>();
			uzytkownicyHasla.put("domyslny/domyslny",UzytkownikDomyslny.zwrocReferencje());
			}
		licznik++;
	}
	
	/** Zwraca uzytkownika lokalnego na podstawie poprawnie wpisanych danych weryfikacyjnych.
	 * @param hasloUzytkownik Parametry wpisane w postaci "has�o/u�ytkownik". Parametr domyslny/domyslny zwraca u�ytkownika domyslnego
	 * @return Uzytkownika lokalnego w przypadku poprawnej weryfikacji, null w przypadku niepoprawnej
	 */
	UzytkownikLokalny zwrocUzytkownikaLokalnego(String hasloUzytkownik)
	{
		return uzytkownicyHasla.get(hasloUzytkownik);
	}

	/** Zwraca numer u�ytkownika w systemie */
	public Integer zwrocNrUzytkownika()
	{
		return nrUzytkownika;
	}
	
	/** Zapisuje statyczn� tablic� u�ytkownik�w lokalnych do zmiennej niestatycznej */
	void zapiszUzytkownikow()
	{
		uzytkownicyHaslaSer = uzytkownicyHasla;
	}
	
	/** Odczytuje niestatyczn� tablic� u�ytkownik�w lokalnych i wczytuje j� do zmiennej statycznej */
	void wczytajUzytkownikow()
	{
		uzytkownicyHasla = uzytkownicyHaslaSer;
	}
}
