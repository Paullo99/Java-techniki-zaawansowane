package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WishesGenerator extends Application {

	public static void main(String[] args)  {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		 try {
	            Pane root = (Pane) FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("/css/defaultStylesheet.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.setResizable(false);
	            primaryStage.show();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }

	}
}
