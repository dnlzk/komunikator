package pl.nalazek.komunikator.logika;

import java.awt.FileDialog;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import javax.swing.SwingUtilities;
import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.gui.Gui;
import pl.nalazek.komunikator.logika.logowanie.Logowanie;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa odpowiadaj�ca za sterownie w programie */
public class Sterowanie implements ISterowanie {

	private Gui gui;
	private Sterowanie sterowanie;
	private Model model;
	private Logowanie logowanie;
	private boolean online = false;
	private LinkedHashSet<String[]> listaKluczy;
	private RozmowaID biezacaRozmowa;
	private String zmiennaText;
	
	/** Konstruktor. Tworzy instancj� komponentu Logowanie */
	public Sterowanie()
	{
		logowanie = new Logowanie(this);	
	}
	
	/** Uruchamia sterowanie w programie. */
	public void sterowanieStart()
	{
		sterowanie = this;
		gui = Program.komponentGui(this);
		model = Program.komponentModel(this);
		
		gui.dezaktywujPanelRozmowa();
		gui.dezaktywujPrzyciskiPaneluUzytkownicy();
		gui.dezaktywujPrzyciskiPaneluUzytkownicyLaczenie();
		logowanie.zaloguj("domyslny", "domyslny");
		odswiezListeKontaktow();
		SwingUtilities.invokeLater(new Runnable(){public void run() { gui.panel2Dodaj.setEnabled(true); }} ); 
	}
		
	/** Od�wie�a list� kontakt�w widzialn� w g��wnym oknie programu */
	private void odswiezListeKontaktow()
	{
		String [] t = wygenerujListeUzytkownikow();
		if(t[0] != "")
		SwingUtilities.invokeLater(new Runnable(){public void run() { gui.listaUzytkownikow.setListData(wygenerujListeUzytkownikow()); 
																			gui.listaUzytkownikow.setEnabled(true);
																			gui.panel2Usun.setEnabled(true);}} );
		else SwingUtilities.invokeLater(new Runnable(){public void run() {  gui.listaUzytkownikow.setListData(wygenerujListeUzytkownikow());
																			gui.listaUzytkownikow.clearSelection();
																			gui.listaUzytkownikow.setEnabled(false);
																			gui.panel2Usun.setEnabled(false);}} );

	}
	
	/** Tworzy list� kontakt�w, kt�ra b�dzie widoczna w g��wnym oknie programu */
	private String[] wygenerujListeUzytkownikow()
	{
		listaKluczy =  model.listaUZ();
		Iterator<String[]> i = listaKluczy.iterator();
		String[] lista;
		if(listaKluczy.size()!=0) 
		lista = new String[listaKluczy.size()];
		else lista =  new String[] { "" };
		int j = 0;
		while(i.hasNext())
		{
			lista[j] = i.next()[0];
			j++;
		}
		return lista;
	}
	
	/** Odbiera od interfersju graficznego ��danie zamkni�cia programu i zamyka program 
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zadanieZamknieciaProgramu(Gui guii)
	{
		if(guii == Program.komponentGui(this))
		{
			if(biezacaRozmowa != null)
			{
				rozlacz();
			}
			if(online) offlineUstaw();
			logowanie.wyloguj();
			System.exit(0);
		}
		
	}
	
	/** Odbiera od interfersju graficznego ��danie dodania u�ytkownika zdalnego 
	 * @param param Tablica z parametrami okre�laj�cymi nowego u�ytkownika zdalnego. param[0]:nazwa u�ytkownika, param[1]:adres ip */
	public void dodanoUzytkownikaZdalnego(String[] param)
	{	
		InetSocketAddress adr = new InetSocketAddress(param[1],model.zwrocAktywnyPortSluchajacy());
		try{
			model.dodajUzytkownikaZdalnego(param[0], adr);
		}
		catch(Wyjatek w)
		{
			odbierzWyjatek(w.getMessage());
		}
		odswiezListeKontaktow();
		
	}
	
	/** Odbiera od interfersju graficznego ��danie dodania u�ytkownika zdalnego 
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void usunietoUzytkownikaZdalnego(Gui guii)
	{
		if(guii == gui)
		if(!gui.listaUzytkownikow.isSelectionEmpty())
		{	
			try 
			{
				String[] refer = zwrocUzytkownikaWolajacegoZListy();
				model.usunUzytkownikaZdalnego(refer[0], refer[1]);
			} catch (Wyjatek e)
			{
				odbierzWyjatek(e.getMessage());
			}
			odswiezListeKontaktow();
			}
	}

	/** Zwraca u�ytkownika kt�ry obecnie jest zaznaczony na li�cie 
	 * @return Tablic� z parametrami okre�laj�cymi u�ytkownika zdalnego obecnie zaznaczonego na li�cie. param[0]:nazwa u�ytkownika, param[1]:adres ip
	 */
	private String[] zwrocUzytkownikaWolajacegoZListy()
	{
		Iterator<String[]> i = listaKluczy.iterator();
		int a = gui.listaOstatniIndeks;
		for(int j = 0; j < a; j++)
		{
			i.next();
		}
		return i.next();
	}

	/** Odbiera od interfejsu graficznego ��danie przej�cia w tryb online
	 * @param guii Referencja do interfejsu graficznego 
	 * @param stan Tryb przej�cia w online. 0:Internet, 1:Localhost1, 2:Localhost2
	 */
 	public void zmienionoNaOnline(Gui guii, int stan)
	{
		if(guii == Program.komponentGui(this))
			{
				try {
					model.ustawOnline(true, LocalHost.fromInt(stan));
					
		
				}
				 catch (ClassNotFoundException e) {
						odbierzWyjatek(e.getMessage());
					} catch (IOException e) {
						odbierzWyjatek(e.getMessage());
					} catch (Wyjatek e) {
						if(e.getMessage().contains("otworzy\u0107")) {
							odbierzWyjatek(e.getMessage());
							gui.menuOnline.setSelected(false);
							return;
						}
						odbierzWyjatek(e.getMessage());
					}
	
				online = true;
				sprawdzPrzyciskiLaczenia();
				switch(stan)
				{
				case 0: SwingUtilities.invokeLater(new Runnable(){public void run() { gui.napisPaska.setText("Online: " + Program.komponentModel(sterowanie).mojeIP()); }} ); break;
				case 1: SwingUtilities.invokeLater(new Runnable(){public void run() { gui.napisPaska.setText("Online: " + "127.0.0.1:5623"); }} ); break;
				case 2: SwingUtilities.invokeLater(new Runnable(){public void run() { gui.napisPaska.setText("Online: " + "127.0.0.1:5624"); }} ); break;
				}
				gui.dezaktywujWyborPrzelacznikowOnline();
			
			
			}
		
	}
	
 	/** Odbiera od interfejsu graficznego ��danie przej�cia w tryb offline
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zmienionoNaOffline(Gui guii)
	{
		if(guii == Program.komponentGui(this))
			{
			offlineUstaw();
			online = false;
			}
		
	}
	
	/** Ustawia offline w sterowaniu */
	private void offlineUstaw()
	{
		if(biezacaRozmowa != null)
			rozlacz();
		try {
			model.ustawOnline(false, null);
			
		} catch (ClassNotFoundException e) {
			odbierzWyjatek(e.getMessage());
		} catch (IOException e) {
			odbierzWyjatek(e.getMessage());
		} catch (Wyjatek e) {
			odbierzWyjatek(e.getMessage());
		}
		online = false;
		sprawdzPrzyciskiLaczenia();
		SwingUtilities.invokeLater(new Runnable(){public void run() { gui.napisPaska.setText("Offline"); }} ); 
		gui.aktywujWyborPrzelacznikowOnline();
	}
	
	/** Odbiera od interfejsu graficznego zmian� zaznacznia na li�cie u�ytkownik�w
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zmienionoStanListy(Gui guii)
	{
		if(guii == Program.komponentGui(this))
		{
			sprawdzPrzyciskiLaczenia();
					}
		
		
	}
	
	/** Odbiera od interfejsu graficznego ��danie nawi�zania po��czenia z u�ytkownikiem zdalnym
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zadaniePolaczenia(Gui guii)
	{
		if(guii == gui)
			if(!gui.listaUzytkownikow.isSelectionEmpty())
			{	
				pasekPostepuUstaw(true, "��czenie...");
				String[] refer = zwrocUzytkownikaWolajacegoZListy();
				try 
				{	
					biezacaRozmowa = model.rozpocznijRozmowe(model.zwrocUzytkownikaZdalnego(refer[1], refer[0]));
				} catch (Wyjatek e)
				{
					odbierzWyjatek(e.getMessage());
				}
				finally
				{
					pasekPostepuUstaw(false,null);
				}
				pasekPostepuUstaw(false,null);
				if(biezacaRozmowa == null)
				SwingUtilities.invokeLater(new Runnable(){public void run() { gui.oknoDialogoweWyjatek("Brak odpowiedzi od u�ytkownika: " + zwrocUzytkownikaWolajacegoZListy()[0] + "(" + zwrocUzytkownikaWolajacegoZListy()[1] + ")");}} );
				else if(biezacaRozmowa.zwrocIdRozmowy() == 0)	
				SwingUtilities.invokeLater(new Runnable(){public void run() { gui.oknoDialogoweWyjatek("U�ytkownik: " + zwrocUzytkownikaWolajacegoZListy()[0] + "(" + zwrocUzytkownikaWolajacegoZListy()[1] + ") nie zgodzi� si� na rozpocz�cie rozmowy..");}} );
				else 
				{
				SwingUtilities.invokeLater(new Runnable(){public void run() { gui.panel1Etykieta.setText("Po��czono z: " + zwrocUzytkownikaWolajacegoZListy()[0] + ", IP: " + zwrocUzytkownikaWolajacegoZListy()[1]); 
																				gui.aktywujPanelRozmowa();}} );
				sprawdzPrzyciskiLaczenia();
				}
			}
			
	}
	
	/** Odbiera od interfejsu graficznego ��danie zako�czenia po��czenia z u�ytkownikiem zdalnym
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zadanieRozlaczenia(Gui guii)
	{
		if(guii == gui)
		{
			rozlacz();
		}
	}
	
	/** Odbiera od interfejsu graficznego ��danie wys�ania pliku do uczestnik�w rozmowy
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zadanieWyslaniaPliku(Gui guii)
	{
		if(guii == gui)
		{
		FileDialog oknoPlik = new FileDialog(gui, "Wybierz plik", FileDialog.LOAD);
		oknoPlik.setVisible(true);
		String sciezkaPliku = oknoPlik.getDirectory() + oknoPlik.getFile();
		if(sciezkaPliku.isEmpty()) pasekPostepuUstaw(true, "Wys�ano ��danie odebrania pliku. Oczekiwanie na odpowied�...");
		try {
			StanWyslaniaPliku stan = model.wyslijPlik(null, biezacaRozmowa, sciezkaPliku);
			pasekPostepuUstaw(false,null);
			if(stan.stan == 1) SwingUtilities.invokeLater(new Runnable(){public void run() {  gui.oknoDialogoweInfo("Plik zosta� wys�any!", "Stan wysy�ania pliku");}} );
			else if(stan.stan == 0) SwingUtilities.invokeLater(new Runnable(){public void run() {  gui.oknoDialogoweInfo("U�ytkownik nie zgadza si� na odbi�r pliku!", "Stan wysy�ania pliku");}} );
			else if(stan.stan == -1) SwingUtilities.invokeLater(new Runnable(){public void run() {  gui.oknoDialogoweInfo("U�ytkownik nie odpowiada na ��danie odebrania pliku!", "Stan wysy�ania pliku");}} );
			else ;
			
			
		} catch (Wyjatek e) {
			odbierzWyjatek(e.toString());
		}
		}
	}
	
	/** Zaka�cza bie��c� rozmow� */
	private void rozlacz()
	{
		try {
			model.zakonczRozmowe(biezacaRozmowa);
		} catch (IOException e) {
			odbierzWyjatek(e.getMessage() + e.getCause());
		}				
		//model.zakonczRozmowe(biezacaRozmowa);
		biezacaRozmowa = null;
		sprawdzPrzyciskiLaczenia();
		gui.dezaktywujPanelRozmowa();
		SwingUtilities.invokeLater(new Runnable(){public void run() { gui.panel1Etykieta.setText("Roz��czono");}} );
	}
	
	/** Odbiera od interfejsu graficznego ��danie wys�ania wiadomo�ci do uczestnik�w rozmowy
	 * @param guii Referencja do interfejsu graficznego 
	 */
	public void zadanieWyslaniaWiadomosci(Gui guii)
	{
		if(guii == gui)
		{
			zmiennaText = gui.panel1PoleTekstowesc.getText();
			
			try {
				model.wyslijWiadomosc(biezacaRozmowa, new String(gui.panel1PoleTekstoweWpissc.getText()));
			} catch (IOException e) {
			odbierzWyjatek(e.getMessage()+e.getCause());
			}
			SwingUtilities.invokeLater(new Runnable(){public void run() { 
			zmiennaText = zmiennaText.concat("\n" + gui.panel1PoleTekstoweWpissc.getText());
			gui.panel1PoleTekstowesc.setText(zmiennaText);
			gui.panel1PoleTekstoweWpissc.setText("");
			}} );
		}
	}
	
	/** Funkcja zarz�dza przyciskami zwi�zanymi z po��czeniem */
	private void sprawdzPrzyciskiLaczenia()
	{
		if(online && !gui.listaUzytkownikow.isSelectionEmpty()) 
		{
			SwingUtilities.invokeLater(new Runnable(){public void run() { 
													 if(biezacaRozmowa!=null)
													 {   	
													 		
													    	if(biezacaRozmowa.zwrocNazweUzykownika().equals(zwrocUzytkownikaWolajacegoZListy()[0]))
													    		{ gui.panel2Polacz.setEnabled(false); 
													    		  gui.panel2Rozlacz.setEnabled(true); }
													    	
													   		else 
														    {
												    		gui.panel2Polacz.setEnabled(false); 
												    		gui.panel2Rozlacz.setEnabled(false);
														    }	
													 }
													 else 
													    	{
													    		gui.panel2Polacz.setEnabled(true);
													    		gui.panel2Rozlacz.setEnabled(false);
													    	}
												    }} );
		}											    
		else 
			gui.dezaktywujPrzyciskiPaneluUzytkownicyLaczenie();

	}
	
	//----------------------------------------------------------------------------------------Interfejs prawy
	@Override
	public void odbierzWiadomosc(String ip, RozmowaID id, String wiadomosc) {
		String text = gui.panel1PoleTekstowesc.getText();
		Calendar czas = Calendar.getInstance();
		gui.panel1PoleTekstowesc.setText(text.concat("\n\t" + czas.get(Calendar.HOUR_OF_DAY) 
				+ ":" + czas.get(Calendar.MINUTE) + ":" + czas.get(Calendar.SECOND) +" " + wiadomosc));
	}

	@Override
	public OdpowiedzSterowania odbierzRozmowe(String ip, RozmowaID rozmowa) {
		int odp = gui.oknoDialogowePytanie("Czy zgadzasz si\u0119 na rozpocz\u0119cie rozmowy z nast\u0119puj\u0105cym u\u017cytkownikiem:\n"
				+ "u\u017cytkownik " + rozmowa.zwrocNazweUzykownika() + ", IP: " + ip + ".");
		switch(odp)
		{
		case -1: return new OdpowiedzSterowania(-1);
		case 0: return new OdpowiedzSterowania(0);
		case 1: {
			
			SwingUtilities.invokeLater(new Runnable(){public void run() { gui.panel1Etykieta.setText("Po��czono z: " + zwrocUzytkownikaWolajacegoZListy()[0] + ", IP: " + zwrocUzytkownikaWolajacegoZListy()[1]);
																			gui.aktywujPanelRozmowa(); }} );
			odswiezListeKontaktow();
			biezacaRozmowa = rozmowa;
			sprawdzPrzyciskiLaczenia();
			return new OdpowiedzSterowania(1);
		}
		}
		return null;
		
	}
	
	/** Odbiera zako�czenie rozmowy spowodowane niespodziewanym roz��czeniem */
	public void odbierzZakonczenieRozmowyWymuszone() {
			model.rozmowy().get(biezacaRozmowa.zwrocIdRozmowy()).zakonczWymuszenie();
		SwingUtilities.invokeLater(new Runnable(){public void run() { 
			gui.dezaktywujPanelRozmowa();
			gui.panel1Etykieta.setText("Roz��czono"); }} );
			biezacaRozmowa = null;
			sprawdzPrzyciskiLaczenia();
		
	}
	
	@Override
	public void odbierzZakonczenieRozmowy(String ip, RozmowaID id) {
		SwingUtilities.invokeLater(new Runnable(){public void run() { 
			gui.dezaktywujPanelRozmowa();
			gui.panel1Etykieta.setText("Roz��czono"); }} );
			biezacaRozmowa = null;
			sprawdzPrzyciskiLaczenia();
		
	}

	@Override
	public void odbierzWyjatek(String opis) 
	{
		gui.oknoDialogoweWyjatek(opis);
		
	}

	@Override
	public OdpowiedzSterowania odbierzPlik(String ip, RozmowaID id, PlikNaglowek plik, StanWyslaniaPliku postep) 
	{
		long dlug = plik.danePliku.length();

		int odp = gui.oknoDialogowePytanie("Czy zgadzasz si� na odebranie nast�puj�cego pliku:\n"
				+ "u\u017cytkownik: " + id.zwrocNazweUzykownika() + ", IP: " + ip + "\n"
				+ "nazwa pliku: " + plik.danePliku.getName() + ", rozmiar: " + dlug + " bajt�w");
		switch(odp)
		{
		case -1: return new OdpowiedzSterowania(-1);
		case 0: return new OdpowiedzSterowania(0);
		case 1: pasekPostepuUstaw(true,"Pobieranie pliku...");
			return new OdpowiedzSterowania(1);
		}
		return new OdpowiedzSterowania(-1);
	}
	
	/** Informuje o odebraniu wcze�niej uzgodnionego pakietu z danymi (pliku)
	 * @param nazwaKoncowa Nazwa pliku
	 * @return �cie�ka pliku, gdzie plik b�dzie zapisany
	 */
	public String odebranoPlik(String nazwaKoncowa)
	{
		pasekPostepuUstaw(false,null);
		FileDialog oknoPlik = new FileDialog(gui, "Wybierz katalog docelowy", FileDialog.SAVE);
		oknoPlik.setFile(nazwaKoncowa);
		oknoPlik.setVisible(true);
		return oknoPlik.getDirectory() + oknoPlik.getFile();
		
	}
	
	/** Zarz�dza paskiem post�pu
	 * @param aktywny Warto�� "true" sprawia �e pasek jest widoczny, warto�� "false" przeciwnie.
	 * @param tekst Tekst wy�wietlany na pasku
	 */
	public void pasekPostepuUstaw(final boolean aktywny, final String tekst)
	{
		try {
			SwingUtilities.invokeAndWait(new Runnable(){public void run() {
				if(aktywny)
				gui.pasekPostepu.setVisible(true);
				else gui.pasekPostepu.setVisible(false);
				if(tekst != null) 
					{
					gui.pasekPostepu.setString(tekst);
					gui.pasekPostepu.setStringPainted(true);
					}
				else gui.pasekPostepu.setStringPainted(false);
			}});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	
	}

}	



