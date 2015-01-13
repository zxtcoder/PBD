import java.lang.Math.*;

public class QuadE extends Element{
	
	public double kx, kz;//grad of the magnet
	
	public QuadE(double r, double l, double kxx, double kzz){
		super(r,l,2);
		kx=kxx; kz=kzz;
	}

	void calParticle(Particle p, double dt){
		double ds=p.vs*dt;
		Matrix tm=new Matrix(6,6); Matrix pm=new Matrix(6,1);
		Matrix npm;
		double Kx=p.charge*PhyC.e*kx/p.mass/PhyC.u/p.vs;
		double Kz=p.charge*PhyC.e*kz/p.mass/PhyC.u/p.vs;
		double Kxs=0,Kzs=0,mx00=0,mx01=0,mx10=0,mx11=0,mz00=0,mz01=0,mz10=0,mz11=0;
		
		if(Kx>0 && Kz>0){

		    Kxs=Math.sqrt(Kx);
		    Kzs=Math.sqrt(Kz);
		
		    mx00=Math.cos(Kxs*ds);
		    mx01=1/Kxs*Math.sin(Kxs*ds);
		    mx10=-1*Kxs*Math.sin(Kxs*ds);
		    mx11=mx00;

		    mz00=Math.cosh(Kzs*ds);
		    mz01=1/Kzs*Math.sinh(Kzs*ds);
		    mz10=Kzs*Math.sinh(Kzs*ds);
		    mz11=mz00;
		}
		
		if(Kx<0 && Kz<0){

		    Kxs=Math.sqrt(-1*Kx);
		    Kzs=Math.sqrt(-1*Kz);
		
		    mz00=Math.cos(Kzs*ds);
		    mz01=1/Kzs*Math.sin(Kzs*ds);
		    mz10=-1*Kzs*Math.sin(Kzs*ds);
		    mz11=mz00;

		    mx00=Math.cosh(Kxs*ds);
		    mx01=1/Kxs*Math.sinh(Kxs*ds);
		    mx10=Kxs*Math.sinh(Kxs*ds);
		    mx11=mx00;
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
	
////////////////////////////////////////////////////
	public static void main(String[] args) {
		Particle np=new Particle(1,1,1,2,3,2,3,1);
		QuadE nqe=new QuadE(10,10,1e-12,1e-12);
		nqe.calParticle(np, 1);
		System.out.printf("%f %f %f %f %f %f\n", np.x, np.z, np.s, np.xp, np.zp, np.vs);
		
	}
}
