package pt.up.fe.pt.lpoo.bombermen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity
{
    @Override
    public String toString()
    {
        return "[Entity - Type: " + _type + " Guid: " + _guid + " Position: " + _sprite.getX() + ", " + _sprite.getY() + " ]";
    }

    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_BOMB = 1;
    public static final int TYPE_EXPLOSION = 2;
    public static final int TYPE_WALL = 3;
    public static final int TYPE_POWER_UP = 4;

    private int _type;
    protected Sprite _sprite;
    protected TextureRegion _regions[][];
    private int _guid;

    Entity(int type, Sprite sprite, int guid)
    {
        _type = type;
        _sprite = sprite;
        _guid = guid;
    }

    public void SplitSprite(int tileWidth, int tileHeight)
    {
        _regions = _sprite.split(tileWidth, tileHeight);
    }

    public int GetType()
    {
        return _type;
    }

    public int GetGuid()
    {
        return _guid;
    }

    public Sprite GetSprite()
    {
        return _sprite;
    }

    public float GetX()
    {
        return _sprite.getX();
    }

    public float GetY()
    {
        return _sprite.getY();
    }

    public float GetWidth()
    {
        return _sprite.getWidth();
    }

    public float GetHeight()
    {
        return _sprite.getHeight();
    }

    public void SetX(float x)
    {
        _sprite.setPosition(x, GetY());
    }

    public void SetY(float y)
    {
        _sprite.setPosition(GetX(), y);
    }

    public boolean IsPlayer()
    {
        return _type == TYPE_PLAYER;
    }

    public boolean IsBomb()
    {
        return _type == TYPE_BOMB;
    }

    public boolean IsExplosion()
    {
        return _type == TYPE_EXPLOSION;
    }

    public boolean IsWall()
    {
        return _type == TYPE_WALL;
    }

    public boolean IsPowerUp()
    {
        return _type == TYPE_POWER_UP;
    }

    public Player ToPlayer()
    {
        return IsPlayer() ? (Player) this : null;
    }

    public Bomb ToBomb()
    {
        return IsBomb() ? (Bomb) this : null;
    }

    public Explosion ToExplosion()
    {
        return IsExplosion() ? (Explosion) this : null;
    }

    public Wall ToWall()
    {
        return IsWall() ? (Wall) this : null;
    }

    public PowerUp ToPowerUp()
    {
        return IsPowerUp() ? (PowerUp) this : null;
    }

    public abstract void OnCollision(Entity other);

    public abstract void OnDestroy();

    public abstract void OnExplode(Explosion e);

    public abstract void draw(SpriteBatch batch);
}
