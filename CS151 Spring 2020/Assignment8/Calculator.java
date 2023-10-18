/**
 * @author azsy
 *
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button; 
import javafx.scene.layout.*;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.scene.control.TextField;
import javafx.stage.Stage;  

import java.util.*;

public class Calculator extends Application{
	
	private double firstOperand = 0.0;
	private double tempOperand = 0.0;
	private String currentOperation = "";
	private TextField outputField;
	private ArrayList<Button> btns;
	
	@Override
	public void start(Stage stage){
		try {
			stage.setTitle("Calculator");
			outputField = new TextField("0");
			btns = new ArrayList<>();
			
			Button b0 = new Button();
			b0.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/zero_btn.png'); -fx-background-size: 100% 100%");
			
			Button b1 = new Button();
			b1.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/one_btn.png'); -fx-background-size: 100% 100%");
			
			Button b2 = new Button();
			b2.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/two_btn.png'); -fx-background-size: 100% 100%");
			
			Button b3 = new Button();
			b3.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/three_btn.png'); -fx-background-size: 100% 100%");
			
			Button b4 = new Button(); 
			b4.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/four_btn.png'); -fx-background-size: 100% 100%");
			
			Button b5 = new Button();
			b5.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/five_btn.png'); -fx-background-size: 100% 100%");
			
			Button b6 = new Button();
			b6.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/six_btn.png'); -fx-background-size: 100% 100%");
			
			Button b7 = new Button();
			b7.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/seven_btn.png'); -fx-background-size: 100% 100%");
			
			Button b8 = new Button();
			b8.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/eight_btn.png'); -fx-background-size: 100% 100%");
			
			Button b9 = new Button();
			b9.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/nine_btn.png'); -fx-background-size: 100% 100%");
			
			btns.add(b0);
			btns.add(b1);
			btns.add(b2);
			btns.add(b3);
			btns.add(b4);
			btns.add(b5);
			btns.add(b6);
			btns.add(b7);
			btns.add(b8);
			btns.add(b9);
			
			
			for(int j = 0; j < 10; j++) {		
				int count = j;
				btns.get(j).setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String currentTxt = outputField.getText();
						String txt = "" + count;
						currentTxt = currentTxt + txt;
						currentTxt = currentTxt.replaceFirst("^0+(?!$)", "");
						outputField.setText(currentTxt);
					}	
				});
			}
			/*
			for(int i = 0; i < 10; i++) {
				btns.get(i).setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Button tmp = (Button)event.getSource();
						String currentTxt = outputField.getText();
						String txt = "";
						if(tmp == btns.get(0)) {
							txt = "0";
						}else if(tmp == btns.get(1)) {
							txt = "1";
						}else if(tmp == btns.get(2)) {
							txt = "2";
						}else if(tmp == btns.get(3)) {
							txt = "3";
						}else if(tmp == btns.get(4)) {
							txt = "4";
						}else if(tmp == btns.get(5)) {
							txt = "5";
						}else if(tmp == btns.get(6)) {
							txt = "6";
						}else if(tmp == btns.get(7)) {
							txt = "7";
						}else if(tmp == btns.get(8)) {
							txt = "8";
						}else {
							txt = "9";
						}
						
						currentTxt = currentTxt + txt;
						currentTxt = currentTxt.replaceFirst("^0+(?!$)", "");
						outputField.setText(currentTxt);
					}
				});
			}
			*/
			Button ADD = new Button();
			ADD.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/add_btn.png'); -fx-background-size: 100% 100%");
			ADD.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					currentOperation = "+";
					
					String tmp = outputField.getText();
					try {
						firstOperand = Double.parseDouble(tmp);
						outputField.setText("0");
					}catch(NumberFormatException e) {
						resetCalc();
					}
				}
			});
			
			Button SUB = new Button();
			SUB.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/sub_btn.png'); -fx-background-size: 100% 100%");
			SUB.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					currentOperation = "-";
					
					String tmp = outputField.getText();
					try {
						firstOperand = Double.parseDouble(tmp);
						outputField.setText("0");
					}catch(NumberFormatException e) {
						resetCalc();
					}
				}
			});
			
			Button MULT = new Button();
			MULT.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/mult_btn.png'); -fx-background-size: 100% 100%");
			MULT.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					currentOperation = "*";
					
					String tmp = outputField.getText();
					try {
						firstOperand = Double.parseDouble(tmp);
						outputField.setText("0");
					}catch(NumberFormatException e) {
						resetCalc();
					}
				}
			});
			
			Button DIV = new Button();
			DIV.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/div_btn.png'); -fx-background-size: 100% 100%");
			DIV.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					currentOperation = "/";
					
					String tmp = outputField.getText();
					try {
						firstOperand = Double.parseDouble(tmp);
						outputField.setText("0");
					}catch(NumberFormatException e) {
						resetCalc();
					}
				}
			});
			
			Button EQ = new Button();
			EQ.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/eq_btn.png'); -fx-background-size: 100% 100%");
			EQ.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					double result = 0.0;
					tempOperand = Double.parseDouble(outputField.getText());
					if(currentOperation.equals("+")) {
						result = firstOperand + tempOperand;
					}else if(currentOperation.equals("-")) {
						result = firstOperand - tempOperand;
					}else if(currentOperation.equals("*")) {
						result = firstOperand * tempOperand;
					}else{
						if(tempOperand != 0.0) {
							result = firstOperand / tempOperand;
						}else {
							resetCalc();
						}
					}
					outputField.setText("" + result);
					firstOperand = result;
				}
			});
			
			Button AC = new Button();
			AC.setStyle("-fx-min-height: 55px; -fx-min-width: 110px; -fx-background-image: url('calculator_images/clear_btn.png'); -fx-background-size: 100% 100%");
			AC.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					resetCalc();
				}
			});
			
			Button VE = new Button();
			VE.setStyle("-fx-min-height: 55px; -fx-min-width: 110px; -fx-background-image: url('calculator_images/pos_neg_btn.png'); -fx-background-size: 100% 100%");
			VE.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String tmp = outputField.getText();
					if(tmp.indexOf(".") < 0) {
						int temp = Integer.parseInt(outputField.getText());
						temp *= -1;
						outputField.setText(""+temp);
					}else {
						double temp = Double.parseDouble(outputField.getText());
						temp *= -1;
						outputField.setText(""+temp);
					}
				}
			});
			
			Button DEC = new Button();
			DEC.setStyle("-fx-min-height: 55px; -fx-min-width: 55px; -fx-background-image: url('/calculator_images/decimal_btn.png'); -fx-background-size: 100% 100%");
			DEC.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String temp = outputField.getText();
					if(temp.indexOf(".") < 0) {
						outputField.setText(temp + ".");
					}
				}
			});
			
			GridPane r = new GridPane();
			r.setHgap(1);
			r.setVgap(1);
			r.add(outputField, 0, 0, 5, 1);
			
			r.add(AC, 0, 1, 2, 1);
	        r.add(VE, 2, 1, 2, 1);
			
			r.add(b0, 0, 2); 
	        r.add(b1, 1, 2);
	        r.add(b2, 2, 2);
	        r.add(ADD, 3, 2);
			
	        r.add(b3, 0, 3); 
	        r.add(b4, 1, 3);
	        r.add(b5, 2, 3);
	        r.add(SUB, 3, 3);
	        
	        r.add(b6, 0, 4); 
	        r.add(b7, 1, 4);
	        r.add(b8, 2, 4);
	        r.add(MULT, 3, 4);
	        
	        r.add(b9, 0, 5); 
	        r.add(DEC, 1, 5);
	        r.add(EQ, 2, 5);
	        r.add(DIV, 3, 5);
	        
	        
			// create a scene 
	        Scene sc = new Scene(r, 225, 309);
	        
	        // set the scene 
	        stage.setScene(sc); 
	        stage.setResizable(false);
	        stage.show();
			
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void resetCalc() {
		currentOperation = "";
		firstOperand = 0.0;
		tempOperand = 0.0;
		outputField.setText("0");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
