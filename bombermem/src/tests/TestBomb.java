package tests;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import logic.Bomb;

public class TestBomb extends Bomb {

	public TestBomb(int guid, Point2D position, int playerOwnerId, int radius,int strength, int time) {
		super(guid, position, playerOwnerId, radius, strength, time);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Rectangle2D GetBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
