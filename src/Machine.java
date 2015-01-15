import java.io.File;
import java.util.*;
import java.io.*;

public class Machine {
	public ArrayList<Element> eList;
	public ArrayList<Particle> pList;
	public ArrayList<Matrix> sigma;

	public double sumTime, sumLength;
	public double dt, nowTime;
	public int nowStep, logStep;
	public String logName;
	
	public Machine(){
		eList=new ArrayList<Element>();
		pList=new ArrayList<Particle>();
		sigma=new ArrayList<Matrix>();

		sumTime=sumLength=0;
		dt=nowTime=0;
		nowStep=logStep=0;
		logName="";

	}
	
	public void calSigma(){
		int i=0, j=0;
		double nowL=0.0, tmpL1=0, tmpL2=0, ds=0;
		for(i=0;i<eList.size();i++){
			if(eList.get(i).name==0){
				IonSE ionse=(IonSE)eList.get(i);
				double x0=ionse.xA, xp0=ionse.xpA; double z0=ionse.zA, zp0=ionse.zpA;
				Matrix ms0=new Matrix(4, 4), ms;
				Particle p;
				ms0.setValue(0, 0, x0*x0); ms0.setValue(1, 1, xp0*xp0);
				ms0.setValue(2, 2, z0*z0); ms0.setValue(3, 3, zp0*zp0);
				sigma.add(ms0);

				ds=ionse.vs0*dt;
				nowL=0;
				while(nowL<sumLength){
					tmpL1=tmpL2=0;
					for(j=0;j<eList.size();j++){
						tmpL1+=eList.get(j).length;
						if(tmpL1>=nowL){
							tmpL2=tmpL1-eList.get(j).length;
							switch(eList.get(j).name){
							//IonSE
							case 0:break;
							//DriftE
							case 1:
								ms0=sigma.get(sigma.size()-1);
                                ms=((DriftE)(eList.get(j))).calSigma(ms0, ds);
                                sigma.add(ms);
								break;
						    //QuadE
							case 2:
								ms0=sigma.get(sigma.size()-1);
								p=new Particle(ionse.pMass, ionse.pCharge, 0, 0, 0, 0, 0, ionse.vs0);
								ms=((QuadE)(eList.get(j))).calSigma(p, ms0, ds);
								sigma.add(ms);
								break;
							//DiE
							case 3:
								ms0=sigma.get(sigma.size()-1);
                                if(nowL<ds + tmpL2){
                                	ms=((DiE)(eList.get(j))).calSigma(ms0, ds, 1);
                                }
                                else if(nowL>tmpL1 - ds){
                                	ms=((DiE)(eList.get(j))).calSigma(ms0, ds, 2);
                                }
                                else{
                                	ms=((DiE)(eList.get(j))).calSigma(ms0, ds, 0);
                                	
                                }
                                sigma.add(ms);
								break;
							}
							break;
						}
					}

					nowL+=ds;
				}
				
				
				break;
			}
		}
	}
	
	public void clearParticle(){
		pList.clear();
	}
	
	public void setdt(double dtt){
		dt=dtt;
	}
	
	public void addDriftE(int index, double r, double l){
	    DriftE nde=new DriftE();
	    nde.setPara(r, l);
	    eList.add(index,nde);
	    sumLength=sumLength + nde.length;
	}
	
	public void addQuadE(int index, double r, double l, double kx, double kz){
		QuadE nqe=new QuadE();
		nqe.setPara(r, l, kx, kz);
		eList.add(index,nqe);
		sumLength=sumLength + nqe.length;
	}
	
	public void addDiE(int index, double r, double l, double n, double rho, double theta, double betai, double betao){
	    DiE ndie=new DiE();
	    ndie.setPara(r, l, n, rho, theta, betai, betao);
	    eList.add(index, ndie);
	    sumLength=sumLength + ndie.length;
	}
	
	public void addIonSEType2(int index, ArrayList<Particle> pl, double r, double l, double p, 
	    double durT, double beamInt, double pMass, double pCharge, double pEnergy,
	    double xA, double xpA, double zA, double zpA, double sA, double spA){
		IonSE nise=new IonSE();
		nise.setPara2(pl, r, l, p, durT, beamInt, pMass, pCharge, pEnergy, xA, xpA, zA, zpA, sA, spA);
		eList.add(index, nise);
	    sumLength=sumLength + nise.length;

	}
	
	public void calLength(){
		int i=0;
		sumLength=0;
		for(i=0;i<eList.size();i++){
			sumLength+=eList.get(i).length;
		}
		
	}
	
	public void outInfo(){
		int pNum=0, i=0;
		pNum=pList.size();
		for(i=0;i<pNum;i++){
			//if(pList.get(i).flag==0)
			    System.out.printf("%f %f %f %f %f %f\n", pList.get(i).x, pList.get(i).z, pList.get(i).s, pList.get(i).vx, pList.get(i).vz, pList.get(i).vs);
		}
	}
	
	public void outLog(){
		try{
		    if(nowStep==0){
			    File logDir=new File(this.logName);
			    if(!logDir.exists()){
				    logDir.mkdirs();
			    }
			    File infoFile=new File(this.logName+"/info.txt");
			    FileWriter fW=new FileWriter(infoFile);
			    fW.write("Name " + this.logName + "\n");
			    fW.write("SumLength " + this.sumLength + "\n");
			    fW.write("Time " + this.sumTime + "\n");
			    fW.write("dt " + this.dt + "\n");
                fW.write("Name");
			    for(Element eTmp: this.eList){
			    	fW.write(" " + eTmp.name);
			    }
			    fW.write("\nLength");
			    for(Element eTmp: this.eList){
			    	fW.write(" " + eTmp.length);
			    }
			    fW.write("\nRadius");
			    for(Element eTmp: this.eList){
			    	fW.write(" " + eTmp.radius);
			    }
			    fW.close();
			    
			    
			    File sigmaFile=new File(this.logName+"/sigma.txt");
			    fW=new FileWriter(sigmaFile);
			    fW.write("x xp z zp\n");
			    int i=0;
			    for(i=0;i<sigma.size();i++){
			    	double x=(sigma.get(i).getValue(0, 0)); double xp=(sigma.get(i).getValue(1, 1));
			    	double z=(sigma.get(i).getValue(2, 2)); double zp=(sigma.get(i).getValue(3, 3));
			    	fW.write("" + x + " " + xp + " " + z + " " + zp + "\n");
			    }

			    fW.close();
		    }
		    if(nowStep%logStep==0){
		    	int i=0;
		    	String stepStr=String.format("%010d", nowStep);
		    	File logF=new File(this.logName + "/Step" + stepStr + ".txt");
		    	FileWriter fW=new FileWriter(logF);
		    	fW.write("flag m charge x z s xp zp vx vz vs\n");
		    	for(i=0;i<this.pList.size();i++){
		    		Particle pT=pList.get(i);
		    		fW.write("" + pT.flag + " " + pT.mass + " " + pT.charge + " " + 
		    		         pT.x + " " + pT.z + " " + pT.s + " " + pT.xp + " " + pT.zp + 
		    				" " + pT.vx + " " + pT.vz + " " + pT.vs + "\n");
		    	}

		    	fW.close();

		    }
		}catch(Exception e){}

	}
	public void setRunPara(double tStep, double sTime, int logStepp, String logNamee){
		dt=tStep;
		sumTime=sTime;
		logStep=logStepp; logName=logNamee;
		this.calLength();
	}	

	public void run(){
		int i=0, j=0;
		double pNum=0, eNum=0, tmpL1=0, tmpL2=0;
		double outNum=0;
//////////////////////////////////////////
		eNum=eList.size();
		nowTime=0; nowStep=0;
		this.calLength();
		for(Element e: eList){
			if(e.name==0)((IonSE)e).rT=0;
		}
////////////////////////////////////////
		
		while(nowTime<sumTime){
			this.outLog();
			pNum=pList.size();
    		nowTime=nowTime + dt;
    		nowStep++;

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
		//nm.addIonSEType2(nm.eList.size(),nm.pList, 100, 0, 2, 0.1, 10000, 1, 1, 0.00001, 0.1, 0.01, 0.5, 0.01, 1, 0.1);
//		nm.addIonSEType2(nm.eList.size(),nm.pList, 10, 100, 2, 1, 100, 1, 1, 0.00001, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
		nm.addIonSEType2(nm.eList.size(),nm.pList, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		((IonSE)nm.eList.get(0)).setPara2(nm.pList, 10, 100, 2, 1, 100, 1, 1, 0.0001, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);

		nm.addDriftE(nm.eList.size(),10, 100);
		//nm.addQuadE(nm.eList.size(), 100, 1000, 1e-10, 1e-10);
		//nm.addQuadE(100, 1000, -1e-10, -1e-10);
		nm.setRunPara(0.001, 1, 1, "test3");
		nm.calSigma();
		nm.run();
		nm.outInfo();

	}

}
