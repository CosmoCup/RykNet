package RykNet.Packet;

import RykNet.*;
import java.net.Socket;

public class PacketDisconnectClient extends RykNetPacket {

    RykNetClient rykNetClient;

    public PacketDisconnectClient(RykNetClient rykNetClient) {
        this.rykNetClient = rykNetClient;
    }

    @Override
    public String PacketID() { return "disconnect_client"; }

    @Override
    public String Encode() {
        return null;
    }

    @Override
    public void Decode(String data) {  }

    public void HandlePacket(Socket socket) {
        rykNetClient.Disconnect();
    }
}
