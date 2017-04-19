package inClassExamples;
import java.util.ArrayList;
import java.util.Iterator;

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

public class Target extends Group{
	final int NPANELS = 20;
	final double RADIUS = 600;
	ArrayList<Box> panels = new ArrayList<Box>(NPANELS);
	
	public Target()
	{	
		super();
		final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.WHITE);
		double slice = 2*Math.PI/NPANELS;
        for (int i = 0; i < NPANELS; i++)
        {
        	Box box = new Box(160,60,10);
    		box.setMaterial(greenMaterial);
	        box.setTranslateX(RADIUS*Math.sin(i*slice));
	        box.setTranslateZ(RADIUS*Math.cos(i*slice));
	        if (i % 2 == 0)
	        	box.setTranslateY(-60);
	        box.setRotate(i*slice*180/Math.PI);
	        box.setRotationAxis(new Point3D(0,1,0));
	        panels.add(box);
        }
        getChildren().addAll(panels);
        
        //NOTE: Can't really use this RotateTransition - it will behave very
        // strangely once the taget panels are lopsided.
        // Should use Rotate transform like in Gun class.
        RotateTransition rotator = new RotateTransition(Duration.millis(8000), this);
		rotator.setByAngle(360);
		rotator.setAxis(new Point3D(0,1,0));
		rotator.setInterpolator(Interpolator.LINEAR);
		rotator.setCycleCount(Animation.INDEFINITE);
		rotator.setAutoReverse(false);
		rotator.play();	
	}
	
	/**
	 * Return true if b intersects a panel.
	 * In that case, the panel is removed from the target object.
	 * (We move ball into panel coordinate space to avoid
	 * wrapping effect of non-aligned Box.)
	 */
	public boolean isTouching(Bubble b)
	{
		Bounds bb = b.localToScene(b.getBoundsInLocal());
		Iterator<Box> iter = panels.iterator();
		while (iter.hasNext())
		{
			Box t = iter.next();
			if(t.getBoundsInLocal().intersects(t.sceneToLocal(bb))){
				getChildren().remove(t);
				iter.remove();
				return true;
			}
		}
		return false;
	}
}
