import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
import java.io.IOException;

public class Transforms2D extends JPanel{

	private class Display extends JPanel{
		
		protected void paintComponent(Graphics g){
		
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.translate(300,300);  // Moves (0,0) to the center of the display.
			
			int whichTransform = transformSelect.getSelectedIndex();
			
			Polygon myPolygon = new Polygon();
			
			int r = 150;	//radius
			int n = 13;		//how many angles
			double posX, posY;
			
			for(int i=1; i<=n; i++){
				
				posX = r * Math.cos((2 * i * Math.PI)/n);
				posY = r * Math.sin((2 * i * Math.PI)/n);	

				myPolygon.addPoint((int)posX, (int)posY);
			}
			
			// TODO Apply transforms here, depending on the value of whichTransform!
			switch(whichTransform) {
			case 0:
				//nothing
				break;
			case 1:		//scale down
				g2.scale(.5, .5);
				break;
			
			case 2:		//scale and rotate45 right
				g2.rotate(Math.toRadians(45));
				g2.scale(1.4, 1.4);
				break;
				
			case 3:		//upside down and reflect horizontally
				g2.scale(-0.3, -1);
				break;
				
			case 4:
				g2.shear(0.5, 0);
				break;
				
			case 5: 
				g2.scale(1, 0.3);
				g2.translate(0, -600);
				break;
				
			case 6:
				g2.shear(0.5, 0);
				g2.rotate(Math.toRadians(90));
				break;
				
			case 7:
				g2.scale(0.5, 1);
				g2.rotate(Math.toDegrees(2*Math.PI));
				break;
				
			case 8:
				g2.scale(1, 0.5);
				g2.translate(0, 350);
				g2.rotate(Math.toRadians(35));
				break;
				
			case 9:
				g2.shear(0.3, 0);
				g2.rotate(Math.toRadians(180));
				g2.translate(-130, -120);
				break;
			}
			
			g2.fill(myPolygon);
			g2.draw(myPolygon);
		}
	}

	private Display display;
	private JComboBox<String> transformSelect;

	public Transforms2D() throws IOException {
		
		display = new Display();
		display.setBackground(Color.GREEN);
		display.setPreferredSize(new Dimension(600,600));
		transformSelect = new JComboBox<String>();
		transformSelect.addItem("None");
	
		for (int i = 1; i < 10; i++) {
			transformSelect.addItem("No. " + i);
		}
		
		transformSelect.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				display.repaint();
			}
		});
		
		setLayout(new BorderLayout(3,3));
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createLineBorder(Color.GRAY,10));
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout(FlowLayout.CENTER));
		top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		top.add(new JLabel("Transform: "));
		top.add(transformSelect);
		add(display,BorderLayout.CENTER);
		add(top,BorderLayout.NORTH);
	}

	public static void main(String[] args) throws IOException {
	
		JFrame window = new JFrame("Zadanie 1");
		window.setContentPane(new Transforms2D());
		window.pack();
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation( (screen.width - window.getWidth())/2, (screen.height - window.getHeight())/2 );
		window.setVisible(true);
	}
}
	