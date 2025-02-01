package com.draaisa.view;

import com.draaisa.controller.UsuarioController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuPrincipalScreen {
    private Stage primaryStage;
    private String usuarioActual;

    public MenuPrincipalScreen(Stage primaryStage, String usuarioActual) {
        this.primaryStage = primaryStage;
        this.usuarioActual = usuarioActual;
    }

    public void mostrarMenu() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        MenuBar menuBar = new MenuBar();
        Menu menuArchivo = new Menu("Archivo");
        MenuItem cerrarSesion = new MenuItem("Cerrar Sesión");
        cerrarSesion.setOnAction(e -> mostrarPantallaInicio());
        menuArchivo.getItems().add(cerrarSesion);

        Menu menuProductos = new Menu("Productos");
        Menu menuProveedores = new Menu("Proveedores");
        MenuItem abrirProveedores = new MenuItem("Gestión de Proveedores");
        abrirProveedores.setOnAction(e -> {
            // Crea una instancia de ProveedorView pasando el usuario actual
            new ProveedorView(usuarioActual).start(primaryStage);
        });
        menuProveedores.getItems().add(abrirProveedores);
        
        Menu menuClientes = new Menu("Clientes");
        Menu menuFabricantes = new Menu("Fabricantes");
        Menu menuPrestadores = new Menu("Prestadores de Servicios");

        Menu menuUsuarios = new Menu("Usuarios");
        if (UsuarioController.tienePermiso(usuarioActual)) {
            menuUsuarios.getItems().add(new MenuItem("Gestión de Usuarios"));
        }

        menuBar.getMenus().addAll(menuArchivo, menuProductos, menuProveedores, menuClientes, menuFabricantes, menuPrestadores,
                menuUsuarios);

        VBox centro = new VBox(15);
        centro.setAlignment(Pos.CENTER);

        ImageView imgLogo = new ImageView(
                new Image(getClass().getResource("/images/draaisa_logo.jpg").toExternalForm()));
        imgLogo.setFitWidth(150);
        imgLogo.setPreserveRatio(true);

        Label lblBienvenida = new Label("Bienvenido, " + usuarioActual);
        lblBienvenida.setFont(new Font(20));

        centro.getChildren().addAll(imgLogo, lblBienvenida);
        root.setTop(menuBar);
        root.setCenter(centro);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Menú Principal - Draaisa");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void mostrarPantallaInicio() {
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.start(primaryStage);
    }
}
