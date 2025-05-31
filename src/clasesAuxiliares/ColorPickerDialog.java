package clasesAuxiliares;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ColorPickerDialog {
    public static Color mostrar() {
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Selecciona un color");
        ColorPicker picker = new ColorPicker();
        dialog.getDialogPane().setContent(new VBox(picker));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(b -> b == ButtonType.OK ? picker.getValue() : null);

        return dialog.showAndWait().orElse(Color.BLACK);
    }
}
