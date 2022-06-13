package com.example.temperatureconverter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button convertButton;

    @FXML
    public TextField tempText;

    @FXML
    public ChoiceBox tempChoice;
    private boolean flag=true;
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tempChoice.getItems().add("Celsius to Fahrenheit");
        tempChoice.getItems().add("Fahrenheit to Celsius");
        tempChoice.setValue("Celsius to Fahrenheit");

        tempChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String o, String n) {
                if(n.equals("Celsius to Fahrenheit")){
                    flag=true;
                }else{
                    flag=false;
                }
            }
        });
        convertButton.setOnAction(actionEvent -> {
            convert();
        });

    }

    public void convert(){
        String text=tempText.getText();
            float num=0.0f;
        try {
            num=Float.parseFloat(text);

        }catch (Exception ex){
             warUser();
             return;
        }


        float newtemperature=0.0f;
        if(flag){
            newtemperature=(num*9/5)+32;
            showTemperature(num,newtemperature);
        }else{
              newtemperature=(num-32)*5/9;
            showTemperature(num,newtemperature);
        }


    }
    public void warUser(){
        Alert alertDialog=new Alert(Alert.AlertType.WARNING);
        alertDialog.setTitle("Welcome");
        alertDialog.setHeaderText("Warning");
        alertDialog.setContentText("Please Enter valid temperature");
        alertDialog.show();
    }
    public void showTemperature(Float oldtemp,Float newTemp){
        String oS,nS;

        Alert alertDialog=new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("Welcome");
        alertDialog.setHeaderText("Temperature Convertor");
        if(flag)
        {
            oS="Old temperature: "+oldtemp+" C";
            nS="New Temperature: "+newTemp+" F";
        }else{
            oS="Old temperature: "+oldtemp+" F";
            nS="New Temperature: "+newTemp+" C";

        }

        alertDialog.setContentText(oS+"\n"+nS);
        alertDialog.show();

    }
}
