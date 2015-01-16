import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.BevelBorder;
import java.util.*;

public class MainWindow extends JFrame{
/////////////////////////////////////////////////////
	private JLabel label1;
	private JButton bIonS, bDrift, bQuad, bDi;
	private JButton bDel, bRunControl, bDrawReal, bPlot, bStartRun;
	private JPanel pLine, pControl, toolBar, eleBar, statusBar;
	private JScrollPane spLine, spControl;
	
	private JMenuBar mbar;
	private JMenu[] menus={ new JMenu("File"), new JMenu("Element"), new JMenu("Control"), new JMenu("Help") };
	private JMenuItem[] fileItem={ new JMenuItem("open"), new JMenuItem("save"), new JMenuItem("close")	};
	private JMenuItem[] elementItem={ new JMenuItem("IonSource"), new JMenuItem("Drift"), new JMenuItem("Quad"), new JMenuItem("Diode") };
	private JMenuItem[] ctrlMenuItem={ new JMenuItem("Delete"), new JMenuItem("RunSet"), new JMenuItem("Plot"), new JMenuItem("Run") };
///////////////////////////////////////////////////////////
	private Machine mac;
	private ArrayList<JButton> eleButton;
	private ActionListener eBListener;
	private ActionListener bListener;
	
	private ArrayList<Object> controlItem;
	
//////////////////////////////////////////////////////////////
	private int sNowIndex;
	
/////////////////////////////////////////////////////////////////
	public MainWindow(){
		sNowIndex=-1;
		mac=new Machine();
		eleButton=new ArrayList<JButton>();
		controlItem=new ArrayList<Object>();
		
		pLine=new JPanel();
		pLine.setBackground(Color.WHITE);
		pLine.setLayout(null);
		pLine.setPreferredSize(new Dimension(800,100));
		//pLine.setBorder(new LineBorder(Color.RED));
		JScrollPane spLine=new JScrollPane(pLine);
		spLine.setBounds(10, 80, 770, 130);
		spLine.setSize(770,130);

		
		pControl=new JPanel();
		pControl.setBackground(Color.WHITE);
		pControl.setLayout(null);
		//pControl.setBorder(new LineBorder(Color.RED));
		//pControl.setBounds(10, 370, 770, 200);
		spControl=new JScrollPane(pControl);
		spControl.setBounds(10,250,770,340);
	
		
		this.add(spLine);
		this.add(spControl);
		
		this.menuInit();
		this.listenerInit();
		this.toolBarInit();
		this.eleBarInit();
		this.statusBarInit();

		this.setTitle("Particle Beam Designer by zxt@pku.edu.cn");
		this.setSize(800, 650);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
	public void listenerInit(){
		eBListener=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton bTmp=(JButton)(e.getSource());
				int index=eleButton.indexOf(bTmp);
				eleButton.get(sNowIndex).setBackground(Color.ORANGE);
				sNowIndex=index;
				eleButton.get(sNowIndex).setBackground(Color.RED);
				drawControlPane("element");
			}
		};
		bListener=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton bTmp=(JButton)(e.getSource());
				if(bTmp==bIonS){
					mac.addIonSEType2(sNowIndex+1, mac.pList, 0,0,0,0,0,0,0,0,0,0,0,0,0,0);
					drawEButton();
				}
				else if(bTmp==bDrift){
					mac.addDriftE(sNowIndex+1, 0, 0);
					drawEButton();
				}
				else if(bTmp==bQuad){
					mac.addQuadE(sNowIndex+1, 0, 0, 0, 0);
					drawEButton();
				}
				else if(bTmp==bDi){
					mac.addDiE(sNowIndex+1, 0, 0, 0, 0, 0, 0, 0);
					drawEButton();
				}
				else if(bTmp==bDel){
					if(sNowIndex>=0 && sNowIndex<mac.eList.size()){
					    mac.eList.remove(sNowIndex);
					    drawEButton();
					}
				}
				else if(bTmp==bRunControl){
					drawControlPane("runcontrol");
				}
				else if(bTmp==bStartRun){
					mac.run();
					JOptionPane.showMessageDialog(null, ""+mac.pList.size()+" atoms simulation is done!");
					mac.clearParticle();
				}
				else if(bTmp==bDrawReal){
					
				}
				else if(bTmp==bPlot){
					PlotWindow pw=new PlotWindow(mac.logName);
				}
				else{}
				
			}
		};
	}
	
	
	public void menuInit(){

		int i=0;
		mbar=new JMenuBar();
		for(i=0;i<fileItem.length;i++){
		    menus[0].add(fileItem[i]);
		}
		for(i=0;i<elementItem.length;i++){
		    menus[1].add(elementItem[i]);
		}
		for(JMenu jm : menus){
			mbar.add(jm);
		}
		this.setJMenuBar(mbar);
	}
	
	
	public void eleBarInit(){
		eleBar=new JPanel();
		eleBar.setLayout(null);
		eleBar.setBounds(10, 5, 770, 80);

		Icon iIonS=new ImageIcon(getClass().getResource(PhyC.eleImg[0]));
		bIonS=new JButton("", iIonS);
		bIonS.setBounds(0, 0, 48, 70);
		bIonS.addActionListener(bListener);

		iIonS=new ImageIcon(getClass().getResource(PhyC.eleImg[1]));
		bDrift=new JButton("", iIonS);
		bDrift.setBounds(50,0, 48, 70);
		bDrift.addActionListener(bListener);

		iIonS=new ImageIcon(getClass().getResource(PhyC.eleImg[2]));
		bQuad=new JButton("", iIonS);
		bQuad.setBounds(100, 0, 48, 70);
		bQuad.addActionListener(bListener);
		
		iIonS=new ImageIcon(getClass().getResource(PhyC.eleImg[3]));
		bDi=new JButton("", iIonS);
		bDi.setBounds(150, 0, 48, 70);
		bDi.addActionListener(bListener);

		this.add(eleBar);
		eleBar.add(bIonS);
		eleBar.add(bDrift);
		eleBar.add(bQuad);
		eleBar.add(bDi);
	}

		
	public void toolBarInit(){
		toolBar=new JPanel();
		toolBar.setLayout(null);
		toolBar.setBounds(10, 215, 770, 40);

    	Icon iIonS=new ImageIcon(getClass().getResource(PhyC.bImg[0]));
		bDel=new JButton("", iIonS);
		bDel.setBounds(0, 0, 32, 32);
		bDel.addActionListener(bListener);

    	iIonS=new ImageIcon(getClass().getResource(PhyC.bImg[1]));
		bDrawReal=new JButton("", iIonS);
		bDrawReal.setBounds(34, 0, 32, 32);
		bDrawReal.addActionListener(bListener);

    	iIonS=new ImageIcon(getClass().getResource(PhyC.bImg[2]));
		bRunControl=new JButton("", iIonS);
		bRunControl.setBounds(68, 0, 32, 32);
		bRunControl.addActionListener(bListener);

    	iIonS=new ImageIcon(getClass().getResource(PhyC.bImg[3]));
		bPlot=new JButton("", iIonS);
		bPlot.setBounds(102, 0, 32, 32);
		bPlot.addActionListener(bListener);

    	iIonS=new ImageIcon(getClass().getResource(PhyC.bImg[5]));
		bStartRun=new JButton("", iIonS);
		bStartRun.setBounds(730, 0, 32, 32);
		bStartRun.setBackground(Color.RED);
		bStartRun.addActionListener(bListener);


		this.add(toolBar);
		toolBar.add(bDel);
		toolBar.add(bDrawReal);
		toolBar.add(bRunControl);
		toolBar.add(bPlot);
		toolBar.add(bStartRun);
		//this.add(pLine);
	}

	public void statusBarInit(){
		statusBar=new JPanel();
		statusBar.setLayout(null);
		statusBar.setBounds(10, 600, 770, 40);
	}
	
	public void drawEButton(){
		int i=0,name=-1;
		Icon iconTmp;
		clearEButton();
		this.repaint();

		for(i=0;i<mac.eList.size();i++){
			name=mac.eList.get(i).name;
			iconTmp=new ImageIcon(getClass().getResource(PhyC.eleImg[name]));
			eleButton.add(new JButton("",iconTmp));
			eleButton.get(i).setBounds(10+i*50,10,50,80);
			eleButton.get(i).setBackground(Color.ORANGE);
			eleButton.get(i).addActionListener(this.eBListener);
			pLine.add(eleButton.get(i));
		}
	    pLine.setPreferredSize(new Dimension((mac.eList.size()+2)*50, 130));

        while(sNowIndex>=mac.eList.size()){
	    	sNowIndex--;
	    }
        if(sNowIndex<0 && mac.eList.size()>0){
        	sNowIndex=0;
        }
	    if(sNowIndex>=0 && sNowIndex<mac.eList.size()){
		     eleButton.get(this.sNowIndex).setBackground(Color.RED);
	    }
	    
		this.repaint();

	}


	public void clearEButton(){
		int i=0;
		for(i=0;i<this.eleButton.size();i++){
			this.pLine.remove(eleButton.get(i));
		}
		eleButton.clear();
		
	}
	
	
	public void drawControlPane(String type){
		int i=0;
		for(i=0;i<controlItem.size();i++)
			pControl.remove((Component)controlItem.get(i));
		controlItem.clear();
		if(type=="element"){
		    int eleName=mac.eList.get(sNowIndex).name;
		    if(eleName==0){//IonS
			    ionsControl();
		    }
	    	else if(eleName==1){//Drift
		    	driftControl();
	    	}
		    else if(eleName==2){//Quad
			    quadControl();
		    }
		    else if(eleName==3){//Di
			    diControl();
		    }
		    else{}
		}
		else if(type=="runcontrol") runControl();
	}
	
	public void runControl(){
		Icon iconTmp=new ImageIcon(getClass().getResource(PhyC.bImg[4]));
		JButton bSave=new JButton("",iconTmp); bSave.setBounds(5, 5, 32, 32);
		JLabel lRunTime=new JLabel("T(s)"); lRunTime.setBounds(5,40,100,25);
		JLabel ldt=new JLabel("dt(s)"); ldt.setBounds(5,70,100,25);
		JLabel lLogStep=new JLabel("LogStep"); lLogStep.setBounds(5,100,100,25);
		JLabel lLogName=new JLabel("LogName"); lLogName.setBounds(5,130,100,25);

		JTextField tRunTime=new JTextField(); tRunTime.setBounds(110,40,300,25);
    	JTextField tdt=new JTextField(); tdt.setBounds(110,70,300,25);
    	JTextField tLogStep=new JTextField(); tLogStep.setBounds(110,100,300,25);
    	JTextField tLogName=new JTextField(); tLogName.setBounds(110,130,300,25);
		
		
		controlItem.add(bSave); pControl.add(bSave);
		controlItem.add(lRunTime);pControl.add(lRunTime); controlItem.add(tRunTime);pControl.add(tRunTime);
		controlItem.add(ldt);pControl.add(ldt); controlItem.add(tdt);pControl.add(tdt);
		controlItem.add(tLogStep);pControl.add(tLogStep); controlItem.add(lLogStep);pControl.add(lLogStep);
		controlItem.add(tLogName);pControl.add(tLogName); controlItem.add(lLogName);pControl.add(lLogName);
		
		tRunTime.setText(Double.toString(mac.sumTime));
		tdt.setText(Double.toString(mac.dt));
		tLogStep.setText(Integer.toString(mac.logStep));
		tLogName.setText(mac.logName);
		
		bSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mac.sumTime=Double.valueOf(tRunTime.getText());
				mac.dt=Double.valueOf(tdt.getText());
				mac.logStep=Integer.valueOf(tLogStep.getText());
				mac.logName=tLogName.getText();
			}
		});
		
		this.repaint();
	}
	
	
	public void driftControl(){
		Icon iconTmp=new ImageIcon(getClass().getResource(PhyC.bImg[4]));
		JButton bSave=new JButton("",iconTmp);
		bSave.setBounds(5, 5, 32, 32);
		JLabel lR=new JLabel("R(m)");
		lR.setBounds(5,40,50,25);
		JLabel lL=new JLabel("L(m)");
		lL.setBounds(5,70,50,25);

		JTextField tR=new JTextField();
		tR.setBounds(60,40,300,25);

    	JTextField tL=new JTextField();
		tL.setBounds(60,70,300,25);
		
		
		controlItem.add(bSave); pControl.add(bSave);
		controlItem.add(lR);pControl.add(lR);
		controlItem.add(lL);pControl.add(lL);
		controlItem.add(tR);pControl.add(tR);
		controlItem.add(tL);pControl.add(tL);
		
		String rTmp=Double.toString(((DriftE)mac.eList.get(sNowIndex)).radius);
		String lTmp=Double.toString(((DriftE)mac.eList.get(sNowIndex)).length);
		tR.setText(rTmp);
		tL.setText(lTmp);
		
		bSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				double dR=Double.valueOf(tR.getText());
				double dL=Double.valueOf(tL.getText());
				((DriftE)mac.eList.get(sNowIndex)).setPara(dR, dL);
				mac.calLength();
			}
		});
		
		this.repaint();
	}
	
	
	public void ionsControl(){
		Icon iconTmp=new ImageIcon(getClass().getResource(PhyC.bImg[4]));
		JButton bSave=new JButton("",iconTmp); bSave.setBounds(5, 5, 32, 32);

		JLabel lR=new JLabel("R(m)"); lR.setBounds(5,40,50,25);
		JLabel lL=new JLabel("L(m)"); lL.setBounds(5,70,50,25);
		JLabel lp=new JLabel("pos(m)"); lp.setBounds(5,100,50,25);
		JLabel ldurT=new JLabel("ΔT(s)"); ldurT.setBounds(5,130,50,25);
		JLabel lbeamInt=new JLabel("flux/s"); lbeamInt.setBounds(5,160,50,25);
		JLabel lmass=new JLabel("m(u)"); lmass.setBounds(5,190,50,25);
		JLabel lcharge=new JLabel("q(e)"); lcharge.setBounds(5,220,50,25);
		JLabel lenergy=new JLabel("E(eV)"); lenergy.setBounds(5,250,50,25);

		JLabel lxA=new JLabel("Δx(m)"); lxA.setBounds(360,40,50,25);
		JLabel lxpA=new JLabel("Δxp"); lxpA.setBounds(360,70,50,25);
		JLabel lzA=new JLabel("Δz(m)"); lzA.setBounds(360,100,50,25);
		JLabel lzpA=new JLabel("Δzp"); lzpA.setBounds(360,130,50,25);
		JLabel lsA=new JLabel("Δs(m)"); lsA.setBounds(360,160,50,25);
		JLabel lspA=new JLabel("Δsp"); lspA.setBounds(360,190,50,25);

		JTextField tR=new JTextField(); tR.setBounds(60,40,250,25);
    	JTextField tL=new JTextField(); tL.setBounds(60,70,250,25);
    	JTextField tp=new JTextField(); tp.setBounds(60,100,250,25);
		JTextField tdurT=new JTextField(); tdurT.setBounds(60,130,250,25);
		JTextField tbeamInt=new JTextField(); tbeamInt.setBounds(60,160,250,25);
		JTextField tmass=new JTextField(); tmass.setBounds(60,190,250,25);
		JTextField tcharge=new JTextField(); tcharge.setBounds(60,220,250,25);
		JTextField tenergy=new JTextField(); tenergy.setBounds(60,250,250,25);

		JTextField txA=new JTextField(); txA.setBounds(410,40,250,25);
		JTextField txpA=new JTextField(); txpA.setBounds(410,70,250,25);
		JTextField tzA=new JTextField(); tzA.setBounds(410,100,250,25);
		JTextField tzpA=new JTextField(); tzpA.setBounds(410,130,250,25);
		JTextField tsA=new JTextField(); tsA.setBounds(410,160,250,25);
		JTextField tspA=new JTextField(); tspA.setBounds(410,190,250,25);	
		
		controlItem.add(bSave); pControl.add(bSave);
		controlItem.add(lR);pControl.add(lR); controlItem.add(tR);pControl.add(tR);
		controlItem.add(lL);pControl.add(lL); controlItem.add(tL);pControl.add(tL);
		controlItem.add(lp);pControl.add(lp); controlItem.add(tp);pControl.add(tp);
		controlItem.add(ldurT);pControl.add(ldurT); controlItem.add(tdurT);pControl.add(tdurT);
		controlItem.add(lbeamInt);pControl.add(lbeamInt); controlItem.add(tbeamInt);pControl.add(tbeamInt);
		controlItem.add(lmass);pControl.add(lmass); controlItem.add(tmass);pControl.add(tmass);
		controlItem.add(lcharge);pControl.add(lcharge); controlItem.add(tcharge);pControl.add(tcharge);
		controlItem.add(lenergy);pControl.add(lenergy); controlItem.add(tenergy);pControl.add(tenergy);

		controlItem.add(lxA);pControl.add(lxA); controlItem.add(txA);pControl.add(txA);
		controlItem.add(lzA);pControl.add(lzA); controlItem.add(tzA);pControl.add(tzA);
		controlItem.add(lsA);pControl.add(lsA); controlItem.add(tsA);pControl.add(tsA);
		controlItem.add(lxpA);pControl.add(lxpA); controlItem.add(txpA);pControl.add(txpA);
		controlItem.add(lzpA);pControl.add(lzpA); controlItem.add(tzpA);pControl.add(tzpA);
		controlItem.add(lspA);pControl.add(lspA); controlItem.add(tspA);pControl.add(tspA);
		
		String rTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).radius);
		String lTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).length);
		String pTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).position);
		String durTTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).durT);
		String beamIntTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).beamInt);
		String massTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).pMass);
		String chargeTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).pCharge);
		String energyTmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).pEnergy);
		String xATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).xA);
		String xpATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).xpA);
		String zATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).zA);
		String zpATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).zpA);
		String sATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).sA);
		String spATmp=Double.toString(((IonSE)mac.eList.get(sNowIndex)).spA);

		tR.setText(rTmp); tL.setText(lTmp); tp.setText(pTmp); tdurT.setText(durTTmp);
		tbeamInt.setText(beamIntTmp); tmass.setText(massTmp); tcharge.setText(chargeTmp); tenergy.setText(energyTmp);
		txA.setText(xATmp); txpA.setText(xpATmp); tzA.setText(zATmp); tzpA.setText(zpATmp); tsA.setText(sATmp); tspA.setText(spATmp); 

		bSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				double dR=Double.valueOf(tR.getText()); double dL=Double.valueOf(tL.getText());
				double dp=Double.valueOf(tp.getText()); double ddurT=Double.valueOf(tdurT.getText());
				double dbeamInt=Double.valueOf(tbeamInt.getText()); double dmass=Double.valueOf(tmass.getText());
				double dcharge=Double.valueOf(tcharge.getText()); double denergy=Double.valueOf(tenergy.getText());
				double dxA=Double.valueOf(txA.getText()); double dxpA=Double.valueOf(txpA.getText());
				double dzA=Double.valueOf(tzA.getText()); double dzpA=Double.valueOf(tzpA.getText());
				double dsA=Double.valueOf(tsA.getText()); double dspA=Double.valueOf(tspA.getText());
				((IonSE)mac.eList.get(sNowIndex)).setPara2(mac.pList, dR, dL, dp, ddurT, dbeamInt, 
						dmass, dcharge, denergy, dxA, dxpA, dzA, dzpA, dsA, dspA);
				mac.calLength();
			}
		});
		
		this.repaint();
	}
	
	
	public void diControl(){
		Icon iconTmp=new ImageIcon(getClass().getResource(PhyC.bImg[4]));
		JButton bSave=new JButton("",iconTmp);
		bSave.setBounds(5, 5, 32, 32);
		JLabel lR=new JLabel("R(m)"); lR.setBounds(5,40,50,25);
		JLabel lL=new JLabel("L(m)"); lL.setBounds(5,70,50,25);
		JLabel ln=new JLabel("n(0-1)"); ln.setBounds(5,100,50,25);
		JLabel lrho=new JLabel("ρ"); lrho.setBounds(5,130,50,25);
		JLabel ltheta=new JLabel("θ"); ltheta.setBounds(5,160,50,25);
		JLabel lbetai=new JLabel("βi"); lbetai.setBounds(5,190,50,25);
		JLabel lbetao=new JLabel("βo"); lbetao.setBounds(5,220,50,25);

		JTextField tR=new JTextField(); tR.setBounds(60,40,300,25);
    	JTextField tL=new JTextField(); tL.setBounds(60,70,300,25);
    	JTextField tn=new JTextField(); tn.setBounds(60,100,300,25);
    	JTextField trho=new JTextField(); trho.setBounds(60,130,300,25);
    	JTextField ttheta=new JTextField(); ttheta.setBounds(60,160,300,25);
    	JTextField tbetai=new JTextField(); tbetai.setBounds(60,190,300,25);
    	JTextField tbetao=new JTextField(); tbetao.setBounds(60,220,300,25);
		
		
		controlItem.add(bSave); pControl.add(bSave);
		controlItem.add(lR);pControl.add(lR);
		controlItem.add(lL);pControl.add(lL);
		controlItem.add(ln);pControl.add(ln);
		controlItem.add(lrho);pControl.add(lrho);
		controlItem.add(ltheta);pControl.add(ltheta);
		controlItem.add(lbetai);pControl.add(lbetai);
		controlItem.add(lbetao);pControl.add(lbetao);
		controlItem.add(tR);pControl.add(tR);
		controlItem.add(tL);pControl.add(tL);
		controlItem.add(tn);pControl.add(tn);
		controlItem.add(trho);pControl.add(trho);
		controlItem.add(ttheta);pControl.add(ttheta);
		controlItem.add(tbetai);pControl.add(tbetai);
		controlItem.add(tbetao);pControl.add(tbetao);
		
		String rTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).radius);
		String lTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).length);
		String nTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).n);
		String rhoTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).rho);
		String thetaTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).theta);
		String betaiTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).betai);
		String betaoTmp=Double.toString(((DiE)mac.eList.get(sNowIndex)).betao);
		tR.setText(rTmp); tL.setText(lTmp); tn.setText(nTmp);
		trho.setText(rhoTmp); ttheta.setText(thetaTmp); tbetai.setText(betaiTmp); tbetao.setText(betaoTmp);
		
		bSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				double dR=Double.valueOf(tR.getText()); double dL=Double.valueOf(tL.getText());
				double dn=Double.valueOf(tn.getText()); double drho=Double.valueOf(trho.getText());
				double dtheta=Double.valueOf(ttheta.getText()); double dbetai=Double.valueOf(tbetai.getText());
				double dbetao=Double.valueOf(tbetao.getText());
				((DiE)mac.eList.get(sNowIndex)).setPara(dR, dL, dn, drho, dtheta, dbetai, dbetao);
				mac.calLength();
			}
		});
		
		this.repaint();
	}
	
	public void quadControl(){
		Icon iconTmp=new ImageIcon(getClass().getResource(PhyC.bImg[4]));
		JButton bSave=new JButton("",iconTmp);
		bSave.setBounds(5, 5, 32, 32);
		JLabel lR=new JLabel("R(m)");
		lR.setBounds(5,40,50,25);
		JLabel lL=new JLabel("L(m)");
		lL.setBounds(5,70,50,25);
		JLabel lKx=new JLabel("kx(T/m)");
		lKx.setBounds(5,100,50,25);
		JLabel lKz=new JLabel("kz(T/m)");
		lKz.setBounds(5,130,50,25);


		JTextField tR=new JTextField();
		tR.setBounds(60,40,300,25);

    	JTextField tL=new JTextField();
		tL.setBounds(60,70,300,25);

		JTextField tKx=new JTextField();
		tKx.setBounds(60,100,300,25);
		
		JTextField tKz=new JTextField();
		tKz.setBounds(60,130,300,25);
	
		
		controlItem.add(bSave); pControl.add(bSave);
		controlItem.add(lR);pControl.add(lR);
		controlItem.add(lL);pControl.add(lL);
		controlItem.add(tR);pControl.add(tR);
		controlItem.add(tL);pControl.add(tL);
		controlItem.add(lKx);pControl.add(lKx);
		controlItem.add(lKz);pControl.add(lKz);
		controlItem.add(tKx);pControl.add(tKx);
		controlItem.add(tKz);pControl.add(tKz);
		
		String rTmp=Double.toString(((QuadE)mac.eList.get(sNowIndex)).radius);
		String lTmp=Double.toString(((QuadE)mac.eList.get(sNowIndex)).length);
		String kxTmp=Double.toString(((QuadE)mac.eList.get(sNowIndex)).kx);
		String kzTmp=Double.toString(((QuadE)mac.eList.get(sNowIndex)).kz);
		tR.setText(rTmp);
		tL.setText(lTmp);
		tKx.setText(kxTmp);
		tKz.setText(kzTmp);
		
		bSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				double dR=Double.valueOf(tR.getText());
				double dL=Double.valueOf(tL.getText());
				double dKx=Double.valueOf(tKx.getText());
				double dKz=Double.valueOf(tKz.getText());
				((QuadE)mac.eList.get(sNowIndex)).setPara(dR, dL, dKx, dKz);
				mac.calLength();
			}
		});
		
		this.repaint();
	}
	////////////////////////////////////////////////////////
	public static void main(String[] args) {
		MainWindow mw=new MainWindow();

	}

}
