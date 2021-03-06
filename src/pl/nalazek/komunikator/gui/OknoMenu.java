package pl.nalazek.komunikator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import pl.nalazek.komunikator.Program;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentująca zarządce obsługi menu */
public class OknoMenu implements ActionListener, ItemListener {
		private Gui gui;
		
		/** Konstruktor
		 * @param gui Referencja do interfejsu graficznego programu */
		public OknoMenu(Gui gui)
		{
		this.gui = gui;	
		}
		
		/** Funkcja realizująca interfejs ActionListener, uruchamiana na skutek zdarzenia przychodzące z menu *
		 * @param arg0 Referencja do zdarzenia */
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand() == "Online")
			{
				if(gui.menuOnline.isSelected() == true)
					Program.komponentSterowanie().zmienionoNaOnline(gui, gui.menuOnlineWybor);
				else
					Program.komponentSterowanie().zmienionoNaOffline(gui);
			}
			if(arg0.getActionCommand() == "Zako\u0144cz")
			{
				Program.komponentSterowanie().zadanieZamknieciaProgramu(gui);
			}
			if(arg0.getActionCommand() == "O programie...")
			{
				gui.oknoDialogoweInfo();
			}
			
		}

		/** Funkcja realizująca interfejs ItemListener, uruchamiana na skutek zmiany wyboru trybu w jakim program pracuje online *
		 * @param arg0 Referencja do zdarzenia */
		public void itemStateChanged(ItemEvent arg0) {
			if(arg0.getSource() == gui.onlnInternet)
			{
				if(gui.onlnInternet.isSelected())
				gui.menuOnlineWybor =  0;
			}
			else if(arg0.getSource() == gui.onlnLchst1)
			{
				if(gui.onlnLchst1.isSelected())
					gui.menuOnlineWybor =  1;
			}
			else if(arg0.getSource() == gui.onlnLchst2)
			{
				if(gui.onlnLchst2.isSelected())
					gui.menuOnlineWybor =  2;
			}
		}

}
