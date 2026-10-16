package edu.uam.ni.evaluacion;

import edu.uam.ni.evaluacion.controller.ProductoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        URL rutaFXML = Main.class.getResource(
                "/edu/uam/ni/evaluacion/vista/producto-view.fxml"
        );

        if (rutaFXML == null) {
            throw new IOException(
                    "No se encontró producto-view.fxml. Revisa la carpeta src/main/resources."
            );
        }

        FXMLLoader cargador = new FXMLLoader(rutaFXML);
        Scene escena = new Scene(cargador.load(), 1150, 720);

        stage.setTitle("Papelería Campus - Catálogo de productos");
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.setScene(escena);
        stage.show();

        ProductoController controller = cargador.getController();
        controller.cargarProductosIniciales();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
