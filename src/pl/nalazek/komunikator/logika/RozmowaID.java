package pl.nalazek.komunikator.logika;

import java.io.Serializable;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa zawieraj¹ca nr identyfikacyjny rozmowy na komputerze lokalnym */
public class RozmowaID implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idRozmowy;
	private String nazwaUzytkownikaLokalnego;
	
	/** Konstruktor */
	public RozmowaID()
	{
		idRozmowy = System.currentTimeMillis();
	}
	
	/** Konstruktor z parametrem zmieniaj¹cym nr id 
	 * @param zmienna Tablica zmiennych typu Integer na podstawie których zmieniany jest nr id
	 */
	RozmowaID(Integer[] zmienna)
	{
		this();
		for(Integer a : zmienna)
			idRozmowy -= a;
	}

	/** Konstruktor z parametrem
	 * @param nr Liczba ca³kowita któr¹ bêdzie nr id
	 */
	RozmowaID(int nr)
	{
		idRozmowy = nr;
	}
	
	/** Konstruktor z parametrami
	 * @param nr Liczba ca³kowita któr¹ bêdzie nr id
	 * @param a Nazwa u¿ytkownika lokalnego, który bêdzie w³aœcicielem rozmowy
	 */
	RozmowaID(int nr, String a)
	{
		idRozmowy = nr;
		nazwaUzytkownikaLokalnego = a;
	}
	
	/** Zwraca id rozmowy w postaci liczbowej
	 * @return Liczba ca³kowita bêd¹ca nr id rozmowy
	 */
	long zwrocIdRozmowy()
	{
		return idRozmowy;
	}
	
	/** Zwraca id rozmowy w postaci liczbowej
	 * @return Liczba ca³kowita bêd¹ca nr id rozmowy
	 */
	long toLong()
	{
		return idRozmowy;
	}
	
	/** Ustawia nazwê u¿ytkownika, który jest w³aœcicielem rozmowy
	 * @param nazwa Nazwa u¿ytkownika
	 */
	void ustawNazweUzytkownika(String nazwa)
	{
		nazwaUzytkownikaLokalnego = nazwa;
	}
	
	/** Zwraca nazwê u¿ytkownika, który jest w³aœcicielem rozmowy
	 * @return Nazwa u¿ytkownika, w³aœciciela rozmowy
	 */
	String zwrocNazweUzykownika()
	{
		return nazwaUzytkownikaLokalnego;
	}
}
