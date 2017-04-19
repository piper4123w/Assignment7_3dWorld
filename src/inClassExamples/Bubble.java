package inClassExamples;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Bubble extends Sphere {

	public double vX = 0;
	public double vY = 0;
	public double vZ = 0;
	
    public Bubble(double radius, Color fill) {
		super(radius);
		final PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(fill);
        mat.setSpecularColor(Color.WHITE);
        setMaterial(mat);
    }

    public void update() {
        setTranslateX(getTranslateX() + vX);
        setTranslateY(getTranslateY() + vY);
        setTranslateZ(getTranslateZ() + vZ);
    }
    
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    