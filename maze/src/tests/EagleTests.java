package tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import logic.Architect;
import logic.DefaultMazeGenerator;
import logic.DragonBehaviour;
import logic.Maze;
import logic.MazeGenerator;
import logic.Wall;
import model.Position;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Key;

public class EagleTests
{
    private static Architect _architect;
    private static Maze _maze;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MazeGenerator mg = new DefaultMazeGenerator();

        _architect = new Architect();
        _architect.SetMazeGenerator(mg);
    }

    @Before
    public void setUpBeforeTest() /* throws Exception */
    {
        _architect.ConstructMaze(10, 1, DragonBehaviour.Idle);
        _maze = _architect.GetMaze();
    }

    @Test
    public void EagleInitiallyOnHeroTest()
    {
        // inicialmente a �guia est� poisada no bra�o do her�i e acompanha-o;
        assertThat(_maze.GetEaglePosition(), is(_maze.GetHeroPosition()));
        assertThat(_maze.MoveHero(Key.RIGHT), is(true));
        assertThat(_maze.GetEaglePosition(), is(_maze.GetHeroPosition()));
    }
    
    @Test
    public void EagleFlightTest()
    {
        // por ordem do her�i, a �guia pode levantar voo em dire��o � espada,
        //  pelo caminho mais pr�ximo poss�vel de uma linha reta;

        Key[] movements1 = {
            Key.RIGHT, Key.RIGHT, Key.RIGHT
        };

        for (Key k : movements1)
        {
            _maze.MoveHero(k);
            _maze.Update();
        }

        _maze.SendEagleToSword();

        _maze.Update();
        _maze.Update();
        
        assertThat(_maze.GetEaglePosition(), is(not(_maze.GetHeroPosition())));

        // quando est� a voar, a �guia pode estar sobre qualquer quadr�cula;
        _maze.Update();
        _maze.Update();

        assertThat(_maze.GetCell(_maze.GetEaglePosition()).GetValue(), is(instanceOf(Wall.class)));
    }
    
    @Test
    public void EagleReachSwordTest()
    {
        // quando chega � quadr�cula da espada, a �guia desce para apanhar a espada (se ainda a� estiver);
        
        _maze.SendEagleToSword();
        
        for (int i = 0; i < 9; ++i)
            _maze.Update();

        assertThat(_maze.IsEagleAlive(), is(true));
        assertThat(_maze.IsEagleArmed(), is(true));
    }
    
    @Test
    public void EagleReachUnexistingSwordTest()
    {
        // quando chega � quadr�cula da espada, a �guia desce para apanhar a espada (se ainda a� estiver);
        
        _maze.SendEagleToSword();

        for (int i = 0; i < 4; ++i)
            _maze.Update();
        
        _maze.SetSwordPosition(new Position(-1, -1));
        
        for (int i = 0; i < 5; ++i)
            _maze.Update();
        
        assertThat(_maze.IsEagleAlive(), is(true));
        assertThat(_maze.IsEagleArmed(), is(false));
    }
    
    @Test
    public void EagleReachSwordWithDragonTest()
    {
        // quando chega � quadr�cula da espada, a �guia desce para apanhar a espada (se ainda a� estiver); 
        //  se um drag�o estive acordado nessa posi��o ou adjacente, mata a �guia;

        _maze.SetDragonPosition(0, new Position(1, 7));

        _maze.SendEagleToSword();
        
        for (int i = 0; i < 9; ++i)
            _maze.Update();

        assertThat(_maze.IsEagleAlive(), is(false));
    }
    
    @Test
    public void EagleFlightBackTest()
    {
        // assim que pega a espada, a �guia levanta voo de novo em dire��o � posi��o de partida
        //  (onde levantou voo do bra�o do her�i);

        Position takeOffPos = _maze.GetEaglePosition();
        
        _maze.SendEagleToSword();
        
        //_maze.MoveHero(Key.RIGHT);
        
        for (int i = 0; i < 9; ++i)
            _maze.Update();

        assertThat(_maze.IsEagleAlive(), is(true));
        assertThat(_maze.IsEagleArmed(), is(true));
        
        for (int i = 0; i < 7; ++i)
            _maze.Update();

        assertThat(_maze.GetEaglePosition(), is(takeOffPos));
        assertThat(_maze.IsEagleAlive(), is(true));
        assertThat(_maze.IsEagleArmed(), is(true));
        
        _maze.Update();
        
        assertThat(_maze.IsEagleArmed(), is(false));
        assertThat(_maze.IsHeroArmed(), is(true));
    }
    
    @Test
    public void EagleFlightBackCatchByHeroTest()
    {
        // voltando � posi��o de partida, se n�o estiver a� o her�i, a �guia permanece no solo at� o 
        //  her�i a apanhar, correndo o risco de ser morta por um drag�o.
        
        _maze.SendEagleToSword();
        
        _maze.MoveHero(Key.RIGHT);
        
        for (int i = 0; i < 20; ++i)
            _maze.Update();
        
        _maze.MoveHero(Key.LEFT);
        _maze.Update();
        
        assertThat(_maze.IsEagleAlive(), is(true));
        assertThat(_maze.IsEagleArmed(), is(false));
        assertThat(_maze.IsHeroArmed(), is(true));
    }
    
    @Test
    public void EagleFlightBackWithDragon()
    {
        // voltando � posi��o de partida, se n�o estiver a� o her�i, a �guia permanece no solo at� o 
        //  her�i a apanhar, correndo o risco de ser morta por um drag�o.
        
        _maze.SendEagleToSword();
        
        _maze.MoveHero(Key.RIGHT);
        _maze.MoveHero(Key.RIGHT);
        
        for (int i = 0; i < 10; ++i)
            _maze.Update();
        
        _maze.MoveDragon(0, Key.UP);
        
        for (int i = 0; i < 8; ++i)
            _maze.Update();
        
        assertThat(_maze.IsEagleAlive(), is(false));
    }
}
