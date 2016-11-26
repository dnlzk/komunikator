package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikLokalny;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;
import java.io.*;
import java.util.*;
import java.net.*;


/** Klasa odpowiadaj¹ca za model programu */
public class Model implements IModel {

	private WatekSluchajacy watekSluchajacy = null;
	static final int portSluch = 5622, portSluchLH1 = 5623, portSluchLH2 = 5624;
	private static int portSluchAktywny;
	private UzytkownikLokalny aktywnyUzytkownikLokalny;
	private AktywneKontakty kontakty;
	/** Nr rozmowy / rozmowa */
	private volatile HashMap<Long,Rozmowa> rozmowy;
	/** Nr rozmowy / odpowiedz */
	private volatile HashMap<Long,Pakiet> pytania;

	


	
	/** Konstruktor klasy */
	public Model()
	{
		rozmowy = new HashMap<Long,Rozmowa>();
		pytania = new HashMap<Long,Pakiet>();
		Watek.ustawRefModelu(this);
	}
	
	public void ustawOnline(boolean wartosc, LocalHost localhost)
			throws IOException, ClassNotFoundException, Wyjatek {
		if(wartosc){
		if(localhost == LocalHost.IP)
		{
			watekSluchajacy = new WatekSluchajacy(portSluchAktywny = portSluch);
			watekSluchajacy.start();
		}
		else if(localhost == LocalHost.LOCALHOST1)
		{
			watekSluchajacy = new WatekSluchajacy(portSluchAktywny = portSluchLH1);
			watekSluchajacy.start();
		}
		else if(localhost == LocalHost.LOCALHOST2)
		{
			watekSluchajacy = new WatekSluchajacy(portSluchAktywny = portSluchLH2);
			watekSluchajacy.start();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		if(!watekSluchajacy.isAlive()) throw new Wyjatek("Nie mo¿na otworzyæ portu do s³uchania");
		}
		
		else
			{
			watekSluchajacy.interrupt();
			portSluchAktywny = 0;
			}
		
				 	 
	}

	public String mojeIP() {
		try{ return InetAddress.getLocalHost().toString(); }
		catch(UnknownHostException a) {	return "Nieznany host"; }
	}

	public RozmowaID rozpocznijRozmowe(UzytkownikZdalny uzytkownik) throws Wyjatek {
		Rozmowa rozmowa = new Rozmowa(aktywnyUzytkownikLokalny,uzytkownik);
		if(rozmowa.czyAktywna()) {
		RozmowaPytanie pytanie = new RozmowaPytanie(rozmowa.zwrocIdRozmowy());
		pytania().put(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy(),null);
		synchronized(rozmowa.semaforKolejek)
		{rozmowa.obslugaKolejki(uzytkownik, pytanie);}
		
		
		try {
			for(int i = 0; i<50; i++)
			{
				Thread.sleep(200);
				/* sprawdzenie czy odpowiedz która przysz³a jest opdowiedzi¹ oczekiwan¹ */
				RozmowaOdpowiedz pakietOdpowiadajacy = (RozmowaOdpowiedz) pytania().get(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy());
				if(pakietOdpowiadajacy != null)
					{
					/* OdpowiedŸ pozytywna */
					if(pakietOdpowiadajacy.sprawdzPoprawnoscOdpowiedzi(pytanie))
							{
							rozmowa.dodajIdRozmowyDocelowej(uzytkownik, pakietOdpowiadajacy.zwrocIdRozmowyZrodlowej());
							rozmowy().put(rozmowa.zwrocIdRozmowy().zwrocIdRozmowy(), rozmowa);
							pytania().remove(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy()); 
							return rozmowa.zwrocIdRozmowy();
							}
					/* OdpowiedŸ negatywna */
					else
							{
						    pytania().remove(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy());
							rozmowa.usunUzytkownikaZdalnego(uzytkownik, false);
						    return new RozmowaID(0);
							}
					}
			} 
			pytania().remove(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy());
			rozmowa.usunUzytkownikaZdalnego(uzytkownik, false);
		} catch (InterruptedException e) {
			throw new Wyjatek("W oczekiwaniu na odpowiedŸ wyst¹pi³ wyj¹tek.\n" + e.getMessage() + ";;" + e.getCause());
		}	}	
		
		
		return null;
	}

	public LinkedHashSet<String[]> listaUZ() {
		return kontakty.zwrocKontaktyNazwy();
	}

	public StanWyslaniaPliku wyslijPlik(UzytkownikZdalny uZdalny, RozmowaID id, String sciezka_pliku) throws Wyjatek {
		
		try {
			Dane dane = new Dane(id, null, sciezka_pliku);
			DanePytanie pytanie = new DanePytanie(dane);
			pytania().put(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy(),null);
			synchronized(rozmowy().get(id.zwrocIdRozmowy()).semaforKolejek)
			{rozmowy.get(id.zwrocIdRozmowy()).obslugaKolejki(uZdalny, pytanie);}

			for(int i = 0; i<8; i++)
			{
				Thread.sleep(2000);
				/* sprawdzenie czy odpowiedz która przysz³a jest opdowiedzi¹ oczekiwan¹ */
				DaneOdpowiedz pakietOdpowiadajacy = (DaneOdpowiedz) pytania().get(pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy());
				if(pakietOdpowiadajacy != null)
					{
					/* OdpowiedŸ pozytywna */
					if(pakietOdpowiadajacy.sprawdzPoprawnoscOdpowiedzi(pytanie))
							{
						synchronized(rozmowy().get(id.zwrocIdRozmowy()).semaforKolejek)
							{rozmowy.get(id.zwrocIdRozmowy()).obslugaKolejki(uZdalny, dane);}
							pytania().remove(dane.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy()); 
							return new StanWyslaniaPliku(1);
							}
					/* OdpowiedŸ negatywna */
					else
							{
						    pytania().remove(dane.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy()); 
							return new StanWyslaniaPliku(0);
							}
					}
			}
			pytania().remove(dane.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy());
			return new StanWyslaniaPliku(-1);
		} 
		catch (InterruptedException e) 
		{
		throw new Wyjatek("W oczekiwaniu na odpowiedŸ wyst¹pi³ wyj¹tek.\n" + e.getMessage() + ";;" + e.getCause());
		}		
		catch (IOException e) 
		{
		Program.komponentSterowanie().odbierzWyjatek(e.getMessage() + " " + e.getCause());
		} 
		catch (Wyjatek e) 
		{
		Program.komponentSterowanie().odbierzWyjatek(e.getMessage() + " " + e.getCause());
		}
		return null;
	}

	public void wyslijWiadomosc(RozmowaID id, String wiadomosc)
			throws IOException {
		Wiadomosc wiad = new Wiadomosc(wiadomosc, id);
		synchronized(rozmowy.get(id.zwrocIdRozmowy()).semaforKolejek)
		{rozmowy.get(id.zwrocIdRozmowy()).obslugaKolejki(null, wiad);}
		
	}

	public void zakonczRozmowe(RozmowaID id) throws IOException {
		
		rozmowy.get(id.zwrocIdRozmowy()).zakoncz();
	}
	
	/** Zwraca aktywny port s³uchaj¹cy
	 * @return 5622 w trybie Internet, 5623 w trybie LocalHost1, 5624 w trybie LocalHost2;
	 */
	int zwrocAktywnyPortSluchajacy()
	{
		return portSluchAktywny;
	}
	
	
	/** Loguje u¿ytkownika lokalnego do modelu programu *
	 * @param uzytkowikLokalny U¿ytkownik lokalny która ma zostaæ zalogowany
	 * @param kontakty Lista kontaktow w postaci zbioru LinkedHashSet<UzytkownikZdalny>
	 */
	public void zalogowano(UzytkownikLokalny uzytkowikLokalny, LinkedHashSet<UzytkownikZdalny> kontakty)
	{
		aktywnyUzytkownikLokalny = uzytkowikLokalny;
		this.kontakty = new AktywneKontakty(kontakty);
	}
	
	/** Wylogowywuje u¿ytkownika lokalnego */
	public void wylogowano()
	{
		aktywnyUzytkownikLokalny = null;
		this.kontakty = null;
	}
	
	/** Zwraca zalogowanego u¿ytkownmika lokalnego
	 * @return Zalogowany u¿ytkownik lokalny
	 */
	public UzytkownikLokalny zwrocAktywnyUzytkownikLokalny()
	{
		return aktywnyUzytkownikLokalny;
	}
	
	//metody synchronizowane
	/** Dodaje u¿ytkownika zdalnego
	 * @param nazwa Nazwa u¿ytkownika zdalnego
	 * @param adresSocket Adres gniazdka
	 * @return Dodany u¿ytkownik zdalny
	 */
	synchronized public UzytkownikZdalny dodajUzytkownikaZdalnego(String nazwa, InetSocketAddress adresSocket) throws Wyjatek
	{
		UzytkownikZdalny uZ = kontakty.dodajKontakt(adresSocket, nazwa);
		if(uZ == null) throw new Wyjatek("Kontakt ju¿ istnieje!");
		else return uZ;
	}
	
	/** Usuwa u¿ytkownika zdalnego
	 * @param nazwa Nazwa u¿ytkownika zdalnego
	 * @param ip Adres ip u¿ytkownika
	 */
	synchronized public void usunUzytkownikaZdalnego(String nazwa, String ip) throws Wyjatek
	{ 
		if(!kontakty.usunKontakt(ip, nazwa)) throw new Wyjatek("Kontakt nie istnieje!");
	}
	
	/** Zwraca u¿ytkownika zdalnego *
	 * @param nazwa Nazwa u¿ytkownika zdalnego
	 * @param ip Adres ip u¿ytkownika
	 * @return W przypadku odnalezienia zwraca u¿ytkownika zdalnego, w przeciwnym razie "null"
	 */
	synchronized public UzytkownikZdalny zwrocUzytkownikaZdalnego(String ip, String nazwa)
	{
		return kontakty.zwrocUzytkownikaZdalnego(ip, nazwa);
	}
	
	/** Zwraca przeciwny port s³uchaj¹cy
	 * @return 5622 w trybie Internet, 5624 w trybie LocalHost1, 5623 w trybie LocalHost2
	 */
	synchronized static int zwrocAktywnyPortSluchajacyPrzeciwny()
	{
		if(portSluchAktywny == portSluch) return portSluch;
		else if (portSluchAktywny == portSluchLH1) return portSluchLH2;
		else if (portSluchAktywny == portSluchLH2) return portSluchLH1;
		else return -1;
	}

	/** Umo¿liwia synchronizowany dostêp do tocz¹cych siê w programie rozmów 
	 * @return Odwzorowanie w postaci nr id rozmowy i referencji do rozmowy
	 */
	synchronized HashMap<Long,Rozmowa> rozmowy()
	{
		return rozmowy;
	}
	
	/** Umo¿liwia synchronizowany dostêp do aktywnych pytañ w programie
	 * @return Odwzorowanie w postaci nr id rozmowy i referencji pakietu odpowiadaj¹cego
	 */
	synchronized HashMap<Long,Pakiet> pytania()
	{
		return pytania;
	}
	
	

}