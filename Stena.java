
import knižnica.*;

import static knižnica.Kláves.*;
import static knižnica.Svet.*;
import static knižnica.ÚdajeUdalostí.*;

/**
 * Hlavná trieda hry Stena. Prepája funkčnosť ostatných tried.
 */
public class Stena extends GRobot
{
	public static double globálnaMierka = 1.0;

	private static double poslednáMierka = 1.0;
	private final static Písmo písmo = new Písmo("Arial", Písmo.TUČNÉ, 50);

	private final Zoznam<Tehla> tehly = new Zoznam<>();
	private final Platforma platforma;
	private final Lopta lopta;
	private boolean výhra = false;
	private int úroveň = 0;


	private Stena()
	{
		super(šírkaZariadenia(), výškaZariadenia());
		rozmery(800, 600);
		písmo(písmo);
		platforma = new Platforma();
		lopta = new Lopta();
		ďalšiaÚroveň();
	}

	public void ďalšiaÚroveň()
	{
		výhra = true;
		Tehla.deaktivujVšetky();
		if (úroveň > 10) return;
		++úroveň;
		lopta.reset();
		platforma.reset();

		for (int i = 0; i < 19 * úroveň; ++i)
		{
			if (0 == i % 2) continue;
			Tehla tehla = Tehla.dajTehlu();
			tehla.skočNa(-360 + 40 * (i % 19),
				280 - 20 * (i / 19));
		}

		Tehla.dajAktívne(tehly);
		výhra = false;
	}


	@Override public void tik() // (Časovač.)
	{
		if (výhra) return;

		if (lopta.aktívny())
		{
			lopta.koliduje(platforma);

			boolean všetkoZničené = true;
			for (Tehla tehla : tehly)
				if (tehla.aktívny())
				{
					všetkoZničené = false;
					if (lopta.koliduje(tehla))
					{
						tehla.buchni();
						break;
					}
				}

			if (všetkoZničené && !výhra)
			{
				ďalšiaÚroveň();
				if (výhra)
					lopta.deaktivuj();
			}
		}

		if (neboloPrekreslené()) prekresli();
	}

	// Hlavný robot slúži na zobrazovanie informačných textov.
	// 
	@Override public void kresliTvar()
	{
		veľkosť(veľkosť() * Stena.globálnaMierka);
		skočNa(polohaX() * Stena.globálnaMierka,
			polohaY() * Stena.globálnaMierka);

		if (poslednáMierka != globálnaMierka)
		{
			písmo(písmo.deriveFont((float)(50f * globálnaMierka)));
			poslednáMierka = globálnaMierka;
		}

		if (výhra) text("Výhra!");
		else if (!časovačAktívny()) text("Pauza");
		obdĺžnik();
	}


	// *** Automatická zmena mierky pri zmene veľkosti okna. ***

	@Override public void zmenaVelkostiOkna()
	{
		upravMierku();
		if (časovačAktívny()) žiadajPrekreslenie(); else prekresli();
	}

	public static void upravMierku()
	{
		double mierkaX = viditeľnáŠírka() / 800.0;
		double mierkaY = viditeľnáVýška() / 600.0;
		globálnaMierka = mierkaX < mierkaY ? mierkaX : mierkaY;
	}


	/**
	 * Hlavná metóda. (Vstupný bod aplikácie.)
	 */
	public static void main(String[] args)
	{
		použiKonfiguráciu("Stena.cfg");
		try
		{
			nekresli();
			new Stena();
		}
		finally
		{
			upravMierku();
			prekresli();
		}
	}
}
