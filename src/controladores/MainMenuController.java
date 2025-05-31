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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafxmlapplication.PoiUPVApp;

/**
 * FXML Controller class
 *
 * @author ivanbofilloret
 */
public class MainMenuController implements Initializable {

    @FXML
    private ImageView avatarUsuario;
    @FXML
    private Label nombreUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onSolveProblem(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/FXMLDocument.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);
    }

    @FXML
    private void onViewResults(ActionEvent event) {
    }

    @FXML
    private void onEditProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/modificarPerfilFXML.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);
    }

    @FXML
    private void onOpenCarta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/FXMLDocument.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);
    }

    @FXML
    private void onLogout(ActionEvent event) {
    }
    
}
