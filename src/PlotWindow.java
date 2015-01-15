import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class PlotPanel extends JPanel{
	public String dataPath, plotFName;
	public int plotType;

	public ArrayList<String> fNameList;
	
	double sumLength;
	public ArrayList<Double> lenList, radList;
	public ArrayList<Integer> eNameList;

	public PlotPanel(String dPath){
		dataPath=dPath;
		plotType=0;
		plotFName="";
		eNameList=new ArrayList<Integer>();
		lenList=new ArrayList<Double>();
		radList=new ArrayList<Double>();

		this.getFileList();
		this.readInfo();
	}
	
	public void getFileList(){
		fNameList=new ArrayList<String>();
		File file=new File(dataPath);
		File[] fList=file.listFiles();
		for(File fTmp : fList){
			if(fTmp.isFile()){
				String name=fTmp.getName();
				if(name.charAt(0)=='S')
				    fNameList.add(name);
			}
		}
		fNameList.sort(null);

	}
	
	public void readInfo(){
		int i=0;
		String tmpLine; String[] itemTmp;
		try{
			File fInfo=new File(dataPath + "/info.txt");
			BufferedReader in=new BufferedReader(new FileReader(fInfo));
			for(i=0;i<4;i++) in.readLine();

			itemTmp=(in.readLine()).split(" ");
			for(i=1;i<itemTmp.length;i++){
				eNameList.add(Integer.valueOf(itemTmp[i]));
			}

			itemTmp=(in.readLine()).split(" ");
			sumLength=0;
			for(i=1;i<itemTmp.length;i++){
				lenList.add(Double.valueOf(itemTmp[i]));
				sumLength+=Double.valueOf(itemTmp[i]);
			}

			itemTmp=(in.readLine()).split(" ");
			for(i=1;i<itemTmp.length;i++) radList.add(Double.valueOf(itemTmp[i]));
			in.close();

		}catch(Exception e){}
	}
	
	public void readPData(String dataName, ArrayList<Particle> pdList){
		String tmpLine;
		String[] tmpData;
		try{
			File fData=new File(dataName);
			BufferedReader in=new BufferedReader(new FileReader(fData));
			in.readLine();
			while((tmpLine=in.readLine())!=null){
				tmpData=tmpLine.split(" ");
				double m=Double.valueOf(tmpData[1]), charge=Double.valueOf(tmpData[2]),
						x=Double.valueOf(tmpData[3]), z=Double.valueOf(tmpData[4]), s=Double.valueOf(tmpData[5]),
						vx=Double.valueOf(tmpData[8]), vz=Double.valueOf(tmpData[9]), vs=Double.valueOf(tmpData[10]);
				Particle tmpP=new Particle(m,charge,x,z,s,vx,vz,vs);
				tmpP.flag=Integer.valueOf(tmpData[0]);
				pdList.add(tmpP);
			}
			in.close();
		}catch(Exception e){}
		
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		switch(plotType){
		case 1:
		case 2:
		case 3: plotPhase(g);break;
		case 4:
		case 5: plotTrace(g);break;
		}
	}
	
	public void plotPhase(Graphics g){
		ArrayList<Particle> pData=new ArrayList<Particle>();
		readPData(plotFName, pData);
		ArrayList<Double> xData=new ArrayList<Double>(), yData=new ArrayList<Double>();

		int i=0;
		double xMax=PhyC.MIN, yMax=PhyC.MIN, xMin=PhyC.MAX, yMin=PhyC.MAX;
		double cCW=0, cCH=0;
		int panelW=this.getWidth(), panelH=this.getHeight()-50;
		String xStr="", yStr=""; 

		for(i=0;i<pData.size();i++){
			switch(plotType){
			case 1:xData.add(pData.get(i).x); yData.add(pData.get(i).xp); xStr="X"; yStr="X'";break;
			case 2:xData.add(pData.get(i).z); yData.add(pData.get(i).zp); xStr="Z"; yStr="Z'";break;
			case 3:xData.add(pData.get(i).x); yData.add(pData.get(i).z); xStr="X"; yStr="Z";break;
			default:return;
			}
		}
		for(i=0;i<xData.size();i++){
			if(xData.get(i)>xMax)xMax=xData.get(i);
			if(xData.get(i)<xMin)xMin=xData.get(i);
			if(yData.get(i)>yMax)yMax=yData.get(i);
			if(yData.get(i)<yMin)yMin=yData.get(i);
		}
		xMax=xMax + (xMax-xMin)*0.1;
		xMin=xMin - (xMax-xMin)*0.1;
		yMax=yMax + (yMax-yMin)*0.1;
		yMin=yMin - (yMax-yMin)*0.1;
		cCW=(int)panelW/(xMax-xMin); cCH=(int)panelH/(yMax-yMin);

		g.drawLine(0, panelH , panelW, panelH);
		g.drawLine(0, (int)((0-yMin)*cCH) , panelW, (int)((0-yMin)*cCH));
		g.drawLine((int)((0-xMin)*cCW), 0 , (int)((0-xMin)*cCW), panelH);
		
		g.setColor(Color.RED);
		g.drawString(xStr +":" + xMin + "~" + xMax + "  " + yStr + ":" + yMin + "~" + yMax, 2, panelH+30);

		g.setColor(Color.BLUE);
		for(i=0;i<xData.size();i++){
			double x=xData.get(i), y=yData.get(i);
			int px=(int)((x-xMin)*cCW);
			int py=(int)((y-yMin)*cCH);
			g.fillRect(px, py, 5, 5);
		}

	}
	
	public void plotTrace(Graphics g){
		ArrayList<Particle> pData0=new ArrayList<Particle>();
		ArrayList<Particle> pData1=new ArrayList<Particle>();

		ArrayList<Double> xData0=new ArrayList<Double>(), yData0=new ArrayList<Double>();
		ArrayList<Double> xData1=new ArrayList<Double>(), yData1=new ArrayList<Double>();
		
		int i=0,j=0;
		double rMax=PhyC.MIN, rMin=PhyC.MAX;
		double cCW=0, cCH=0;
		int panelW=this.getWidth(), panelH=this.getHeight()-50;
		String xStr="", yStr=""; 
		for(i=0;i<radList.size();i++)
			if(radList.get(i)>rMax) rMax=radList.get(i);
		cCW=panelW/(sumLength*1.1); cCH=panelH/(rMax*2*1.1);
		
		g.drawLine(0, panelH/2, panelW, panelH/2);
		g.drawLine(0, panelH, panelW, panelH);
		
		double nowLength=0.0;
		for(i=0;i<eNameList.size();i++){
			if(lenList.get(i)>0){
				int x=(int)(nowLength*cCW); int width=(int)(lenList.get(i)*cCW);
				int y=(int)(panelH/2-radList.get(i)*cCH); int height=(int)(radList.get(i)*2*cCH);
				g.drawRect(x,y,width,height);
				nowLength+=lenList.get(i);
			}
		}
		
		
		this.readPData(dataPath + "/" + fNameList.get(0), pData0);
		for(i=1;i<fNameList.size();i++){
            this.readPData(dataPath + "/" + fNameList.get(i), pData1);
            if(plotType==4){
            	for(j=0;j<pData0.size();j++){
            		xData0.add(pData0.get(j).s); yData0.add(pData0.get(j).x);
            		xData1.add(pData1.get(j).s); yData1.add(pData1.get(j).x);
            	}
            }
            if(plotType==5){
             	for(j=0;j<pData0.size();j++){
            		xData0.add(pData0.get(j).s); yData0.add(pData0.get(j).z);
            		xData1.add(pData1.get(j).s); yData1.add(pData1.get(j).z);
            	}
            }
            for(j=0;j<xData0.size();j++){
            	int x0=(int)(xData0.get(j)*cCW); int x1=(int)(xData1.get(j)*cCH);
            	int y0=(int)(panelH/2+yData0.get(j)*cCH); int y1=(int)(panelH/2+yData1.get(j)*cCH);
            	g.drawLine(x0, y0, x1, y1);
            }
            pData0=pData1;
		}

		
		

	}
	
}

public class PlotWindow extends JFrame{
	private PlotPanel pPanel;
	private JPanel toolBar;


	public PlotWindow(String path){

		pPanel=new PlotPanel(path);
		pPanel.setBackground(Color.WHITE);
		pPanel.setBorder(new LineBorder(Color.BLACK));
		pPanel.setBounds(2, 100, this.getWidth()-8, this.getHeight()-150);
		this.add(pPanel);
		
		toolBar=new JPanel();
		toolBar.setBackground(Color.WHITE);
		toolBar.setBounds(2, 0, this.getWidth()-8, 100);
		toolBar.setBorder(new LineBorder(Color.BLACK));
		this.add(toolBar);
		
		this.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				PlotWindow pw=(PlotWindow)e.getSource();
				pw.pPanel.setBounds(2, 100, pw.getWidth()-8, pw.getHeight()-150);
				pw.toolBar.setBounds(2, 0, pw.getWidth()-8, 100);
				pw.repaint();
			}
		});

		this.setLayout(null);
		this.setTitle("Plot Analyse" + path);
		this.setSize(800, 600);
		this.setVisible(true);
	}


/////////////////////////////////////////////////////////
	public static void main(String[] args) {
		String path="/home/zxt/workspace/PBD/test1";
		PlotWindow npw=new PlotWindow(path);
		npw.pPanel.plotFName=path + "/Step0000000009.txt";
		npw.pPanel.plotType=4;
		npw.repaint();
	}

}
