package RykNet;

public class RykNet {

    public static void Print(String msg) {
        System.out.println("[RykNet] " + msg);
    }

    public static String PacketDataBreak() {
        return ";";
    }

    public enum DisconnectReason {
        QUIT,
        TIMEOUT,
        SERVER_STOP
    }

}
