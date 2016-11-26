package pl.nalazek.komunikator.logika;

import java.io.*;
import java.net.Socket;

//import javax.net.ssl.SSLSocket;
//import javax.net.ssl.SSLSocketFactory;


import pl.nalazek.komunikator.Program;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Watek tworz�cy po��czenie do wysylania na okre�lonym porcie */
public class WatekWysylajacy extends Watek {
	
	private /*SSL*/Socket gniazdoWysylajace = null;
	private int port;
	private ObjectOutputStream strumienWych;
	private Rozmowa rozmowa;
	private UzytkownikZdalny uZdalny;
	private Pakiet pakiet;
	private String adresIp;
	/** Semafor obs�uguj�cy dost�p do strumienia wysy�aj�cego. Umo�liwia u�ycie na nim metod wait() i notify(). */
	Object semafor;
	
	/** Konstruktor w�tku wysy�aj�cego dla u�ytkownika zdalnego 
	 * @param rozmowa Referencja do rozmowy, z kt�rej b�dzie obs�ugiwany ten w�tek
	 * @param uZdalny Referencja do u�ytkownika zdalnego, kt�ry ma zosta� pod��czony strumie� wychodz�cy w tym w�tku
	 * @param port Port u�ytkownika zdalnego do kt�rego ma zosta� pod��czony strumie� wychodz�cy w tym w�tku
	 */ 
	public WatekWysylajacy(Rozmowa rozmowa, UzytkownikZdalny uZdalny, int port)
	{
		semafor = new Object();
		this.port = port;
		this.uZdalny = uZdalny;
		this.rozmowa = rozmowa;	
	}
	
	/** Konstruktor w�tku wysy�aj�cego dla pojedynczego pakietu 
	 * @param pakiet Pakiet do wys�ania
	 * @param adresIp Adres IP komputera docelowego
	 * @param port Port komputera docelowego
	 */
	public WatekWysylajacy(Pakiet pakiet, String adresIp, int port)
	{
		semafor = new Object();
		this.port = port;
		this.adresIp = adresIp;
		this.pakiet = pakiet;
	}
	
	/**  G��wna metoda w�tku */
	public void run()
	{
		try{
			
			/*SSLSocketFactory wytworniaGniazdekSSL = (SSLSocketFactory)SSLSocketFactory.getDefault();*/
			if(uZdalny != null) 
			{
				String ip = (uZdalny.zwrocIp().equals("localhost")) 
						|| (uZdalny.zwrocIp().equals("127.0.0.1")) ? 
						null : uZdalny.zwrocIp();
				gniazdoWysylajace = /*(SSLSocket)wytworniaGniazdekSSL.createSocket(uZdalny.zwrocIp(),port)*/
									new Socket(ip , port);
			}
			else if(adresIp != null)
				gniazdoWysylajace = /*(SSLSocket)wytworniaGniazdekSSL.createSocket(adresIp, port)*/
						new Socket(adresIp=="localhost" ? null : adresIp, port);
			else 
				interrupt();
			BufferedOutputStream bos = new BufferedOutputStream(gniazdoWysylajace.getOutputStream());
			strumienWych = new ObjectOutputStream(bos);
			if(uZdalny != null)
			{
				synchronized(semafor) {
				
					while(!interrupted())
					{
					
						semafor.wait();
						
						Pakiet a =  rozmowa.obslugaKolejki(uZdalny,null);
						strumienWych.writeObject(a);
						strumienWych.flush();
						strumienWych.reset();
    				}
				}
			}
			else if(adresIp != null)
			{
				strumienWych.writeObject(pakiet);
				strumienWych.flush();
			}
			gniazdoWysylajace.close();
			strumienWych.close();
		}
		catch(IOException w)
		{
			if(w.getMessage().contains("refused")) ;
			else Program.komponentSterowanie().odbierzWyjatek("W w�tku: " + this.getName() + " wyst�pi� wyj�tek.\n" + w.getMessage() + ";;" + w.getCause());
		}
		catch(InterruptedException w)
		{
		}
		catch(Throwable w)
		{
			w.getMessage();
		}
		finally
		{
			try 
			{
				if(gniazdoWysylajace != null) gniazdoWysylajace.close() ;
				if(strumienWych != null) strumienWych.close();
			} 
			catch (IOException i) 
			{ 
				i.getMessage();
				}
		}
		
	}
}
