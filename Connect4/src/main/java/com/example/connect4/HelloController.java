package com.example.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int CIRCLE_DIAMETER = 80;

    private  String Player_One = "Player One";
    private String Player_Two = "Player Two";
    private static final String discCOLOR1 = "#24303E";
    private static final String discCOLOR2 = "#4CAA88";

    private boolean isPlayerOneTurn = true;
    private Disc insertedDiscsArray[][] = new Disc[ROWS][COLUMNS];

    private  boolean isAllowedToInsert=false;
    private boolean isNameSet=false;


    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane insertedDiscPane;

    @FXML
    public Label playerNameLabel;

    @FXML
    public TextField firstTextField;

    @FXML
    public  TextField secondTextField;

    @FXML
    public Button setNameBtn;

    public void createPlayground() {

        Shape rectangleWithHoles = createGridStructure();

            rectangleWithHoles.setFill(Color.WHITE);
            rootGridPane.add(rectangleWithHoles, 0, 1);

            rootGridPane.setOnMouseClicked(mouseEvent -> {
                if(!isNameSet){
                    Alert alert=new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Connect4");
                    alert.setHeaderText("Please Enter Names first");
                    alert.show();
                }
                    List<Rectangle> rectangleList = createClickableColumns();
                    for (Rectangle rectangle : rectangleList)
                        rootGridPane.add(rectangle, 0, 1);

                });







                setNameBtn.setOnAction(actionEvent -> {
                    setNames();

                });





    }

    public void setNames(){
        String name1=firstTextField.getText();
        String name2=secondTextField.getText();
        if(name1.equals("") || name2.equals("")) {
            isNameSet=false;
            isAllowedToInsert=false;
        }
        else {
            isNameSet=true;

            isAllowedToInsert=true;
            Player_One=name1;
            Player_Two=name2;
            playerNameLabel.setText(name1);
            firstTextField.setDisable(true);
            secondTextField.setDisable(true);
        }




    }
    public Shape createGridStructure() {
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {

                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setSmooth(true);

                circle.setTranslateX(j * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                circle.setTranslateY(i * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }
        return rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumns(){
        List<Rectangle> rectangleList=new ArrayList<>();
        for(int col=0;col<COLUMNS;col++){
            Rectangle rectangle=new Rectangle(CIRCLE_DIAMETER,(ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
            rectangle.setOnMouseEntered(mouseEvent -> {
                rectangle.setFill(Color.valueOf("#eeeeee26"));
            });
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));
            final int column=col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if(isAllowedToInsert){

                    insertDisc(new Disc(isPlayerOneTurn),column);
                    isAllowedToInsert=false;
                }
            });
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }
    private void insertDisc(Disc disc,int column){
        int row=ROWS-1;
        while(row>=0){
            if(getDiscIfPresent(row,column)==null) break;

            row--;
        }

        if(row<0) return;
        insertedDiscsArray[row][column]=disc;
        insertedDiscPane.getChildren().add(disc);
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        int currentRow=row;
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        translateTransition.setOnFinished(actionEvent -> {
            isAllowedToInsert=true;
            if(gameEnded(currentRow,column)){
                    gameOver();
                    return;
            }
            isPlayerOneTurn=!isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn?Player_One:Player_Two);
        });
        translateTransition.play();
    }
    private boolean gameEnded(int row,int column){
        List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3)
                .mapToObj(r->new Point2D(r,column))
                .collect(Collectors.toList());

        List<Point2D> horizontalPoints= IntStream.rangeClosed(column-3,column+3)
                .mapToObj(col->new Point2D(row,col))
                .collect(Collectors.toList());

        Point2D startPoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6)
                .mapToObj(i->startPoint1.add(i,-i))
                .collect(Collectors.toList());

        Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6)
                .mapToObj(i->startPoint2.add(i,i))
                .collect(Collectors.toList());

        boolean isEnded=checkCombinations(verticalPoints) || checkCombinations(horizontalPoints) || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;
        for(Point2D point:points){

            int rowIndexForArray=(int)point.getX();
            int columnIndexForArray=(int)point.getY();

            Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForArray);

            if(disc!=null && disc.isPlayerOneMove==isPlayerOneTurn){
                       chain++;
                       if(chain==4){
                           return true;
                       }
            }else{
                chain=0;
            }
        }
        return false;
    }

    private void gameOver(){
           String winner =isPlayerOneTurn?Player_One:Player_Two;
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect4");
        alert.setHeaderText("Winner is player "+winner);
        alert.setContentText("Want to play again?");
        ButtonType yesbtn=new ButtonType("Yes");
        ButtonType nobtn=new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yesbtn,nobtn);
        Platform.runLater(()->{
            Optional<ButtonType> btnClicked=alert.showAndWait();

            if(btnClicked.isPresent() && btnClicked.get()==yesbtn){
                   resetGame();
            }else{
                Platform.exit();
                System.exit(0);
            }
        });
    }
    private Disc getDiscIfPresent(int row,int column){
        if(row>=ROWS || row<0 ||column>=COLUMNS || column<0)
            return null;

        return insertedDiscsArray[row][column];
    }
    private  static class Disc extends Circle{
        private final boolean isPlayerOneMove;
        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove=isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER/2);
            setFill(isPlayerOneMove?Color.valueOf(discCOLOR1):Color.valueOf(discCOLOR2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);

        }

    }
    public void resetGame(){
        insertedDiscPane.getChildren().clear();

        for(int i=0;i<ROWS;i++)
            for(int j=0;j<COLUMNS;j++)
                insertedDiscsArray[i][j]=null;

        isPlayerOneTurn=true;

        playerNameLabel.setText(Player_One);

        createPlayground();
    }
    public void newGame(){
        insertedDiscPane.getChildren().clear();

        for(int i=0;i<ROWS;i++)
            for(int j=0;j<COLUMNS;j++)
                insertedDiscsArray[i][j]=null;

        isPlayerOneTurn=true;

        Player_One="Player One";
        Player_Two="Player Two";
        firstTextField.setDisable(false);
        secondTextField.setDisable(false);
        firstTextField.setText("");
        secondTextField.setText("");
        playerNameLabel.setText(Player_One);
        isNameSet=false;

        isAllowedToInsert=false;
        createPlayground();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}