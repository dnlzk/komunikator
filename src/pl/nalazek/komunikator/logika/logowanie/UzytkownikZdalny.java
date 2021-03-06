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
	 * @param adresSocket Gniazdo słuchające użytkownika zdalnego
	 * @param nazwa Nazwa użytkownika zdalnego
	 */
	public UzytkownikZdalny(InetSocketAddress adresSocket, String nazwa)
	{
		this();
		this.adresSocket = adresSocket;
		this.ip = adresSocket.getAddress().getHostAddress();
		port = adresSocket.getPort();
		this.nazwa = nazwa;
	}
	
	/** Zwraca nr ip użytkownika zdalnego
	 * @return Numer ip
	 */
	public String zwrocIp()
	{
		if(ip != null) return ip;
		else return null;
	}
	
	/** Zwraca nazwę użytkownika zdalnego
	 * @return Nazwa użytkownika zdalnego
	 */
	public String zwrocNazwe()
	{
		return nazwa;
	}
	
	/** Zwraca port słuchający użytkownika zdalnego
	 * @return Numer portu
	 */
	public int zwrocPort()
	{
		return port;
	}
	
	/** Ustawia port słuchający użytkownika zdalnego
	 * @param port Numer portu
	 */
	public void ustawPort(int port)
	{
		this.port = port;
	}
	
	/** Zmienia nazwę użytkownika zdalnego
	 * @param nowaNazwa Nowa nazwa użytkownika zdalnego
	 */
	public void zmienNazwe(String nowaNazwa)
	{
		nazwa = nowaNazwa;
	}
	
	/** Zwraca gniazdo słuchające użytkownika zdalnego
	 * @return Gniazdo
	 */
	public InetSocketAddress zwrocAdresGniazda()
	{
		return adresSocket;
	}
}
