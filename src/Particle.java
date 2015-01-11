//Unit: mass:u  charge:e  energy:eV  x:m  vx:m/s

public class Particle {
	public double mass, charge, energy;
	public double x, z, s;
	public double xp, zp;//xp=dx/ds=vx/vs, zp=dz/ds=vz/vs
	public double vx, vz, vs;
	
	public Particle(double m, double q, double xx, double zz, double ss, double vxx, double vzz, double vss){
		mass=m; charge=q; 
		x=xx; z=zz; s=ss;
		vx=vxx; vz=vzz; vs=vss;
		reset();
	}
	
	public double calEnergy(){
		double m=mass*PhyC.u;
		energy=0.5*m*(vx*vx + vz*vz + vs*vs)/PhyC.eV;
		return energy;
	}
	
	public void calXPZP(){
		xp=vx/vs; zp=vz/vs;
	}
	
	public void reset(){
		calEnergy();
		calXPZP();
	}
	
////////////////////////////////////////////////////////
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
