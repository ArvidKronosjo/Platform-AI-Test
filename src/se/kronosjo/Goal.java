package se.kronosjo;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class Goal extends Circle
{

	public Goal(float centerPointX, float centerPointY, float radius) 
	{
		super(centerPointX, centerPointY, radius);
	}
	
	public float CalculateScore(Seeker S, GameContainer Gc)
	{
		Rectangle R = S.getRectangle();
		Line l = new Line(R.getCenterX(),R.getCenterY(),this.getCenterX(),this.getCenterY());
		
		float ScreenSquared = Gc.getWidth()*Gc.getWidth() + Gc.getHeight()*Gc.getHeight();
		float DistSquared = l.lengthSquared();
		float points = (float)Math.sqrt((double)(ScreenSquared-DistSquared));
		
		return points;
	}
	

}
