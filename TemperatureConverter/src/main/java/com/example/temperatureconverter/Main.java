package com.example.temperatureconverter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new
                FXMLLoader(getClass().getResource("app_layout.fxml"));
        VBox rootNode = loader.load();
        MenuBar menuBar=createMenu();
        rootNode.getChildren().add(0,menuBar);
        Scene scene = new Scene(rootNode);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");

        primaryStage.show();
    }
    public MenuBar createMenu(){
          MenuBar menuBar=new MenuBar();

          Menu fileMenu=new Menu("File");

          MenuItem newitem=new MenuItem("New");
        MenuItem quititem=new MenuItem("Quit");
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        newitem.setOnAction(actionEvent -> {
            System.out.println("new clicked");
        });

        quititem.setOnAction(actionEvent -> {
            Platform.exit();
        });
        fileMenu.getItems().addAll(newitem,separatorMenuItem,quititem);


        Menu helpMenu=new Menu("help");

        MenuItem aboutitem=new MenuItem("About");

        helpMenu.getItems().addAll(aboutitem);

        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }
}
