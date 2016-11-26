package pl.nalazek.komunikator.logika;

import java.util.*;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa obs³uguj¹ca w¹tki w programie*/
public class Watek extends Thread{
	
	/** Referencja do modelu, dla obs³ugi pakietów */
	protected static Model modelRef;
	/** Lista przechowuj¹ca wszystkie aktywne w¹tki*/
	static ArrayList<Watek> listaWatkow = null;
	
	/** Konstruktor */
	public Watek()
	{
		if(listaWatkow == null) { listaWatkow = new ArrayList<Watek>(); }
		listaWatkow.add(Watek.this);
	}
	
	/** Rozserzona metoda, oddziedziczona z klasy Thread. Dodatkowo usuwa aktualny w¹tek z listy w¹tków*/
	public void interrupt()
	{
		super.interrupt();
		listaWatkow.remove(Watek.this);
	}
	
	/** Zabija w¹tek */
	public void zabijWatek()
	{
		interrupt();
	}
	
	/** Zabija wszystkie stworzone w¹tki*/
	public void zabijWszystkieWatki()
	{
		Iterator<Watek> itertr = listaWatkow.iterator();
		while(itertr.hasNext())
		{
			itertr.next().interrupt();
		}
	}
	
	/** Ustawia referencjê do modelu programu */
	static void ustawRefModelu(Model m)
	{
		modelRef = m;
	}
	
	/** Sprawdza czy dany w¹tek jeszcze istnieje */
	public static boolean sprawdzCzyIstniejeWatek(Watek w)
	{
		if(listaWatkow.contains(w)) return true;
		else return false;
	}
}
