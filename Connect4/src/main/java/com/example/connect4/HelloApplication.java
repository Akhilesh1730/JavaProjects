package com.example.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        GridPane rootGridPane=fxmlLoader.load();
        controller=fxmlLoader.getController();

                controller.createPlayground();



        Pane myPane= (Pane)rootGridPane.getChildren().get(0);

        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        myPane.getChildren().add(0,menuBar);

        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
     public MenuBar createMenu(){
         Menu filemenu=new Menu("File");

         MenuItem newgame=new MenuItem("New game");
         newgame.setOnAction(actionEvent -> controller.newGame());

         MenuItem resetgame=new MenuItem("Reset game");
         resetgame.setOnAction(actionEvent -> controller.resetGame());

         MenuItem quitgame=new MenuItem("Quit game");
         quitgame.setOnAction(actionEvent -> quitGame());
         filemenu.getItems().addAll(newgame,resetgame,new SeparatorMenuItem(),quitgame);

         Menu aboutmenu=new Menu("About");

         MenuItem aboutgame=new MenuItem("About Connect4");
         aboutgame.setOnAction(actionEvent -> {
             aboutGame();
         });

         MenuItem aboutme=new MenuItem("About Me");
         aboutme.setOnAction(actionEvent -> aboutMe());
         aboutmenu.getItems().addAll(aboutgame,aboutme);

         MenuBar menuBar=new MenuBar();
         menuBar.getMenus().addAll(filemenu,aboutmenu);
         return menuBar;

     }

    private void aboutMe() {
       Alert alert=new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("About The Developer");
       alert.setHeaderText("AKHILESH DHORE");
       alert.setContentText("Love Java and Live Java");
       alert.show();
    }

    private void aboutGame() {
        Alert alertDialog=new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("Connect4 Game");
        alertDialog.setHeaderText("How To Play!!!");
        alertDialog.setContentText("Connect Four is a two-player connection " +
                "game in which the players first choose a color and " +
                "then take turns dropping colored discs from the top into a " +
                "seven-column, six-row vertically suspended grid. " +
                "The pieces fall straight down, occupying the next available " +
                "space within the column. The objective of the game is to be the " +
                "first to form a horizontal, vertical, or diagonal line of " +
                "four of one's own discs. Connect Four is a solved game. " +
                "The first player can always win by playing the right moves.");
        alertDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alertDialog.show();
    }

    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }



    public static void main(String[] args) {
        launch();
    }
}