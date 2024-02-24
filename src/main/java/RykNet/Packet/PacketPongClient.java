package RykNet.Packet;

import RykNet.*;
import java.net.Socket;

public class PacketPongClient extends RykNetPacket {

    long timestamp;

    public PacketPongClient(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String PacketID() { return "pong_client"; }

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
        RykNet.Print("Ping took " + timeElipsed + "ms");
    }


}
