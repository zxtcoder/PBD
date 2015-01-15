
public class Matrix {
	private double[][] mat;
	public int row,col;

	public Matrix(int r, int c){
		int i,j;
		row=r;col=c;
		mat=new double[r][c];
		for(i=0;i<r;i++)
			for(j=0;j<c;j++)
				mat[i][j]=0;

	}
	
	public double getValue(int i, int j){
		return mat[i][j];
	}
	
	public void setValue(int i, int j, double value){
		mat[i][j]=value;
	}
	
	public double[][] getMat(){
		return mat;
	}
	
	public Matrix multiMat(Matrix m2){
		int r,c,i;
		double sum;
		if(col!=m2.row) return null;
		Matrix m3=new Matrix(row, m2.col);
		for(r=0;r<row;r++){
			for(c=0;c<m2.col;c++){
				sum=0.0;
				for(i=0;i<col;i++){
					sum=sum+mat[r][i]*m2.getValue(i,c);
				}
				m3.setValue(r, c, sum);
			}
		}
		return m3;
	}
	
	public Matrix tMat(){
		Matrix m2=new Matrix(col, row);
		int i=0,j=0;
		for(i=0;i<row;i++)
			for(j=0;j<col;j++)
				m2.setValue(j, i, mat[j][i]);
		return m2;
		
	}
	
///////////////////////////////////////////////////////
	public static void main(String[] args) {
		int i,j;
		Matrix m1=new Matrix(2,2);
		Matrix m2=new Matrix(2,2);
		Matrix m3=new Matrix(2,1);
		Matrix m4;
		Matrix m5;
		m1.setValue(0, 0, 1); m1.setValue(0, 1, 2); m1.setValue(1, 0, 3); m1.setValue(1, 1, 4);
		m2.setValue(0, 0, 2); m2.setValue(0, 1, 3); m2.setValue(1, 0, 4); m2.setValue(1, 1, 5);
		m3.setValue(0, 0, 2); m3.setValue(1, 0, 3); 
		m4=m1.multiMat(m2);
		m5=m1.tMat();

		for(i=0;i<m5.row;i++){
			for(j=0;j<m5.col;j++)
				System.out.printf("%f ", m5.getValue(i, j));
			System.out.printf("\n");
		}

	}

}
