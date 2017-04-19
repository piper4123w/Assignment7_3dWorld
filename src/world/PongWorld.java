package world;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.*;

public class PongWorld extends Application{
	private final double HEIGHT = 600;
	private final double WIDTH = 600;
	
	private PerspectiveCamera camera;
	private Group cameraDolly;

	private void constructWorld(Group root) {
		AmbientLight light = new AmbientLight(Color.rgb(100, 100, 100));
		
//		PointLight pl = new PointLight();
//		pl.setTranslateX(100);
//		pl.setTranslateY(-100);
//		pl.setTranslateZ(-100);
//		root.getChildren().add(pl);
		
		PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.LIGHTGREEN);
		
		Box paddle1 = new Box(50, 50, 0);
		paddle1.setMaterial(greenMaterial);
		
		PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.RED);
		redMaterial.setSpecularColor(Color.TOMATO);
		
		Sphere ball = new Sphere(20);
		ball.setMaterial(redMaterial);
		ball.setTranslateZ(20);
		
		
		
		root.getChildren().addAll(paddle1, ball);
	}

	

	@Override
	public void start(Stage primaryStage) {
		Group sceneRoot = new Group();
		constructWorld(sceneRoot);
		
		Scene scene = new Scene(sceneRoot, WIDTH, HEIGHT, true);
		scene.setFill(Color.BLACK);
		
		camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(100000.0);
		scene.setCamera(camera);
		
		cameraDolly = new Group();
		cameraDolly.setTranslateZ(-1000);
		//cameraDolly.setTranslateX(200);;
		cameraDolly.getChildren().add(camera);
		sceneRoot.getChildren().add(cameraDolly);
		
		Rotate xRotate = new Rotate(0,0,0,0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0,0,0,0,Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate, yRotate);
		
		
		primaryStage.setTitle("PongWorld");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
