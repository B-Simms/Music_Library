package com.blake.music_library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("Music Library");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

/*
For new JavaFX projects

.gitignore for java add

	*.iml
	.idea

for javaFX projects
right click Project > Open Module Settings
	Project - Make sure language level matches
	Global Libraries - right click JavaFX - add to modules

right click src folder > new > module-info.java
	requires javafx.fxml;
	requires javafx.controls;

	//opens "package name";
	opens */
