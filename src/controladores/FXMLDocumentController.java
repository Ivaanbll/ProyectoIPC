/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxmlapplication.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    private Label mousePosistion;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label mousePosition;
    @FXML
    private Pane drawingPane;
        @FXML
    private ImageView cartaNauticaImg;
    @FXML
    private ImageView transportadorImg;
    @FXML
    private Menu Dibujar;
    @FXML
    private Menu Limpiar;
    @FXML
    private Menu Herramientas;
    @FXML
    private ImageView reglaImg;
    
    //Variables para las herramientas de la carta nautica
    private boolean modoLineaActivo = false;
    private boolean esperandoSegundoClick = false;
    private double startX, startY,medidaX, medidaY, transpX, transpY, arcoStartX, arcoStartY,
                    reglaX, reglaY;
    private boolean modoPuntoActivo = false;
    private boolean modoEliminarActivo = false;
    private boolean modoArcoActivo = false;
    private boolean hacerRadio = false;
    private boolean modoTextoActivo = false;
    private boolean modoMedirActivo = false;
    private boolean esperandoSegundoPuntoMedida = false;
    private double angulo = 0;
    private boolean transportadorVisible = false;
    private boolean modoExtremosActivo = false;
    private boolean reglaVisible = false;
    private double anguloRegla = 0;






    



    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la mousePosistion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la mousePosistion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    /* private void initData() {
            data=map_listview.getItems();
            data.add(new Poi("1F", "Edificion del DSIC", 275, 250));
            data.add( new Poi("Agora", "Agora", 575, 350));
            data.add( new Poi("Pista", "Pista de atletismo y campo de futbol", 950, 350));
    }
    */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.33);
        zoom_slider.setMax(1.67);
        zoom_slider.setValue(0.87);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        
        // Codigo para el transportador
       
        transportadorImg.setVisible(false);
        
        transportadorImg.setOnMousePressed(e -> {
        transpX = e.getSceneX() - transportadorImg.getLayoutX();
        transpY = e.getSceneY() - transportadorImg.getLayoutY();
        e.consume();
        });
        
        transportadorImg.setOnMouseDragged(e -> {
            transportadorImg.setLayoutX(e.getSceneX() - transpX);
            transportadorImg.setLayoutY(e.getSceneY() - transpY);
            e.consume();
        
        });
        
        transportadorImg.setOnScroll(e -> {
        if(!transportadorImg.isVisible()) return;
        double rotacion = e.getDeltaY() > 0 ? 5 : -5;
        angulo = (angulo + rotacion) % 360;
        transportadorImg.setRotate(angulo);
        e.consume();
        
        
            });
        
        // Codigo para la regla
       
        reglaImg.setVisible(false);
        
        reglaImg.setOnMousePressed(e -> {
        reglaX = e.getSceneX() - reglaImg.getLayoutX();
        reglaY = e.getSceneY() - reglaImg.getLayoutY();
        e.consume();
        });
        
        reglaImg.setOnMouseDragged(e -> {
            reglaImg.setLayoutX(e.getSceneX() - reglaX);
            reglaImg.setLayoutY(e.getSceneY() - reglaY);
            e.consume();
        
        });
        
        reglaImg.setOnScroll(e -> {
        if(!reglaImg.isVisible()) return;
        double rotacionRegla = e.getDeltaY() > 0 ? 5 : -5;
        angulo = (angulo + rotacionRegla) % 360;
        reglaImg.setRotate(angulo);
        e.consume();
        
        
            });
        
        
        

    }



    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0.0, 0.0);
                } //(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }

    @FXML
    private void handleLineaAction(ActionEvent event) {        
        modoLineaActivo = true;
        esperandoSegundoClick = false;
        drawingPane.setCursor(Cursor.CROSSHAIR);
    }
    
    
    //Metodo para los listeners de eliminar marcas de la Carta nautica
    
private void asignarListenersEliminacion(Node nodo) {
    final Color colorHover = Color.ORANGE;

    nodo.setOnMouseEntered(e -> {
        if (modoEliminarActivo) {
            drawingPane.setCursor(Cursor.HAND);

            if (nodo instanceof Circle circle) {
                // Puntos y arcos
                if (circle.getFill() != null && !circle.getFill().equals(Color.TRANSPARENT)) {
                    circle.setUserData(circle.getFill());
                    circle.setFill(colorHover);
                } else {
                    circle.setUserData(circle.getStroke());
                    circle.setStroke(colorHover);
                }

            } else if (nodo instanceof Line line) {
                line.setUserData(line.getStroke());
                line.setStroke(colorHover);

            } else if (nodo instanceof Text text) {
                text.setUserData(text.getFill());
                text.setFill(colorHover);
            }
        }
    });

    nodo.setOnMouseExited(e -> {
        if (modoEliminarActivo) {
            drawingPane.setCursor(Cursor.DISAPPEAR);

            if (nodo instanceof Circle circle) {
                if (circle.getFill() != null && !circle.getFill().equals(Color.TRANSPARENT)) {
                    Color original = (Color) circle.getUserData();
                    if (original != null) circle.setFill(original);
                } else {
                    Color original = (Color) circle.getUserData();
                    if (original != null) circle.setStroke(original);
                }

            } else if (nodo instanceof Line line) {
                Color original = (Color) line.getUserData();
                if (original != null) line.setStroke(original);

            } else if (nodo instanceof Text text) {
                Color original = (Color) text.getUserData();
                if (original != null) text.setFill(original);
            }
        }
    });

    nodo.setOnMouseClicked(e -> {
        if (modoEliminarActivo) {
            drawingPane.getChildren().remove(nodo);
            modoEliminarActivo = false;
            drawingPane.setCursor(Cursor.DEFAULT);
            e.consume();
        }
    });
}



@FXML
private void handleMouseClick(MouseEvent event) {
    double x = event.getX();
    double y = event.getY();

    // --- Codigo para hacer los puntos ---
    if (modoPuntoActivo) {
        Circle punto = new Circle(x, y, 3.5, Color.GREEN);
        punto.setStroke(Color.BLACK);
        punto.setStrokeWidth(1.5);

        asignarListenersEliminacion(punto);

        drawingPane.getChildren().add(punto);
        modoPuntoActivo = false;
        drawingPane.setCursor(Cursor.DEFAULT);
        return;
    }

    // --- Codigo para hacer las lineas ---
    if (modoLineaActivo) {
        if (!esperandoSegundoClick) {
            startX = x;
            startY = y;
            esperandoSegundoClick = true;
        } else {
            Line linea = new Line(startX, startY, x, y);
            linea.setStroke(Color.DARKBLUE);
            linea.setStrokeWidth(2.0);

            asignarListenersEliminacion(linea);

            drawingPane.getChildren().add(linea);
            modoLineaActivo = false;
            esperandoSegundoClick = false;
            drawingPane.setCursor(Cursor.DEFAULT);
        }
        return;
    }
    
    // --- Codigo para hacer los arcos ---
if (modoArcoActivo) {
    if (!hacerRadio) {
        arcoStartX = x;
        arcoStartY = y;
        hacerRadio = true;
    } else {
        double dx = x - arcoStartX;
        double dy = y - arcoStartY;
        double radio = Math.sqrt(dx * dx + dy * dy);
        
        Circle centroArco = new Circle(arcoStartX, arcoStartY, 1.5, Color.DARKGREEN);
        Circle arco = new Circle(arcoStartX, arcoStartY, radio);
        arco.setStroke(Color.DARKGREEN);
        arco.setFill(Color.TRANSPARENT);
        arco.setStrokeWidth(2.0);
        centroArco.setStrokeWidth(1.0);
        
        

        asignarListenersEliminacion(arco);
        asignarListenersEliminacion(centroArco);

        drawingPane.getChildren().add(arco);
        drawingPane.getChildren().add(centroArco);
        // Reset estado
        modoArcoActivo = false;
        hacerRadio = false;
        drawingPane.setCursor(Cursor.DEFAULT);
    }
    return;
}

// --- ANOTAR TEXTO ---
if (modoTextoActivo) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Insertar texto");
    dialog.setHeaderText("Introduce el texto que deseas anotar:");
    dialog.setContentText("Texto:");

    Optional<String> resultado = dialog.showAndWait();
    resultado.ifPresent(texto -> {
        Text anotacion = new Text(x, y, texto);
        anotacion.setFill(Color.DARKBLUE);
        anotacion.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        asignarListenersEliminacion(anotacion);

        drawingPane.getChildren().add(anotacion);
    });

    modoTextoActivo = false;
    drawingPane.setCursor(Cursor.DEFAULT);
    return;
}

// --- Codigo para medir distancias ---
if (modoMedirActivo) {
    if (!esperandoSegundoPuntoMedida) {
        medidaX = x;
        medidaY = y;
        esperandoSegundoPuntoMedida = true;
    } else {
        double x2 = x;
        double y2 = y;
        double dx = x2 - medidaX;
        double dy = y2 - medidaY;
        double distanciaPx = Math.sqrt(dx * dx + dy * dy);

        Line medida = new Line(medidaX, medidaY, x2, y2);
        medida.setStroke(Color.DARKMAGENTA);
        medida.setStrokeWidth(2.0);

        double textoX = (medidaX + x2) / 2;
        double textoY = (medidaY + y2) / 2 - 5;
        String textoDistancia = String.format("%.2f px", distanciaPx); // Puedes convertir a millas si sabes la escala
        Text texto = new Text(textoX, textoY, textoDistancia);
        texto.setFill(Color.DARKMAGENTA);
        texto.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        asignarListenersEliminacion(medida);
        asignarListenersEliminacion(texto);

        drawingPane.getChildren().addAll(medida, texto);

        modoMedirActivo = false;
        esperandoSegundoPuntoMedida = false;
        drawingPane.setCursor(Cursor.DEFAULT);
        System.out.println("Distancia medida: " + textoDistancia);
    }
    return;
}

// --- Codigo para marcar extremos ---
if (modoExtremosActivo) {
    double ancho = drawingPane.getWidth();
    double alto = drawingPane.getHeight();

    Line lineaVertical = new Line(x, 0, x, alto);
    lineaVertical.setStroke(Color.GRAY);
    lineaVertical.getStrokeDashArray().addAll(6.0, 6.0); // Línea punteada

    Line lineaHorizontal = new Line(0, y, ancho, y);
    lineaHorizontal.setStroke(Color.GRAY);
    lineaHorizontal.getStrokeDashArray().addAll(6.0, 6.0);

    asignarListenersEliminacion(lineaVertical);
    asignarListenersEliminacion(lineaHorizontal);

    drawingPane.getChildren().addAll(lineaVertical, lineaHorizontal);

    modoExtremosActivo = false;
    drawingPane.setCursor(Cursor.DEFAULT);
    System.out.println("Extremos trazados desde (" + x + ", " + y + ")");
    return;
}




}



@FXML
private void activarModoPunto() {
    modoPuntoActivo = true;
    drawingPane.setCursor(Cursor.CROSSHAIR);
}

@FXML
private void activarModoEliminar() {
    modoEliminarActivo = true;
    drawingPane.setCursor(Cursor.CROSSHAIR);
}

    @FXML
    private void activarBorrarTodo(ActionEvent event) {
        drawingPane.getChildren().removeIf(nodo -> !(nodo instanceof ImageView));
    }

    @FXML
    private void activarModoArco(ActionEvent event) {
        modoArcoActivo = true;
        hacerRadio = false;
        drawingPane.setCursor(Cursor.CROSSHAIR);
    
    }

    @FXML
    private void activarModoTexto(ActionEvent event) {
        modoTextoActivo = true;
        drawingPane.setCursor(Cursor.TEXT);
    }

    @FXML
    private void activarModoMedir() {
        modoMedirActivo = true;
        esperandoSegundoPuntoMedida = false;
        drawingPane.setCursor(Cursor.CROSSHAIR);
        System.out.println("Modo medir distancia ACTIVADO.");
    }

    @FXML
    private void mostrarTransportador(ActionEvent event) {
        transportadorVisible = !transportadorVisible;
        transportadorImg.setVisible(transportadorVisible);
        drawingPane.setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    private void marcarExtremos(ActionEvent event) {
        modoExtremosActivo = true;
        drawingPane.setCursor(Cursor.CROSSHAIR);
        
    }

    @FXML
    private void mostrarRegla(ActionEvent event) {
        reglaVisible = !reglaVisible;
        reglaImg.setVisible(reglaVisible);
        drawingPane.setCursor(Cursor.OPEN_HAND);
    }








    }




