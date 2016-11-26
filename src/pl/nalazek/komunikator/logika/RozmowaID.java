package pl.nalazek.komunikator.logika;

import java.io.Serializable;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa zawieraj�ca nr identyfikacyjny rozmowy na komputerze lokalnym */
public class RozmowaID implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idRozmowy;
	private String nazwaUzytkownikaLokalnego;
	
	/** Konstruktor */
	public RozmowaID()
	{
		idRozmowy = System.currentTimeMillis();
	}
	
	/** Konstruktor z parametrem zmieniaj�cym nr id 
	 * @param zmienna Tablica zmiennych typu Integer na podstawie kt�rych zmieniany jest nr id
	 */
	RozmowaID(Integer[] zmienna)
	{
		this();
		for(Integer a : zmienna)
			idRozmowy -= a;
	}

	/** Konstruktor z parametrem
	 * @param nr Liczba ca�kowita kt�r� b�dzie nr id
	 */
	RozmowaID(int nr)
	{
		idRozmowy = nr;
	}
	
	/** Konstruktor z parametrami
	 * @param nr Liczba ca�kowita kt�r� b�dzie nr id
	 * @param a Nazwa u�ytkownika lokalnego, kt�ry b�dzie w�a�cicielem rozmowy
	 */
	RozmowaID(int nr, String a)
	{
		idRozmowy = nr;
		nazwaUzytkownikaLokalnego = a;
	}
	
	/** Zwraca id rozmowy w postaci liczbowej
	 * @return Liczba ca�kowita b�d�ca nr id rozmowy
	 */
	long zwrocIdRozmowy()
	{
		return idRozmowy;
	}
	
	/** Zwraca id rozmowy w postaci liczbowej
	 * @return Liczba ca�kowita b�d�ca nr id rozmowy
	 */
	long toLong()
	{
		return idRozmowy;
	}
	
	/** Ustawia nazw� u�ytkownika, kt�ry jest w�a�cicielem rozmowy
	 * @param nazwa Nazwa u�ytkownika
	 */
	void ustawNazweUzytkownika(String nazwa)
	{
		nazwaUzytkownikaLokalnego = nazwa;
	}
	
	/** Zwraca nazw� u�ytkownika, kt�ry jest w�a�cicielem rozmowy
	 * @return Nazwa u�ytkownika, w�a�ciciela rozmowy
	 */
	String zwrocNazweUzykownika()
	{
		return nazwaUzytkownikaLokalnego;
	}
}
