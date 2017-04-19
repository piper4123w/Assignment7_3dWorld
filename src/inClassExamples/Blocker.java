package inClassExamples;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class Blocker extends Group{
	final int NPANELS = 8;
	final double RADIUS = 500;
	Box[] panels = new Box[NPANELS];
	
	public Blocker()
	{	
		super();
		final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.WHITE);
        double slice = 2*Math.PI/NPANELS;
        for (int i = 0; i < NPANELS; i++)
        {
        	Box box = new Box(100, 80, 10);
        	box.setMaterial(redMaterial);
	        box.setTranslateX(RADIUS*Math.sin(i*slice));
	        box.setTranslateZ(RADIUS*Math.cos(i*slice));
	        box.setRotate(i*slice*180/Math.PI);
	        box.setRotationAxis(new Point3D(0,1,0));
	        panels[i] = box;
        }
        getChildren().addAll(panels);
        
        RotateTransition rotator = new RotateTransition(Duration.millis(9000), this);
		rotator.setByAngle(360);
		rotator.setAxis(new Point3D(0,-1,0));
		rotator.setInterpolator(Interpolator.LINEAR);
		rotator.setCycleCount(Animation.INDEFINITE);
		rotator.setAutoReverse(false);
		rotator.play();	
	}
	
	/**
	 * Return true if b intersects any of the panels.
	 * (We move ball into panel coordinate space to avoid
	 * wrapping effect of non-aligned Box.)
	 */
	public boolean isTouching(Bubble b)
	{
		Bounds bb = b.localToScene(b.getBoundsInLocal());
		for (int i = 0; i < NPANELS; i++)
        {
			if(panels[i].getBoundsInLocal().intersects(panels[i].sceneToLocal(bb))){
				return true;
			}
        }
		return false;
	}
}