package pl.nalazek.komunikator.logika.logowanie;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca u�ytkownika lokalnego domy�lnego.
 * U�ywany gdy nie istnieje �aden inny profilowany u�ytkownik programu
 * Klasa jest typu singleton. Tylko jeden u�ytkownik domy�lny mo�e istnie� w programie.
 */
public class UzytkownikDomyslny extends UzytkownikLokalny {
	private static final long serialVersionUID = 1L;
	private static UzytkownikDomyslny referencja;
	
	private UzytkownikDomyslny()
	{ 
		nrUzytkownika = 0; 
		nazwa = "Uzytkownik domyslny";
	}
	
	/** Metoda zwracaj�ca referencj� do obiektu
	 * @return Referencj� do u�ytkownika lokalnego
	 */
	public static UzytkownikDomyslny zwrocReferencje()
	{
		if(referencja == null)
			referencja = new UzytkownikDomyslny();
		return referencja;
	}

}
