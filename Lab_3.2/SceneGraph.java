package gk3dot2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;

//Zadanie B
public class SceneGraph extends JPanel {

    public static void main(String[] args) {
        JFrame window = new JFrame("Scene Graph 2D");
        window.setContentPane( new SceneGraph() );
        window.pack();
        window.setLocation(100,60);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private final static int WIDTH = 800;   // The preferred size for the drawing area.
    private final static int HEIGHT = 600;

    private final static double X_LEFT = -4;    // The xy limits for the coordinate system.
    private final static double X_RIGHT = 4;
    private final static double Y_BOTTOM = -3;
    private final static double Y_TOP = 3;

    private final static Color BACKGROUND = Color.WHITE; // Initial background color for drawing.

    private final static double scale = 0.75; //rotation scale
    
    private float pixelSize;  // The size of a pixel in drawing coordinates.

    private int frameNumber = 0;  // Current frame number, goes up by one in each frame.

    private CompoundObject world; // SceneGraphNode representing the entire scene.

    // TODO: Define global variables to represent animated objects in the scene.
    //group of shapes 1
    private TransformedObject myPolygon_1a;
    private TransformedObject myPolygon_1b;
    private TransformedObject rect_1;
    private TransformedObject base_1;

    //group of shapes 2
    private TransformedObject myPolygon_2a;
    private TransformedObject myPolygon_2b;
    private TransformedObject rect_2;
    private TransformedObject base_2;
    
    //group of shapes 3
    private TransformedObject myPolygon_3a;
    private TransformedObject myPolygon_3b;
    private TransformedObject rect_3;
    private TransformedObject base_3;
    
    /**
     *  Builds the data structure that represents the entire picture.
     */
    private void createWorld() {

        world = new CompoundObject();  // Root node for the scene graph.

        //group of shapes 1
        myPolygon_1a = new TransformedObject(filledPolygon);
        myPolygon_1a.setScale(0.0135,0.0135);
        myPolygon_1a.setColor(Color.RED);
        myPolygon_1a.setTranslation(-3.075,1.82);
        world.add(myPolygon_1a);
        
        myPolygon_1b = new TransformedObject(filledPolygon);
        myPolygon_1b.setScale(0.0135,0.0135);
        myPolygon_1b.setColor(Color.RED);
        myPolygon_1b.setTranslation(-1.30,1.20);
        world.add(myPolygon_1b);

        rect_1 = new TransformedObject(filledRect);
        rect_1.setScale(2,0.2);
        rect_1.setColor(Color.GREEN);
        rect_1.setTranslation(-2.17,1.50);
        rect_1.setRotation(-20);
        world.add(rect_1);
        
        base_1 = new TransformedObject(filledTriangle);
        base_1.setScale(0.6,1.8);
        base_1.setColor(Color.MAGENTA);
        base_1.setTranslation(-2.2,-0.3);
        world.add(base_1);
        
        //group of shapes 2
        myPolygon_2a = new TransformedObject(filledPolygon);
        myPolygon_2a.setScale(0.01,0.01);
        myPolygon_2a.setColor(Color.BLUE);
        myPolygon_2a.setTranslation(-3.075+1.8,1.82-2.5);
        world.add(myPolygon_2a);
        
        myPolygon_2b = new TransformedObject(filledPolygon);
        myPolygon_2b.setScale(0.0105,0.0105);
        myPolygon_2b.setColor(Color.BLUE);
        myPolygon_2b.setTranslation(-1.3+1.8,1.20-2.5);
        world.add(myPolygon_2b);

        rect_2 = new TransformedObject(filledRect);
        rect_2.setScale(2,0.3);
        rect_2.setColor(Color.LIGHT_GRAY);
        rect_2.setTranslation(-2.17+1.8,1.50-2.5);
        rect_2.setRotation(-20);
        world.add(rect_2);

        base_2 = new TransformedObject(filledTriangle);
        base_2.setScale(0.2,1.8);
        base_2.setColor(Color.DARK_GRAY);
        base_2.setTranslation(-2.2+1.8,-0.3-2.5);
        world.add(base_2);
        
        
        //group of shapes 3
        myPolygon_3a = new TransformedObject(filledPolygon);
        myPolygon_3a.setScale(0.007,0.007);
        myPolygon_3a.setColor(Color.GREEN);
        myPolygon_3a.setTranslation(-3.075+4.3,1.82-0.85);
        world.add(myPolygon_3a);
        
        myPolygon_3b = new TransformedObject(filledPolygon);
        myPolygon_3b.setScale(0.007,0.007);
        myPolygon_3b.setColor(Color.GREEN);
        myPolygon_3b.setTranslation(-1.30+4.3,1.20-0.85);
        world.add(myPolygon_3b);

        rect_3 = new TransformedObject(filledRect);
        rect_3.setScale(2,0.2);
        rect_3.setColor(Color.BLACK);
        rect_3.setTranslation(-2.17+4.3,1.50-0.85);
        rect_3.setRotation(-20);
        world.add(rect_3);
        
        base_3 = new TransformedObject(filledTriangle);
        base_3.setScale(0.5,1.3);
        base_3.setColor(Color.YELLOW);
        base_3.setTranslation(-2.2+4.3,-0.3-0.3);
        world.add(base_3);

        } // end createWorld()


    /**
     * This method is called just before each frame is drawn.  It updates the modeling
     * transformations of the objects in the scene that are animated.
     */
    public void updateFrame() {
        frameNumber++;
        // TODO: Update state in preparation for drawing the next frame.
        
        myPolygon_1a.setRotation(frameNumber*scale*4);
        myPolygon_1b.setRotation(frameNumber*scale*5);
        
        myPolygon_2a.setRotation(frameNumber*scale*1.5);
        myPolygon_2b.setRotation(frameNumber*scale*3);
        
        myPolygon_3a.setRotation(frameNumber*scale*20);
        myPolygon_3b.setRotation(frameNumber*scale*30);
        
    }



    //------------------- A Simple Scene Object-Oriented Scene Graph API ----------------

    private static abstract class SceneGraphNode {
        Color color;  // If not null, the default color for this node and its children.
        // If null, the default color is inherited.
        SceneGraphNode setColor(Color c) {
            this.color = c;
            return this;
        }
        final void draw(Graphics2D g) {
            Color saveColor = null;
            if (color != null) {
                saveColor = g.getColor();
                g.setColor(color);
            }
            doDraw(g);
            if (saveColor != null) {
                g.setColor(saveColor);
            }
        }
        abstract void doDraw(Graphics2D g);
    }

    /**
     *  Defines a subclass, CompoundObject, of SceneGraphNode to represent
     *  an object that is made up of sub-objects.  Initially, there are no
     *  sub-objects.  Objects are added with the add() method.
     */
    private static class CompoundObject extends SceneGraphNode {
        ArrayList<SceneGraphNode> subobjects = new ArrayList<SceneGraphNode>();
        CompoundObject add(SceneGraphNode node) {
            subobjects.add(node);
            return this;
        }
        void doDraw(Graphics2D g) {
            for (SceneGraphNode node : subobjects)
                node.draw(g);
        }
    }

    /**
     *  TransformedObject is a subclass of SceneGraphNode that
     *  represents an object along with a modeling transformation to
     *  be applied to that object.  The object must be specified in
     *  the constructor.  The transformation is specified by calling
     *  the setScale(), setRotate() and setTranslate() methods. Note that
     *  each of these methods returns a reference to the TransformedObject
     *  as its return value, to allow for chaining of method calls.
     *  The modeling transformations are always applied to the object
     *  in the order scale, then rotate, then translate.
     */
    private static class TransformedObject extends SceneGraphNode {
        SceneGraphNode object;
        double rotationInDegrees = 0;
        double scaleX = 1, scaleY = 1;
        double translateX = 0, translateY = 0;
        
        TransformedObject(SceneGraphNode object) {
            this.object = object;
        }
        
        TransformedObject setRotation(double degrees) {
            rotationInDegrees = degrees;
            return this;
        }
        
        TransformedObject setTranslation(double dx, double dy) {
            translateX = dx;
            translateY = dy;
            return this;
        }
        
        TransformedObject setScale(double sx, double sy) {
            scaleX = sx;
            scaleY = sy;
            return this;
        }
        
        void doDraw(Graphics2D g) {
            AffineTransform savedTransform = g.getTransform();
            if (translateX != 0 || translateY != 0)
                g.translate(translateX,translateY);
            if (rotationInDegrees != 0)
                g.rotate( rotationInDegrees/180.0 * Math.PI);
            if (scaleX != 1 || scaleY != 1)
                g.scale(scaleX,scaleY);
            object.draw(g);
            g.setTransform(savedTransform);
        }
    }

    // Create some basic objects as custom SceneGraphNodes.

    private static SceneGraphNode line = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  g.draw( new Line2D.Double( -0.5,0, 0.5,0) ); }
    };

    private static SceneGraphNode rect = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  g.draw(new Rectangle2D.Double(-0.5,-0.5,1,1)); }
    };

    private static SceneGraphNode filledRect = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  g.fill(new Rectangle2D.Double(-0.5,-0.5,1,1)); }
    };

    private static SceneGraphNode circle = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  g.draw(new Ellipse2D.Double(-0.5,-0.5,1,1)); }
    };

    private static SceneGraphNode filledCircle = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  g.fill(new Ellipse2D.Double(-0.5,-0.5,1,1)); }
    };

    private static SceneGraphNode filledTriangle = new SceneGraphNode() {
        void doDraw(Graphics2D g) {  // width = 1, height = 1, center of base is at (0,0);
            Path2D path = new Path2D.Double();
            path.moveTo(-0.5,0);
            path.lineTo(0.5,0);
            path.lineTo(0,1);
            path.closePath();
            g.fill(path);
        }
    };
    private static SceneGraphNode filledPolygon = new SceneGraphNode() {
        
    	void doDraw(Graphics2D g) {
        
    		int n=13;
            int r=55;
            
            int[] xDim = new int[n];
            int[] yDim = new int[n];
            
            for(int i=0;i<n;i++){
            	
                xDim[i]= (int)(r * Math.cos((Math.PI/2 + 2*Math.PI*i)/n));
                yDim[i]= (int)(r * Math.sin((Math.PI/2 + 2*Math.PI*i)/n));
            }
            
            Path2D path = new Path2D.Double();
            path.moveTo(xDim[0],yDim[0]);
            
            for (int i=1;i<n;i++){
            	
                path.lineTo(xDim[i],yDim[i]);
            }
            
            path.closePath();
            g.fill(path);
        }
    };



    //--------------------------------- Implementation ------------------------------------

    private JPanel display;  // The JPanel in which the scene is drawn.

    /**
     * Constructor creates the scene graph data structure that represents the
     * scene that is to be drawn in this panel, by calling createWorld().
     * It also sets the preferred size of the panel to the constants WIDTH and HEIGHT.
     * And it creates a timer to drive the animation.
     */
    public SceneGraph() {
        display = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                applyLimits(g2, X_LEFT, X_RIGHT, Y_TOP, Y_BOTTOM, false);
                g2.setStroke( new BasicStroke(pixelSize) ); // set default line width to one pixel.
                world.draw(g2);
            }
        };
        display.setPreferredSize( new Dimension(WIDTH,HEIGHT));
        display.setBackground( BACKGROUND );
        final Timer timer = new Timer(17,new ActionListener() { // about 60 frames per second
            public void actionPerformed(ActionEvent evt) {
                updateFrame();
                repaint();
            }
        });
        final JCheckBox animationCheck = new JCheckBox("Run Animation");
        animationCheck.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (animationCheck.isSelected()) {
                    if ( ! timer.isRunning() )
                        timer.start();
                }
                else {
                    if ( timer.isRunning() )
                        timer.stop();
                }
            }
        });
        JPanel top = new JPanel();
        top.add(animationCheck);
        setLayout(new BorderLayout(5,5));
        setBackground(Color.DARK_GRAY);
        setBorder( BorderFactory.createLineBorder(Color.DARK_GRAY,4) );
        add(top,BorderLayout.NORTH);
        add(display,BorderLayout.CENTER);
        createWorld();
    }



    /**
     * Applies a coordinate transform to a Graphics2D graphics context.  The upper left corner of
     * the viewport where the graphics context draws is assumed to be (0,0).  The coordinate
     * transform will make a requested rectangle visible in the drawing area.  The requested
     * limits might be adjusted to preserve the aspect ratio.  (This method sets the global variable
     * pixelSize to be equal to the size of one pixel in the transformed coordinate system.)
     * @param g2 The drawing context whose transform will be set.
     * @param xleft requested x-value at left of drawing area.
     * @param xright requested x-value at right of drawing area.
     * @param ytop requested y-value at top of drawing area.
     * @param ybottom requested y-value at bottom of drawing area; can be less than ytop, which will
     *     reverse the orientation of the y-axis to make the positive direction point upwards.
     * @param preserveAspect if preserveAspect is false, then the requested rectangle will exactly fill
     * the viewport; if it is true, then the limits will be expanded in one direction, horizontally or
     * vertically, to make the aspect ratio of the displayed rectangle match the aspect ratio of the
     * viewport.  Note that when preserveAspect is false, the units of measure in the horizontal and
     * vertical directions will be different.
     */
    private void applyLimits(Graphics2D g2, double xleft, double xright,
                             double ytop, double ybottom, boolean preserveAspect) {
        int width = display.getWidth();   // The width of the drawing area, in pixels.
        int height = display.getHeight(); // The height of the drawing area, in pixels.
        if (preserveAspect) {
            // Adjust the limits to match the aspect ratio of the drawing area.
            double displayAspect = Math.abs((double)height / width);
            double requestedAspect = Math.abs(( ybottom-ytop ) / ( xright-xleft ));
            if (displayAspect > requestedAspect) {
                double excess = (ybottom-ytop) * (displayAspect/requestedAspect - 1);
                ybottom += excess/2;
                ytop -= excess/2;
            }
            else if (displayAspect < requestedAspect) {
                double excess = (xright-xleft) * (requestedAspect/displayAspect - 1);
                xright += excess/2;
                xleft -= excess/2;
            }
        }
        double pixelWidth = Math.abs(( xright - xleft ) / width);
        double pixelHeight = Math.abs(( ybottom - ytop ) / height);
        pixelSize = (float)Math.min(pixelWidth,pixelHeight);
        g2.scale( width / (xright-xleft), height / (ybottom-ytop) );
        g2.translate( -xleft, -ytop );
    }

}