package pl.nalazek.komunikator.logika;

import java.io.*;
import java.util.Random;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca pakiet danych przesy�anych mi�dzy instancjami programu *
 */
public class Pakiet implements Serializable {

	static final long serialVersionUID = 1L;
	String nazwa = "Pakiet Ogolny";
	protected RozmowaID rozmowaZrodlowa, rozmowaDocelowa;
	protected long kluczWeryf;
	NrIdPakietu nrid = NrIdPakietu.PAKIET;
	
	/** Konstruktor */
	public Pakiet()
	{
		Random generator = new Random();
		kluczWeryf = generator.nextLong();
	}
	
	/** Zwraca nazw� pakietu
	 * @return Nazwa pakietu w postaci ci�gu znak�w
	 */
	public String getNazwa()
	{
		return nazwa;
	}
	
	/** Zwraca NrIdPakietu
	 * @return NrIdPakietu
	 */
	public NrIdPakietu getNrId()
	{
		return nrid;
	}
	
	/** Zwraca referencj� do rozmowy �r�d�owej, tzn. nadawcy pakietu *
	 * @return Referencja do sk�adnika rozmowa �r�d�owa.
	 */
	public RozmowaID zwrocIdRozmowyZrodlowej()
	{
		return rozmowaZrodlowa;
	}
	
	/** Zwraca referencj� do rozmowy docelowej, tzn. adresata pakietu *
	 * @return Referencja do rozmowy docelowej
	 */
	public RozmowaID zwrocIdRozmowyDocelowej()
	{
		return rozmowaDocelowa;
	}

	/** Zwraca klucz weryfikacyjny pakietu
	 * @return Klucz weryfikacyjny w postaci long
	 */
	public long zwrocKlucz()
	{
		return kluczWeryf;
	}
	
	/** Dodaje Id rozmowy docelowej *
	 * @param rozmowaDocelowa Id rozmowy docelowej
	 */
	void dodajIdRozmowyDocelowej(RozmowaID rozmowaDocelowa)
	{
		this.rozmowaDocelowa = rozmowaDocelowa;
	}
}
