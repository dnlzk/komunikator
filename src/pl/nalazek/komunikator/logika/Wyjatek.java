package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

public class Wyjatek extends Exception {

	private static final long serialVersionUID = 1L;
	public Wyjatek(String komunikat)
	{
		super(komunikat);
	}
}
