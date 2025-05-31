
package controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.util.converter.LocalDateStringConverter;
import javafxmlapplication.PoiUPVApp;

/**
 * FXML Controller class
 *
 * @author ivanbofilloret
 */
public class LoginFXMLController implements Initializable {

    @FXML
    private TextField emailField;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private TextField passwordField;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private DatePicker dateField;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private TextField nicknameField;
    @FXML
    private Label nicknameErrorLabel;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button archivoButton;
    
    
    
    private BooleanProperty validEmail;
    private ChangeListener<String> listenerEmail;
    
    private BooleanProperty validPassword;
    private ChangeListener<String> listenerPassword;
    
    private BooleanProperty validDate;
    private ChangeListener<LocalDate> listenerDate;
    
    private BooleanProperty validNickname;
    private ChangeListener<String> listenerNickname;
    
    private String rutaImagen = null;
    
    
    private void checkEmail() {
        String email = emailField.getText();
//        boolean isValid = email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        validEmail.set(isValid); //actualiza la property asociada
        showError(isValid, emailField, emailErrorLabel); //muestra o esconde el mensaje de error
    }
    
    private void checkPassword() {
        String password = passwordField.getText();
        boolean isValid = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$");
        validPassword.set(isValid); //actualitza la property associada
        showError(isValid, passwordField, passwordErrorLabel); //mostra o amaga el missatge d' error
    }
    
    private void checkDate(){
        LocalDate value = dateField.getValue();
        boolean isValid = value.isBefore(LocalDate.now().minus(16, YEARS));
        validDate.set(isValid);
        showError(isValid, dateField, dateErrorLabel);
    }
    
    private void checkNickname(){
        String nickname = nicknameField.getText();
        boolean isValid = nickname.length() < 15 && nickname.length() > 6;
        validNickname.set(isValid);
        showError(isValid,nicknameField, nicknameErrorLabel);
    }
    
    LocalDateStringConverter localDateStringConverter = new LocalDateStringConverter() {
        @Override
    public LocalDate fromString(String value) {
        try {
            return super.fromString(value);
           } catch (Exception e) {
        System.out.println("Exception in fromString");
        return LocalDate.now();
    }
    }
        @Override
    public String toString(LocalDate value) {
    return super.toString(value);
}
};
    
    
    
    private void showError(boolean isValid, Node field, Node errorMessage){
        errorMessage.setVisible(!isValid);
        field.setStyle(((isValid) ? "" : "-fx-background-color: #FCE5E0"));
    }


    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        acceptButton.disableProperty().set(true);
        
        // TODO
        validEmail = new SimpleBooleanProperty(false);
        validPassword = new SimpleBooleanProperty(false);
        validDate = new SimpleBooleanProperty(false);
        validNickname = new SimpleBooleanProperty(false);
        dateField.setConverter(localDateStringConverter);

        //When the field loses focus, the field is validated. 
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkEmail();
                if (!validEmail.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerEmail == null) {
                        listenerEmail = (a, b, c) -> checkPassword();
                        emailField.textProperty().addListener(listenerEmail);
                    }
                }
            }
        });
        
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkPassword();
                if (!validPassword.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerPassword == null) {
                        listenerPassword = (a, b, c) -> checkPassword();
                        passwordField.textProperty().addListener(listenerPassword);
                    }
                }
            }
        });
      
        dateField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkDate();
                if (!validDate.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerDate == null) {
                        listenerDate= (a, b, c) -> checkEmail();
                        dateField.valueProperty().addListener(listenerDate);
                    }
                }
            }
        });
        
        nicknameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
        
            if(!newVal){
                checkNickname();
                if(!validNickname.get()){
                
                        if(listenerNickname == null) {
                            listenerNickname = (a, b, c) -> checkNickname();
                            nicknameField.textProperty().addListener(listenerNickname);
                        }
                }
            }
        
                });
        
        acceptButton.disableProperty().bind(validEmail.not().or(validPassword.not()).or(validDate.not())
        .or(validNickname.not()));

        
        
        
        
    }    

    
    
    @FXML
    private void handleAcceptButtonOnAction(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String nickname = nicknameField.getText();
        String password = passwordField.getText();
        LocalDate birth = dateField.getValue();
               
        
        
        emailField.clear();
        passwordField.clear();
        nicknameField.clear();
        dateField.setValue(null);
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/InicioFXML.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);
    }

    @FXML
    private void handleCancelButtonOnAction(ActionEvent event) throws IOException {
        emailField.clear();
        passwordField.clear();
        nicknameField.clear();
        dateField.setValue(null);
        validEmail.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/InicioFXML.fxml"));
        Parent root = loader.load();
        PoiUPVApp.setRoot(root, false);

        
    }

    @FXML
    private void seleccionarAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if(selectedFile != null) {
        rutaImagen = selectedFile.getAbsolutePath();
        }
    }
    
}
