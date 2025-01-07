package serverFX;

import javafx.application.Application;
import javafx.stage.Stage;
import serverFX.command.Command;
import serverFX.command.CommandCreator;
import serverFX.command.CommandExecutor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    ServerSocket server;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    DataSingleton dataSingleton  = DataSingleton.getInstance();
    private int counter = 1;
    private ExecutorService threadExecutor;

    public void runServer() {

        try // set up server to receive connections; process connections
        {
           // server = new ServerSocket(33333,100);
            server = new ServerSocket(4444);
           // create ServerSocket
            // end while
            while (true) {
                waitForConnection(); // wait for a connection
            }
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // end catch
    } // end method runServer

    private void waitForConnection() throws IOException {
        Socket s = server.accept();
        RunClient client = new RunClient(s, counter);
        threadExecutor.execute(client);

        counter++;
    } // end method waitForConnection

    @Override
    public void run() {
        threadExecutor = Executors.newCachedThreadPool();
        new Thread(this::runServer).start();
    }

    public class RunClient implements Runnable {

        private DataOutputStream output; // output stream to client
        private DataInputStream input; // input stream from client
        private final Socket connection; // connection to client
        private final int counter;
        private Executor commandExecutor;

        public RunClient(Socket connection, int counter) throws IOException {
            this.connection = connection;
            this.counter = counter;
            this.input = new DataInputStream(connection.getInputStream());
            this.output = new DataOutputStream(connection.getOutputStream());

        }

        @Override
        public void run() {

            try {
                processConnection();
            }
            catch (IOException eofException) {

            }
        }

        private void processConnection() throws IOException {
            String message = "";
            CommandExecutor commandExecutor = new CommandExecutor();
            do
            {
                    message = input.readUTF();

                    Command newCommand = CommandCreator.newCommand(message);
                    String reply = new String(commandExecutor.execute(newCommand).getBytes(), "UTF-8");

                    output.writeUTF(reply);
                    output.flush();

            } while (!message.equals("quit"));
            input.close();
            output.close();
            connection.close();
        }
    }
}
