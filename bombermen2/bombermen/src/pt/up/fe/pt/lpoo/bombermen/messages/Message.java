package pt.up.fe.pt.lpoo.bombermen.messages;

import java.io.Serializable;

public abstract class Message implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static final int SMSG_PING = 0;
    public static final int SMSG_SPAWN = 1;
    public static final int CMSG_MOVE = 2;
    public static final int CMSG_PLACE_BOMB = 3;

    public final int Type;

    public Message(int type)
    {
        Type = type;
    }
}
