package pl.nalazek.komunikator.logika;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa uruchumiaj¹ca nowy w¹tek do obs³ugi przychodz¹cych pakietów */
public class WatekObslugujacyPakiet extends Watek{
	
	private ObjectInputStream strumienPrzych;
	private Socket gniazdoSluchajace = null;
	private String adresIp;
	
	/** Konstruktor 
	 * @param iS Obietkowy strumieñ przychodz¹cy
	 * @param gniazdoSluchajace Gniazdo do którego podpiêty jest strumieñ
	 */
	public WatekObslugujacyPakiet(ObjectInputStream iS, Socket gniazdoSluchajace)
	{
		strumienPrzych = iS;
		this.gniazdoSluchajace = gniazdoSluchajace;
	}
	
	/**  G³ówna metoda w¹tku */
	public void run()
	{
		try
		{
			InetSocketAddress adresSockt = (InetSocketAddress)gniazdoSluchajace.getRemoteSocketAddress();
			String nazwaUzytkownika;
			if(adresSockt.getAddress().isLoopbackAddress()) adresIp = "127.0.0.1";
			else adresIp = adresSockt.getAddress().getHostAddress();
			while(!interrupted()) {
			Pakiet pakiet = (Pakiet)strumienPrzych.readObject();
			nazwaUzytkownika = pakiet.zwrocIdRozmowyZrodlowej().zwrocNazweUzykownika();
			System.out.println("Pakiet obs³u¿ony");
			UzytkownikZdalny uZdalny = modelRef.zwrocUzytkownikaZdalnego(adresIp, nazwaUzytkownika);

			
		NrIdPakietu nr = pakiet.getNrId();
		
		/** Pakiet z wiadomoœci¹ */
		if(nr == NrIdPakietu.WIADOMOSC)
		{
	    Wiadomosc wiadomosc = (Wiadomosc)pakiet;
	    Program.komponentSterowanie().odbierzWiadomosc(adresIp, wiadomosc.zwrocIdRozmowyDocelowej(), wiadomosc.zwrocTekst());
		}
		
		/** Pakiet z ¿¹daniem odebrania danych */
		else if(nr == NrIdPakietu.DANE_PYTANIE)
		{
			DanePytanie pytanie = (DanePytanie)pakiet;
			OdpowiedzSterowania kod = Program.komponentSterowanie().odbierzPlik(adresIp, pytanie.zwrocIdRozmowyDocelowej(), pytanie.plikInfo, null);
			switch(kod.kod)
			{
			case 0: //odmowa
				synchronized(modelRef.rozmowy().get(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()).semaforKolejek)
				{ modelRef.rozmowy().get(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy())
					.obslugaKolejki(uZdalny, new DaneOdpowiedz(pytanie,false)); }
				break;
			case 1: //zgoda
				modelRef.pytania().put(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy(), null);
				synchronized(modelRef.rozmowy().get(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()).semaforKolejek)
				{
				modelRef.rozmowy().get(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy())
					.obslugaKolejki(uZdalny, new DaneOdpowiedz(pytanie,true)); }
				
				try {
								
								
						for(int i = 0; i<800; i++)
						{
							Thread.sleep(20);
							/* sprawdzenie czy odpowiedz która przysz³a jest opdowiedzi¹ oczekiwan¹ */
							Dane dane = (Dane) modelRef.pytania().get(pytanie.zwrocIdRozmowyDocelowej().zwrocIdRozmowy());
							if(dane != null)
								{
								/* OdpowiedŸ pozytywna */
								if(dane.zwrocKlucz() == pytanie.zwrocKlucz())
										{
										dane.zwrocPlik().zapiszPlik(kod.sciezka);
										modelRef.pytania().remove(pytanie.zwrocKlucz()); 
										}
								/* OdpowiedŸ negatywna */
								else
										{
										modelRef.pytania().remove(pytanie.zwrocKlucz());
										}
								}
						}
						modelRef.pytania().remove(pytanie.zwrocKlucz());
						
					} 
					catch (InterruptedException e) 
					{
						throw new Wyjatek("W oczekiwaniu na odpowiedŸ wyst¹pi³ wyj¹tek.\n" + e.getMessage() + ";;" + e.getCause());
					}
					catch (IOException e) 
					{
					throw new Wyjatek("Przy zapisywaniu pliku wyst¹pi³ wyj¹tek.\n" + e.getMessage() + ";;" + e.getCause());
					}
				break;
			case -1: //brak odpowiedzi
				break;
			}
		}
		
		/** Pakiet z ¿¹daniem rozmowy */
		else if(nr == NrIdPakietu.ROZMOWA_PYTANIE)
		{
			RozmowaPytanie pytanie = (RozmowaPytanie)pakiet;
			/* sprawdŸ u¿ytkownika zdalnego w kontaktach*/
			if(uZdalny == null)
			   	uZdalny = modelRef.dodajUzytkownikaZdalnego(nazwaUzytkownika, adresSockt);
			/* stworzenie rozmowy */
			Rozmowa rozmowa = new Rozmowa(modelRef.zwrocAktywnyUzytkownikLokalny(),uZdalny);
			OdpowiedzSterowania kod = Program.komponentSterowanie().odbierzRozmowe(adresIp, rozmowa.zwrocIdRozmowy());
			switch(kod.kod)
			{
			case 0: //odmowa
				//( new WatekWysylajacy(new RozmowaOdpowiedz(pytanie, new RozmowaID(0,"U¿ytkownikNegatywny")), adresIp, Model.zwrocAktywnyPortSluchajacyPrzeciwny()) ).start();
				synchronized(rozmowa.semaforKolejek)
				{ rozmowa.obslugaKolejki(uZdalny, new RozmowaOdpowiedz(pytanie, new RozmowaID(0,"U¿ytkownikNegatywny"))); }
				break;
			case 1: //zgoda
				rozmowa.dodajIdRozmowyDocelowej(uZdalny, pytanie.zwrocIdRozmowyZrodlowej());
				
				/* dodanie do aktywnych rozmów */
				modelRef.rozmowy().put(rozmowa.zwrocIdRozmowy().zwrocIdRozmowy(), rozmowa);
				
				/* wys³anie pakietu potwierdzaj¹cego */
				synchronized(rozmowa.semaforKolejek)
				{ rozmowa.obslugaKolejki(uZdalny, new RozmowaOdpowiedz(pytanie,rozmowa.zwrocIdRozmowy())); }
				break;
			case -1: //brak odpowiedzi
				break;
			}
			
		}
		
		/** Pakiet z opdowiedzi¹ na ¿¹danie rozmowy */
		else if(nr == NrIdPakietu.ROZMOWA_ODPOWIEDZ)
		{
		RozmowaOdpowiedz odpowiedz = (RozmowaOdpowiedz)pakiet;
		if( modelRef.pytania().containsKey(odpowiedz.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()) )
			modelRef.pytania().put(odpowiedz.zwrocIdRozmowyDocelowej().zwrocIdRozmowy(), odpowiedz);
		else throw new Wyjatek("Odebrano przeterminowany  pakiet z odpowiedzi¹ na ¿¹danie rozmowy!");
		}
		
		
		/** Pakiet z opdowiedzi¹ na ¿¹danie danych */
		else if(nr == NrIdPakietu.DANE_ODPOWIEDZ)
		{
		DaneOdpowiedz odpowiedz = (DaneOdpowiedz)pakiet;
		if( modelRef.pytania().containsKey(odpowiedz.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()) )
			modelRef.pytania().put(odpowiedz.zwrocIdRozmowyDocelowej().zwrocIdRozmowy(), odpowiedz);
		else throw new Wyjatek("Odebrano przeterminowany pakiet z odpowiedzi¹ na ¿¹danie danych!");
		}
		
		/** Pakiet z danymi */
		else if(nr == NrIdPakietu.DANE)
		{
		Dane dane = (Dane)pakiet;
		if( modelRef.pytania().containsKey(dane.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()) )
			{modelRef.pytania().remove(dane.zwrocIdRozmowyDocelowej().zwrocIdRozmowy());
		String sciezka = Program.komponentSterowanie().odebranoPlik(dane.plikInfo.danePliku.getName());
		dane.zwrocPlik().zapiszPlik(sciezka);
			}
		else throw new Wyjatek("Odebrano pakiet z danymi! Pakiet potwierdzaj¹cy nie by³ wysy³any...");
		}
		
		/** Rozmowa koniec */
		else if(nr == NrIdPakietu.ROZMOWA_KONIEC)
		{
		RozmowaKoniec pakietKoniec = (RozmowaKoniec)pakiet;
		if( modelRef.rozmowy().containsKey(pakietKoniec.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()))
			{
			modelRef.rozmowy().get(pakietKoniec.zwrocIdRozmowyDocelowej().zwrocIdRozmowy()).usunUzytkownikaZdalnego(modelRef.zwrocUzytkownikaZdalnego(adresIp, nazwaUzytkownika), false);
			Program.komponentSterowanie().odbierzZakonczenieRozmowy(adresIp, pakietKoniec.zwrocIdRozmowyDocelowej());
			}
		
		}
		
		/** Inny pakiet */
		else if(nr == NrIdPakietu.PAKIET)
		{
			
		}
		
		/** Nieznany pakiet */
		else
		{
			throw new Wyjatek("Odebrano nieznany pakiet!");
		}
		}
		}
		catch(ClassNotFoundException w)
		{
			Program.komponentSterowanie().odbierzWyjatek("W w¹tku: " + this.getName() + " wyst¹pi³ wyj¹tek.\n" + w.getMessage() + ";;" + w.getCause());
			System.out.println(w.getMessage() + " " + w.getCause());
		}
		catch(EOFException w)
		{
			Program.komponentSterowanie().odbierzWyjatek("Zakoñczono po³¹czenie przychodz¹ce z " + adresIp + "!");
		}
		catch(SocketException w)
		{
			if(w.getMessage().contains("Connection reset")) {
				Program.komponentSterowanie().odbierzWyjatek("Zakoñczono po³¹czenie przychodz¹ce z " + adresIp + "!");
				Program.komponentSterowanie().odbierzZakonczenieRozmowyWymuszone();
			}
		}
		
		catch(IOException w)
		{
			Program.komponentSterowanie().odbierzWyjatek("W w¹tku: " + this.getName() + " wyst¹pi³ wyj¹tek.\n" + w.getMessage() + ";;" + w.getCause());
			System.out.println(w.getMessage() + " " + w.getCause());
		}
		catch(Exception w)
		{
			Program.komponentSterowanie().odbierzWyjatek("W w¹tku: " + this.getName() + " wyst¹pi³ wyj¹tek.\n" + w.getMessage() + ";;" + w.getCause());
			System.out.println(w.getMessage() + " " + w.getCause());
		}


	}
	

}
