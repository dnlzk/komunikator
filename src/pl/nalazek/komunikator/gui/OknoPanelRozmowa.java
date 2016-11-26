package pl.nalazek.komunikator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.Watek;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca zarz¹dce obs³ugi panelu rozmowa */
public class OknoPanelRozmowa implements ActionListener{
	private Gui gui;
	
	/** Konstruktor
	 * @param gui Referencja do interfejsu graficznego programu */
	public OknoPanelRozmowa(Gui gui)
	{
		this.gui = gui;
		JDialog.setDefaultLookAndFeelDecorated(true);
	}
	
	/** Funkcja realizuj¹ca interfejs ActionListener, uruchamiana na skutek zdarzenia przychodz¹ce z panelu rozmowa *
	 * @param arg0 Referencja do zdarzenia */
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand() == "Czy\u015b\u0107")
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() 
				{
						gui.panel1PoleTekstoweWpissc.setText("");
				}
			});
		}
		
		else if(arg0.getActionCommand() == "Wy\u015blij")
				Program.komponentSterowanie().zadanieWyslaniaWiadomosci(gui);

		else if(arg0.getActionCommand() == "Wy\u015blij plik")
			(new Watek(){public void run(){
				Program.komponentSterowanie().zadanieWyslaniaPliku(gui);
			}}).start();

	}

}
