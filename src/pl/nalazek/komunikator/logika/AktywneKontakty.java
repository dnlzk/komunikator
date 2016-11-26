package pl.nalazek.komunikator.logika;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa obs³uguj¹ca kontakty dla aktualnego u¿ytownika zalogowanego w programie */
public class AktywneKontakty {
	
	private LinkedHashSet<UzytkownikZdalny> kontakty;
	private HashMap<String,LinkedHashSet<UzytkownikZdalny>> kontaktyIp;
	private HashMap<String,LinkedHashSet<UzytkownikZdalny>> kontaktyNazwa;
	private LinkedHashSet<String[]> kontaktyNazwy;
	
	/** Konstruktor klasy 
	 * @param kontakty Kontakty w postaci LinkedHashSet<UzytkownikZdalny> na których klasa bêdzie pracowaæ
	 */
	public AktywneKontakty(LinkedHashSet<UzytkownikZdalny> kontakty)
	{
		this.kontakty = kontakty;
		przebuduj();
	}
	
	/** Funkcja sprawdza czy dany kontakt istnieje w spisie
	 * @param ip IP kontaktu
	 * @param nazwa Nazwa u¿ytkownika
	 * @return Wartoœæ "true" w przypadku gdy kontakt istnieje, wartoœæ "false" w przypadku gdy kontakt nie istnieje w spisie
	 */
	public boolean sprawdzCzyIstnieje(String ip, String nazwa)
	{
		if(kontaktyIp.containsKey(ip))
		{
			Iterator<UzytkownikZdalny> i = kontaktyIp.get(ip).iterator();
			while(i.hasNext())
			{
				if(i.next().zwrocNazwe() == nazwa) return true;
			}
		}
		return false;	
	}
	
	/** Funkcja szuka uzykownika zdalnego w spisie po adresie ip i jego nazwie 
	 * @param ip IP szukanego u¿ytkownika
	 * @param nazwa Nazwa szukanego u¿ytkownika
	 * @return Referencja do UzykownikaZdalnego w przypadku odnalezienia, "null" w przypadku nie odnalezienia. 
	 */
	public UzytkownikZdalny zwrocUzytkownikaZdalnego(String ip, String nazwa)
	{
		if(kontaktyIp.containsKey(ip))
		{
			LinkedHashSet<UzytkownikZdalny> list = kontaktyIp.get(ip);
			Iterator<UzytkownikZdalny> i = list.iterator();
			while(i.hasNext())
			{
				UzytkownikZdalny uZ = i.next();
				if(uZ.zwrocNazwe().equals(nazwa)) 
					return uZ;
			}
		}
		return null;
	}
	
	/** Dodaje nowy kontakt
	 * @param adresSocket Adres gniazdka u¿ytkownika zdalnego
	 * @param nazwa Nazwa u¿ytkowika zdalnego
	 * @return Referencja do obiektu UzytkownikZdalny, gdy kontakt zosta³ prawid³owo dodany, wartoœæ "null" gdy kontakt ju¿ istnieje
	 */
	public UzytkownikZdalny dodajKontakt(InetSocketAddress adresSocket, String nazwa)
	{
		if(!sprawdzCzyIstnieje(adresSocket.getAddress().getHostAddress(),nazwa))
		{
			UzytkownikZdalny uZ = new UzytkownikZdalny(adresSocket,nazwa);
			kontakty.add(uZ);
			przebuduj();
			return uZ;
		}
		return null;
	}
	
	/** Zwraca listê kontaktów w postaci tablicy String
	 * @return Duplikat listy kontaktów: String[3] w postaci [0]:nazwa, [1]:ip, [2]:nr u¿ytkownika w systemie
	 */
	public LinkedHashSet<String[]> zwrocKontaktyNazwy()
	{
		return new LinkedHashSet<String[]>(kontaktyNazwy);
	}
		
	/** Usuwa kontakt ze spisu *
	 * @param ip Ip u¿ytkownika zdalnego
	 * @param nazwa Nazwa u¿ytkownika zdalnego
	 * @return Wartoœæ "true" gdy kontakt zosta³ usuniêty, wartoœæ "false" gdy kontakt nie istnieje
	 */
	public boolean usunKontakt(String ip, String nazwa)
	{
		UzytkownikZdalny uZ = zwrocUzytkownikaZdalnego(ip,nazwa);
		if(uZ != null)
		{
			kontakty.remove(uZ);
			przebuduj();
			return true;
		}
		return false;
	}
	
	/** Tworzy listy kontaktów na potrzeby klasy AktywneKontakty */
	private void przebuduj()
	{
		utworzKontaktyIp();
		utworzKontaktyNazwa();
		utworzKontaktyNazwy();
	}
	
	/** Tworzy odwzorowanie HashMap adresów ip z u¿ytkownikami zdalnymi */
	private void utworzKontaktyIp()
	{
		Iterator<UzytkownikZdalny> i = kontakty.iterator();
		kontaktyIp = new HashMap<String,LinkedHashSet<UzytkownikZdalny>>();
		
		while(i.hasNext())
		{
			String ip = i.next().zwrocIp();
			kontaktyIp.put(ip, new LinkedHashSet<UzytkownikZdalny>());
			Iterator<UzytkownikZdalny> j = kontakty.iterator();
			while(j.hasNext())
			{
				UzytkownikZdalny uZ = j.next();
				if(uZ.zwrocIp() == ip)
					kontaktyIp.get(ip).add(uZ);
			}
		}
	}
	
	/** Tworzy odwzorowanie HashMap nazw u¿ytkowników z u¿ytkownikami zdalnymi */
	private void utworzKontaktyNazwa()
	{
		Iterator<UzytkownikZdalny> i = kontakty.iterator();
		kontaktyNazwa = new HashMap<String,LinkedHashSet<UzytkownikZdalny>>();
		
		while(i.hasNext())
		{
			String nazwa = i.next().zwrocNazwe();
			kontaktyNazwa.put(nazwa, new LinkedHashSet<UzytkownikZdalny>());
			Iterator<UzytkownikZdalny> j = kontakty.iterator();
			while(j.hasNext())
			{
				UzytkownikZdalny uZ = j.next();
				if(uZ.zwrocNazwe() == nazwa)
					kontaktyNazwa.get(nazwa).add(uZ);
			}
		}
	}
	
	/** Tworzy zbiór LinkedHashSet zawieracj¹cy tablice obiektów String z nazw¹ u¿ytkownika, adresem ip i nr u¿ytkownika */
	private void utworzKontaktyNazwy()
	{
		Iterator<UzytkownikZdalny> i = kontakty.iterator();
		kontaktyNazwy = new LinkedHashSet<String[]>();
		
		while(i.hasNext())
		{
			UzytkownikZdalny uZ = i.next();
			String[] obj = new String[3];
			obj[0] = uZ.zwrocNazwe();
			obj[1] = uZ.zwrocIp();
			obj[2] = uZ.zwrocNrUzytkownika().toString();
			kontaktyNazwy.add(obj);

		}
	}
	
}
