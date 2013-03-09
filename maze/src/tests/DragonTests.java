package tests;

import static org.junit.Assert.*;
import logic.Architect;
import logic.DefaultMazeGenerator;
import logic.DragonBehaviour;
import logic.Maze;
import model.Position;

import org.junit.BeforeClass;
import org.junit.Test;

import utils.Key;
import utils.RandomEngine;

public class DragonTests
{
    private static Architect _architect;
    private static Maze _maze;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _architect = new Architect();
    }

    @Test
    public void MoveDragonSuccessTest()
    {

        _architect.SetMazeGenerator(new DefaultMazeGenerator());
        _architect.ConstructMaze(10, 1, DragonBehaviour.Idle);
        _maze = _architect.GetMaze();
        assertEquals(new Position(1, 3), _maze.GetDragonPosition(0));
        assertTrue(_maze.MoveDragon(0, Key.UP));
        assertEquals(new Position(1, 2), _maze.GetDragonPosition(0));
    }

    @Test
    public void MoveDragonFailureTest()
    {

        _architect.SetMazeGenerator(new DefaultMazeGenerator());
        _architect.ConstructMaze(10, 1, DragonBehaviour.Idle);
        _maze = _architect.GetMaze();
        assertEquals(new Position(1, 3), _maze.GetDragonPosition(0));
        assertFalse(_maze.MoveDragon(0, Key.LEFT));
        assertEquals(new Position(1, 3), _maze.GetDragonPosition(0));
    }

    @Test
    public void DragonSleepTest()
    {

        _architect.SetMazeGenerator(new DefaultMazeGenerator());
        _architect.ConstructMaze(10, 1, DragonBehaviour.Sleepy);
        _maze = _architect.GetMaze();
        assertFalse(_maze.IsDragonSleeping(0));
        _maze.SetDragonToSleep(0, 2);
        assertTrue(_maze.IsDragonSleeping(0));
        Position DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(DragPos, _maze.GetDragonPosition(0));
        assertTrue(_maze.IsDragonSleeping(0));
        _maze.Update();
        assertEquals(DragPos, _maze.GetDragonPosition(0));
        assertFalse(_maze.IsDragonSleeping(0));
    }

    @Test
    public void DragonRandomMovementTestWithHeroDeadAtEnd()
    {

        _architect.SetMazeGenerator(new DefaultMazeGenerator());
        _architect.ConstructMaze(10, 1, DragonBehaviour.RandomMovement);
        _maze = _architect.GetMaze();
        RandomTester r = new RandomTester();
        RandomEngine.SetRandom(r);

        r.PushInt(0);
        Position DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(DragPos, _maze.GetDragonPosition(0));

        r.PushInt(2);
        DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(new Position(DragPos.X, DragPos.Y + 1), _maze.GetDragonPosition(0));

        r.PushInt(3);
        DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(DragPos, _maze.GetDragonPosition(0));

        r.PushInt(3);
        DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(DragPos, _maze.GetDragonPosition(0));

        r.PushInt(1);
        DragPos = _maze.GetDragonPosition(0);
        _maze.Update();
        assertEquals(new Position(DragPos.X, DragPos.Y - 1), _maze.GetDragonPosition(0));

        r.PushInt(1);
        DragPos = _maze.GetDragonPosition(0);
        assertTrue(_maze.IsHeroAlive());
        _maze.Update();
        assertEquals(new Position(DragPos.X, DragPos.Y - 1), _maze.GetDragonPosition(0));
        assertFalse(_maze.IsHeroAlive());
        assertTrue(_maze.IsDragonAlive(0));
    }

    @Test
    public void DragonRandomMovementTestWithHeroAliveAtEnd()
    {
        _architect.SetMazeGenerator(new DefaultMazeGenerator());
        _architect.ConstructMaze(10, 1, DragonBehaviour.RandomMovement);
        _maze = _architect.GetMaze();
        RandomTester r = new RandomTester();
        RandomEngine.SetRandom(r);

        r.PushInt(1);
        Position DragPos = _maze.GetDragonPosition(0);
        assertTrue(_maze.IsHeroAlive());
        _maze.EquipHero();
        assertTrue(_maze.IsHeroArmed());
        _maze.Update();
        assertEquals(new Position(DragPos.X, DragPos.Y - 1), _maze.GetDragonPosition(0));
        assertTrue(_maze.IsHeroAlive());
        assertFalse(_maze.IsDragonAlive(0));
    }

}