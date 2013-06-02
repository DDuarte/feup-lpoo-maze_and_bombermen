package pt.up.fe.pt.lpoo.bombermen;

import java.awt.Point;

import pt.up.fe.pt.lpoo.bombermen.messages.SMSG_SPAWN;
import pt.up.fe.pt.lpoo.bombermen.messages.SMSG_SPAWN_WALL;

public class Wall extends Entity
{
    public Wall(int guid, Point pos)
    {
        super(TYPE_WALL, guid, pos);
    }

    @Override
    public SMSG_SPAWN GetSpawnMessage()
    {
        return new SMSG_SPAWN_WALL(GetGuid());
    }
}
