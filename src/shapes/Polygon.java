package shapes;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public abstract class Polygon {
	private Primitive shape;
	private Material mat;
	private Color3f color;
	private Appearance app;
	
	public Polygon (Material m, Color3f c) {
		mat = m;
		mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
		color = c;
	}
	public Polygon () {
	}
	
	protected void setShape (Primitive p) {
		shape = p;
	}
	
	protected void setAppearances() {
		app = new Appearance();
		app.setMaterial(mat);
		ColoringAttributes colors = new ColoringAttributes(color, ColoringAttributes.NICEST);
		
		app.setColoringAttributes(colors);
		app.setMaterial(mat);
		shape.setAppearance(app);
	}
	
	public abstract Node getShape ();
	
	public void setMat (Material m) {
		mat = m;
	}
	
	public void setColor (Color3f col) {
		color = col;
	}
	
	public Appearance getAppearance() {
		return app;
	}
	
	public abstract String getName ();

}
