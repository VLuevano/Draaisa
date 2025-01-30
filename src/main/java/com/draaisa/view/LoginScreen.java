package com.draaisa.view;

import com.draaisa.controller.UsuarioController;
import com.draaisa.model.Usuario;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label("Draaisa - Sistema de Inventario");
        Button btnIniciar = new Button("Iniciar sesión");
        Button btnCerrar = new Button("Cerrar");
        
        btnIniciar.setOnAction(e -> mostrarSeleccionUsuario());
        btnCerrar.setOnAction(e -> primaryStage.close());
        
        root.getChildren().addAll(lblTitulo, btnIniciar, btnCerrar);
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.show();
    }

    private void mostrarSeleccionUsuario() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label("Selecciona tu usuario");
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
        primaryStage.setScene(new Scene(root, 300, 400));
    }

    private void mostrarPantallaContrasena(String nombreUsuario) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label("Ingresa tu contraseña");
        PasswordField txtContrasena = new PasswordField();
        Button btnIngresar = new Button("Ingresar");
        
        btnIngresar.setOnAction(e -> {
            if (UsuarioController.iniciarSesion(nombreUsuario, txtContrasena.getText())) {
                mostrarMenuPrincipal();
            } else {
                new Alert(Alert.AlertType.ERROR, "Contraseña incorrecta").show();
            }
        });
        
        root.getChildren().addAll(lblTitulo, txtContrasena, btnIngresar);
        primaryStage.setScene(new Scene(root, 300, 400));
    }

    private void mostrarMenuPrincipal() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        
        Label lblTitulo = new Label("Menú Principal");
        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> mostrarPantallaInicio());
        
        root.getChildren().addAll(lblTitulo, btnCerrarSesion);
        primaryStage.setScene(new Scene(root, 300, 400));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
