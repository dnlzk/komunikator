package pl.nalazek.komunikator.logika;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikLokalny;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj�ca rozmow� w programie */
public class Rozmowa {
	private RozmowaID idRozmowy;
	private UzytkownikLokalny uLokalny;
	private volatile HashMap<UzytkownikZdalny,Queue<Pakiet>> kolejkaWych;
	private volatile HashMap<UzytkownikZdalny,WatekWysylajacy> watkiWysylajacy; 
	private volatile HashMap<UzytkownikZdalny,RozmowaID> idRozmowyDocelowej;
	Object semaforKolejek;
	private boolean aktywna = false;
	
	/** Konstruktor
	 * @param uLokalny U�ytkownik lokalny inicjuj�cy rozmow�
	 * @param uZdalny U�ytkownik zdalny z kt�rym rozpoczynana jest rozmowa
	 * @throws Wyjatek
	 */
	public Rozmowa(UzytkownikLokalny uLokalny, UzytkownikZdalny uZdalny) throws Wyjatek
	{
		Integer[] zmienne = {uLokalny.zwrocNrUzytkownika(), uZdalny.zwrocNrUzytkownika()}; 
		idRozmowy = new RozmowaID(zmienne);
		idRozmowy.ustawNazweUzytkownika(uLokalny.zwrocNazwe());
		this.uLokalny = uLokalny;
		kolejkaWych = new HashMap<UzytkownikZdalny,Queue<Pakiet>>();
		watkiWysylajacy = new HashMap<UzytkownikZdalny,WatekWysylajacy>();
		idRozmowyDocelowej = new HashMap<UzytkownikZdalny,RozmowaID>();
		semaforKolejek = new Object();
		dodajUzytkownikaZdalnego(uZdalny);

	}
	
	/** Ko�czy wszystkie nawi�zane po��czenia w rozmowie. Wysy�a pakiet RozmowaKoniec do uczestnik�w rozmowy */ 
	public void zakoncz()
	{
		
		Iterator<UzytkownikZdalny> u = watkiWysylajacy.keySet().iterator();
		while(u.hasNext()) usunUzytkownikaZdalnego(u.next(),true);
		aktywna = false;
	}
	
	/** Ko�czy wszystkie nawi�zane po��czenia w rozmowie. Nie wysy�a pakietu RozmowaKoniec do uczestnik�w rozmowy */ 
	public void zakonczWymuszenie()
	{
		
		Iterator<UzytkownikZdalny> u = watkiWysylajacy.keySet().iterator();
		while(u.hasNext()) usunUzytkownikaZdalnego(u.next(),false);
		aktywna = false;
	}
	
	/** Zwraca id rozmowy
	 * @return id rozmowy
	 */
	synchronized public RozmowaID zwrocIdRozmowy()
	{
		return idRozmowy;
	}
	
	/** Zwraca u�ytkownika lokalnego - gospodarza rozmowy w programie 
	 * @return U�ytkownik lokalny
	 */
	synchronized public UzytkownikLokalny zwrocUzytkownikaLokalnego()
	{
		return uLokalny;
	}
	
	/** Dodaje u�ytkownika zdalnego do rozmowy, tworz�c nowy w�tek wysy�aj�cy oraz kolejk� wychodz�c�
	 * @param uZdalny Dodawany u�ytkownik zdalny
	 * @throws Wyjatek
	 */
	synchronized public void dodajUzytkownikaZdalnego(UzytkownikZdalny uZdalny) throws Wyjatek
	{
		int port;
		port = Model.zwrocAktywnyPortSluchajacyPrzeciwny();
		WatekWysylajacy nowyWatek = new WatekWysylajacy(this,uZdalny,port);
		watkiWysylajacy.put(uZdalny, nowyWatek);
		watkiWysylajacy.get(uZdalny).start();
		while(nowyWatek.getState() != Thread.State.WAITING)
			if(nowyWatek.getState() == Thread.State.TERMINATED) {
				break;
			};
		if(nowyWatek.getState() == Thread.State.WAITING) {
			kolejkaWych.put(uZdalny, new LinkedList<Pakiet>());
			aktywna = true;
		}
		else {
			watkiWysylajacy.remove(uZdalny);
			zakoncz();
			throw new Wyjatek("Nie mo�na po��czy� ze zdalnym u�ytkownikiem!");
		}
		
		
	}
	
	/** Usuwa u�ytkownika zdalnego z rozmowy.
	 * @param uZdalny Usuwany u�ytkownik zdalny
	 * @param jaRozlaczam Warto�� "true" oznacza, �e inicjuj�cym roz��czenie jest u�ytkownik lokalny. Warto�� "false" oznacza, �e inicjuj�cym roz��czenie jest u�ytkownik zdalny
	 */
	synchronized public void usunUzytkownikaZdalnego(UzytkownikZdalny uZdalny, boolean jaRozlaczam)
	{
		if(jaRozlaczam)obslugaKolejki(uZdalny, new RozmowaKoniec(idRozmowyDocelowej.get(uZdalny),idRozmowy));
		if(watkiWysylajacy.get(uZdalny) != null)	
		while(!( watkiWysylajacy.get(uZdalny).getState() == Thread.State.WAITING ||  watkiWysylajacy.get(uZdalny).getState() == Thread.State.TERMINATED )) ;
		
		kolejkaWych.remove(uZdalny);
		synchronized(watkiWysylajacy.get(uZdalny).semafor)
		{	
			watkiWysylajacy.get(uZdalny).interrupt();
		}
		watkiWysylajacy.remove(uZdalny);
		usunIdRozmowyDocelowej(uZdalny);
	}

	/** Dodaje id rozmowy docelowej dla danego u�ytkownika zdalnego w celu poprawnego adresowania pakiet�w
	 * @param uZdalny U�ytkownik zdalny 
	 * @param id Dodawane id rozmowy docelowej
	 */
	synchronized public void dodajIdRozmowyDocelowej(UzytkownikZdalny uZdalny, RozmowaID id)
	{
		idRozmowyDocelowej.put(uZdalny, id);
	}
	
	/** Usuwa id rozmowy docelowej dla danego u�ytkownika zdalnego
	 * @param uZdalny U�ytkownik zdalny
	 */
	synchronized private void usunIdRozmowyDocelowej(UzytkownikZdalny uZdalny)
	{
		idRozmowyDocelowej.remove(uZdalny);
	}
	
	/** Zwraca informacj� o tym czy rozmowa jest aktywna, tzn. czy jest po��czenie co najmniej z jednym u�ytkownikiem zdalnym
	 * @return Warto�� "true" w przypadku gdy rozmowa jest aktywna, warto�� "false" w przeciwnym wypadku
	 */
	boolean czyAktywna()
	{
		return aktywna;
	}
	
	/** Obs�uguje kolejk� wychodz�c� dla danego u�ytkownika. Wyjmuje lub wk�ada pakiety do kolejki, a tak�e informuje w�tek wysy�aj�cy o nowym pakiecie w kolejce.
	 * @param uZdalny U�ytkownik zdalny do kt�rego kolejki chcemy si� odnie��. Warto�� "null" powoduje w�o�enie pakietu do wszytskich kolejek w rozmowie.
	 * @param pakiet Pakiet do przes�ania. Warto�� "null" powoduj� wyj�cie z kolejki pierwszego elementu (funkcja u�ywana przez w�tek wysy�aj�cy). 
	 * @return W przypadku podania jako drugi parametr warto�ci "null" zwraca Pakiet, w przeciwnym razie zwraca warto�� "null".
	 */
	Pakiet obslugaKolejki(UzytkownikZdalny uZdalny, Pakiet pakiet)
	{	
		if(uZdalny != null)
		{
			if(pakiet == null)
				synchronized(semaforKolejek)
				{ return kolejkaWych.get(uZdalny).poll(); }
			else
				{
				if(pakiet.zwrocIdRozmowyDocelowej() == null) pakiet.dodajIdRozmowyDocelowej(idRozmowyDocelowej.get(uZdalny)); //oznaczenie pakietu 
					try{
					synchronized(semaforKolejek)
					{kolejkaWych.get(uZdalny).offer(pakiet);}
					}
					catch(IllegalStateException w)
					{
					Program.komponentSterowanie().odbierzWyjatek("W rozmowie: " + idRozmowy.zwrocIdRozmowy() + " wyst�pi� wyj�tek.\n" + w.getMessage() + ";;" + w.getCause());
					}
					synchronized (watkiWysylajacy.get(uZdalny).semafor)
					{
						watkiWysylajacy.get(uZdalny).semafor.notify();
						}
					return null;
				}
		}
		else {
			Iterator<UzytkownikZdalny> i;
			synchronized(semaforKolejek) {i = kolejkaWych.keySet().iterator();}
				while(i.hasNext())
				{	
					UzytkownikZdalny uZ = i.next();
					if(pakiet.zwrocIdRozmowyDocelowej() == null) pakiet.dodajIdRozmowyDocelowej(idRozmowyDocelowej.get(uZ)); //oznaczenie pakietu 
					try{
						synchronized(semaforKolejek){
					kolejkaWych.get(uZ).offer(pakiet);}
					}
					catch(IllegalStateException w)
					{
					Program.komponentSterowanie().odbierzWyjatek("W rozmowie: " + idRozmowy.zwrocIdRozmowy() + " wyst�pi� wyj�tek.\n" + w.getMessage() + ";;" + w.getCause());
					}
					synchronized (watkiWysylajacy.get(uZ).semafor)
					{
						watkiWysylajacy.get(uZ).semafor.notify();
						}
					return null;
				}
			}
		return null;
	}
}
