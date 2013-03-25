package logic;

import utils.Key;

public enum Direction
{
    North, // N - 0�
    Northeast, // NE - 45�
    East, // S - 90�
    Southeast, // SE - 135�
    South, // S - 180�
    Southwest, // SW - 225�
    West, // W - 270�
    Northwest; // NW - 315�

    public static Direction FromKey(Key k)
    {
        switch (k)
        {
            case DOWN:
                return Direction.South;
            case LEFT:
                return Direction.West;
            case RIGHT:
                return Direction.East;
            case UP:
                return Direction.North;
            default:
                return null;
        }
    }
}
