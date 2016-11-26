package pl.nalazek.komunikator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.Watek;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca zarz¹dce obs³ugi panelu rozmowa */
public class OknoPanelUzytkownicy implements ActionListener, ListSelectionListener{
		private Gui gui;

		/** Konstruktor
		 * @param gui Referencja do interfejsu graficznego programu */
		public OknoPanelUzytkownicy(Gui gui)
		{
			this.gui = gui;
			JDialog.setDefaultLookAndFeelDecorated(true);
		}
		
		/** Funkcja realizuj¹ca interfejs ActionListener, uruchamiana na skutek zdarzenia przychodz¹ce z panelu u¿ytkownicy *
		 * @param arg0 Referencja do zdarzenia */
		public void actionPerformed(ActionEvent arg0) 
		{
			if(arg0.getActionCommand() == "Dodaj")
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run() 
					{
					String[] dane = oknoDialogoweNowyUzytkownik();
					if(dane != null) Program.komponentSterowanie().dodanoUzytkownikaZdalnego(dane);
					}
				});
			}
			else if(arg0.getActionCommand() == "Usu\u0144")
			{
					if(!gui.listaUzytkownikow.isSelectionEmpty())
						(new Watek(){public void run(){
							Program.komponentSterowanie().usunietoUzytkownikaZdalnego(gui);
						}}).start();
			}
			else if(arg0.getActionCommand() == "Po\u0142\u0105cz")
			{
					if(!gui.listaUzytkownikow.isSelectionEmpty())
					{	
						(new Watek(){public void run(){
							Program.komponentSterowanie().zadaniePolaczenia(gui);
						}}).start();
						
					}

			}
			else if(arg0.getActionCommand() == "Roz\u0142\u0105cz")
			{
					
					if(!gui.listaUzytkownikow.isSelectionEmpty())
					(new Watek(){public void run(){
					Program.komponentSterowanie().zadanieRozlaczenia(gui);
					}}).start();
			}
		}

		/** Funkcja realizuj¹ca interfejs ListSelectionListener, uruchamiana na skutek zdarzenia przychodz¹ce z panelu u¿ytkownicy *
		 * @param arg0 Referencja do zdarzenia */
		public void valueChanged(ListSelectionEvent arg0) {
			for(int i = 0; i <= arg0.getLastIndex(); i++)
			{
				if(gui.listaUzytkownikow.isSelectedIndex(i))
					gui.listaOstatniIndeks = i;
			}
			
			Program.komponentSterowanie().zmienionoStanListy(gui);
		}

		/** Funkcja uruchamia okno dialogowe do wpisywania danych nowego u¿ytkownika */
		private String[] oknoDialogoweNowyUzytkownik()
		{
			
			JTextField nazwa = new JTextField();
			JTextField ip = new JTextField();
			JComponent[] wejscie = new JComponent[]
					{
					new JLabel("Nazwa u\u017cytkownika: "), nazwa,
					new JLabel("Adres IP (dla trybu lokalnego \'localhost\'): "), ip
					};
			if(JOptionPane.showConfirmDialog(gui, wejscie, "Wprowad\u017a dane", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			{
			String ipp = ip.getText();
			boolean zlaWartosc = false;
			

			if(ipp.length() == 15)
			{
			ipp = ipp.substring(0, 15);
				if(ipp.charAt(3) == '.' &&	ipp.charAt(7) == '.' &&	ipp.charAt(11) == '.')
				{
					for(int i = 0; i < ipp.length(); i++ )
						{
						if(i == 3 || i==7 || i==11) continue;
						if(ipp.charAt(i) > 47 && ipp.charAt(i) < 58)
							continue;
						else zlaWartosc = true;
						}	
					
				}
				else zlaWartosc = true;
			}
			else zlaWartosc = true;
			if(nazwa.getText().length() == 0) {
											JOptionPane.showMessageDialog(gui, "Wprowad\u017a nazwê u\u017cytkownika!", 
											"Wprowadzono b\u0119\u0119dne dane", JOptionPane.WARNING_MESSAGE);
											return null;
			}
			
			if(ipp.matches("localhost")) return new String[] { nazwa.getText(), "localhost" };
			if(zlaWartosc) 
				{
				JOptionPane.showMessageDialog(gui, "Wprowadzono adres ip o b\u0142\u0119dnym formacie."
						+ "\nPoprawny format to xxx.xxx.xxx.xxx gdzie \'x\' oznacza liczb\u0119", 
						"Wprowadzono b\u0119\u0119dne dane", JOptionPane.WARNING_MESSAGE);
				return null;
				}
			
			return new String[] { nazwa.getText(), ip.getText() };					
			}
			return null;
		}

		
}

