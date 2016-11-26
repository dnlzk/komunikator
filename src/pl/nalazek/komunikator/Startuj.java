package pl.nalazek.komunikator;

import pl.nalazek.komunikator.gui.Gui;
import pl.nalazek.komunikator.logika.Model;
import pl.nalazek.komunikator.logika.Sterowanie;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** G³ówna klasa konstruuj¹ca program */ 
public class Startuj {

	/** Konstruktor klasy */
	public Startuj()
	{
	Program.utworz(new Gui(),new Model(),new Sterowanie()).uruchom();
	}
}
