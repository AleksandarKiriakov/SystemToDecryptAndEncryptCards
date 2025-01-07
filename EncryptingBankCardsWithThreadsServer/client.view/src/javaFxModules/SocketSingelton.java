package javaFxModules;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketSingelton {
    private static SocketSingelton instance = new SocketSingelton();
    String name;
    String numberCard ="";
    Socket client;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    public static SocketSingelton getInstance() {
        return instance;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public Socket getClient() {
        return client;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setdOut() throws IOException {
        this.dataOutputStream = new DataOutputStream(client.getOutputStream());
    }


    public void setClient(Socket client) {
        this.client = client;
    }


}
