package gk_lab5;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT; 


public class Lab5 extends GLJPanel implements GLEventListener, KeyListener{

	public static void main(String[] args) {
		JFrame window = new JFrame("Some Objects in 3D");
		Lab5 panel = new Lab5();
		window.setContentPane(panel);
		window.pack();
		window.setResizable(false);
		window.setLocation(50,50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public Lab5() {
		super( new GLCapabilities(null) ); // Makes a panel with default OpenGL "capabilities".
		setPreferredSize( new Dimension(700,700) );
		addGLEventListener(this); // This panel will respond to OpenGL events.
		addKeyListener(this);  // The panel will respond to key events.
	}

	public void selectObject() {
		//TODO
	}
	
	private int objectNumber = 2;       
	
	private boolean useAnaglyph = false; // Should anaglyph stereo be used?
	                                     //    (Controlled by space bar.)

	private int rotateX = 0;    // Rotations of the cube about the axes.
	private int rotateY = 0;    //   (Controlled by arrow, PageUp, PageDown keys;
	private int rotateZ = 0;    //    Home key sets all rotations to 0.)

	private GLUT glut = new GLUT(); // An object for drawing GLUT shapes.

	private int N = 13;			//defines the version of the task
	
	/**
	 * The method that draws the current object, with its modeling transformation.
	 */
	private void draw(GL2 gl2) {

		gl2.glRotatef(rotateZ,0,0,1);   // Apply rotations to complete object.
		gl2.glRotatef(rotateY,0,1,0);
		gl2.glRotatef(rotateX,1,0,0);
		
		switch(objectNumber) {
		
		case 1: 
				gl2.glTranslatef(0, 0, -7.f);
				Helix(gl2, N);
			break;
			
		case 2: 
				
				Pyramid(gl2, N);
			break;
		}
	}

	
	
	private void Helix(GL2 gl2, float N) {
		
		float pointSize = 10f;
		gl2.glPointSize(pointSize);
		gl2.glColor3f(0,1,0);
		float r = 5.0f;
		float jumpZ = 0.05f;
		float X, Y, Z = 0.0f;
		
		for(float angle = 0.0f; angle <= (2*Math.PI*N); angle += 0.1f){
	
			X = r*(float)Math.cos(angle);
			Y = r*(float)Math.sin(angle);
			
			gl2.glPointSize(pointSize+=(jumpZ*angle)/N);
			gl2.glBegin(GL2.GL_POINTS);
			gl2.glVertex3f(X, Y, Z);
			gl2.glEnd();
			
			Z+=jumpZ;
		}
	
		gl2.glPopMatrix();
		gl2.glFlush();
	}
	
	private void Triangle(GL2 gl2, float[] color) {
		
//		gl2.glBegin(GL2.GL_TRIANGLES);
		
		gl2.glColor3f(color[0],color[1],color[2]);		
		gl2.glVertex2f(1,0);
		gl2.glVertex2f(0,1);
		gl2.glVertex2f(0,0);
		
		
//		gl2.glEnd();
	}
	
	private void Pyramid(GL2 gl2, int N) {
		
		float radius = 5.f;
		float X, Y;
		float Z = 0;
		float z = 5;
		
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);//tworzenie podstawy
		gl2.glColor3f(0,1,0);
		gl2.glVertex3f(0,0,Z);
		for(int i=0; i<=2*N; i++) {
			
			X = radius * (float)Math.cos(2 * Math.PI * i/N);
			Y = radius * (float)Math.sin(2 * Math.PI * i/N);
		
			gl2.glVertex3f(X, Y, Z);
			
			if(i%2 == 0) 
				gl2.glVertex3f(0,0,Z);
		
		}
		gl2.glEnd();
		
		
		//tworzenie boków
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);
		gl2.glColor3f(0, 0.7f, 0);
		
		gl2.glVertex3f(0,0,z);
		for(int i=0; i<=2*N; i++) {
			
			X = radius * (float)Math.cos(2 * Math.PI * i/N);
			Y = radius * (float)Math.sin(2 * Math.PI * i/N);
		
			gl2.glVertex3f(X, Y, Z);
			
			if(i%2 == 0) 
				gl2.glVertex3f(0,0,z);
		
		}
		gl2.glEnd();
		
		
	}
	 
	
	//-------------------- Draw the Scene  -------------------------

	/**
	 * The display method is called when the panel needs to be drawn.
	 * It's called when the window opens and it is called by the keyPressed
	 * method when the user hits a key that modifies the scene.
	 */
	public void display(GLAutoDrawable drawable) {    

		GL2 gl2 = drawable.getGL().getGL2(); // The object that contains all the OpenGL methods.

		if (useAnaglyph) {
			gl2.glDisable(GL2.GL_COLOR_MATERIAL); // in anaglyph mode, everything is drawn in white
			gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, new float[]{1,1,1,1}, 0);
		}
		else { 
			gl2.glEnable(GL2.GL_COLOR_MATERIAL);  // in non-anaglyph mode, glColor* is respected
		}        	
		gl2.glNormal3f(0,0,1); // (Make sure normal vector is correct for object 1.)

		gl2.glClearColor( 0, 0, 0, 1 ); // Background color (black).
		gl2.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );


		if (useAnaglyph == false) {
			gl2.glLoadIdentity(); // Make sure we start with no transformation!
			gl2.glTranslated(0,0,-15);  // Move object away from viewer (at (0,0,0)).
			draw(gl2);
		}
		else {
			gl2.glLoadIdentity(); // Make sure we start with no transformation!
			gl2.glColorMask(true, false, false, true);
			gl2.glRotatef(4,0,1,0);
			gl2.glTranslated(1,0,-15); 
			draw(gl2);  // draw the current object!
			gl2.glColorMask(true, false, false, true);
			gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			gl2.glLoadIdentity();
			gl2.glRotatef(-4,0,1,0);
			gl2.glTranslated(-1,0,-15); 
			gl2.glColorMask(false, true, true, true);
			draw(gl2);
			gl2.glColorMask(true, true, true, true);
		}

	} // end display()

	/** The init method is called once, before the window is opened, to initialize
	 *  OpenGL.  Here, it sets up a projection, turns on some lighting, and enables
	 *  the depth test.
	 */
	public void init(GLAutoDrawable drawable) {
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glFrustum(-3.5, 3.5, -3.5, 3.5, 5, 25);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glEnable(GL2.GL_LIGHTING);  
		gl2.glEnable(GL2.GL_LIGHT0);
		gl2.glLightfv(GL2.GL_LIGHT0,GL2.GL_DIFFUSE,new float[] {0.7f,0.7f,0.7f},0);
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glLineWidth(3);  // make wide lines for the stellated dodecahedron.
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// called when user resizes the window
	}

	// ----------------  Methods from the KeyListener interface --------------

	/**
	 * Responds to keypressed events.  The four arrow keys control the rotations
	 * about the x- and y-axes.  The PageUp and PageDown keys control the rotation
	 * about the z-axis.  The Home key resets all rotations to zero.  The number
	 * keys 1, 2, 3, 4, 5, and 6 select the current object number.  Pressing the space
	 * bar toggles anaglyph stereo on and off.  The panel is redrawn to reflect the
	 * change.
	 */
	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		boolean repaint = true;
		if ( key == KeyEvent.VK_LEFT )
			rotateY -= 6;
		else if ( key == KeyEvent.VK_RIGHT )
			rotateY += 6;
		else if ( key == KeyEvent.VK_DOWN)
			rotateX += 6;
		else if ( key == KeyEvent.VK_UP )
			rotateX -= 6;
		else if ( key == KeyEvent.VK_PAGE_UP )
			rotateZ += 6;
		else if ( key == KeyEvent.VK_PAGE_DOWN )
			rotateZ -= 6;
		else if ( key == KeyEvent.VK_HOME )
			rotateX = rotateY = rotateZ = 0;
		else if (key == KeyEvent.VK_1)
			objectNumber = 1;
		else if (key == KeyEvent.VK_2)
			objectNumber = 2;
		else if (key == KeyEvent.VK_SPACE)
			useAnaglyph = ! useAnaglyph;
		else
			repaint = false;
		if (repaint)
			repaint();
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

} // end class Lab4