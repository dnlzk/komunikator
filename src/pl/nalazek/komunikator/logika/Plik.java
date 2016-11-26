package pl.nalazek.komunikator.logika;

import java.io.*;
import java.lang.management.ManagementFactory;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Klasa reprezenuj�ca plik danych */
public class Plik extends PlikNaglowek  {
	
	private static final long serialVersionUID = 1L;
	byte[] dane;
	
	/** Konstruktor
	 * @param sciezka_pliku �cie�ka pliku
	 * @throws IOException 
	 * @throws Wyjatek
	 */
	public Plik(String sciezka_pliku) throws IOException, Wyjatek
	{
		danePliku = new File(sciezka_pliku);
		FileInputStream fis = new FileInputStream(danePliku);
		long free = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() - ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
		Float free2 = (float) (free/(1000000));
		if(fis.available() < free )
		{
			dane = new byte[ (int) danePliku.length() ];
			fis.read(dane);
		}
		else {
			fis.close();
			throw new Wyjatek("Plik kt�ry pr�bujesz wys�a� jest za du�y!\n"
					+ "Maksymalny rozmiar pami�ci JavaVM to: " + free + "bajt�w (" + free2 + "MB)." );

		}
		fis.close();
	}
	
	/** Zapisuje plik na dysku
	 * @param sciezka �cie�ka do kt�rej b�dzie zapisany plik
	 * @throws IOException
	 */
	public void zapiszPlik(String sciezka) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(sciezka);
		fos.write(dane);
		fos.close();
		
	}
}
