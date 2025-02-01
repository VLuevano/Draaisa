package com.draaisa.view;

import com.draaisa.controller.UsuarioController;
import com.draaisa.model.Usuario;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.List;

public class LoginScreen extends Application {
    private Stage primaryStage;
    private String usuarioActual;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/draaisa_logo.jpg").toExternalForm()));
        mostrarPantallaInicio();
    }

    private void centrarPantalla(Stage stage) {
        stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth()) / 2);
        stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - stage.getHeight()) / 2);
    }

    private void mostrarPantallaInicio() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        ImageView imgLogo = new ImageView(
                new Image(getClass().getResource("/images/draaisa_logo.jpg").toExternalForm()));
        imgLogo.setFitWidth(120);
        imgLogo.setPreserveRatio(true);

        Label lblTitulo = new Label("Draaisa - Sistema de Inventario");
        lblTitulo.getStyleClass().add("titulo");

        Button btnIniciar = new Button("Iniciar sesión");
        Button btnCerrar = new Button("Cerrar");

        btnIniciar.setOnAction(e -> mostrarSeleccionUsuario());
        btnCerrar.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(imgLogo, lblTitulo, btnIniciar, btnCerrar);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.setResizable(false);
        primaryStage.show();
        centrarPantalla(primaryStage);
    }

    private void mostrarSeleccionUsuario() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        Label lblTitulo = new Label("Selecciona tu usuario");
        lblTitulo.getStyleClass().add("titulo");

        ListView<String> listaUsuarios = new ListView<>();
        List<Usuario> usuarios = UsuarioController.consultarUsuarios();

        for (Usuario usuario : usuarios) {
            listaUsuarios.getItems().add(usuario.getNombreUsuario());
        }

        Button btnSeleccionar = new Button("Seleccionar");
        btnSeleccionar.setOnAction(e -> {
            String nombreUsuario = listaUsuarios.getSelectionModel().getSelectedItem();
            if (nombreUsuario != null) {
                mostrarPantallaContrasena(nombreUsuario);
            }
        });

        root.getChildren().addAll(lblTitulo, listaUsuarios, btnSeleccionar);
        primaryStage.setScene(scene);
        centrarPantalla(primaryStage);
    }

    private void mostrarPantallaContrasena(String nombreUsuario) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
    
        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
    
        Label lblTitulo = new Label("Ingresa tu contraseña");
        lblTitulo.getStyleClass().add("titulo");
    
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");
    
        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setOnAction(e -> {
            if (UsuarioController.iniciarSesion(nombreUsuario, txtContrasena.getText())) {
                usuarioActual = nombreUsuario;
                MenuPrincipalScreen menuPrincipalScreen = new MenuPrincipalScreen(primaryStage, usuarioActual);
                menuPrincipalScreen.mostrarMenu();
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Contraseña incorrecta");
                alerta.getDialogPane().setPrefSize(250, 100);
                alerta.show();
            }
        });
    
        root.getChildren().addAll(lblTitulo, txtContrasena, btnIngresar);
        primaryStage.setScene(scene);
        centrarPantalla(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
