package logic;

import model.Position;
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

    public static void ApplyMovement(Position p, Direction d)
    {
        switch (d)
        {
            case East:
                p.X++;
                break;
            case North:
                p.Y--;
                break;
            case Northeast:
                p.X++;
                p.Y--;
                break;
            case Northwest:
                p.X--;
                p.Y--;
                break;
            case South:
                p.Y++;
                break;
            case Southeast:
                p.X++;
                p.Y++;
                break;
            case Southwest:
                p.X--;
                p.Y++;
                break;
            case West:
                p.X--;
                break;
        }
    }
}
