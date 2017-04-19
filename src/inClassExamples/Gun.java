package inClassExamples;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Gun extends Group{
	
	Color color = Color.YELLOW;
	
	Rotate rot = new Rotate();
	final int UP = 0;
	final int DOWN = 1;
	int state = UP;
	int highAngle = 30;
	int lowAngle = 0;
	int angle = lowAngle;
	
	public Gun()
	{
		super();
		final PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(color);
        mat.setSpecularColor(Color.WHITE);
        Sphere s = new Sphere(30);
        s.setMaterial(mat);
		Cylinder barrel = new Cylinder(20,80);
		barrel.setRotate(90);
		barrel.setRotationAxis(new Point3D(1,0,0));
		barrel.setTranslateZ(60);
		barrel.setMaterial(mat);
		Cylinder muzzle = new Cylinder(24, 30);
		muzzle.setRotate(90);
		muzzle.setRotationAxis(new Point3D(1,0,0));
		muzzle.setTranslateZ(110);
		muzzle.setMaterial(mat);
		getChildren().addAll(s, barrel, muzzle);

		rot.setAngle(angle);
		rot.setAxis(Rotate.X_AXIS);
		getTransforms().add(rot);
		
		setTranslateY(-20);
	}
	
	public void update()
	{
		switch (state){
		case UP: angle += 1;
		     if (angle > highAngle)
		    	 state = DOWN;
		     break;
		case DOWN: angle -= 1;
			if (angle < lowAngle)
				state = UP;
			break;
		}
		rot.setAngle(angle);
	}
	
}