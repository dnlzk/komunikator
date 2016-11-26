package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

import java.util.*;
import java.io.*;
import java.net.InetSocketAddress;
import pl.nalazek.komunikator.logika.logowanie.UzytkownikZdalny;

/** Interfejs obs³uguj¹cy Model aplikacji*/
public interface IModel {
	
	/** Zarz¹dza widzialnoœci¹ u¿ytkownika w sieci
	 * @param wartosc Wartoœæ "prawda" umo¿liwia nawi¹zanie po³¹cznia z programem z zewn¹trz. Wartoœæ "fa³sz" wy³¹cza widocznoœæ w sieci.
	 * @param localhost Parametr okreœlaj¹cy tryb pracy online.
	 * */
	void ustawOnline(boolean wartosc, LocalHost localhost) throws IOException, ClassNotFoundException, Wyjatek;

	/** Dodaje uzytkownika zdalnego do listy uzytkownikow obecnie zalogowanego uzytkownika lokalnego.
	 * @param nazwaUzytkownika Nazwa (alias) dodawanego uzytkownika.
	 * @param adresSocket Gniazdo u¿ytkownika zdalnego
	 * */
	UzytkownikZdalny dodajUzytkownikaZdalnego(String nazwaUzytkownika, InetSocketAddress adresSocket) throws Wyjatek;

    /** Usuwa uzytkownika zdalnego z listy uzytkownikow obecnie zalogowanego uzytkownika lokalnego.
	 * @param nazwaUzytkownika Nazwa (alias) dodawanego uzytkownika.
	 * @param adres Adres IP lub ID z zewnêtrznego serwera WWW/SQL.
	 * */
    void usunUzytkownikaZdalnego(String nazwaUzytkownika, String adres) throws Wyjatek;

    /** Zwraca adres IP komputera na którym uruchomiony jest program
     * @return Adres IP w postaci ci¹gu znaków w formacie: xxx.xxx.xxx.xxx
     * */
    String mojeIP();

    /** Wysy³a ¿¹danie nawi¹zania rozmowy ze zdalnym u¿ytkownikiem
     * @param uzytkownik Referencja do uzytkownika zdalnego
     * @return Referencja do rozmowy w przypadku zgody, referencja do rozmowy z id=0 w przypadku odmowy, "null" w przypadku braku odpowiedzi.*/
    RozmowaID rozpocznijRozmowe(UzytkownikZdalny uzytkownik) throws Wyjatek;

    /** Zwraca zbiór u¿ytkowników zdalnych dla obecnie zalogowanego u¿ytkownika lokalnego.
     * @return Lista u¿ytkowników zdalnych w postaci zbioru LinkedHashSet<String[]>: [0]:nazwa u¿ytkownika zdalnego, [1]:ip, [2]:nr u¿ytkownika w systemie */
    LinkedHashSet<String[]> listaUZ();

    /** Wysy³a ¿¹danie odebrania pliku przez u¿ytkownika zdalnego
     * @param uZdalny Adresat
     * @param id Referencja do rozmowy 
     * @param sciezka_pliku Scie¿ka wysy³anego pliku
     * @return Obiekt reprezentuj¹cy stan wysy³ania pliku */
    StanWyslaniaPliku wyslijPlik(UzytkownikZdalny uZdalny, RozmowaID id, String sciezka_pliku) throws Wyjatek;

    /** Wysy³a wiadomoœæ do u¿ytkownika
     * @param id Referencja do rozmowy z której wysy³ana jest wiadomoœæ
     * @param wiadomosc Treœæ wiadomoœci
     * */
    void wyslijWiadomosc(RozmowaID id, String wiadomosc) throws IOException;

    /** Koñczy rozmowê
     * @param id Referencja do rozmowy
     */
    void zakonczRozmowe(RozmowaID id) throws IOException;

}

