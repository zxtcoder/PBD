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
	private JButton bDetector;
	private JPanel pLine, pControl;
	private JScrollPane spLine, spControl;
	
	private JMenuBar mbar;
	private JMenu[] menus={ new JMenu("File"), new JMenu("Element"), new JMenu("Control"), new JMenu("Help") };
	private JMenuItem[] fileItem={ new JMenuItem("open"), new JMenuItem("save"), new JMenuItem("close")	};
	private JMenuItem[] elementItem={ new JMenuItem("IonSource"), new JMenuItem("Drift"), new JMenuItem("Quad"), new JMenuItem("Diode") };
///////////////////////////////////////////////////////////
	private Machine mac;
	private ArrayList<JButton> eleButton;
	private ActionListener eBListener;
	
//////////////////////////////////////////////////////////////
	private int sNowIndex;
	
/////////////////////////////////////////////////////////////////
	public MainWindow(){
		sNowIndex=0;
		mac=new Machine();
		eleButton=new ArrayList<JButton>();
		
		pLine=new JPanel();
		pLine.setBackground(Color.WHITE);
		pLine.setLayout(null);
		pLine.setPreferredSize(new Dimension(800,100));
		//pLine.setBorder(new LineBorder(Color.RED));
		JScrollPane spLine=new JScrollPane(pLine);
		spLine.setBounds(10, 60, 770, 130);
		spLine.setSize(770,130);

		
		pControl=new JPanel();
		pControl.setBackground(Color.WHITE);
		pControl.setLayout(null);
		//pControl.setBorder(new LineBorder(Color.RED));
		//pControl.setBounds(10, 370, 770, 200);
		spControl=new JScrollPane(pControl);
		spControl.setBounds(10,200,770,300);
	
		
		this.add(spLine);
		this.add(spControl);
		
		this.menuInit();
		this.buttonInit();
		this.listenerInit();

		this.setTitle("Particle Beam Designer");
		this.setSize(800, 600);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	public void listenerInit(){
		eBListener=new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton bTmp=(JButton)(e.getSource());
				int index=eleButton.indexOf(bTmp);
				eleButton.get(sNowIndex).setBackground(Color.BLACK);
				sNowIndex=index;
				eleButton.get(sNowIndex).setBackground(Color.RED);
				drawControlPane();
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
	
	public void buttonInit(){
		Icon iIonS=new ImageIcon(getClass().getResource("images/IonS.gif"));
		bIonS=new JButton("", iIonS);
		bIonS.setBackground(Color.BLUE);
		bIonS.setBounds(10, 5, 48, 48);

		bDrift=new JButton("");
		bDrift.setBounds(60,5, 48, 48);

		bQuad=new JButton("");
		bQuad.setBounds(110,5, 48, 48);
		
		bDi=new JButton("");
		bDi.setBounds(160,5, 48, 48);
		
		bDetector=new JButton("");
		bDetector.setBounds(210, 5, 48, 48);
		
		this.add(bIonS);
		this.add(bDrift);
		this.add(bQuad);
		this.add(bDi);
		this.add(bDetector);
		//this.add(pLine);
	}

	public void drawEButton(){
		int i=0,name=-1;
		Icon iconTmp;
		for(i=0;i<mac.eList.size();i++){
			name=mac.eList.get(i).name;
			iconTmp=new ImageIcon(getClass().getResource(PhyC.eleImg[name]));
			eleButton.add(new JButton("",iconTmp));
			eleButton.get(i).setBounds(10+i*50,10,50,80);
			eleButton.get(i).setBackground(Color.BLACK);
			eleButton.get(i).addActionListener(this.eBListener);
			pLine.add(eleButton.get(i));
		}
		eleButton.get(this.sNowIndex).setBackground(Color.RED);
		pLine.setPreferredSize(new Dimension((mac.eList.size()+2)*50, 130));
	}

	
	public void drawControlPane(){
	}
	
	////////////////////////////////////////////////////////
	public static void main(String[] args) {
		MainWindow mw=new MainWindow();
		for(int i=0;i<10;i++){
		    mw.mac.addQuadE(i, 10, 100, 1, 1);
		}
		mw.drawEButton();
		mw.repaint();

	}

}
