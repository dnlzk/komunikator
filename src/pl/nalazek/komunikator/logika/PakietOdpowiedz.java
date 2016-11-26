package pl.nalazek.komunikator.logika;

/**
 * @author Daniel Nalazek
 * Copyright (C) 2014 Daniel Nalazek
 */

/** Abstrakcyjna klasa reprezentuj¹ca odpowiedŸ na pakiety pytajace */
public abstract class PakietOdpowiedz extends Pakiet {
	private static final long serialVersionUID = 1L;

	/** Weryfikuje poprawnoœæ odpowiedzi
	 * @param pytanie Pakiet pytaj¹cy
	 * @return Wartoœæ "true" w przypadku potwierdzenia poprawnoœci odpowiedzi, wartoœæ "false" w przeciwnym wypadku
	 */
	public boolean sprawdzPoprawnoscOdpowiedzi(PakietPytanie pytanie)
	{
		long t = pytanie.kluczWeryf / pytanie.zwrocIdRozmowyZrodlowej().zwrocIdRozmowy();
		if(t == kluczWeryf) return true;
		else return false;
	}


}
