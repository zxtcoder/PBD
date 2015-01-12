import java.util.*;

public class IonSE extends Element{
	
	private double durT, beamInt;
	private double pMass, pCharge, pEnergy, vs0;
	private double rT;
	private double position;

	
	private int ionSType;
	
//////type2////////////////
	private double xA, xpA, zA, zpA, sA, spA;
	
	private ArrayList<Particle> pList;
	
	public IonSE(ArrayList<Particle> pl, double r, double l, double p, double durTT, double beamIntt, double pMasss, double pChargee, double pEnergyy){
		super(r,l,0);
		durT=durTT; beamInt=beamIntt;
		pMass=pMasss; pCharge=pChargee; pEnergy=pEnergyy;
		ionSType=0;
		pList=pl;
		rT=0;
		position=p;
		xA=xpA=zA=zpA=spA=0;
		vs0=Math.sqrt(2*pEnergy*PhyC.eV/pMass/PhyC.u);
	}
	
	public void setISType1(){
		ionSType=1;
	}
	
	public void setISType2(double xAA, double xpAA, double zAA, double zpAA, double sAA, double spAA){
		ionSType=2;
		xA=xAA; xpA=xpAA; 
		zA=zAA; zpA=zpAA; 
		sA=sAA; spA=spAA;
	}
	
	public void CalParticle(double dt){
		int i;
		double x,xp,z,zp,s,sp;
		double vx, vz, vs;
		if(rT>this.durT) return;

		rT=rT+dt;
		int dPNum=(int)(dt*beamInt);
		if(ionSType==1){
			for(i=0;i<dPNum;i++){
				Particle np=new Particle(pMass, pCharge, 0, 0, 0, 0, position, vs0);
				pList.add(np);
			}
		}
		else if(ionSType==2){
			for(i=0;i<dPNum;i++){
				x=Math.random()*xA*2-xA; xp=Math.random()*xpA*2-xpA;
				z=Math.random()*zA*2-zA; zp=Math.random()*zpA*2-zpA;
				s=Math.random()*sA*2-sA; sp=Math.random()*spA*2-spA;
				if(x*x/xA/xA + xp*xp/xpA/xpA + z*z/zA/zA + zp*zp/zpA/zpA + s*s/sA/sA + sp*sp/spA/spA <= 1){
					vx=xp*vs0; vz=zp*vs0; vs=vs0+sp*vs0; 
					Particle np=new Particle(pMass, pCharge, x, z, position+s, vx, vz, vs);
     				pList.add(np);
				}
				else i--;
			}
		}
		else{}
	}
	

////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		ArrayList<Particle> pl=new ArrayList<Particle>();
		IonSE ise=new IonSE(pl, 10, 10, 9, 100, 100, 1, 1, 1);
		ise.setISType2(1,0.2,1,0.2,1,0.2);
		ise.CalParticle(1000);
		int num=pl.size();
		int i;
		for(i=0;i<num;i++){
			System.out.printf("%f %f\n", pl.get(i).s, pl.get(i).vs);
			
		}

	}

}
