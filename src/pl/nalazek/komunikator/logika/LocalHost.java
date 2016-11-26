package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Zmienna enum reprezentuj¹ca wybór trybu pracy online */
public enum LocalHost {
	IP("Adres w sieci zewnêtrznej"), LOCALHOST1("Pierwszy serwer lokalny"), LOCALHOST2("Drugi serwer lokalny");
	
	private String opis;
	private LocalHost(String opis)
	{
		this.opis = opis;
	}

	/** Tworzy zmienn¹ Localhost z liczby ca³kowitej
	 * @param nr 0:IP, 1:LOCALHOST1, 2:LOCALHOST2
	 * @return enum LocalHost
	 */
	public static LocalHost fromInt(int nr)
	{
		switch(nr)
		{
		case 0: return LocalHost.IP; 
		case 1: return LocalHost.LOCALHOST1; 
		case 2: return LocalHost.LOCALHOST2;
		}
		return null;
	}
	
	/** Zwraca opis zmiennej
	 * @return Opis zmiennej
	 */
	public String getDescription()
	{
		return opis;
	}

}
