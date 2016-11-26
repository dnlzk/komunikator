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

/** Klasa reprezentuj¹ca rozmowê w programie */
public class Rozmowa {
	private RozmowaID idRozmowy;
	private UzytkownikLokalny uLokalny;
	private volatile HashMap<UzytkownikZdalny,Queue<Pakiet>> kolejkaWych;
	private volatile HashMap<UzytkownikZdalny,WatekWysylajacy> watkiWysylajacy; 
	private volatile HashMap<UzytkownikZdalny,RozmowaID> idRozmowyDocelowej;
	Object semaforKolejek;
	private boolean aktywna = false;
	
	/** Konstruktor
	 * @param uLokalny U¿ytkownik lokalny inicjuj¹cy rozmowê
	 * @param uZdalny U¿ytkownik zdalny z którym rozpoczynana jest rozmowa
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
	
	/** Koñczy wszystkie nawi¹zane po³¹czenia w rozmowie. Wysy³a pakiet RozmowaKoniec do uczestników rozmowy */ 
	public void zakoncz()
	{
		
		Iterator<UzytkownikZdalny> u = watkiWysylajacy.keySet().iterator();
		while(u.hasNext()) usunUzytkownikaZdalnego(u.next(),true);
		aktywna = false;
	}
	
	/** Koñczy wszystkie nawi¹zane po³¹czenia w rozmowie. Nie wysy³a pakietu RozmowaKoniec do uczestników rozmowy */ 
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
	
	/** Zwraca u¿ytkownika lokalnego - gospodarza rozmowy w programie 
	 * @return U¿ytkownik lokalny
	 */
	synchronized public UzytkownikLokalny zwrocUzytkownikaLokalnego()
	{
		return uLokalny;
	}
	
	/** Dodaje u¿ytkownika zdalnego do rozmowy, tworz¹c nowy w¹tek wysy³aj¹cy oraz kolejkê wychodz¹c¹
	 * @param uZdalny Dodawany u¿ytkownik zdalny
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
			throw new Wyjatek("Nie mo¿na po³¹czyæ ze zdalnym u¿ytkownikiem!");
		}
		
		
	}
	
	/** Usuwa u¿ytkownika zdalnego z rozmowy.
	 * @param uZdalny Usuwany u¿ytkownik zdalny
	 * @param jaRozlaczam Wartoœæ "true" oznacza, ¿e inicjuj¹cym roz³¹czenie jest u¿ytkownik lokalny. Wartoœæ "false" oznacza, ¿e inicjuj¹cym roz³¹czenie jest u¿ytkownik zdalny
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

	/** Dodaje id rozmowy docelowej dla danego u¿ytkownika zdalnego w celu poprawnego adresowania pakietów
	 * @param uZdalny U¿ytkownik zdalny 
	 * @param id Dodawane id rozmowy docelowej
	 */
	synchronized public void dodajIdRozmowyDocelowej(UzytkownikZdalny uZdalny, RozmowaID id)
	{
		idRozmowyDocelowej.put(uZdalny, id);
	}
	
	/** Usuwa id rozmowy docelowej dla danego u¿ytkownika zdalnego
	 * @param uZdalny U¿ytkownik zdalny
	 */
	synchronized private void usunIdRozmowyDocelowej(UzytkownikZdalny uZdalny)
	{
		idRozmowyDocelowej.remove(uZdalny);
	}
	
	/** Zwraca informacjê o tym czy rozmowa jest aktywna, tzn. czy jest po³¹czenie co najmniej z jednym u¿ytkownikiem zdalnym
	 * @return Wartoœæ "true" w przypadku gdy rozmowa jest aktywna, wartoœæ "false" w przeciwnym wypadku
	 */
	boolean czyAktywna()
	{
		return aktywna;
	}
	
	/** Obs³uguje kolejkê wychodz¹c¹ dla danego u¿ytkownika. Wyjmuje lub wk³ada pakiety do kolejki, a tak¿e informuje w¹tek wysy³aj¹cy o nowym pakiecie w kolejce.
	 * @param uZdalny U¿ytkownik zdalny do którego kolejki chcemy siê odnieœæ. Wartoœæ "null" powoduje w³o¿enie pakietu do wszytskich kolejek w rozmowie.
	 * @param pakiet Pakiet do przes³ania. Wartoœæ "null" powodujê wyjêcie z kolejki pierwszego elementu (funkcja u¿ywana przez w¹tek wysy³aj¹cy). 
	 * @return W przypadku podania jako drugi parametr wartoœci "null" zwraca Pakiet, w przeciwnym razie zwraca wartoœæ "null".
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
					Program.komponentSterowanie().odbierzWyjatek("W rozmowie: " + idRozmowy.zwrocIdRozmowy() + " wyst¹pi³ wyj¹tek.\n" + w.getMessage() + ";;" + w.getCause());
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
					Program.komponentSterowanie().odbierzWyjatek("W rozmowie: " + idRozmowy.zwrocIdRozmowy() + " wyst¹pi³ wyj¹tek.\n" + w.getMessage() + ";;" + w.getCause());
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
