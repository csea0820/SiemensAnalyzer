package sei.buaa.debug.metric;

import sei.buaa.debug.entity.StatementSum;


public class WongSusp implements ISuspsCalculator{

	int Nf,Ns;
	int Nf1,Nf2,Nf3;
	int Ns1,Ns2,Ns3;
	
	double wf1 = 1.0,wf2 = 0.1,wf3 = 0.01;
	double ws1 = 1.0,ws2 = 0.1;
	double alpha = 0.01;
	double Xfs = 0.0;
	

	public double calcSups(StatementSum eSum) {
		int a00 = eSum.getA00();
		int a01 = eSum.getA01();
		int a10 = eSum.getA10();
		int a11 = eSum.getA11();
		
		Nf = a11;
		Ns = a10;
		
		if (Nf == 0)Nf1 = 0;
		else if (Nf == 1)Nf1 = 1;
		else if (Nf >= 2)Nf1 = 2;
		
		if (Nf <= 2)Nf2 = 0;
		else if (Nf >= 3 && Nf <= 6)Nf2 = Nf-2;
		else if (Nf > 6)Nf2 = 4;
		
		if (Nf <= 6)Nf3 = 0;
		else if (Nf > 6)Nf3 = Nf-6;
		
		if (Nf1 == 0 || Nf1 == 1)Ns1 = 0;
		else if (Nf1 == 2 && Ns >= 1)Ns1 = 1;
		
		if (Ns <= Ns1)Ns2 = 0;
		else if (Ns > Ns1 && Ns < Nf2+Ns1)Ns2 = Ns - Ns1;
		else if (Ns >= Nf2 + Ns1) Ns2 = Nf2;
		
		if (Ns < Ns1 + Ns2)Ns3 = 0;
		else if (Ns >= Ns1 + Ns2)Ns3 = Ns - Ns1 - Ns2;
		
		Xfs = (a01+11)*1.0/(a00+a10);
		
		return wf1*Nf1 + wf2*Nf2 + wf3*Nf3 - (ws1*Ns1 + ws2*Ns2 + alpha*Xfs*Ns3);
	}

}
