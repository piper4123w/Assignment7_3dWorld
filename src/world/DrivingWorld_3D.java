/*Author: Kyle Lawson
 * Date:  4/21/17
 * 
 * Description: 3D adaptation of the 2d driving world example.
 * 
 */

package world;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.util.Duration;

public class DrivingWorld_3D extends Application {
	private final double HEIGHT = 600;
	private final double WIDTH = 600;
	private final int FPS = 30;

	private PerspectiveCamera camera;
	private Group cameraDolly;

	private int rows = 5;
	private int cols = 5;
	ArrayList<Box> buildings;

	Box car;
	Direction dir = Direction.UP;

	private void constructWorld(Group root) {
		AmbientLight light = new AmbientLight(Color.rgb(100, 100, 100));

		buildings = new ArrayList<Box>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Box b = new Box(140, 140, 300);
				b.setTranslateX((i - (rows / 2)) * 300);
				b.setTranslateY((j - (cols / 2)) * 300);
				PhongMaterial colors = new PhongMaterial();
				int grayness = (int) (105 + (Math.random() * 150));
				colors.setDiffuseColor(Color.rgb(grayness, grayness, grayness));
				colors.setSpecularColor(Color.WHITE);
				b.setMaterial(colors);
				buildings.add(b);
			}
		}

		PointLight pl = new PointLight();
		pl.setTranslateX(100);
		pl.setTranslateY(-100);
		pl.setTranslateZ(-100);
		root.getChildren().add(pl);

		PhongMaterial streetColor = new PhongMaterial();
		streetColor.setDiffuseColor(Color.DARKGRAY);
		streetColor.setSpecularColor(Color.GREY);
		Box street = new Box(rows * 300, cols * 300, 1);
		street.setMaterial(streetColor);

		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.RED);
		redMaterial.setSpecularColor(Color.TOMATO);
		car = new Box(50, 50, 15);
		car.setTranslateZ(-20);
		car.setTranslateX(150);
		car.setMaterial(redMaterial);

		root.getChildren().addAll(buildings);
		root.getChildren().addAll(light, street, car);
	}

	private void update() {
		// TODO Auto-generated method stub
		double x = car.getTranslateX();
		double y = car.getTranslateY();
		if (dir == Direction.UP || dir == Direction.DOWN) {
			if ((int) (Math.abs(y)) % 300 == 150) {
				System.out.println("TURN");
				dir = ((Math.random() > 0.5) ? Direction.RIGHT : Direction.LEFT);
			}
		} else if (dir == Direction.RIGHT || dir == Direction.LEFT) {
			if ((int) (Math.abs(x)) % 300 == 150) {
				System.out.println("TURN");
				dir = ((Math.random() > 0.5) ? Direction.UP : Direction.DOWN);
			}
		}

		if (dir == Direction.UP) {
			y++;
		} else if (dir == Direction.DOWN) {
			y--;
		} else if (dir == Direction.RIGHT) {
			x++;
		} else
			x--;
		car.setTranslateX(x);
		car.setTranslateY(y);
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
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
		// cameraDolly.setTranslateX(200);;
		cameraDolly.getChildren().add(camera);
		sceneRoot.getChildren().add(cameraDolly);

		Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate, yRotate);

		// Use keyboard to control camera position
		scene.setOnKeyPressed(event -> {
			double change = 15;
			// What key did the user press?
			KeyCode keycode = event.getCode();

			Point3D delta = null;
			if (keycode == KeyCode.A && cameraDolly.getTranslateX() > -(cols / 2) * 300) {
				delta = new Point3D(-change, 0, 0);
			}
			if (keycode == KeyCode.D && cameraDolly.getTranslateX() < (cols / 2) * 300) {
				delta = new Point3D(change, 0, 0);
			}
			if (keycode == KeyCode.W && cameraDolly.getTranslateY() > -(rows / 2) * 300) {
				delta = new Point3D(0, -change, 0);
			}
			if (keycode == KeyCode.S && cameraDolly.getTranslateY() < (rows / 2) * 300) {
				delta = new Point3D(0, change, 0);
			}
			if (delta != null) {
				Point3D delta2 = camera.localToParent(delta);
				cameraDolly.setTranslateX(cameraDolly.getTranslateX() + delta2.getX());
				cameraDolly.setTranslateY(cameraDolly.getTranslateY() + delta2.getY());
				cameraDolly.setTranslateZ(cameraDolly.getTranslateZ() + delta2.getZ());

			}
		});

		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS), new EventHandler<ActionEvent>() {
			@Override
			public void handle(javafx.event.ActionEvent event) {
				// update position
				update();
			}

		});
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		primaryStage.setTitle("PongWorld");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
