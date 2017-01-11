/**
 * Provides IO communication for a client or server socket
 * Created by Max Buster
 */

package utils.IO;

import utils.Constants.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketIO {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketIO(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Loops searching the input stream for message delimiter, returns once it is found
     * @throws IOException - On read error
     * @throws InterruptedException - On thread sleep error
     */
    public void wait_for_message() throws IOException, InterruptedException {
        int message = in.readInt();
        while (message != Constants.MESSAGE_START) {
            Thread.sleep(50);
            message = in.readInt();
        }
    }

    /**
     * @param message_type - The constant type defined according to the message protocol
     * @param message_body - The object contents of the message
     * @throws IOException - On write error
     */
    public void write_message(int message_type, Object message_body) throws IOException {
        out.writeInt(message_type);
        out.writeObject(message_body);
    }

    /**
     * @return - The integer message type
     * @throws IOException - On read error
     */
    public int read_message_type() throws IOException {
        return in.readInt();
    }

    /**
     * @return - A generic object containing the message body
     * @throws IOException - On write error
     * @throws ClassNotFoundException - On error deserialize the message body
     */
    public Object read_message_body() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    /**
     * Attempts to close the socket connection
     */
    public void close_socket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
