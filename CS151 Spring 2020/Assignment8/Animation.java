/**
 * @author azsy
 *
 */
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.event.*;
import javafx.scene.paint.*;

public class Animation extends Application{
	@Override
	public void start(Stage s) {
		
		Sphere sphere = new Sphere();
		sphere.setRadius(100.0f);
		PhongMaterial sMaterial = new PhongMaterial();
		sMaterial.setDiffuseColor(Color.BLUE);
		sphere.setMaterial(sMaterial);
		
		Cylinder cylinder = new Cylinder();
		cylinder.setRadius(100.0f);
		cylinder.setHeight(200.0);
		PhongMaterial cMaterial = new PhongMaterial();
		cMaterial.setDiffuseColor(Color.FUCHSIA);
		cylinder.setMaterial(cMaterial);
		
		Box box = new Box();
		box.setWidth(200.0);
		box.setHeight(200.0);
		box.setDepth(200.0);
		PhongMaterial bMaterial = new PhongMaterial();
		bMaterial.setDiffuseColor(Color.CHOCOLATE);
		box.setMaterial(bMaterial);
		
		Polygon poly = new Polygon();
		poly.getPoints().addAll(new Double[] {
			200.0, 50.0,
			400.0, 50.0,
			450.0, 150.0,
			400.0, 250.0,
			200.0, 250.0,
			150.0, 150.0,
		});
		poly.setFill(Color.FORESTGREEN);
		
		// menu for sphere
		Menu sphereMenu = new Menu("Sphere");
		
		// rotation transition for sphere
		MenuItem sp1 = new MenuItem("Rotate");
		sphereMenu.getItems().add(sp1);
		sp1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RotateTransition sphereTransition = new RotateTransition();
				sphereTransition.setDuration(Duration.millis(100));
				sphereTransition.setNode(sphere);
				sphereTransition.setAxis(Rotate.Y_AXIS);
				sphereTransition.setAutoReverse(true);
				sphereTransition.setByAngle(120);
				sphereTransition.setCycleCount(50);
				sphereTransition.play();
			}
		});
		
		// scale transition for sphere
		MenuItem sp2 = new MenuItem("Scale");
		sphereMenu.getItems().add(sp2);
		sp2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ScaleTransition sphereTransition = new ScaleTransition();
				sphereTransition.setDuration(Duration.millis(1000));
				sphereTransition.setNode(sphere);
				sphereTransition.setAutoReverse(true);
				sphereTransition.setByX(1.5);
				sphereTransition.setByY(1.5);
				sphereTransition.setCycleCount(6);
				sphereTransition.play();
			}
		});
		
		// sequential transition for sphere
		MenuItem sp3 = new MenuItem("Sequential");
		sphereMenu.getItems().add(sp3);
		sp3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// #1
				TranslateTransition st = new TranslateTransition();
				st.setDuration(Duration.millis(1000));
				st.setNode(sphere);
				st.setByX(100);
				st.setByY(100);
				st.setCycleCount(4);
				st.setAutoReverse(true);
				
				ScaleTransition stt = new ScaleTransition();
				stt.setDuration(Duration.millis(1000));
				stt.setNode(sphere);
				stt.setAutoReverse(true);
				stt.setByX(1.5);
				stt.setByY(1.5);
				stt.setCycleCount(6);
				
				TranslateTransition st2 = new TranslateTransition();
				st2.setDuration(Duration.millis(1000));
				st2.setNode(sphere);
				st2.setByX(100);
				st2.setByY(0);
				st2.setCycleCount(4);
				st2.setAutoReverse(true);
				
				SequentialTransition sphereSequence = new SequentialTransition(st, stt, st2);
				sphereSequence.play();
			}
		});
		
		// menu for cylinder
		Menu cylinderMenu = new Menu("Cylinder");
		
		// rotation transition for cylinder
		MenuItem c1 = new MenuItem("Rotate");
		cylinderMenu.getItems().add(c1);
		c1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RotateTransition cylinderTransition = new RotateTransition();
				cylinderTransition.setDuration(Duration.millis(1000));
				cylinderTransition.setNode(cylinder);
				cylinderTransition.setAxis(Rotate.Z_AXIS);
				cylinderTransition.setAutoReverse(true);
				cylinderTransition.setByAngle(90);
				cylinderTransition.setCycleCount(2);
				cylinderTransition.play();
			}
		});
				
		// scale transition for cylinder
		MenuItem c2 = new MenuItem("Scale");
		cylinderMenu.getItems().add(c2);
		c2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ScaleTransition cylinderTransition = new ScaleTransition();
				cylinderTransition.setDuration(Duration.millis(1000));
				cylinderTransition.setNode(cylinder);
				cylinderTransition.setAutoReverse(true);
				cylinderTransition.setByX(1.5);
				cylinderTransition.setByY(1.5);
				cylinderTransition.setCycleCount(6);
				cylinderTransition.play();
			}
		});
				
		// sequential transition for cylinder
		MenuItem c3 = new MenuItem("Sequential");
		cylinderMenu.getItems().add(c3);
		c3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// #1
				TranslateTransition ct = new TranslateTransition();
				ct.setDuration(Duration.millis(1000));
				ct.setNode(cylinder);
				ct.setByX(100);
				ct.setByY(-100);
				ct.setCycleCount(4);
				ct.setAutoReverse(true);
				
				// #2
				ScaleTransition ct1 = new ScaleTransition();
				ct1.setDuration(Duration.millis(1000));
				ct1.setNode(cylinder);
				ct1.setAutoReverse(true);
				ct1.setByX(1.5);
				ct1.setByY(1.5);
				ct1.setCycleCount(6);
				
				// #3
				RotateTransition ct2 = new RotateTransition();
				ct2.setDuration(Duration.millis(1000));
				ct2.setNode(cylinder);
				ct2.setAxis(Rotate.Z_AXIS);
				ct2.setAutoReverse(true);
				ct2.setByAngle(-120);
				ct2.setCycleCount(4);
						
				SequentialTransition cylinderSequence = new SequentialTransition(ct, ct1, ct2);
				cylinderSequence.play();
			}
		});
		
		Menu boxMenu = new Menu("Box");
		
		MenuItem b1 = new MenuItem("Rotate");
		boxMenu.getItems().add(b1);
		b1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RotateTransition boxTransition = new RotateTransition();
				boxTransition.setDuration(Duration.millis(1000));
				boxTransition.setNode(box);
				boxTransition.setAxis(Rotate.Y_AXIS);
				boxTransition.setAutoReverse(false);
				boxTransition.setByAngle(360);
				boxTransition.setCycleCount(2);
				boxTransition.play();
				
			}
		});
		
		MenuItem b2 = new MenuItem("Scale");
		boxMenu.getItems().add(b2);
		b2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ScaleTransition boxTransition = new ScaleTransition();
				boxTransition.setDuration(Duration.millis(1000));
				boxTransition.setNode(box);
				boxTransition.setAutoReverse(true);
				boxTransition.setByX(1.5);
				boxTransition.setByY(1.5);
				boxTransition.setCycleCount(6);
				boxTransition.play();
			}
		});
		
		MenuItem b3 = new MenuItem("Sequential");
		boxMenu.getItems().add(b3);
		b3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TranslateTransition bt = new TranslateTransition();
				bt.setDuration(Duration.millis(1000));
				bt.setNode(box);
				bt.setByX(-100);
				bt.setByY(100);
				bt.setCycleCount(4);
				bt.setAutoReverse(true);
				
				// #2
				ScaleTransition bt1 = new ScaleTransition();
				bt1.setDuration(Duration.millis(1000));
				bt1.setNode(box);
				bt1.setAutoReverse(true);
				bt1.setByX(1.5);
				bt1.setByY(1.5);
				bt1.setCycleCount(6);
				
				// #3
				RotateTransition bt2 = new RotateTransition();
				bt2.setDuration(Duration.millis(1000));
				bt2.setNode(box);
				bt2.setAxis(Rotate.Y_AXIS);
				bt2.setAutoReverse(true);
				bt2.setByAngle(-120);
				bt2.setCycleCount(4);
						
				SequentialTransition boxSequence = new SequentialTransition(bt, bt1, bt2);
				boxSequence.play();
			}
		});
		
		
		Menu polygonMenu = new Menu("Polygon");
		
		MenuItem p1 = new MenuItem("Rotate");
		polygonMenu.getItems().add(p1);
		p1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RotateTransition polyTransition = new RotateTransition();
				polyTransition.setDuration(Duration.millis(1000));
				polyTransition.setNode(poly);
				polyTransition.setAxis(Rotate.Y_AXIS);
				polyTransition.setAutoReverse(false);
				polyTransition.setByAngle(360);
				polyTransition.setCycleCount(2);
				polyTransition.play();
			}
		});
		
		MenuItem p2 = new MenuItem("Scale");
		polygonMenu.getItems().add(p2);
		p2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ScaleTransition polyTransition = new ScaleTransition();
				polyTransition.setDuration(Duration.millis(1000));
				polyTransition.setNode(poly);
				polyTransition.setAutoReverse(true);
				polyTransition.setByX(1.5);
				polyTransition.setByY(1.5);
				polyTransition.setCycleCount(6);
				polyTransition.play();
			}
		});
		
		MenuItem p3 = new MenuItem("Fade");
		polygonMenu.getItems().add(p3);
		p3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FadeTransition polyFade = new FadeTransition();
				polyFade.setDuration(Duration.millis(1000));
				polyFade.setNode(poly);
				polyFade.setAutoReverse(true);
				polyFade.setFromValue(10);
				polyFade.setToValue(0.1);
				polyFade.setCycleCount(6);
				polyFade.play();
			}
		});
		
		MenuItem p4 = new MenuItem("Sequential");
		polygonMenu.getItems().add(p4);
		p4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TranslateTransition pt = new TranslateTransition();
				pt.setDuration(Duration.millis(1000));
				pt.setNode(poly);
				pt.setByX(-100);
				pt.setByY(-100);
				pt.setCycleCount(4);
				pt.setAutoReverse(true);
				
				// #2
				ScaleTransition ps = new ScaleTransition();
				ps.setDuration(Duration.millis(1000));
				ps.setNode(poly);
				ps.setAutoReverse(true);
				ps.setByX(1.5);
				ps.setByY(1.5);
				ps.setCycleCount(6);
				
				// #3
				RotateTransition pr = new RotateTransition();
				pr.setDuration(Duration.millis(1000));
				pr.setNode(poly);
				pr.setAxis(Rotate.Y_AXIS);
				pr.setAutoReverse(true);
				pr.setByAngle(-120);
				pr.setCycleCount(4);
				
				// #4
				FadeTransition pf = new FadeTransition();
				pf.setDuration(Duration.millis(1000));
				pf.setNode(poly);
				pf.setAutoReverse(true);
				pf.setFromValue(10);
				pf.setToValue(0.1);
				pf.setCycleCount(6);	
				SequentialTransition polySequence = new SequentialTransition(pt, ps, pr, pf);
				polySequence.play();
			}
		});
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(sphereMenu);
		menuBar.getMenus().add(cylinderMenu);
		menuBar.getMenus().add(boxMenu);
		menuBar.getMenus().add(polygonMenu);
		
		BorderPane border = new BorderPane();
		GridPane grid = new GridPane();
		
		Scene scene = new Scene(border);
		s.setScene(scene);
		
		border.setTop(menuBar);
		border.setCenter(grid);
		
		grid.setHgap(2);
		grid.add(sphere, 0, 1);
		grid.add(box, 1, 1);
		grid.add(cylinder, 0, 2);
		grid.add(poly, 1, 2);
		grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(12);
		grid.setVgap(12);
		
		s.setTitle("Animation");
		s.setX(300);
		s.setY(400);
		s.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
