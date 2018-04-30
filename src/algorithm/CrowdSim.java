package algorithm;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import shapes.Polygon;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Random;
import javax.media.j3d.*;
import javax.vecmath.*;

/*
 * CrowdSim is the main class that creates the data and
 * implements it onto a Java3D scene view. The simulation is
 * initially prepared in the constructor and the scene is created
 * through the constructSim() method.
 */

public class CrowdSim extends Applet implements Simulation {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Boid> boids;
	private double seperateDist = 0.25;
	private double seperateFactor = 200;
	private double moveFactor = 2000;
	private double boundFactor = 0.0075;
	private BranchGroup bg;
	
	public CrowdSim (Polygon shape, int density, double spd) {
		bg = new BranchGroup();
		boids = new ArrayList<Boid>();
		
		// generate the positions of the boids, set their polygon type
		// using data from user, and scale them to fit the scene
		// view

		for (int i = 0; i < density; ++i) {
			
			// randomly generate positions to create random scatter effect
			
			float x = negRand((float) Math.random());
			float y = negRand((float) Math.random());

			Vector2f loc = new Vector2f(x,y);
			Boid newBoid = new Boid (loc, shape, spd);
			
			// transform vector for moving
			newBoid.displayGroup = new TransformGroup();
			newBoid.display = new Transform3D();
			newBoid.display.setTranslation(new Vector3d(x,y,0));
			
			// determine polygon type based on user input
			if (newBoid.getPoly().getName() == "sphere") {
				Sphere s = new Sphere(0.5f);
				s.setAppearance(newBoid.getPoly().getAppearance());
				newBoid.displayGroup.addChild(s);
			}
			else if (newBoid.getPoly().getName() == "cylinder") {
				Cylinder cyl = new Cylinder(0.5f, 0.5f);
				cyl.setAppearance(newBoid.getPoly().getAppearance());
				newBoid.displayGroup.addChild(cyl);
			}
			else if (newBoid.getPoly().getName() == "box") {
				Box bx = new Box(0.5f, 0.5f, 0.5f, newBoid.getPoly().getAppearance());
				newBoid.displayGroup.addChild(bx);
				
			}
			else {
				Cone co = new Cone(0.5f, 0.5f);
				co.setAppearance(newBoid.getPoly().getAppearance());
				newBoid.displayGroup.addChild(co);
			}
			
			// set up the boid to be manipulated through the simulation
			newBoid.display.setScale(0.05f);
			newBoid.displayGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			newBoid.displayGroup.setTransform(newBoid.display);
			boids.add(newBoid);
			bg.addChild(newBoid.displayGroup);
			
		}
		
		// set up output window for simulation
		
		setLayout(new BorderLayout());
		Canvas3D canvas = createCanvas();
		add(canvas);
		
		// begin simulation call
		
		constructSim(canvas);
	}
	
	// simple function that helps randomize coordinates on spawn
	public float negRand(float num) {
		Random rand = new Random();
		int coinflip = rand.nextInt(10) + 1;
		if (coinflip > 5) {
			return num * -1;
		}
		else {
			return num;
		}
	}
	
	// main function of the algorithm that updates all boids positions every frame
	public void step() {
		
		Vector2f groupV = new Vector2f();
		Vector2f collisionV = new Vector2f();
		Vector2f velocityMatchV = new Vector2f();
		Vector2f boundsV = new Vector2f();
		
		// iterates through entire crowd
		for (Boid b : boids) {
			
			groupV = group(b);
			collisionV = avoid(b);
			velocityMatchV = match(b);
			boundsV = bounding(b);
			
			Vector2f total = new Vector2f();
			total.x = total.x + groupV.x + collisionV.x + velocityMatchV.x + boundsV.x;
			total.y = total.y + groupV.y + collisionV.y + velocityMatchV.y + boundsV.y;
			
			Vector2f newVelocity = new Vector2f(b.getVelocity().x + total.x, b.getVelocity().y + total.y);
			Vector2f movenext = new Vector2f (b.getLocation().x + b.getVelocity().x, b.getLocation().y + b.getVelocity().y);
			b.setVelocity(newVelocity);
			b.setLocation(movenext);
			b.display.setTranslation(new Vector3d(movenext.x, movenext.y, 0));
			b.displayGroup.setTransform(b.display);
		}
		
	}
	
	// Four components to the algorithm below
	
	// forces boids to remain in a group
	private Vector2f group(Boid boid) {
		Vector2f center = new Vector2f();
		
		for (Boid b : boids) {
			if (!b.equals(boid)) {
				center.x = center.x + b.getLocation().x;
				center.y = center.y + b.getLocation().y;
			}
		}
		
		center.x = center.x / boids.size() - 1;
		center.y = center.y / boids.size() - 1;
		
		center.x = center.x - boid.getLocation().x;
		center.y = center.y - boid.getLocation().y;
		
		center.x = (float) (center.x / moveFactor);
		center.y = (float) (center.y / moveFactor);
		
		return center;
		
	}
	
	// keeps boids from overlapping too frequently
	private Vector2f avoid(Boid boid) {
		Vector2f correct = new Vector2f();
		Vector2f inPos = new Vector2f(boid.getLocation());
		
		for (Boid b : boids) {
			if (!b.equals(boid)) {
				Vector2f bPos = boid.getLocation();
				Vector2f xDir = new Vector2f(bPos.x - inPos.x, bPos.y - inPos.y);
				
				if (Math.abs(xDir.x) < seperateDist && Math.abs(xDir.y) < seperateDist) {
					correct.x = correct.x - xDir.x;
					correct.y = correct.y - xDir.y;
					
					correct.x = (float) (correct.x / seperateFactor);
					correct.y = (float) (correct.y / seperateFactor);
				}
				
			}
		}
		
		return correct;
	}
	
	// attempts to keep crowd at similar velocity
	private Vector2f match(Boid boid) {
		Vector2f bestVelocity = new Vector2f();
		
		for (Boid b : boids) {
			if (!b.equals(boid)) {
				bestVelocity.x = bestVelocity.x + b.getLocation().x;
				bestVelocity.y = bestVelocity.y + b.getLocation().y;
			}
		}
		
		bestVelocity.x = bestVelocity.x / boids.size() - 1;
		bestVelocity.y = bestVelocity.y / boids.size() - 1;
		bestVelocity.x = bestVelocity.x - boid.getVelocity().x;
		bestVelocity.y = bestVelocity.y - boid.getVelocity().y;
		bestVelocity.x = (float) (bestVelocity.x / 9600);
		bestVelocity.y = (float) (bestVelocity.y / 9600);
		
		return bestVelocity;
	}
	
	// gets crowd to stay within the view of the scene
	private Vector2f bounding(Boid boid) {
		Vector2f bnd = new Vector2f();
		float xmin = (float) -0.9;
		float xmax = (float) 0.9;
		float ymin = (float) -0.9;
		float ymax = (float) 0.9;
		
		Vector2f inPos = boid.getLocation();
		
		if (inPos.x < xmin) {
			bnd.x += 0.01;
		}
		else if (inPos.x > xmax) {
			bnd.x += -0.01;
		}
		if (inPos.y < ymin) {
			bnd.y += 0.01;
		}
		else if (inPos.y > ymax) {
			bnd.y += -0.01;
		}
		
		bnd.x = (float) (bnd.x / boundFactor);
		bnd.y = (float) (bnd.y / boundFactor);
		
		return bnd;
	}
	
	public void setBranchGroup(BranchGroup bgg) {
		bg = bgg;
	}
	
	private Canvas3D createCanvas()

	{

		GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();

        template.setDoubleBuffer(GraphicsConfigTemplate3D.REQUIRED);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice dev = env.getDefaultScreenDevice();

        Canvas3D canvas = new Canvas3D(dev.getBestConfiguration(template));     

        canvas.setDoubleBufferEnable(true);	

        return canvas;

	}
	
	// follows basic Java3D data structure to construct the simulation
	
	public void constructSim(Canvas3D canvas) {

		   SimpleUniverse universe = new SimpleUniverse(canvas);
		   
		   // lighting
		   Color3f light1Color = new Color3f(0f, 1.1f, 1.1f);
		   BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0);	  
		   Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
		   DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		   light1.setInfluencingBounds(bounds);	   
		   light1.setEnable(true);
		   bg.addChild(light1);

		   // look towards the simulation
		   
		   universe.getViewingPlatform().setNominalViewingTransform();
		   
		   universe.getViewer().getView().setBackClipDistance(100.0);
		   
		   // add the group of objects to the Universe

		   universe.addBranchGraph(bg);
	} 
	
	// uses JavaFX animation library to run the algorithm every frame
	public void animate() {
		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame update = new KeyFrame(Duration.seconds(0.2),
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent event) {
                    	step();
                    }
                });
		
		tl.getKeyFrames().add(update);
		tl.play();
	}
	
	
}
