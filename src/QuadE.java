import java.lang.Math.*;

public class QuadE extends Element{
	
	private double kx, kz;//grad of the magnet
	
	public QuadE(double r, double l, double kxx, double kzz){
		super(r,l);
		kx=kxx; kz=kzz;
	}

	void calParticle(Particle p, double dt){
		double ds=p.vs*dt;
		Matrix tm=new Matrix(6,6); Matrix pm=new Matrix(6,1);
		Matrix npm;
		double Kx=p.charge*PhyC.e*kx/p.mass/PhyC.u/p.vs;
		double Kxs=Math.sqrt(Kx);
		double Kz=p.charge*PhyC.e*kz/p.mass/PhyC.u/p.vs;
		double Kzs=Math.sqrt(Kz);
		
		double mx00=Math.cos(Kxs*ds);
		double mx01=1/Kxs*Math.sin(Kxs*ds);
		double mx10=-1*Kxs*Math.sin(Kxs*ds);
		double mx11=mx00;

		double mz00=Math.cosh(Kzs*ds);
		double mz01=1/Kzs*Math.sinh(Kzs*ds);
		double mz10=Kzs*Math.sinh(Kzs*ds);
		double mz11=mz00;

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
		p.reset();
		
	}
	
	public static void main(String[] args) {
		
	}
}
