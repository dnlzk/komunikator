package pl.nalazek.komunikator.gui;


import java.awt.*;
import javax.swing.*;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezentuj¹ca interfejs graficzny programu */
public class Gui extends JFrame {
	
	/** Napis nad polem z romow¹ */
	public JLabel    	panel1Etykieta;
	/** Pole tekstowe z rozmow¹ */
	public JTextPane 	panel1PoleTekstowesc;
	/** Pole tekstowe do wpisywania wiadomoœci */
	public JTextArea 	panel1PoleTekstoweWpissc;
	/** Przycisk "Czyœæ" */
	public JButton   	panel1Wymaz;
	/** Przycisk "Wyœlij" */
	public JButton   	panel1Wyslij;
	/** Przycisk "Wyœlij plik" */
	public JButton   	panel1WyslijPlik;
	/** Przycisk "Po³¹cz" */
	public JButton   	panel2Polacz; 
	/** Przycisk "Roz³¹cz" */
	public JButton   	panel2Rozlacz;
	/** Przycisk "Dodaj" */
	public JButton   	panel2Dodaj;
	/** Przycisk "Usuñ" */
	public JButton   	panel2Usun;
	/** Kratka do zaznaczania online */
	public JCheckBoxMenuItem menuOnline;
	/** Napis na pasku stanu */
	public JLabel 		napisPaska; 
	/** Lista wyœwietlanych u¿ytkowników */
	public JList<String>	listaUzytkownikow;
	/** Pasek postêpu */
	public JProgressBar pasekPostepu;
	/** WskaŸnik zaznaczonego elementu na liœcie u¿ytkowników */
	public int listaOstatniIndeks = -1;
	/** Pole wyboru "Internet" */
	JRadioButtonMenuItem onlnInternet;
	/** Pole wyboru "Localhost1" */
	JRadioButtonMenuItem onlnLchst1;
	/** Pole wyboru "Localhost2" */
	JRadioButtonMenuItem onlnLchst2;
	/** Wskazuje zaznaczone pole wyboru */
	int menuOnlineWybor = 1;
	
	private OknoPanelRozmowa panelRozmowa;
	private OknoPanelUzytkownicy panelUzytkownicy;
	private OknoMenu oknoMenu;
	private static final long serialVersionUID = 1L;
		
	/** Konstruktor interfejsu graficznego */
		public Gui()
		{
			// Utworzenie zarz¹dców obs³uguj¹cych zdarzenia przychodz¹ce z okna programu
			panelRozmowa = new OknoPanelRozmowa(this);
			panelUzytkownicy = new OknoPanelUzytkownicy(this);
			oknoMenu = new OknoMenu(this);
			addWindowListener(new Okno(this));
			
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
					getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
					setUndecorated(true);
					
					
					// Budowa panelu rozmowy
					JPanel    	panel1 = new JPanel();
					GroupLayout layout = new GroupLayout(panel1);
								panel1.setLayout(layout);
								layout.setAutoCreateContainerGaps(true);
								layout.setAutoCreateGaps(true);
								panel1Etykieta = new JLabel("Nie po³¹czono");
								
								panel1PoleTekstowesc = new JTextPane();
							  		panel1PoleTekstowesc.setEditable(false);
					JScrollPane panel1PoleTekstowe = new JScrollPane(panel1PoleTekstowesc);
									panel1PoleTekstowe.setAutoscrolls(true);
									panel1PoleTekstowe.setMinimumSize(new Dimension(300,200));
								panel1PoleTekstoweWpissc = new JTextArea("Wpisz wiadomoœæ");
									panel1PoleTekstoweWpissc.setLineWrap(true);
									panel1PoleTekstoweWpissc.setAutoscrolls(true);
					JScrollPane panel1PoleTekstoweWpis = new JScrollPane(panel1PoleTekstoweWpissc);
									panel1PoleTekstoweWpis.setMinimumSize(new Dimension(300,42));
								
									panel1Wymaz = new JButton("Czy\u015b\u0107");
									panel1Wymaz.addActionListener(panelRozmowa);
								panel1Wyslij = new JButton("Wy\u015blij");
									panel1Wyslij.addActionListener(panelRozmowa);
								panel1WyslijPlik = new JButton("Wy\u015blij plik");
									panel1WyslijPlik.addActionListener(panelRozmowa);
					
					layout.setHorizontalGroup(layout.createParallelGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(panel1Etykieta)
									.addComponent(panel1PoleTekstowe)
									.addComponent(panel1PoleTekstoweWpis))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addGroup(layout.createSequentialGroup()
											.addComponent(panel1Wyslij)	
											.addComponent(panel1Wymaz))	
									.addComponent(panel1WyslijPlik)));
					
					layout.setVerticalGroup(layout.createSequentialGroup()
							.addComponent(panel1Etykieta)
							.addComponent(panel1PoleTekstowe)
							.addComponent(panel1PoleTekstoweWpis)
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(panel1Wymaz)
									.addComponent(panel1Wyslij))
							.addComponent(panel1WyslijPlik));
					
					
					//Budowa panelu paska informacyjnego
					JPanel pasek = new JPanel();
					napisPaska = new JLabel("Offline");
					napisPaska.setAlignmentX(Component.RIGHT_ALIGNMENT);
					pasek.setMinimumSize(new Dimension(150,10));
					pasek.setLayout(new BoxLayout(pasek,BoxLayout.Y_AXIS));
					pasek.add(napisPaska);
					
					//Budowa panelu paska postêpu
					pasekPostepu = new JProgressBar();
					pasekPostepu.setIndeterminate(true);
					pasekPostepu.setVisible(false);
					
					//Budowa panelu u¿ytkowników
					JPanel panel2 = new JPanel();
					JLabel      panel2Tytul = new JLabel("U¿ytkownicy:");
						   	panel2Polacz = new JButton("Po\u0142\u0105cz");
						   		panel2Polacz.addActionListener(panelUzytkownicy);
						   	panel2Rozlacz = new JButton("Roz\u0142\u0105cz");
						   		panel2Rozlacz.addActionListener(panelUzytkownicy);
						   	panel2Dodaj = new JButton("Dodaj");
						   		panel2Dodaj.addActionListener(panelUzytkownicy);
						   	panel2Usun = new JButton("Usu\u0144");
						   		panel2Usun.addActionListener(panelUzytkownicy);
					JList<String>   panel2Listaa = new JList<String>();
									panel2Listaa.setVisibleRowCount(10);
									listaUzytkownikow = panel2Listaa;
									listaUzytkownikow.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
									listaUzytkownikow.addListSelectionListener(panelUzytkownicy);
					JScrollPane panel2Lista = new JScrollPane(panel2Listaa);
							panel2Lista.setMinimumSize(new Dimension(158,220));
							panel2Lista.setMaximumSize(new Dimension(158,1000));
					
					GroupLayout layout2 = new GroupLayout(panel2);
					panel2.setLayout(layout2);
					layout2.setAutoCreateContainerGaps(true);
					layout2.setAutoCreateGaps(true);
					
					layout2.setHorizontalGroup(layout2.createParallelGroup()
							.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(panel2Tytul)
								.addComponent(panel2Lista))
							.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
									.addGroup(layout2.createSequentialGroup()
										.addComponent(panel2Polacz)
										.addComponent(panel2Rozlacz))
									.addGroup(layout2.createSequentialGroup()
										.addComponent(panel2Dodaj)
										.addComponent(panel2Usun)))
							);
					
					layout2.setVerticalGroup(layout2.createSequentialGroup()
							.addComponent(panel2Tytul)
							.addComponent(panel2Lista)
							.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(panel2Polacz)
									.addComponent(panel2Rozlacz))
							.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(panel2Dodaj)
									.addComponent(panel2Usun))
									);
					
					//Budowa menu programu
					JMenuBar menu = new JMenuBar();
					setJMenuBar(menu);
					JMenu plik = new JMenu("Plik");
					menu.add(plik);
						JMenuItem zakoncz = new JMenuItem("Zako\u0144cz");
								zakoncz.addActionListener(oknoMenu);
						plik.add(zakoncz);
					JMenu ustawienia = new JMenu("Ustawienia");
					menu.add(ustawienia);
						ustawienia.add(menuOnline = new JCheckBoxMenuItem("Online"));
							menuOnline.addActionListener(oknoMenu);
						ustawienia.addSeparator();
						ButtonGroup wyborOnline = new ButtonGroup();
							onlnInternet = new JRadioButtonMenuItem("Internet");
							onlnLchst1 = new JRadioButtonMenuItem("Localhost 1");
							onlnLchst2 = new JRadioButtonMenuItem("Localhost 2");
							onlnInternet.addItemListener(oknoMenu);
							onlnLchst1.addItemListener(oknoMenu);
							onlnLchst2.addItemListener(oknoMenu);
							wyborOnline.add(onlnInternet);
							wyborOnline.add(onlnLchst1);							
							wyborOnline.add(onlnLchst2);
								onlnLchst1.setSelected(true);
						ustawienia.add(onlnInternet);
						ustawienia.add(onlnLchst1);
						ustawienia.add(onlnLchst2);
					JMenu pomoc = new JMenu("Pomoc");
					menu.add(pomoc);
						JMenuItem oProgramie = new JMenuItem("O programie...");
									oProgramie.addActionListener(oknoMenu);
						pomoc.add(oProgramie);
						
					
					
					
					//Budowa g³ównego okna
					GroupLayout layout0 = new GroupLayout(getContentPane());
					getContentPane().setLayout(layout0);
					layout0.setAutoCreateContainerGaps(true);
					layout0.setAutoCreateGaps(true);
					
					layout0.setHorizontalGroup(layout0.createParallelGroup(GroupLayout.Alignment.TRAILING)
							.addGroup(layout0.createSequentialGroup()
									.addComponent(panel1)
									.addComponent(panel2))
							.addGroup(layout0.createSequentialGroup()
									.addComponent(pasekPostepu)
									.addComponent(pasek)) );
					
					layout0.setVerticalGroup(layout0.createSequentialGroup()
							.addGroup(layout0.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(panel1)
									.addComponent(panel2))
							.addGroup(layout0.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(pasekPostepu)
									.addComponent(pasek)) );
					
					pack();
					setVisible(true);
				}	
			} );
			
		}

		/** Dezaktywuje przyciski panelu rozmowa */
		public void dezaktywujPrzyciskiPaneluRozmowa()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			panel1Wymaz.setEnabled(false);
			panel1Wyslij.setEnabled(false);
			panel1WyslijPlik.setEnabled(false);
			}
			} );
		}
		
		/** Dezaktywuje ca³y panel rozmowa */
		public void dezaktywujPanelRozmowa()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			dezaktywujPrzyciskiPaneluRozmowa();
			panel1PoleTekstoweWpissc.setEnabled(false);
				}
			} );
		}
		
		/** Aktywuje przyciski panelu rozmowa */
		public void aktywujPrzyciskiPaneluRozmowa()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			panel1Wymaz.setEnabled(true);
			panel1Wyslij.setEnabled(true);
			panel1WyslijPlik.setEnabled(true);
				}
			} );
		}
		
		/** Aktywuje ca³y panel rozmowa */
		public void aktywujPanelRozmowa()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			aktywujPrzyciskiPaneluRozmowa();
			panel1PoleTekstoweWpissc.setEnabled(true);
				}
			} );
		}
		
		/** Dezaktywuje przyciski panelu u¿ytkownicy */
		public void dezaktywujPrzyciskiPaneluUzytkownicy()
		{
			dezaktywujPrzyciskiPaneluUzytkownicyLaczenie();
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			
			panel2Dodaj.setEnabled(false);
			panel2Usun.setEnabled(false);
				}
			} );
		}
		
		/** Aktywuje przyciski panelu u¿ytkownicy */
		public void aktywujPrzyciskiPaneluUzytkownicy()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			
			panel2Dodaj.setEnabled(true);
			panel2Usun.setEnabled(true);
				}
			} );
		}
		
		/** Dezktywuje przyciski panelu u¿ytkownicy zwi¹zanie z po³¹czeniami */
		public void dezaktywujPrzyciskiPaneluUzytkownicyLaczenie()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			panel2Polacz.setEnabled(false); 
			panel2Rozlacz.setEnabled(false);
				}
			} );
		}
		
		/** Aktywuje przyciski panelu u¿ytkownicy zwi¹zanie z po³¹czeniami */
		public void aktywujPrzyciskiPaneluUzytkownicyLaczenie()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
			panel2Polacz.setEnabled(true); 
			panel2Rozlacz.setEnabled(true);
				}
			} );
		}
	
		/** Wyœwietla okno dialogowe typu "Ostrze¿enie" z podan¹ w argumencie treœci¹ *
		 * @param s Treœæ ostrze¿enia
		 */
		public void oknoDialogoweWyjatek(String s)
		{
			JOptionPane.showMessageDialog(this, s, "Ostrze\u017cenie", JOptionPane.WARNING_MESSAGE);
		}
		
		/** Wyœwietla okno dialogowe typu "Pytanie" z podan¹ w argumencie treœci¹ *
		 * @param s Treœæ pytanie
		 * @return Liczbê odpowiadaj¹c¹ wyborowi u¿ytkownika (1:tak, 0:nie, -1:anuluj)
		 */
		public int oknoDialogowePytanie(String s)
		{
			int wynik = JOptionPane.showConfirmDialog(this, s, "Pytanie", JOptionPane.YES_NO_CANCEL_OPTION);
			if(wynik == JOptionPane.YES_OPTION) return 1;
			else if(wynik == JOptionPane.NO_OPTION) return 0;
			else return -1;
		}
		
		/** Dezaktywuje mo¿liwoœæ zmieniania wyboru trybu w jakim program pracuje online */
		public void dezaktywujWyborPrzelacznikowOnline()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
			public void run() {
			onlnInternet.setEnabled(false);
			onlnLchst1.setEnabled(false);
			onlnLchst2.setEnabled(false);
			}}) ; 
		}
		
		/** Wyœwietla okno dialogowe z informacjami na temat programu */
		public void oknoDialogoweInfo()
		{
			JOptionPane.showMessageDialog(this, "Komunikator\n"
					+ "\n"
					+ "Wersja: 1.0\n"
					+ "Autor: Daniel Nalazek\n"
					+ "","O programie", JOptionPane.INFORMATION_MESSAGE);
		}


		/** Wyœwietla okno dialogowe typu "Informacja" z podan¹ w argumentach treœci¹ *
		 * @param tekst Treœæ informacji
		 * @param tytul Tytu³ okna 
		 */
		public void oknoDialogoweInfo(String tekst, String tytul)
		{
			JOptionPane.showMessageDialog(this, tekst ,tytul, JOptionPane.INFORMATION_MESSAGE);
		}
				
		/** Aktywuje mo¿liwoœæ zmieniania wyboru trybu w jakim program pracuje online */
		public void aktywujWyborPrzelacznikowOnline()
		{
			SwingUtilities.invokeLater(new Runnable()
			{
			public void run() {
			onlnInternet.setEnabled(true);
			onlnLchst1.setEnabled(true);
			onlnLchst2.setEnabled(true);
			}}) ; 
		}
	
}
