
public class DriftE extends Element{
	
	public DriftE(){
		super(0,0,1);
	}
	
	public void setPara(double r, double l){
		radius=r; length=l;
	}
	
	public Matrix calSigma(Matrix ms0, double ds){
		Matrix tm=new Matrix(4,4), ttm;
		Matrix ms=new Matrix(4,4);
		tm.setValue(0, 0, 1); tm.setValue(0, 1, ds); tm.setValue(1, 0, 0); tm.setValue(1, 1, 1);
		tm.setValue(2, 2, 1); tm.setValue(2, 3, ds); tm.setValue(3, 2, 0); tm.setValue(3, 3, 1);
		ttm=tm.tMat();
		ms=tm.multiMat(ms0.multiMat(ttm));
		return ms;
	}

	void calParticle(Particle p, double dt){
		double ds=p.vs*dt;
		Matrix tm=new Matrix(6,6); Matrix pm=new Matrix(6,1);
		Matrix npm;
		tm.setValue(0, 0, 1); tm.setValue(0, 1, ds); tm.setValue(1, 0, 0); tm.setValue(1, 1, 1);
		tm.setValue(2, 2, 1); tm.setValue(2, 3, ds); tm.setValue(3, 2, 0); tm.setValue(3, 3, 1);
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
	
//////////////////////////////////////////////////////
	public static void main(String[] args) {
		Matrix ms0=new Matrix(4,4);
		ms0.setValue(0, 0, 1); ms0.setValue(1, 1, 1); ms0.setValue(2, 2, 1); ms0.setValue(3, 3, 1);
		DriftE de=new DriftE();
		Matrix ms=de.calSigma(ms0, 1);
		System.out.println("" + ms0.getValue(0, 0) + " " + ms0.getValue(1, 1));



	}

}
