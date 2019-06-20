package Garage.Login;

import Garage.Administrator.*;
import Garage.Main;
import Garage.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static String ADMIN_FXML_LOCATION = "Administrator.fxml";

    @FXML
    private TextField nameFiled;

    @FXML
    private PasswordField passwrodField;

    @FXML
    private Button loginBtn;

    @FXML
    private Label errorLbl;

    private ArrayList<User> users;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LoginService.checkFirstState();

        loadResources();

        setBtnListener();
    }

    private void loadResources(){

        loadUsers();

    }

    private void loadUsers(){
        try{
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(Main.USER_PATH));
            users=(ArrayList<User>) ois.readObject();
        }catch (IOException ioex){
            ioex.printStackTrace();
            new File(Main.USER_PATH).delete();
        }catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }
}



    private void setBtnListener(){


      // loginBtn.setDisable(true);

//        loginBtn.getScene().onKeyPressedProperty().set(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
////                if (nameFiled.getCharacters().length()>0 && passwrodField.getCharacters().length()>0)
////                    loginBtn.setDisable(false);
//            }
//        });

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nameFiled.getText()!="" && passwrodField.getText()!=""){
                    User user= new User(nameFiled.getText(),passwrodField.getText());
                    if (users.contains((user))){
                        if (users.get(users.indexOf(user)) instanceof Administrator){
                            System.out.println("is admin");
                            //start Admin

                            try {
                                FXMLLoader loader =new  FXMLLoader(Administrator.class.getResource(ADMIN_FXML_LOCATION));
                                Parent parent= loader.load();
                                Scene scene= new Scene(parent);
                                Stage stage= new Stage();
                                stage.setScene(scene);
                                stage.show();
                                ((Stage) loginBtn.getScene().getWindow()).close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else
                            System.out.println("is client");
                    }

                }
            }
        });
    }
}
