
import knižnica.*;

/**
 * Tehlička, ktorú má loptička „rozbiť“.
 */
public class Tehla extends KolíznyBlok implements Skrývateľný
{
	// *** Manažment inštancií ***

	private final static Zoznam<Tehla> tehly = new Zoznam<>();

	public static Tehla dajTehlu()
	{
		for (Tehla tehla : tehly)
			if (tehla.neaktívny())
			{
				tehla.reset();
				return tehla;
			}

		Tehla tehla = new Tehla();
		tehly.pridaj(tehla);
		return tehla;
	}

	public static void deaktivujVšetky()
	{
		for (Tehla tehla : tehly)
			if (tehla.aktívny())
			{
				tehla.deaktivuj();
				tehla.skry();
			}
	}

	public static void dajAktívne(Zoznam<Tehla> aktívne)
	{
		aktívne.vymaž();
		for (Tehla tehla : tehly)
			if (tehla.aktívny())
				aktívne.pridaj(tehla);
	}


	// Súkromný konštruktor: Nepovoľujeme vytváranie inštancií (mimo tejto
	// triedy) z dôvodu potreby vnútorného manažmentu ich inštancií.
	// 
	// Na vrátenie „čistej“ (novej alebo recyklovanej) inštancie treba použiť
	// metódu dajTehlu().
	// 
	private Tehla()
	{
		šírka(40); zaoblenie(5);
		ohranič((800 - šírka()) / 2, (600 - výška()) / 2, PLOT);
		farba(červená); reset();
	}

	public void reset()
	{
		skočNa(stred);
		aktivuj(false);
		zobraz();
		priehľadnosť(1);
	}

	public void zarovnajPolohu()
	{
		double výška = výška();
		skočNa(((int)(polohaX() / výška)) * výška,
			((int)(polohaY() / výška)) * výška);
	}

	public void buchni()
	{
		deaktivuj();
	}

	@Override public void náhodnáPoloha()
	{
		super.náhodnáPoloha();
		double polohaY = polohaY();
		if (polohaY < 0) polohaY(polohaY = -polohaY);
		zarovnajPolohu();
	}

	@Override public void aktivita() { zobrazuj(); }
	@Override public void pasivita() { skrývaj(); }
}
