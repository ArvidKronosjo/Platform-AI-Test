package se.kronosjo;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Platform extends Rectangle
{
	
	private static final long serialVersionUID = 1L;
	public static ArrayList<Platform> List = new ArrayList<Platform>();
	
	public Platform(float x, float y,float width,float height)
	{
		super(x,y,width,height);
		List.add(this);
	}
	
	public static void MegaRender(Graphics g)
	{
		g.setColor(Color.darkGray);
		for(int i=0;i!=List.size();i++)
		{
			g.fill(List.get(i));
		}
	}

}
