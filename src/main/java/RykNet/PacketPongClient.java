package RykNet;

import java.net.Socket;

public class PacketPongClient extends RykNetPacket {

    RykNetClient rykNetClient;
    long timestamp;

    public PacketPongClient(RykNetClient rykNetClient, long timestamp) {
        this.rykNetClient = rykNetClient;
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
        rykNetClient.ReceivePong((int) timeElipsed);
    }


}
