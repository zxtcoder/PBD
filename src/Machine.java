import java.util.*;

public class Machine {
	public ArrayList<Element> eList;
	public ArrayList<Particle> pList;

	public double sumTime, sumLength;
	public double dt, nowTime;

	
	public Machine(){
		eList=new ArrayList<Element>();
		pList=new ArrayList<Particle>();
		sumTime=sumLength=0;
		dt=nowTime=0;

	}
	
	public void setdt(double dtt){
		dt=dtt;
	}
	
	public void addDriftE(int index, double r, double l){
	    DriftE nde=new DriftE(r,l);
	    eList.add(index,nde);
	    sumLength=sumLength + nde.length;
	}
	
	public void addQuadE(int index, double r, double l, double kx, double kz){
		QuadE nqe=new QuadE(r,l,kx,kz);
		eList.add(index,nqe);
		sumLength=sumLength + nqe.length;
	}
	
	public void addDiE(int index, double r, double l, double n, double rho, double theta, double betai, double betao){
	    DiE ndie=new DiE(r,l,n,rho,theta,betai,betao);
	    eList.add(index, ndie);
	    sumLength=sumLength + ndie.length;
	}
	
	public void addIonSEType2(int index, ArrayList<Particle> pl, double r, double l, double p, 
	    double durT, double beamInt, double pMass, double pCharge, double pEnergy,
	    double xA, double xpA, double zA, double zpA, double sA, double spA){
		IonSE nise=new IonSE(pl,r,l,p,durT,beamInt,pMass,pCharge,pEnergy);
		nise.setISType2(xA, xpA, zA, zpA, sA, spA);
		eList.add(index, nise);
	    sumLength=sumLength + nise.length;
	}
	
	public void outInfo(){
		int pNum=0, i=0;
		pNum=pList.size();
		for(i=0;i<pNum;i++){
			//if(pList.get(i).flag==0)
			    System.out.printf("%f %f %f %f %f %f\n", pList.get(i).x, pList.get(i).z, pList.get(i).s, pList.get(i).vx, pList.get(i).vz, pList.get(i).vs);
		}
	}
	
	public void run(double tStep, double sTime){
		dt=tStep;
		sumTime=sTime;
		int i=0, j=0;
		double pNum=0, eNum=0, tmpL1=0, tmpL2=0;
		double outNum=0;
		eNum=eList.size();
		while(nowTime<sumTime){
			pNum=pList.size();
    		nowTime=nowTime + dt;

    		for(j=0;j<eNum;j++){
    			if(eList.get(j).name==0){
    				((IonSE)eList.get(j)).CalParticle(dt);
    			}
    		}
    		
			for(i=0;i<pNum;i++){

				if(pList.get(i).flag!=0)continue;
				if(pList.get(i).s > sumLength){
					pList.get(i).flag=2;
					outNum++;
					continue;
				}
				tmpL1=tmpL2=0;

				for(j=0;j<eNum;j++){
					tmpL1=tmpL1 + eList.get(j).length;
					if(tmpL1 >= pList.get(i).s){
						tmpL2=tmpL1 - eList.get(j).length;

						switch(eList.get(j).name){
						//IonSE
						case 0:break;
						//DriftE
						case 1:
							((DriftE)eList.get(j)).calParticle(pList.get(i),dt);
							break;
						//QuadE
						case 2:
							((QuadE)eList.get(j)).calParticle(pList.get(i),dt);
							break;
						//DiE
						case 3:
							if(pList.get(i).s< dt*pList.get(i).vs + tmpL2){
								((DiE)eList.get(j)).calParticle(pList.get(i), dt, 1);
							}
							else if(pList.get(i).s>tmpL2 + eList.get(j).length - dt*pList.get(i).vs){
								((DiE)eList.get(j)).calParticle(pList.get(i), dt, 2);
							}
							else{
								((DiE)eList.get(j)).calParticle(pList.get(i), dt, 0);
							}
							break;
						default:break;
						}
						break;
					}
				}
			}
		}
		
	}
	
	
////////////////////////////////////////////////////////
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Machine nm=new Machine();
		nm.addIonSEType2(nm.eList.size(),nm.pList, 100, 0, 2, 0.1, 10000000, 1, 1, 0.00001, 0.1, 0.01, 0.5, 0.01, 1, 0.1);
		nm.addDriftE(nm.eList.size(),100, 100000);
		//nm.addQuadE(100, 1000, 1e-10, 1e-10);
		//nm.addQuadE(100, 1000, -1e-10, -1e-10);
		nm.run(0.01, 1);
		nm.outInfo();

	}

}
