package GUI;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import shapes.BoxObj;
import shapes.ConeObj;
import shapes.CylinderObj;
import shapes.Polygon;
import shapes.SphereObj;

import com.sun.j3d.utils.applet.MainFrame;

import algorithm.CrowdSim;

import java.awt.Frame;
import javax.media.j3d.*;
import javax.vecmath.*;

/*
 * Crowd Flock Simulation written by Aksel Taylan, 2018.
 * An implementation of Boid's Algorithm with the Java3D library,
 * including a GUI interface using JavaFX to allow for user
 * customization.
 * Special thanks to Shaun Plummer of GitHub, the mathematical
 * implementation of this algorithm is modeled after his project.
 */

// ------------------------------------------------------------

/*
 * Main holds the GUI that feeds user input
 * data into CrowdSim for a customized simulation.
 * It bridges the JavaFX GUI and Java3D fully rendered
 * scene showcasing the actual simulation of Boid's algorithm.
 */

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    		
    		// set up GUI
	        primaryStage.setTitle("Crowd Flock Simulation");
	        GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));
	        
	        Button btn = new Button("Generate Simulation");
	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtn.getChildren().add(btn);
	        grid.add(hbBtn, 1, 4);

	        Scene scene = new Scene(grid, 600, 450);
	        primaryStage.setScene(scene);
	        
	        Text scenetitle = new Text("Crowd Flocking Simulation by Aksel Taylan");
	        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        grid.add(scenetitle, 0, 0, 2, 1);
    
	        Label shapetype = new Label("Type of polygon: ");
	        grid.add(shapetype, 0, 1);
	        
	        Label about = new Label("? About");
	        about.setFont(Font.font("Tahoma",FontWeight.NORMAL, 14));
	        about.setTooltip(new Tooltip("Flocking is a common simulation algorithm used on crowds in computer graphics.\n"
	        		+ "This is an implementation of Boid's algorithm using the Java3D library modeled after\nShaun Plummer's version found on GitHub"));
	        grid.add(about, 3, 0);
	        
	        
	        final ComboBox polygonChoice = new ComboBox();
	        polygonChoice.getItems().addAll(
	            "Sphere",
	            "Cone",
	            "Cylinder",
	            "Box"
	        );
	        
	        grid.add(polygonChoice, 1, 1);

	        Label density = new Label("Density of crowd:");
	        density.setTooltip(new Tooltip("Optimal density for good performance is about 30."));
	        grid.add(density, 0, 2);

	        TextField densityField = new TextField();
	        grid.add(densityField, 1, 2);

	        Label spd = new Label("Speed of crowd:");
	        spd.setTooltip(new Tooltip("Optimal speed for good performance is about 0.05. The default is set to 0.05."));
	        grid.add(spd, 0, 3);
	        
	        Slider slider = new Slider(0, 0.08, 0.05);
	        slider.setShowTickMarks(true);
	        slider.setBlockIncrement(0.01f);
	        slider.snapToTicksProperty();
	        slider.setShowTickLabels(true);
	        grid.add(slider, 1, 3);
	        
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	        	 
	            @Override
	            public void handle(ActionEvent e) {
	            	
	            	// get values chosen by user from GUI
	            	
	            	double speedVal = slider.getValue();
	            	int densityVal = Integer.valueOf(densityField.getText());
	            	String polygonType = String.valueOf(polygonChoice.getValue());
	            	
	            	Polygon poly;
	            	Material mat = new Material();
	            	Color3f col = new Color3f((float) 255,(float) 255,(float) 255);
	            	
	            	
	            	// prepare the simulation based on the user input
	            	
            		if (polygonType == "Sphere") {
            			SphereObj s = new SphereObj(mat, col, 1);
            			poly = s;
            		}
            		else if (polygonType == "Cylinder") {
            			CylinderObj cy = new CylinderObj(mat, col, 1, 1);
            			poly = cy;
            		}
            		else if (polygonType == "Box") {
            			BoxObj box = new BoxObj(mat, col, 1, 1, 1);
            			poly = box;
            		}
            		// by default the shape is a cone
            		else {
            			ConeObj cone = new ConeObj(mat, col, 1, 1);
            			poly = cone;
            		}
            		
            		// initiate simulation
            		
            		CrowdSim sim = new CrowdSim(poly, densityVal, speedVal);
            		Frame frame = new MainFrame(sim, 1000, 1000);
            		
            		// play simulation
            		
            		sim.animate();
	            	}
	            		            	
	        });
	        
	        primaryStage.show();
        };

 public static void main(String[] args) {
        launch(args);
    }
}