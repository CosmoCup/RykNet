package RykNet;

import java.net.Socket;

public abstract class RykNetPacket {

    public abstract String PacketID();
    public abstract String Encode();
    public abstract void Decode(String data);
    public abstract void HandlePacket(Socket socket);

}
