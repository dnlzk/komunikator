package pl.nalazek.komunikator.logika.logowanie;

import java.net.InetSocketAddress;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

public class UzytkownikZdalny extends Uzytkownicy  {
	
	private static final long serialVersionUID = 1L;
	private String ip;
	private InetSocketAddress adresSocket;
	private int port;
	
	/** Konstruktor */
	public UzytkownikZdalny()
	{
		nrUzytkownika = licznik;
	}
	
	/** Konstruktor uzytkownika zdalnego
	 * @param adresSocket Gniazdo s³uchaj¹ce u¿ytkownika zdalnego
	 * @param nazwa Nazwa u¿ytkownika zdalnego
	 */
	public UzytkownikZdalny(InetSocketAddress adresSocket, String nazwa)
	{
		this();
		this.adresSocket = adresSocket;
		this.ip = adresSocket.getAddress().getHostAddress();
		port = adresSocket.getPort();
		this.nazwa = nazwa;
	}
	
	/** Zwraca nr ip u¿ytkownika zdalnego
	 * @return Numer ip
	 */
	public String zwrocIp()
	{
		if(ip != null) return ip;
		else return null;
	}
	
	/** Zwraca nazwê u¿ytkownika zdalnego
	 * @return Nazwa u¿ytkownika zdalnego
	 */
	public String zwrocNazwe()
	{
		return nazwa;
	}
	
	/** Zwraca port s³uchaj¹cy u¿ytkownika zdalnego
	 * @return Numer portu
	 */
	public int zwrocPort()
	{
		return port;
	}
	
	/** Ustawia port s³uchaj¹cy u¿ytkownika zdalnego
	 * @param port Numer portu
	 */
	public void ustawPort(int port)
	{
		this.port = port;
	}
	
	/** Zmienia nazwê u¿ytkownika zdalnego
	 * @param nowaNazwa Nowa nazwa u¿ytkownika zdalnego
	 */
	public void zmienNazwe(String nowaNazwa)
	{
		nazwa = nowaNazwa;
	}
	
	/** Zwraca gniazdo s³uchaj¹ce u¿ytkownika zdalnego
	 * @return Gniazdo
	 */
	public InetSocketAddress zwrocAdresGniazda()
	{
		return adresSocket;
	}
}
