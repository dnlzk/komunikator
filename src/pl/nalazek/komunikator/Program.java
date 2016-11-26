package pl.nalazek.komunikator;

import pl.nalazek.komunikator.gui.Gui;
import pl.nalazek.komunikator.logika.Model;
import pl.nalazek.komunikator.logika.Sterowanie;
import pl.nalazek.komunikator.logika.Watek;
import pl.nalazek.komunikator.logika.WatekSluchajacy;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa typu Singleton, przechowuj�ca referencj� trzech podstawowych komponent�w programu (MVC) */
public class Program {
	
	private static Model model ;
	private static Gui gui;
	private static Sterowanie sterowanie;
	private static Program referencja;
	
	protected Program()
	{}
	
	/** Konstruktor klasy
	 * @param gui Referencja do interfejsu graficznego w postaci klasy Gui
	 * @param model Referencja do modelu programu w postaci klasy Model
	 * @param sterowanie Referencja do sterowania programu w postaci klasy Sterowanie
	 * @return Referencja do programu w postaci klasy program
	 */
	public static Program utworz(Gui gui, Model model, Sterowanie sterowanie)
	{
		if(referencja == null)
		{
			referencja = new Program();
		}
		Program.model = model;
		Program.gui = gui;
		Program.sterowanie = sterowanie;
		return referencja;
	}
	
	/** Metoda umo�liwiaj�ca dost�p do metod zawartych w modelu programu, 
	 * dost�pna tylko dla unikalnego egzemplarza klasy Sterowanie, przekazanego wcze�niej w postaci referencji 
	 * w konstruktorze klasy Program.
	 * @param s Referencja do sterowania programu
	 * @return Referencja do modelu programu lub "null" w przypadku niepoprawnej weryfikacji
	 */
	public static Model komponentModel(Sterowanie s)
	{
		if(s!=null && s == sterowanie)
		return model;
		else return null;
	}
	
	/** Metoda umo�liwiaj�ca dost�p do metod zawartych w interfejsie graficznym programu, 
	 * dost�pna tylko dla unikalnego egzemplarza klasy Sterowanie, przekazanego wcze�niej 
	 * w postaci referencji w konstruktorze klasy Program.
	 * @param s Referencja do sterowania programu
	 * @return Referencja do interfejsu graficznego programu lub "null" w przypadku niepoprawnej weryfikacji
	 */
	public static Gui komponentGui(Sterowanie s)
	{
		if(s!=null && s == sterowanie)
		return gui;
		else return null;
	}
	
	/** Metoda umo�liwiaj�ca dost�p do metod zawartych w interfejsie graficznym programu, 
	 * dost�pna tylko dla w�tk�w s�uchaj�cych w programie
	 * @param s Referencja do klasy WatekSluchajacy
	 * @return Referencja do interfejsu graficznego programu lub "null" w przypadku niepoprawnej weryfikacji
	 */
	public static Gui komponentGui(WatekSluchajacy s)
	{
		if(s!=null && (Watek.sprawdzCzyIstniejeWatek(s)))
		return gui;
		else return null;
	}
	
	/** Metoda umo�liwiaj�ca dost�p do metod zawartych w sterowaniu, dost�pna dla Modelu i Gui
	 * @return Referencja do sterowania
	 */
	public static Sterowanie komponentSterowanie()
	{
		return sterowanie;
	}
	
	/** Metoda uruchamiaj�ca sterowanie programu
	 * Musi zosta� uruchomiona po wywo�aniu konstruktora */
	public void uruchom()
	{
		sterowanie.sterowanieStart();
	}
	

}
