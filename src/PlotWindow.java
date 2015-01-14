import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class PlotPanel extends JPanel{
	String dataPath, plotFName;
	int plotType;
	ArrayList<Particle> pData1;
	ArrayList<Double> lInfo;

	public PlotPanel(String dPath){
		dataPath=dPath;
		plotType=0;
		lInfo=new ArrayList<Double>();
		plotFName="";
	}
	
	public void readLInfo(){
		int i=0;
		String tmpLine;
		try{
			File fInfo=new File(dataPath + "/info.txt");
			BufferedReader in=new BufferedReader(new FileReader(fInfo));
			for(i=0;i<5;i++) in.readLine();
			while((tmpLine=in.readLine())!=null){
				lInfo.add(Double.valueOf(tmpLine));
			}
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
		plotPhase(g);
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
	
	public void plotXTrace(Graphics g){
	}
	
	public void plotZTrace(Graphics g){
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
		npw.pPanel.plotFName=path + "/Step9.txt";
		npw.pPanel.plotType=1;
		npw.repaint();
	}

}
