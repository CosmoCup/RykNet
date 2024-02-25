package RykNet;

import java.util.HashMap;

public class RykNetPacketManager {

    public static HashMap<String, RykNetPacket> Packets = new HashMap<>();

    public static void RegisterPacket(RykNetPacket packet) {
        Packets.put(packet.PacketID(), packet);
    }

    public static RykNetPacket GetPacketFromID(String packetID) {
        return Packets.getOrDefault(packetID, null);
    }

    public static void InitStdPackets(RykNetServer rykNetServer, RykNetClient rykNetClient) {
        RegisterPacket(new PacketMessage(null));

        RegisterPacket(new PacketPingServer(rykNetServer));
        RegisterPacket(new PacketPongServer(0));
        RegisterPacket(new PacketPingClient(rykNetClient));
        RegisterPacket(new PacketPongClient(rykNetClient, 0));

        RegisterPacket(new PacketDisconnectClient(rykNetClient));
        RegisterPacket(new PacketClientDisconnect(rykNetServer, null));
    }


}
