import java.util.*;


public class IonSE extends Element{
	
	private double durT, beamInt;
	private double pMass, pCharge, pEnergy, vs;
	private double rT;

	
	private int ionSType;
	
//////type2////////////////
	private double xA, xpA, zA, zpA, spA;
	
	private ArrayList<Particle> pList;
	
	public IonSE(ArrayList<Particle> pl, double r, double l, double durTT, double beamIntt, double pMasss, double pChargee, double pEnergyy){
		super(r,l);
		durT=durTT; beamInt=beamIntt;
		pMass=pMasss; pCharge=pChargee; pEnergy=pEnergyy;
		ionSType=0;
		pList=pl;
		rT=0;
		xA=xpA=zA=zpA=spA=0;
		vs=Math.sqrt(2*pEnergy*PhyC.eV/pMass/PhyC.u);
	}
	
	public void setISType1(){
		ionSType=1;
	}
	
	public void setISType2(double xAA, double xpAA, double zAA, double zpAA, double spAA){
		ionSType=2;
		xA=xAA; xpA=xpAA; zA=zAA; zpA=zpAA; spA=spAA;
	}
	
	public void CalParticle(double dt){
		int i;
		int dPNum=(int)(dt*beamInt);
		if(ionSType==1){
			for(i=0;i<dPNum;i++){
				Particle np=new Particle(pMass, pCharge, 0, 0, 0, 0, 0, vs);
			}

		}
		else if(ionSType==2){
			for(i=0;i<dPNum;i++){
				
			}
		}
		else{}
	}
	

////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
