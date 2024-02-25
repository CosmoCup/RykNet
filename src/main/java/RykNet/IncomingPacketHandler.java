package RykNet;

import java.io.DataInputStream;
import java.net.*;
import java.util.ArrayList;

public class IncomingPacketHandler extends Thread {

    boolean isRunning = true;

    ArrayList<Socket> sockets = new ArrayList<>();

    public IncomingPacketHandler() {}
    public void AddSocket(Socket socket) {
        sockets.add(socket);
    }
    public void RemoveSocket(Socket socket) { sockets.remove(socket); }
    public ArrayList<Socket> GetSockets() { return (ArrayList<Socket>) sockets.clone(); }

    public void Stop() {
        isRunning = false;
    }

    public void run() {
        while(isRunning) {
            ArrayList<Socket> Isocket = GetSockets();
            for (Socket socket : Isocket) {
                try {


                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    while (inputStream.available()>0) {
                        String data = inputStream.readUTF();

                        String[] packetSectors = data.split(RykNet.PacketDataBreak());
                        if (packetSectors.length < 2) {
                            RykNet.Print("Error receiving packet from " + socket.getRemoteSocketAddress());
                            RykNet.Print("Probably corrupt data, maybe the Encode() method is weird?");
                            continue;
                        }
                        String packetID = packetSectors[0];
                        String packetData = packetSectors[1];

                        RykNetPacket packet = RykNetPacketManager.GetPacketFromID(packetID);
                        if (packet == null) {
                            RykNet.Print("Received invalid packet from " + socket.getRemoteSocketAddress() );
                            RykNet.Print( "Have you registered the packet? -> RegisterPacket( new <YourPacketHere>() )" );
                            continue;
                        }
                        packet.Decode(packetData);
                        packet.HandlePacket(socket);
                    }


                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }





}
