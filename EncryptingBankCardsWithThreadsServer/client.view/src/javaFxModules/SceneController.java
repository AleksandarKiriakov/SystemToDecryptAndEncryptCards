package javaFxModules;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SceneController extends Application {

    private Stage stage;
    @FXML
    private TextField CardNumberField;
    @FXML
    private TextField txtDecrypt;

    @FXML
    private TextField txtEncrypt;
    @FXML
    private TextField PassField;
    private static final String chatServer = "localhost";
    private static final int portNumber = 4444;
    @FXML
    private TextField UserNameField;
    private SocketSingelton singelton = SocketSingelton.getInstance();
    private Parent root;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        File file = new File("/src/loginScreen.fxml");
        if (file.exists()) {
            System.out.println("problem with file");
        }
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource(file.getName())));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.sizeToScene();
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private void connectToServer() throws IOException {
        Socket client = new Socket("localhost", portNumber);
        singelton.client = client;
        singelton.dataOutputStream = new DataOutputStream(singelton.client.getOutputStream());
        singelton.dataInputStream = new DataInputStream(singelton.client.getInputStream());
    }

    @FXML
    public void switchToRegister(ActionEvent event) {

        try {
            File file = new File("/src/registerScreen.fxml");
            if (file.exists()) {
                System.out.println("problem with file");
            }
            root = FXMLLoader.load(getClass().getClassLoader().getResource(file.getName()));
        } catch (IOException e) {
            e.getStackTrace();
        }
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    @FXML
    public void buttonRegisterCheck(ActionEvent event) {
        try {
            connectToServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            if (!UserNameField.getText().isEmpty() &&  !PassField.getText().isEmpty()
                    && !CardNumberField.getText().isEmpty()) {

                singelton.setName(UserNameField.getText());
                singelton.dataOutputStream.writeUTF("register " + singelton.getName() + " "
                        + PassField.getText() + " "
                        + CardNumberField.getText() + " " + "false");
                singelton.dataOutputStream.flush();
                try {
                    if (singelton.dataInputStream.readUTF().equals("close")) {
                        btnQuitClicked(event);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                switchToEncryptDecryptScene(event);

            }else{
                UserNameField.setText("write all");
                PassField.setText("write all");
                CardNumberField.setText("write all");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void buttonLoginCheck(ActionEvent event) {

        try {
            connectToServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            singelton.setName(UserNameField.getText());
            singelton.dataOutputStream.writeUTF("login " + singelton.getName() + " " + PassField.getText());
            singelton.dataOutputStream.flush();
        } catch (IOException E) {
            E.getStackTrace();
        }

        try {
            String s = singelton.dataInputStream.readUTF();
            if (s.equals("true")) {
                switchToAdmin(event);
            } else if (s.equals("close")) {
                btnQuitClicked(event);
            } else {
                switchToEncryptDecryptScene(event);
            }
        } catch (IOException E) {
            E.getStackTrace();
        }


    }

    @FXML
    public void switchToAdmin(ActionEvent event) {

        try {
            File file = new File("/src/admin.fxml");
            if (file.exists()) {
                System.out.println("problem with file");
            }
            root = FXMLLoader.load(getClass().getClassLoader().getResource(file.getName()));
        } catch (IOException e) {
            e.getStackTrace();
        }
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.show();
    }

    @FXML
    public void switchToEncryptDecryptScene(ActionEvent event) {

        try {
            File file = new File("/src/encryptDecryptScreen.fxml");
            if (file.exists()) {
                System.out.println("problem with file");
            }
            root = FXMLLoader.load(getClass().getClassLoader().getResource(file.getName()));
        } catch (IOException e) {
            e.getStackTrace();
        }
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("EncryptOrDecrypt");
        stage.show();
    }

    @FXML
    void btnDecryptClicked(ActionEvent event) {
        try {
            if (!txtEncrypt.getText().isEmpty()) {
                singelton.dataOutputStream.writeUTF("decrypt " + singelton.getName() + " " + txtEncrypt.getText());
                singelton.dataOutputStream.flush();
                txtDecrypt.setText(singelton.dataInputStream.readUTF());
            }else{
                txtEncrypt.setText("write card number");
            }

        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    @FXML
    void btnEncryptClicked(ActionEvent event) {
        try {
            if (!txtDecrypt.getText().isEmpty()) {
                if (singelton.getNumberCard().equals("not your card") ||singelton.getNumberCard().isEmpty()
                        || singelton.getNumberCard().isBlank() ) {
                    singelton.dataOutputStream.writeUTF("encrypt " + singelton.getName() + " " + txtDecrypt.getText());
                    singelton.dataOutputStream.flush();
                    singelton.setNumberCard(singelton.dataInputStream.readUTF());
                    txtEncrypt.setText( singelton.getNumberCard());
                }else{
                    txtEncrypt.setText( singelton.getNumberCard());
                }

            }else{
                txtDecrypt.setText("write card number");
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @FXML
    void btnQuitClicked(ActionEvent event) {
        try {
            singelton.dataOutputStream.writeUTF("quit");
            singelton.dataOutputStream.flush();
            singelton.client.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
