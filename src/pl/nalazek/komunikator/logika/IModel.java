package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

import java.util.*;
import java.io.*;
import java.net.InetSocketAddress;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/** Interfejs obs�uguj�cy Model aplikacji*/
public interface IModel {
	
	/** Zarz�dza widzialno�ci� u�ytkownika w sieci
	 * @param wartosc Warto�� "prawda" umo�liwia nawi�zanie po��cznia z programem z zewn�trz. Warto�� "fa�sz" wy��cza widoczno�� w sieci.
	 * @param localhost Parametr okre�laj�cy tryb pracy online.
	 * */
	void ustawOnline(boolean wartosc, LocalHost localhost) throws IOException, ClassNotFoundException, Wyjatek;

	/** Dodaje uzytkownika zdalnego do listy uzytkownikow obecnie zalogowanego uzytkownika lokalnego.
	 * @param nazwaUzytkownika Nazwa (alias) dodawanego uzytkownika.
	 * @param adresSocket Gniazdo u�ytkownika zdalnego
	 * */
	UzytkownikZdalny dodajUzytkownikaZdalnego(String nazwaUzytkownika, InetSocketAddress adresSocket) throws Wyjatek;

    /** Usuwa uzytkownika zdalnego z listy uzytkownikow obecnie zalogowanego uzytkownika lokalnego.
	 * @param nazwaUzytkownika Nazwa (alias) dodawanego uzytkownika.
	 * @param adres Adres IP lub ID z zewn�trznego serwera WWW/SQL.
	 * */
    void usunUzytkownikaZdalnego(String nazwaUzytkownika, String adres) throws Wyjatek;

    /** Zwraca adres IP komputera na kt�rym uruchomiony jest program
     * @return Adres IP w postaci ci�gu znak�w w formacie: xxx.xxx.xxx.xxx
     * */
    String mojeIP();

    /** Wysy�a ��danie nawi�zania rozmowy ze zdalnym u�ytkownikiem
     * @param uzytkownik Referencja do uzytkownika zdalnego
     * @return Referencja do rozmowy w przypadku zgody, referencja do rozmowy z id=0 w przypadku odmowy, "null" w przypadku braku odpowiedzi.*/
    RozmowaID rozpocznijRozmowe(UzytkownikZdalny uzytkownik) throws Wyjatek;

    /** Zwraca zbi�r u�ytkownik�w zdalnych dla obecnie zalogowanego u�ytkownika lokalnego.
     * @return Lista u�ytkownik�w zdalnych w postaci zbioru LinkedHashSet<String[]>: [0]:nazwa u�ytkownika zdalnego, [1]:ip, [2]:nr u�ytkownika w systemie */
    LinkedHashSet<String[]> listaUZ();

    /** Wysy�a ��danie odebrania pliku przez u�ytkownika zdalnego
     * @param uZdalny Adresat
     * @param id Referencja do rozmowy 
     * @param sciezka_pliku Scie�ka wysy�anego pliku
     * @return Obiekt reprezentuj�cy stan wysy�ania pliku */
    StanWyslaniaPliku wyslijPlik(UzytkownikZdalny uZdalny, RozmowaID id, String sciezka_pliku) throws Wyjatek;

    /** Wysy�a wiadomo�� do u�ytkownika
     * @param id Referencja do rozmowy z kt�rej wysy�ana jest wiadomo��
     * @param wiadomosc Tre�� wiadomo�ci
     * */
    void wyslijWiadomosc(RozmowaID id, String wiadomosc) throws IOException;

    /** Ko�czy rozmow�
     * @param id Referencja do rozmowy
     */
    void zakonczRozmowe(RozmowaID id) throws IOException;

}

