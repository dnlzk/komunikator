package pl.nalazek.komunikator.logika.logowanie;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca u¿ytkownika lokalnego domyœlnego.
 * U¿ywany gdy nie istnieje ¿aden inny profilowany u¿ytkownik programu
 * Klasa jest typu singleton. Tylko jeden u¿ytkownik domyœlny mo¿e istnieæ w programie.
 */
public class UzytkownikDomyslny extends UzytkownikLokalny {
	private static final long serialVersionUID = 1L;
	private static UzytkownikDomyslny referencja;
	
	private UzytkownikDomyslny()
	{ 
		nrUzytkownika = 0; 
		nazwa = "Uzytkownik domyslny";
	}
	
	/** Metoda zwracaj¹ca referencjê do obiektu
	 * @return Referencjê do u¿ytkownika lokalnego
	 */
	public static UzytkownikDomyslny zwrocReferencje()
	{
		if(referencja == null)
			referencja = new UzytkownikDomyslny();
		return referencja;
	}

}
