
/**
 * Rozhranie, ktoré „premení“ objekt na skrývateľný. Ak ide o potomka triedy
 * GRobot, tak prvých šesť metód „zapadne“ do predvolenej implementácie
 * a posledné dve pridajú nové funkcie, ktoré spúšťajú animácie zobrazovania
 * a skrývania objektu.
 */
public interface Skrývateľný
{
	public void zobraz();
	public void skry();
	public boolean skrytý();
	public boolean zobrazený();
	public double priehľadnosť();
	public void priehľadnosť(double priehľadnosť);

	/**
	 * Spustí plynulé animované zobrazovanie objektu. Ak je objekt skrytý,
	 * animácia sa vykonáva od úplnej nepriehľadnosti do úplnej priehľadnosti,
	 * inak pokračuje od aktuálnej úrovne priehľadnosti do úplného zobrazenia.
	 */
	default public void zobrazuj()
	{
		if (skrytý())
		{
			priehľadnosť(0.1);
			zobraz();
		}
		else if (priehľadnosť() < 0.9)
			priehľadnosť(priehľadnosť() + 0.1);
		else if (priehľadnosť() < 1.0)
			priehľadnosť(1.0);
	}

	/**
	 * Spustí plynulé animované skrývanie objektu. Animácia sa spustí iba ak
	 * je objekt zobrazený a postupuje od aktuálnej úrovne (ne)priehľadnosti
	 * do úplného skrytia.
	 */
	default public void skrývaj()
	{
		if (zobrazený())
		{
			if (priehľadnosť() > 0.1)
				priehľadnosť(priehľadnosť() - 0.1);
			else
				skry();
		}
	}
}
