package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca pakiet z ¿¹daniem odebrania danych */
public class DanePytanie extends PakietPytanie {
	
	private static final long serialVersionUID = 1L;
	protected PlikNaglowek plikInfo;

	/** Konstruktor */
	public DanePytanie()
	{	
		nazwa = "Pakiet Danych Pytanie";
		nrid = NrIdPakietu.DANE_PYTANIE;
	}
	
	/** Konstruktor
	 * @param dane Paczka danych która ma zostaæ wys³ana
	 */
	public DanePytanie(Dane dane)
	{
		this();
		plikInfo = dane.plikInfo;
		rozmowaDocelowa = dane.rozmowaDocelowa;
		rozmowaZrodlowa = dane.rozmowaZrodlowa;
	}
	
	/** Zwraca informacjê o pliku
	 * @return Informacja o pliku w postaci klasy PlikNaglowek
	 */
	public PlikNaglowek zwrocPlikInfo()
	{
		return plikInfo;
	}	
	
}
