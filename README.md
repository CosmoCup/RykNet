
# RykNet

A simple and versatile multi-purpose Java-based networking protocol interface

# Dependency

< Maven repo coming soon :^) >

# How to use

## Creating a Client

To create a client object, simply make a new class, which we will call `Client`, and extend `RykNetClient`. Its that simple! To connect to a server, just call `TryConnectToServer(ip, port)`, and RykNet will do all the work for you. The client may `Disconnect()` at any time.

### Example Client class

```
public class Client extends RykNetClient {

    Client() {
        String ip = "localhost";
        int port = 6969;

        boolean connectionSuccessful = TryConnectToServer(ip, port);
    }

}
```

## Creating a Server

Creating a server object uses the same format as the client. Make a new class, that we will call `Server`, and extend `RykNetServer`. In the constructor, you will need to call its `super(port)` constructor to set the port of the server. 

When you want to start accepting connections from clients, just call `StartAcceptingConnections()` and the server will open for any client to connect. You can `StopAcceptingConnections()` at any time to make sure no more clients connect. You can also call `StopServer()` to close the server completelly.

### Example Server class

```
public class Server extends RykNetServer {

    public Server() {
        super(6969);
        StartAcceptingConnections();
    }

}
```

## Creating Packets

Creating packets is fairly simple. For this example we will make a packet that will send a message between the client and the server. 

Make a new class for your packet, We will call the class `PacketSendMessage`, and you extend `RykNetPacket` class. From this you will need to implement some methods : 

### Constructor
Technically a packet constructor isn't necessary, but you'll probably need one. Here you would pass in any data you would send along with the packet. For this example, we would want to include the text message to send the the client or server, so we would write it as so : 

```
public PacketSendMessage(String newMessage) { 
    message = newMessage; 
}
```

The `message` variable in this class would be declared in the class,  and can be private : 

```
private String message;
```

### PacketID Method
Every packet you create will need a unique ID so that the receiver knows what kind of packet it has received. The packet ID can be anything you want, as long as it is not the same as another packets ID. You state the packet ID by returning a string from this method : 

```
@Override
public String PacketID() { 
    return "send_message_id"; 
}
```

### Encode Method
Before a packet is sent to the receiver, it needs to encode the data you want to send into a single string. Since the data we want to send is already a string, we can just pass in the message itself :

```
@Override
public String Encode() {
    return message;
}
```

### Decode Method
When a packet is received, it must be decoded from the encoded string format we stated, back into data. Once again, since the data we are receiving is already meant to be a string, we can simply re-assign the `message` variable to the received data : 

```
@Override
public void Decode(String data) {
    message = data;
}
```

### HandlePacket Method 
Here is where we decide what to do when we receive the packet. This can obviously be whatever you need, but in this case, we want to print out the received message to the console : 

```
public void HandlePacket(Socket socket) {
    String senderIP = socket.getRemoteSocketAddress();
    System.out.println( "[ Message from " +  senderIP + " ] -> " + message);
}
```

The `Socket` that is passed in is the reference to the network socket we received the packet from, use this as you like. In this case, we are getting the IP of the sender so we know who we recieved he packet from.

If there is any class you need to reference inside of this class in the `HandlePacket` method, declare it as a variable and pass it in through the constructor.

### Example Packet Class
Putting all of this together, we would end up with a class like this : 
```
public class PacketSendMessage extends RykNetPacket {

    String message;

    public PacketSendMessage(String newMessage) { 
        message = newMessage; 
    }

    @Override
    public String PacketID() { 
        return "send_message_id"; 
    }

    @Override
    public String Encode() {
        return message;
    }

    @Override
    public void Decode(String data) {
        message = data;
    }

    public void HandlePacket(Socket socket) {
        String senderIP = socket.getRemoteSocketAddress();
        System.out.println( "[ Message from " +  senderIP + " ] -> " + message);
    }
}

```

### Registering the Packet
Once your class is created, RykNet needs to know that your packet exists. You do this through the static `RykNetPacketManager` class and calling :
```
RykNetPacketManager.RegisterPacket( new PacketSendMessage(null) )
```
This would be done outside of the packet class, before any connections between a client and server are created.

We pass null as the `message` parameter during registration as it will be populated either when creating the packet when we send it, or during decoding when it is received. 

### Side Note
You do not need to create the `PacketSendMessage` class in your project, this was only given as an example. A packet to send messages already exists as `PacketMessage(String message)` and functions the exact same.

## Sending Packets
### Sending from the Client to the Server
To send a packet to the server, inside the `Client` class we call :
```
SendPacket( new PacketSendMessage("You like jazz.?"));
```
The client already knows it is sending it to the server, so we dont need to specify where or who we are sending it to.

### Sending from the Server to a Client
To send a packet to the server, we need to know the client socket that we want to send it to. This would be handled on your end, but as an example, lets say we have a List of sockets that are connected to our server :

```
List<Socket> clientSockets = new List<Socket>();
```

Once we have the socket we want to send the packet to, we can call : 

```
SendPacket(clientSocket, new PacketSendMessage("I do like jazz"));
```


## Example Classes
I've provided some example classes you can use to start your project. As long as RykNet is imported correctly, these should work out of the box.

### Client 

```
import RykNet.*;

public class Client extends RykNetClient {

    Client() {
        String ip = "localhost";
        int port = 6969;

        boolean connectionSuccessful = TryConnectToServer(ip, port);

        if (connectionSuccessful) {
            SendPacket( new PacketMessage("You like jazz.?"));
        }
    }

}
```

### Server 

```
import RykNet.*;
import java.net.*;

public class Server extends RykNetServer {

    public Server() {
        super(6969);
        StartAcceptingConnections();
    }


    @Override
    protected void OnClientConnected(Socket clientSocket) {
        SendPacket(clientSocket, new PacketMessage("I do love jazz"));
    }

    @Override
    protected void OnClientDisconnected(Socket clientSocket, RykNet.DisconnectReason reason) {

    }
}
```

# Other Important Info

## RykNetClient Methods

`boolean TryConnectToServer(String ip, int port)` Attempts a connection to the specified server. Returns true if successful, false otherwise.

`int GetCurrentPing()` Returns the last recorded ping to the server. Pings are handles automatically and there is no need to ping the server yourself.

`void Disconnect()` Disconnects the client from the server.

`void SendPacket(RykNetPacket packet)` Sends a packet to the connected server.

## RykNetServer Methods

`void StopServer()` Disconnects all clients and stops the server.

`void DisconnectClient(Socket clientSocket)` Disconnects a client from the server.

`void StartAcceptingConnections()` Opens the server to allow new connections.

`void StopAcceptingConnections()` Closes the server to block new connection.

`void SendPacket(Socket clientSocket, RykNetPacket packet)` Sends a packet to the client.

## RykNetServer Events
These are implemented automatically, and are called when triggered by RykNet. These are meant to be overriden to your needs.

`void OnClientConnected(Socket clientSocket)` Called when a new client connects to the server.

`void OnClientDisconnected(Socket clientSocket, RykNet.DisconnectReason reason)` Called when a client disconnects, both when initiated by the client or by the server.

## Built-in Packets
### Usable Packets -> You can use these at will
`PacketMessage(String message)` Will send a message to either the client or the server.

### Un-usable Packets -> Used internally by RykNet and are not meant to be used elsewhere. Use with caution, or preferably not at all

`PacketPingClient(RykNetClient client)` Will send a request to the client to pong the server.

`PacketPingServer(RykNetServer server)` Will send a request to the server to pong the client.

`PacketPongClient(RykNetClient client, long timestamp)` Will send the requested ping to the server back to the client.

`PacketPongServer(long timestamp)` Will send the requested ping to the client back to the server.

`PacketClientDisconnect(RykNetServer server, DisconnectReason reason)` Sent to the server when the client calls `Disconnect()`.

`PacketDisconnectClient(RykNetClient client)` Sent to the client when the server calls `DisconnectClient(Socket client, DisconnectReason reason)`.
