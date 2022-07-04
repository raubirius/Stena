
import knižnica.*;

/**
 * Trieda slúžiaca na zjednotenie princípu detekcie kolízií loptičky
 * s obdĺžnikovým blokom. Potomkami tejto triedy sú Platforma a Tehla.
 * (Trieda tiež zjendocuje určité spoločné vlastnosti blokov, ako je zdvihnuté
 * pero, hrúbka čiary, kreslenie tvaru…)
 */
public class KolíznyBlok extends GRobot
{
	public KolíznyBlok()
	{
		zdvihniPero();
		vypĺňajTvary();
		hrúbkaČiary(2);
	}

	/**
	 * Vráti hraničné body obdĺžnika slúžiace na detekciu kolízií. Tá je
	 * implementovaná priamo v loptičke, keďže väčšina posudzovaných
	 * vlastností pri mechanizme kolízie patrí práve loptičke. (V podstate
	 * okrem tohto obdĺžnika patrí všetko ostatné loptičke.)
	 */
	public Bod[] dajObdĺžnik()
	{
		Bod záloha = poloha();

		vypniOhraničenie();
		Bod[] obdĺžnik = new Bod[4];
		double výška = 2 * výška();
		double šírka = výška() + šírka();
		preskoč(-šírka / 2, výška / 2);
		obdĺžnik[0] = poloha();
		preskočVpravo(šírka);
		obdĺžnik[1] = poloha();
		odskoč(výška);
		obdĺžnik[2] = poloha();
		preskočVľavo(šírka);
		obdĺžnik[3] = poloha();
		zapniOhraničenie();

		poloha(záloha);
		return obdĺžnik;
	}

	@Override public void kresliTvar()
	{
		veľkosť(veľkosť() * Stena.globálnaMierka);
		skočNa(polohaX() * Stena.globálnaMierka,
			polohaY() * Stena.globálnaMierka);

		obdĺžnik();
		if (vypĺňaTvary())
		{
			farba(čierna);
			kresliObdĺžnik();
		}
	}
}
