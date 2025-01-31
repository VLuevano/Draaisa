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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;

public class LoginScreen extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mostrarPantallaInicio();
    }

    private void mostrarPantallaInicio() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        ImageView imgLogo = new ImageView(new Image(getClass().getResource("/images/draaisa_logo.jpg").toExternalForm()));
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
        primaryStage.show();
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
                mostrarMenuPrincipal();
            } else {
                new Alert(Alert.AlertType.ERROR, "Contraseña incorrecta").show();
            }
        });

        root.getChildren().addAll(lblTitulo, txtContrasena, btnIngresar);
        primaryStage.setScene(scene);
    }

    private void mostrarMenuPrincipal() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        Label lblTitulo = new Label("Menú Principal");
        lblTitulo.getStyleClass().add("titulo");

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> mostrarPantallaInicio());

        root.getChildren().addAll(lblTitulo, btnCerrarSesion);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
