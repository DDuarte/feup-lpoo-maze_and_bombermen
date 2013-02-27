package logic;

import java.util.ArrayList;
import java.util.Random;

import model.Cell;
import model.Grid;
import utils.Key;
import utils.Pair;

public class Maze
{
    private static final Pair<Integer> DEFAULT_POSITION = Pair.IntN(-1, -1);

    public Random r = new Random();

    private Maze(int width, int height)
    {
        _board = new Grid<Character>(width, height, ' ');
    }

    public Maze(int width, int height, int numDragons) // empty maze
    {
        this(width, height);
        for (int i = 0; i < numDragons; i++)
            _dragons.add(new Dragon());
    }

    public int GetWidth()
    {
        return _board.Width;
    }

    public int GetHeight()
    {
        return _board.Height;
    }

    public Maze(int width, int height, String[] cells) // maze defined with list
                                                       // of strings
    {
        this(width, height);

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                char c = cells[j].charAt(i);
                if (c == 'H')
                {
                    if (!_hero.GetPosition().equals(Unit.DEFAULT_POSITION))
                        throw new IllegalArgumentException();

                    _hero.SetPosition(Pair.IntN(i, j));
                }
                else if (c == 'A')
                {
                    if (!_sword.GetPosition().equals(Unit.DEFAULT_POSITION) || !_hero.GetPosition().equals(Unit.DEFAULT_POSITION))
                        throw new IllegalArgumentException();

                    _hero.EquipSword();
                    _hero.SetPosition(Pair.IntN(i, j));
                }
                else if (c == 'E')
                {
                    if (!_sword.GetPosition().equals(Unit.DEFAULT_POSITION) || _hero.IsArmed())
                        throw new IllegalArgumentException();

                    _sword.SetPosition(Pair.IntN(i, j));
                }
                else if (c == 'F')
                {
                    if (!_sword.GetPosition().equals(Unit.DEFAULT_POSITION) || _hero.IsArmed())
                        throw new IllegalArgumentException();

                    _sword.SetPosition(Pair.IntN(i, j));
                    Dragon d = new Dragon();
                    d.SetPosition(Pair.IntN(i, j));
                    _dragons.add(d);
                }
                else if (c == 'D')
                {
                    Dragon d = new Dragon();
                    d.SetPosition(Pair.IntN(i, j));
                    _dragons.add(d);
                }
                else if (c == 'S')
                {
                    if (!_exitPosition.equals(Unit.DEFAULT_POSITION))
                        throw new IllegalArgumentException();

                    _exitPosition = Pair.IntN(i, j);
                }
                else if (c != 'X' && c != ' ')
                    throw new IllegalArgumentException();

                if (c != 'X' && c != ' ')
                    c = ' ';

                _board.SetCell(Pair.IntN(i, j), c);
            }
        }
    }

    @Override
    public String toString()
    {
        _board.SetCell(_exitPosition, 'S');

        if (_sword.IsAlive())
        {
            boolean placedSword = false;

            for (Dragon d: _dragons)
            {
                if (d.IsAlive() && _sword.GetPosition().equals(d.GetPosition()))
                {
                    _board.SetCell(_sword.GetPosition(), 'F');
                    placedSword = true;
                }
                else if (d.IsAlive())
                    _board.SetCell(d.GetPosition(), 'D');
            }
            if (!placedSword)
                _board.SetCell(_sword.GetPosition(), 'E');
        }
        else
        {
            for (Dragon d: _dragons)
                if (d.IsAlive())
                    _board.SetCell(d.GetPosition(), 'D');
        }

        if (_hero.IsAlive())
            _board.SetCell(_hero.GetPosition(), _hero.IsArmed() ? 'A' : 'H');

        String res = _board.toString();

        _board.SetCell(_exitPosition, ' ');
        if (_hero.IsAlive()) _board.SetCell(_hero.GetPosition(), ' ');
        if (_sword.IsAlive()) _board.SetCell(_sword.GetPosition(), ' ');
        for (Dragon d : _dragons)
            if (d.IsAlive()) _board.SetCell(d.GetPosition(), ' ');

        return res;
    }

    private boolean isValidPosition(Pair<Integer> pos)
    {
        return (pos.first >= 0) && (pos.second >= 0)
                && (pos.first < _board.Width) && (pos.second < _board.Height);
    }

    private boolean isAdjacent(Pair<Integer> pos1, Pair<Integer> pos2)
    {
        return (pos1.equals(Pair.IntN(pos2.first + 1, pos2.second)))
                || (pos1.equals(Pair.IntN(pos2.first - 1, pos2.second)))
                || (pos1.equals(Pair.IntN(pos2.first, pos2.second + 1)))
                || (pos1.equals(Pair.IntN(pos2.first, pos2.second - 1)));
    }

    public boolean MoveHero(utils.Key direction)
    {
        boolean result = false;

        switch (direction)
        {
            case UP:
                result = SetHeroPosition(Pair.IntN(_hero.GetPosition().first,
                        _hero.GetPosition().second - 1));
                break;
            case DOWN:
                result = SetHeroPosition(Pair.IntN(_hero.GetPosition().first,
                        _hero.GetPosition().second + 1));
                break;
            case LEFT:
                result = SetHeroPosition(Pair.IntN(
                        _hero.GetPosition().first - 1,
                        _hero.GetPosition().second));
                break;
            case RIGHT:
                result = SetHeroPosition(Pair.IntN(
                        _hero.GetPosition().first + 1,
                        _hero.GetPosition().second));
                break;
        }

        return result;
    }

    public boolean MoveDragon(int index, utils.Key direction)
    {
        boolean result = false;

        if (!IsDragonAlive(index))
            return true;

        switch (direction)
        {
            case UP:
                result = SetDragonPosition(index, Pair.IntN(
                        _dragons.get(index).GetPosition().first,
                        _dragons.get(index).GetPosition().second - 1));
                break;
            case DOWN:
                result = SetDragonPosition(index, Pair.IntN(
                        _dragons.get(index).GetPosition().first,
                        _dragons.get(index).GetPosition().second + 1));
                break;
            case LEFT:
                result = SetDragonPosition(index, Pair.IntN(
                        _dragons.get(index).GetPosition().first - 1,
                        _dragons.get(index).GetPosition().second));
                break;
            case RIGHT:
                result = SetDragonPosition(index, Pair.IntN(
                        _dragons.get(index).GetPosition().first + 1,
                        _dragons.get(index).GetPosition().second));
                break;
        }

        return result;
    }

    public Pair<Integer> GetHeroPosition()
    {
        return _hero.GetPosition();
    }

    public boolean SetHeroPosition(Pair<Integer> pos)
    {
        if (!isValidPosition(pos) || _board.GetCellT(pos) == 'X'
                || (pos.equals(_exitPosition) && !IsHeroArmed()))
            return false;

        _hero.SetPosition(pos);

        return true;
    }

    public boolean SetExitPosition(Pair<Integer> pos)
    {
        if (!isValidPosition(pos))
            return false;

        _exitPosition = pos;

        return true;
    }

    public boolean SetSwordPosition(Pair<Integer> pos)
    {
        if (!isValidPosition(pos) && !pos.equals(DEFAULT_POSITION))
            return false;

        _sword.SetPosition(pos);

        return true;
    }

    public boolean SetDragonPosition(int index, Pair<Integer> pos)
    {
        if ((!isValidPosition(pos) && !pos.equals(DEFAULT_POSITION))
                || (!pos.equals(DEFAULT_POSITION) && _board.GetCellT(pos) == 'X')
                || (!pos.equals(DEFAULT_POSITION) && pos.equals(_exitPosition)))
            return false;

        _dragons.get(index).SetPosition(pos);

        return true;

    }

    public void Update()
    {
        for (int i = 0; i < _dragons.size(); i++)
        {
            boolean success = false;
            while (!success && _dragons.get(i).IsAlive())
            {
                Key dir = Key.toEnum(r.nextInt(5));
                success = (dir == null) || this.MoveDragon(i, dir);
            }
        }

        if (_hero.GetPosition().equals(_exitPosition))
            _finished = true;
        if (_hero.GetPosition().equals(_sword.GetPosition()))
        {
            _sword.Kill();
            _hero.EquipSword();
        }

        for (Dragon d : _dragons)
        {
            if (isAdjacent(_hero.GetPosition(), d.GetPosition())
                    || d.GetPosition().equals(_hero.GetPosition()))
            {
                if (IsHeroArmed())
                    d.Kill();
                else
                    _hero.Kill();
            }
        }
    }

    public Cell<Character> GetCell(Pair<Integer> pos)
    {
        return _board.GetCell(pos);
    }

    public boolean IsFinished()
    {
        return _finished || !IsHeroAlive();
    }

    public boolean IsHeroArmed()
    {
        return _hero.IsArmed();
    }

    public boolean IsHeroAlive()
    {
        return _hero.IsAlive();
    }

    public boolean IsDragonAlive(int index)
    {
        return _dragons.get(index).IsAlive();
    }

    private Grid<Character> _board;

    private Pair<Integer> _exitPosition = DEFAULT_POSITION;

    private Hero _hero = new Hero();
    private Sword _sword = new Sword();
    private ArrayList<Dragon> _dragons = new ArrayList<Dragon>();

    private boolean _finished = false;
}