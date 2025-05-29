/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafxmlapplication.*;

/**
 * FXML Controller class
 *
 * @author ivanbofilloret
 */
public class InicioFXMLController implements Initializable {

    @FXML
    private TextField nicknameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button entrarButton;
    @FXML
    private Button registrarseButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void entrarAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MenuPrincipalFXML.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);
    }

    @FXML
    private void registrarseAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/LoginFXML.fxml"));
        Parent root = loader.load();        
        PoiUPVApp.setRoot(root, false);    
    }
    
}
