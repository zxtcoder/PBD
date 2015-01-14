import java.lang.Math.*;

public class DiE extends Element{
	public double n, rho, theta;
	public double betai, betao;
	
	public DiE(){
		super(0,0,3);
		n=0; rho=0; theta=0;
		betai=0; betao=0;
	}
	
	public void setPara(double r, double l, double nn, double rhoo, double thetaa, double betaii, double betaoo){
		radius=r; length=l;
		n=nn; rho=rhoo; theta=thetaa;
		betai=betaii; betao=betaoo;
	}
	
	void calParticle(Particle p, double dt, int flag){
//flag=0:normal  flag=1:in  flag=2:out
		double ds=p.vs*dt;
		Matrix tm=new Matrix(6,6); Matrix pm=new Matrix(6,1);
		Matrix npm;
		double mx00=0,mx01=0,mx10=0,mx11=0,mz00=0,mz01=0,mz10=0,mz11=0;

		if(flag==0){
			double tC=Math.sqrt(1-n)/rho;
		    mx00=Math.cos(tC*ds);
		    mx01=Math.sin(tC*ds)/tC;
		    mx10=-1*tC*Math.sin(tC*ds);
		    mx11=mx00;

		    tC=Math.sqrt(n)/rho;
		    mz00=Math.cos(tC*ds);
		    mz01=Math.sin(tC*ds)/tC;
		    mz10=-1*tC*Math.sin(tC*ds);
		    mz11=mz00;
		}
		if(flag==1){
		    mx00=1;
		    mx01=0;
		    mx10=Math.tan(betai)/rho;
		    mx11=mx00;

		    mz00=1;
		    mz01=0;
		    mz10=-1*Math.tan(betai)/rho;
		    mz11=mz00;
		}
		if(flag==2){
		    mx00=1;
		    mx01=0;
		    mx10=Math.tan(betao)/rho;
		    mx11=mx00;

		    mz00=1;
		    mz01=0;
		    mz10=-1*Math.tan(betao)/rho;
		    mz11=mz00;
		}


		tm.setValue(0, 0, mx00); tm.setValue(0, 1, mx01); tm.setValue(1, 0, mx10); tm.setValue(1, 1, mx11);
		tm.setValue(2, 2, mz00); tm.setValue(2, 3, mz01); tm.setValue(3, 2, mz10); tm.setValue(3, 3, mz11);
		tm.setValue(4, 4, 1); tm.setValue(4, 5, dt); tm.setValue(5, 4, 0); tm.setValue(5, 5, 1);
		
		pm.setValue(0, 0, p.x); pm.setValue(1, 0, p.xp);
		pm.setValue(2, 0, p.z); pm.setValue(3, 0, p.zp);
		pm.setValue(4, 0, p.s); pm.setValue(5, 0, p.vs);
		
		npm=tm.multiMat(pm);
		p.x=npm.getValue(0, 0);
		p.z=npm.getValue(2, 0);
		p.vs=npm.getValue(5, 0);
		p.s=npm.getValue(4, 0);
		p.vx=npm.getValue(1, 0)*p.vs;
		p.vz=npm.getValue(3, 0)*p.vs;
		if((p.x*p.x + p.z*p.z)>(radius*radius))p.flag=1;
		p.reset();
		
	}
	
	
//////////////////////////////////////////////////
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Particle np=new Particle(1,1,1,2,3,2,3,1);
		double pi=3.14159265358;
		DiE ndie=new DiE();
		ndie.setPara(1,14.137,0.5,9,pi/2,0,0);
		ndie.calParticle(np, 1, 0);
		System.out.printf("%f %f %f %f %f %f\n", np.x, np.z, np.s, np.xp, np.zp, np.vs);
	}

}
