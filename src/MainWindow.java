import java.awt.Color;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame{
	private JLabel label1;
	private JButton bIonS, bDrift, bQuad, bDi;
	private JButton bDetector;
	private JPanel pLine, pControl;
	private JScrollPane spLine;
	
	public MainWindow(){

		label1=new JLabel("BeamLine");
		label1.setBounds(10, 60, 100, 25);

		Icon iIonS=new ImageIcon(getClass().getResource("images/IonS.gif"));
		bIonS=new JButton("", iIonS);
		bIonS.setBounds(10, 10, 48, 48);

		bDrift=new JButton("");
		bDrift.setBounds(60,10, 48, 48);

		bQuad=new JButton("");
		bQuad.setBounds(110,10, 48, 48);
		
		bDi=new JButton("");
		bDi.setBounds(160,10, 48, 48);
		
		bDetector=new JButton("");
		bDetector.setBounds(210, 10, 48, 48);
		
		pLine=new JPanel();
		pLine.setBackground(Color.WHITE);
		//pLine.setBorder(new LineBorder(Color.RED));
		JScrollPane spLine=new JScrollPane(pLine);
		spLine.setBounds(10, 60, 770, 100);
		                                                   
		
		pControl=new JPanel();
		pControl.setBackground(Color.WHITE);
		pControl.setBorder(new LineBorder(Color.RED));
		pControl.setBounds(10, 370, 770, 200);
	
		this.add(label1);
		this.add(bIonS);
		this.add(bDrift);
		this.add(bQuad);
		this.add(bDi);
		this.add(bDetector);
		//this.add(pLine);
		
		
		this.add(spLine);
		this.add(pControl);

		this.setTitle("Particle Beam Designer");
		this.setSize(800, 600);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

////////////////////////////////////////////////////////
	public static void main(String[] args) {
		MainWindow mw=new MainWindow();

	}

}
