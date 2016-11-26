package pl.nalazek.komunikator.logika;

import java.io.*;
import java.util.Random;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pakiet danych przesy³anych miêdzy instancjami programu *
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
	
	/** Zwraca nazwê pakietu
	 * @return Nazwa pakietu w postaci ci¹gu znaków
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
	
	/** Zwraca referencjê do rozmowy Ÿród³owej, tzn. nadawcy pakietu *
	 * @return Referencja do sk³adnika rozmowa Ÿród³owa.
	 */
	public RozmowaID zwrocIdRozmowyZrodlowej()
	{
		return rozmowaZrodlowa;
	}
	
	/** Zwraca referencjê do rozmowy docelowej, tzn. adresata pakietu *
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
