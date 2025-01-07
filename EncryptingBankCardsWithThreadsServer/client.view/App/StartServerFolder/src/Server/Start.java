package Server;

import serverFX.Server;

public class Start {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.run();
    }
}