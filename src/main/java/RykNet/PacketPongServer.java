package RykNet;

import java.net.Socket;

public class PacketPongServer extends RykNetPacket {

    long timestamp;

    public PacketPongServer(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String PacketID() { return "pong_server"; }

    @Override
    public String Encode() {
        return Long.toString(timestamp);
    }

    @Override
    public void Decode(String data) {
        timestamp = Long.parseLong(data);
    }

    @Override
    public void HandlePacket(Socket socket) {
        long timeElipsed = System.currentTimeMillis() - timestamp;
        String socketIP = socket.getRemoteSocketAddress().toString();
        RykNet.Print("Ping to " + socketIP + " took " + timeElipsed + "ms");
    }


}
