
import knižnica.*;

import static knižnica.Kláves.*;
import static knižnica.ÚdajeUdalostí.*;

/**
 * Platforma ovládaná hráčom, ktorá slúži na odrážanie loptičky.
 */
public class Platforma extends KolíznyBlok
{
	public Platforma()
	{
		šírka(200); zaoblenie(20);
		ohranič((800 - šírka()) / 2, (600 - výška()) / 2, PLOT);
		farba(svetlohnedá); vrstva(20); reset();
	}

	public void reset()
	{
		skočNa(0, -(600 - výška()) / 2 + 5);
	}

	@Override public void stlačenieKlávesu()
	{
		if (kláves(VPRAVO)) zrýchleniePosunu(2.5);
		else if (kláves(VĽAVO)) zrýchleniePosunu(-2.5);
	}

	@Override public void uvoľnenieKlávesu()
	{
		boolean vpravo = kláves(VPRAVO);
		if (vpravo || kláves(VĽAVO))
		{
			if (vpravo) zrýchleniePosunu(-5);
			else zrýchleniePosunu(5);
			zastavPoSpomaleníPosunu();
		}
	}
}
