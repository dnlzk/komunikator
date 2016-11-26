package pl.nalazek.komunikator.logika;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ProgressMonitorInputStream;

import pl.nalazek.komunikator.Program;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** W¹tek obs³uguj¹cy nowe po³¹czenia przychodz¹ce */ 
public class WatekSluchajacy extends Watek {
	
	private ServerSocket gniazdoSluchajaceSrw = null;
	private int port;
	private Socket gniazdoSluchajace = null;
	private ProgressMonitorInputStream pmis;
	private BufferedInputStream bis;
	private ObjectInputStream strumienPrzych = null;
	
	/** Konstruktor domyœlny */
	public WatekSluchajacy()
	{ }
	
	/** Konstruktor z parametrem
	 * @param port Numer portu, który ma zostaæ otwarty do nas³uchiwania
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws Wyjatek
	 */
	public WatekSluchajacy(int port) throws IOException, ClassNotFoundException, Wyjatek
	{
		this.port = port;
	}
	
	/**  G³ówna metoda w¹tku */
	public void run()
	{
		try{
			
		/*SSLServerSocketFactory wytworniaGniazdekSSL = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		SSLServerSocket gniazdoSluchajaceSrw = (SSLServerSocket)wytworniaGniazdekSSL.createServerSocket(port); */
			gniazdoSluchajaceSrw = new ServerSocket(port);
			gniazdoSluchajaceSrw.setSoTimeout(1000);
			gniazdoSluchajace = new Socket();
		while(!interrupted())
			{
			try{
			gniazdoSluchajace = /*(SSLSocket)*/gniazdoSluchajaceSrw.accept();
			pmis = new ProgressMonitorInputStream(Program.komponentGui(this),"Pobieranie pliku...",gniazdoSluchajace.getInputStream());
			bis= new BufferedInputStream(pmis);
			strumienPrzych = new ObjectInputStream(bis);
			WatekObslugujacyPakiet a = new WatekObslugujacyPakiet(strumienPrzych, gniazdoSluchajace);
			a.start();
			}
			catch(InterruptedIOException w) {};
			}
		if(strumienPrzych!=null){
			strumienPrzych.close();
			bis.close();
			pmis.close();
		}
		
		gniazdoSluchajace.close();
		gniazdoSluchajaceSrw.close();
		
		}
		catch(IOException w)
		{
			System.out.println(w.getMessage());
		}
		finally
		{
			try {
				if(gniazdoSluchajace != null) gniazdoSluchajace.close();
				if(gniazdoSluchajaceSrw!=null) gniazdoSluchajaceSrw.close();
				if(strumienPrzych!=null){
					strumienPrzych.close();
					bis.close();
					pmis.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
