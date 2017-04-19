package inClassExamples;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 3D demo game
 * 
 * @author mike slattery
 * @version apr 2016
 */
public class Shooting extends Application {
	final String appName = "Shooting3D";
	final int FPS = 30; // frames per second
	final static int WIDTH = 600;
	final static int HEIGHT = 500;
	final double BOUND = 800;
	
	Scene theScene;
	Group root;
	private PerspectiveCamera camera;
	Bubble ball;
	Blocker block;
	Target target;
	Gun gun;
	
	AudioClip implode, crash;
	
	/**
	 * Set up initial data structures/values
	 */
	
	private void constructWorld(Group root)
    {
    	//Lighting
    	AmbientLight light = new AmbientLight(Color.rgb(100,100,100));
    	root.getChildren().add(light);
    	
    	PointLight pl = new PointLight();
    	pl.setTranslateX(100);
    	pl.setTranslateY(-100);
    	pl.setTranslateZ(-100);
    	root.getChildren().add(pl);
    	
    	// Temp axes for reference
//    	final PhongMaterial greenMaterial = new PhongMaterial();
//        greenMaterial.setDiffuseColor(Color.FORESTGREEN);
//        greenMaterial.setSpecularColor(Color.LIMEGREEN);
//        Box xAxis = new Box(200, 10, 10);
//        xAxis.setMaterial(greenMaterial);
//        Box yAxis = new Box(10, 200, 10);
//        yAxis.setMaterial(greenMaterial);
//        Box zAxis = new Box(10, 10, 200);
//        zAxis.setMaterial(greenMaterial);
//        root.getChildren().addAll(xAxis, yAxis, zAxis);
    	
    	block = new Blocker();
    	block.setTranslateY(-120);
    	root.getChildren().add(block);
    	
    	target = new Target();
    	target.setTranslateY(-100);
    	root.getChildren().add(target);
    	
    	gun = new Gun();
    	root.getChildren().add(gun);
    	
    	implode = new AudioClip(ClassLoader.getSystemResource("exit.wav").toString());
        crash = new AudioClip(ClassLoader.getSystemResource("recycle.wav").toString());
		
    }
	
	void setHandlers(Scene scene)
	{
		scene.setOnKeyPressed(
				e -> fire()
				);
	}

	/**
	 *  Update variables for one time step
	 */
	public void update() {
		gun.update();
		if (ball != null) {
			ball.update();
			if (target.isTouching(ball)){
				crash.play();
			}
			if (block.isTouching(ball)){
				implode.play();
				root.getChildren().remove(ball);
				ball = null;
			}
			else if (outside(ball)){
				root.getChildren().remove(ball);
				ball = null;
			}
		}
	}
		
	private boolean outside(Bubble b) {
        // Check if b is outside BOUND
		double x = b.getTranslateX();
		double y = b.getTranslateY();
		double z = b.getTranslateZ();

        return (x*x + y*y + z*z > BOUND * BOUND);
    }
	
	
	// Launch a bubble
	void fire()
	{
		if (ball == null)
		{
			ball = new Bubble(8, Color.BLUE);
			Point3D loc = gun.localToScene(0, 0, 60);
			ball.setTranslateX(loc.getX());
			ball.setTranslateY(loc.getY());
			ball.setTranslateZ(loc.getZ());
			
			Transform rot = gun.getTransforms().get(0);
			Point3D vel = rot.deltaTransform(0, 0, 4);
			//System.out.println(vel);
			ball.vX = vel.getX(); ball.vY = vel.getY(); ball.vZ = vel.getZ();
			ball.setVisible(true);
			root.getChildren().add(ball);
		}
	}
	
	/*
	 * Begin boiler-plate code...
	 * [Animation with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		root = new Group();
		constructWorld(root);
        
        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.BLACK);
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(10);
        Rotate rot = new Rotate();
        rot.setAngle(-5);
		rot.setAxis(Rotate.Y_AXIS);
		camera.getTransforms().add(rot);
        camera.setTranslateX(90);
        camera.setTranslateY(80);
        camera.setTranslateZ(-500);
        
        
        scene.setCamera(camera);
		
		setHandlers(scene);
		
		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
				}
			);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.setScene(scene);
		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
}