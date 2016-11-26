package pl.nalazek.komunikator.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import pl.nalazek.komunikator.Program;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca zarz¹dce obs³ugi ca³ego okna programu */
public class Okno extends WindowAdapter{
		
		private Gui gui;
		
		/** Konstruktor
		 * @param gui Referencja do interfejsu graficznego programu */
		public Okno(Gui gui)
		{
			this.gui = gui;
		}
		
		/** Funkcja odziedziczona po WindowAdapter, uruchamiana w przypadku ¿¹dania zamkniêcia programu */
		public void windowClosing(WindowEvent event)
		 {
		 Program.komponentSterowanie().zadanieZamknieciaProgramu(gui);
		 }
	

}
