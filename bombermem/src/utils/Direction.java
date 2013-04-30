package utils;

import java.awt.geom.Point2D;

/**
 * Enum to define possible movement directions.
 */
public enum Direction
{
    North, /** N - 0� */
    Northeast, /** NE - 45� */
    East, /** S - 90� */
    Southeast, /** SE - 135� */
    South, /** S - 180� */
    Southwest, /** SW - 225� */
    West, /** W - 270� */
    Northwest; /**  NW - 315� */

    /**
     * Convert Key to Direction
     *
     * @param k the key
     * @return the direction
     */
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

    /**
     * Apply movement in a certain direction.
     *
     * @param p the current position (to be modified)
     * @param d the direction to go
     */
    public static void ApplyMovement(Point2D p, Direction d)
    {
        switch (d)
        {
            case East:
                p.setLocation(p.getX() + 1, p.getY());
                break;
            case North:
                p.setLocation(p.getX(), p.getY() - 1);
                break;
            case Northeast:
                p.setLocation(p.getX() + 1, p.getY() - 1);
                break;
            case Northwest:
                p.setLocation(p.getX() - 1, p.getY() - 1);
                break;
            case South:
                p.setLocation(p.getX(), p.getY() + 1);
                break;
            case Southeast:
                p.setLocation(p.getX() + 1, p.getY() + 1);
                break;
            case Southwest:
                p.setLocation(p.getX() - 1, p.getY() + 1);
                break;
            case West:
                p.setLocation(p.getX() - 1, p.getY());
                break;
        }
    }
}
