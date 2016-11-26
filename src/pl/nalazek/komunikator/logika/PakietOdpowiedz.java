package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Abstrakcyjna klasa reprezentuj�ca odpowied� na pakiety pytajace */
public abstract class PakietOdpowiedz extends Pakiet {
	private static final long serialVersionUID = 1L;

	/** Weryfikuje poprawno�� odpowiedzi
	 * @param pytanie Pakiet pytaj�cy
	 * @return Warto�� "true" w przypadku potwierdzenia poprawno�ci odpowiedzi, warto�� "false" w przeciwnym wypadku
	 */
	public boolean sprawdzPoprawnoscOdpowiedzi(PakietPytanie pytanie)
	{
		long t = pytanie.kluczWeryf / pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy();
		if(t == kluczWeryf) return true;
		else return false;
	}


}
