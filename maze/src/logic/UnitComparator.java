package logic;

import java.io.Serializable;
import java.util.Comparator;

public class UnitComparator implements Comparator<Unit>, Serializable
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Unit arg0, Unit arg1)
    {
        if (arg0.IsSameType(arg1))
            return arg0.GetId() - arg1.GetId();

        for (UnitType unt : UnitType.values())
        {
            if (arg0.Is(unt))
                return -1;
            else if (arg1.Is(unt))
                return 1;
        }
        return 0;
    }
}
