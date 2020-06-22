package gk_lab7;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO; 


public class Lab7 extends JPanel implements GLEventListener {

	public static void main(String[] args) {
		JFrame window = new JFrame("Painting and Texturing");
		Lab7 panel = new Lab7();
		window.setContentPane(panel);
		window.setJMenuBar( panel.getMenuBar() );
		window.pack();
		window.setResizable(false);
		window.setLocation(50,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	private PaintPanel paintPanel; 
	private GLJPanel displayGL; 
	private GLUT glut = new GLUT();
	
	
	private Camera camera;	
	private int currentObjectNum = 0;
	
	private int textureRepeatHorizontal = 1;  // Horizontal scale factor for texture transform
	private int textureRepeatVertical = 1;    // Vertical scale factor for texture transform
	
	private Texture currentTexture = null;

	public Lab7() {
		paintPanel = new PaintPanel();
		displayGL = new GLJPanel( new GLCapabilities(null) );
		paintPanel.setPreferredSize( new Dimension(512,512) );
		displayGL.setPreferredSize( new Dimension(512,512) );
		this.setLayout( new GridLayout(1,2,4,4) );
		this.add(paintPanel);
		this.add(displayGL);
		this.setBackground(Color.BLACK);
		this.setBorder( BorderFactory.createLineBorder(Color.BLACK, 3) );
		displayGL.addGLEventListener(this); // This panel will respond to OpenGL events.
		camera = new Camera();
		camera.lookAt( 1,1,4, 0,0,0, 0,1,0 );
		camera.setLimits(-0.8, 0.8, -0.8, 0.8, -2, 2);
		camera.installTrackball(displayGL);
	}

	//-------------------------- Draw a shape -------------------------------------
	
	/**
	 * Draws the currently selected 3D object.
	 */
	private void drawCurrentShape(GL2 gl2) {
		
		Pyramid(gl2, 13, true);
	}
		
	private void Pyramid(GL2 gl2, int N, boolean makeTexCoords) {
		
		float radius = 0.8f;
		float X, Y, Z;
		Y = 0;
		float h = 0.7f;
		
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);
		gl2.glColor3f(0,1,0);
		gl2.glVertex3f(0,0,Y);
		gl2.glVertex3f(0, 0, Y);
		for(int i=0; i<=2*N; i++) {
			
			Z = radius * (float)Math.cos(2 * Math.PI * i/N);
			X = radius * (float)Math.sin(2 * Math.PI * i/N);
			gl2.glTexCoord3d(Z,X,Y);
			gl2.glVertex3f(X, Y, Z);
			
			if(i%2 == 0) 
				gl2.glVertex3f(0,0,0);
				gl2.glTexCoord3d(X,0,Z);
		}
		gl2.glEnd();	
		
		Y+=h;
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);
		gl2.glColor3f(0, 0.7f, 0);
		
		gl2.glVertex3f(0,Y-h,0);
		gl2.glTexCoord3d(0, Y-h, 0);
		
		for(int i=0; i<=2*N; i++) {
			
			Z = radius * (float)Math.cos(2 * Math.PI * i/N);
			X = radius * (float)Math.sin(2 * Math.PI * i/N);
			
			gl2.glTexCoord3d(X,Y,Z);
			gl2.glVertex3f(X, Y-h, Z);
			
			if(i%2 == 0) 
				gl2.glVertex3f(0, Y, 0);
				gl2.glTexCoord3d(X,Y,Z);
		}
		gl2.glEnd();
	}

	// TODO
	private Texture textureFromResource(String resourceName) throws IOException {
		
		URL path = this.getClass().getClassLoader().getResource(resourceName);
		BufferedImage bfImage = ImageIO.read(Objects.requireNonNull(path));
		
		ImageUtil.flipImageVertically(bfImage);
		
		GLContext gl_Context = displayGL.getContext();
		
		if (!gl_Context.isCurrent())
		{
			gl_Context.makeCurrent();
		}
		GL2 gl2 = gl_Context.getGL().getGL2();
		
		Texture myTexture = AWTTextureIO.newTexture(displayGL.getGLProfile(), bfImage, true);
		myTexture.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		myTexture.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		
		return myTexture;
	}
		
	
	
	// TODO
	private Texture textureFromPainting() {
	
		BufferedImage bfImage = paintPanel.copyOSC();
		
		GLContext gl_Context = displayGL.getContext();
		if (!gl_Context.isCurrent())
		{
			gl_Context.makeCurrent();
		}
		GL2 gl2 = gl_Context.getGL().getGL2();

		Texture myTexture = AWTTextureIO.newTexture(displayGL.getGLProfile(), bfImage, true);
		myTexture.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		myTexture.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		
		return myTexture;
	}
		
	
	
	private void paintingFromOpenGL() {
		GLContext context = displayGL.getContext(); // OpenGL context for the display panel.
		boolean needsRelease = false;  // Will be set to true if context needs to be made current.
		if ( ! context.isCurrent() ) {
			    // Make the context current on the current thread.
			context.makeCurrent();
			needsRelease = true;
		}
		GL2 gl2 = context.getGL().getGL2();
		AWTGLReadBufferUtil readBuf = new AWTGLReadBufferUtil(displayGL.getGLProfile(), false);
		BufferedImage img = readBuf.readPixelsToBufferedImage(gl2, true); // Get display content as image.
		if (needsRelease) {
			context.release();
		}
		paintPanel.installImage(img); // copy the image into the PaintPanel.
	}	

	public void display(GLAutoDrawable drawable) {    

		GL2 gl2 = drawable.getGL().getGL2();

		gl2.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		
		camera.apply(gl2); // Sets projection and view transformations.
		
		// TODO
		Texture newTexture = currentTexture;
		if(newTexture != null) 
		{
			newTexture.enable(gl2);
			newTexture.bind(gl2);
		}

		drawCurrentShape(gl2);
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glClearColor(1, 1, 1, 1 );
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glEnable(GL2.GL_NORMALIZE);
		gl2.glEnable(GL2.GL_LIGHTING);  
		gl2.glEnable(GL2.GL_LIGHT0);
		gl2.glEnable(GL2.GL_LIGHT1);
		gl2.glEnable(GL2.GL_LIGHT2);
		gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {1,1,10,0}, 0);
		gl2.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[] {0,5,0,0}, 0);
		gl2.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, new float[] {-5,-1,10,0}, 0);
		float[] dimmer = { 0.3f, 0.3f, 0.3f, 1 };
		for (int i = 0; i <= 2; i++) {
			gl2.glLightfv(GL2.GL_LIGHT0 + i, GL2.GL_DIFFUSE, dimmer, 0);
			gl2.glLightfv(GL2.GL_LIGHT0 + i, GL2.GL_SPECULAR, dimmer, 0);
		}
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);  // Not in OpenGL 1.1
		float[] diffuse = { 1, 1, 1, 1 };
		float[] specular = { 0.3f, 0.3f, 0.3f, 1 };
		gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, diffuse, 0);
		gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
		gl2.glMateriali(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 32);
	}


	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}


	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// called when user resizes the window
	}


	public JMenuBar getMenuBar() {
		JMenuBar menuBar = paintPanel.getMenuBar();
		
		JMenu textureMenu = new JMenu("Texture");
		ActionListener textureListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int itemNum = Integer.parseInt(e.getActionCommand());
				switch ( itemNum ) {
				case 0:
					currentTexture = textureFromPainting();
					break;
				case 1:
					paintingFromOpenGL();
					break;
				case 2:
					currentTexture = null;
					break;
				case 3:
					try {
						currentTexture = textureFromResource("earth.jpg");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case 4:
					try {
						currentTexture = textureFromResource("brick.jpg");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case 5:
					try {
						currentTexture = textureFromResource("clouds.jpg");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				default:
					if (itemNum < 10)
						textureRepeatHorizontal = itemNum - 5;
					else
						textureRepeatVertical = itemNum - 9;
				}
				if (itemNum != 1) {
					displayGL.repaint();
				}
			}
		};

		makeMenuItem(textureMenu, ">>> Texture From Painting >>>", textureListener, 0);
		makeMenuItem(textureMenu, "<<< Painting From OpenGL <<<", textureListener, 1);
		textureMenu.addSeparator();
		makeMenuItem(textureMenu, "No Texture", textureListener, 2);
		makeMenuItem(textureMenu, "Earth Texture", textureListener, 3);
		makeMenuItem(textureMenu, "Brick Texture", textureListener, 4);
		makeMenuItem(textureMenu, "Clouds Texture", textureListener, 5);
		textureMenu.addSeparator();
		makeMenuItem(textureMenu, "Horizontal Repeat = 1", textureListener, 6);
		makeMenuItem(textureMenu, "Horizontal Repeat = 2", textureListener, 7);
		makeMenuItem(textureMenu, "Horizontal Repeat = 3", textureListener, 8);
		makeMenuItem(textureMenu, "Horizontal Repeat = 4", textureListener, 9);
		textureMenu.addSeparator();
		makeMenuItem(textureMenu, "Vertical Repeat = 1", textureListener, 10);
		makeMenuItem(textureMenu, "Vertical Repeat = 2", textureListener, 11);
		makeMenuItem(textureMenu, "Vertical Repeat = 3", textureListener, 12);
		makeMenuItem(textureMenu, "Vertical Repeat = 4", textureListener, 13);
		menuBar.add(textureMenu);
		
		JMenu objectMenu = new JMenu("3D Object");
		ActionListener objectListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentObjectNum = Integer.parseInt(e.getActionCommand());
				displayGL.repaint();
			}
		};
		makeMenuItem(objectMenu, "Cube", objectListener, 0);
		makeMenuItem(objectMenu, "Cylinder", objectListener, 1);
		makeMenuItem(objectMenu, "Cone", objectListener, 2);
		makeMenuItem(objectMenu, "Sphere", objectListener, 3);
		makeMenuItem(objectMenu, "Torus", objectListener, 4);
		makeMenuItem(objectMenu, "Teapot", objectListener, 5);
		makeMenuItem(objectMenu, "Triangular Prism", objectListener, 6);
		menuBar.add(objectMenu);
		
		return menuBar;
	}
	
	/**
	 * Create a menu item and add it to a menu.
	 * @param menu  the menu to which the item will be added
	 * @param name  the text for the item
	 * @param listener  an ActionListener that will handle commands from the item
	 * @param i the action command for the item is set to ("" + i)
	 */
	private void makeMenuItem(JMenu menu, String name, ActionListener listener, int i) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(listener);
		item.setActionCommand("" + i);
		menu.add(item);
	}

} // end class Lab7
