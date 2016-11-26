package pl.nalazek.komunikator.logika;

import java.util.*;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa obs�uguj�ca w�tki w programie*/
public class Watek extends Thread{
	
	/** Referencja do modelu, dla obs�ugi pakiet�w */
	protected static Model modelRef;
	/** Lista przechowuj�ca wszystkie aktywne w�tki*/
	static ArrayList<Watek> listaWatkow = null;
	
	/** Konstruktor */
	public Watek()
	{
		if(listaWatkow == null) { listaWatkow = new ArrayList<Watek>(); }
		listaWatkow.add(Watek.this);
	}
	
	/** Rozserzona metoda, oddziedziczona z klasy Thread. Dodatkowo usuwa aktualny w�tek z listy w�tk�w*/
	public void interrupt()
	{
		super.interrupt();
		listaWatkow.remove(Watek.this);
	}
	
	/** Zabija w�tek */
	public void zabijWatek()
	{
		interrupt();
	}
	
	/** Zabija wszystkie stworzone w�tki*/
	public void zabijWszystkieWatki()
	{
		Iterator<Watek> itertr = listaWatkow.iterator();
		while(itertr.hasNext())
		{
			itertr.next().interrupt();
		}
	}
	
	/** Ustawia referencj� do modelu programu */
	static void ustawRefModelu(Model m)
	{
		modelRef = m;
	}
	
	/** Sprawdza czy dany w�tek jeszcze istnieje */
	public static boolean sprawdzCzyIstniejeWatek(Watek w)
	{
		if(listaWatkow.contains(w)) return true;
		else return false;
	}
}
