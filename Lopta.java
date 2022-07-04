
import knižnica.*;
import static knižnica.Svet.*;

import static knižnica.Kláves.*;
import static knižnica.ÚdajeUdalostí.*;

/**
 * Loptička, ktorá sa odráža od platformy, tehličiek (pri čom ich ničí),
 * zvislých hraníc hracej plochy, hornej hranice hracej plochy a zaniká pri
 * kolízii so spodnou hranicou hracej plochy.
 */
public class Lopta extends GRobot implements Skrývateľný
{
	// *** (Začiatok bloku na účely ladenia.) ***
	/* * /

	private boolean zobrazSmer = false;

	@Override public void stlačenieKlávesu()
	{
		if (kláves(VK_A)) skoč(-3, 0);
		else if (kláves(VK_D)) skoč(3, 0);
		else if (kláves(VK_S)) skoč(0, -3);
		else if (kláves(VK_W)) skoč(0, 3);
		else if (kláves(VK_Q)) vľavo(3);
		else if (kláves(VK_E)) vpravo(3);
		else if (kláves(VK_R)) vpravo(180);
		else if (kláves(VK_T)) Svet.tik();
		else if (kláves(VK_X)) { zobrazSmer = !zobrazSmer; prekresli(); }
		else if (kláves(MEDZERA))
		{
			if (časovačAktívny()) zastavČasovač(); else spustiČasovač();
			prekresli();
		}
	}

	/* */
	// *** (Koniec bloku na účely ladenia.) ***


	public Lopta()
	{
		ohranič((800 - 20) / 2, (600 - 20) / 2, ODRAZ);
		zdvihniPero();
		hrúbkaČiary(2);
		vypĺňajTvary();
		farba(modrá);
		rýchlosť(15, false);
		vrstva(10);
		reset();
	}

	public void reset()
	{
		aktivácia();
		aktivuj(false);
		aktivácia();
	}


	@Override public void aktivácia()
	{
		skočNa(0, -50);
		uhol(náhodnéReálneČíslo(180 + 45, 180 + 90));
	}

	@Override public void kresliTvar()
	{
		veľkosť(veľkosť() * Stena.globálnaMierka);
		skočNa(polohaX() * Stena.globálnaMierka,
			polohaY() * Stena.globálnaMierka);

		krúžok();
		if (vypĺňaTvary())
		{
			farba(čierna);
			kružnica();
		}

		// *** (Patrí k bloku ladenia.) ***
		/* * /
		if (zobrazSmer)
		{
			hrúbkaČiary(0.75);
			skoč();
			farba(svetločervená);
			vpred(8);
		}
		/* */
	}

	@Override public boolean mimoHraníc(Bod[] poleBodov, double uhol)
	{
		Bod priesečník = priesečníkÚsečiek(poleBodov[0],
			poloha(), poleBodov[2], poleBodov[3]);
		if (null != priesečník)
			smer(Svet.smer(priesečník, poleBodov[1]));

		if (poleBodov[2].polohaX() == poleBodov[3].polohaX())
		{
			double smer = smer();

			// Zvislé steny menia uhol, ak sa loptička pohybuje príliš
			// horizontálne, aby nezostala uväznená medzi zvislými stenami:
			if (poleBodov[2].polohaX() > 0)
			{
				if (smer > 170)
				{
					if (smer < 180) smer(smer - 1);
					else if (smer < 190) smer(smer + 1);
				}
			}
			else if (smer < 10) smer(smer + 1);
			else if (smer > 350) smer(smer - 1);

		}
		else if (poleBodov[2].polohaY() == poleBodov[3].polohaY() &&
			poleBodov[2].polohaY() < 0)
		{
			// Spodná stena loptičku „vypne“ na 25 tikov, ale na to, aby sa
			// znova „zapla“ treba mometnálne udržiavať časovač aktívny –
			// niekedy treba hýbať platformou, niekedy nie; príčina? neznáma…
			// (to by bolo treba doriešiť)…
			deaktivuj(25);
		}

		return true;
	}

	@Override public boolean koliduje(GRobot r)
	{
		if (r instanceof KolíznyBlok)
		{
			// Detekcia kolízie loptičky s obdĺžnikom kolízneho bloku.
			// (Pozri aj komentár pri metóde KolíznyBlok.dajObdĺžnik().)
			KolíznyBlok blok = (KolíznyBlok)r;
			Bod poloha = poloha();

			Bod[] obdĺžnik = blok.dajObdĺžnik();
			Bod posledná = poslednáPoloha();

			for (int i = 0; i < 4; ++i)
			{
				Bod A, B;

				switch (i)
				{
				case 1: A = obdĺžnik[2]; B = obdĺžnik[3]; break;
				case 2: A = obdĺžnik[1]; B = obdĺžnik[2]; break;
				case 3: A = obdĺžnik[3]; B = obdĺžnik[0]; break;
				default: A = obdĺžnik[0]; B = obdĺžnik[1];
				}

				Bod priesečník = priesečníkÚsečiek(posledná, poloha, A, B);
				if (null != priesečník)
				{
					Bod bod = najbližšíBodNaPriamke(poloha, A, B);
					double vzdialenosť = Svet.vzdialenosť(poloha, bod);
					otočNa(bod);
					skoč(vzdialenosť * 2);
					smer(Svet.smer(priesečník, poloha()));

					if (i < 2 && blok.rýchlosťPosunu() != 0)
						vpravo(blok.rýchlosťPosunu());

					return true;
				}
			}

			return false;
		}

		return super.koliduje(r);
	}

	@Override public void aktivita() { zobrazuj(); }
	@Override public void pasivita() { skrývaj(); }
}
