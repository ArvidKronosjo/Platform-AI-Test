package se.kronosjo;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Seeker 
{
	public float width = 30;
	public float height = 60;
	
	public int LifeLength = 600;
	public int Age = 0;
	public Boolean Invulerable=false;
	public Boolean isOnGround=false;
	public Color color;
	
	public Boolean IsDead=false;
	
	public float Score=0;
	public int ObjId;
	
	public static ArrayList<Seeker> Seekers = new ArrayList<Seeker>();
	public ArrayList<DNA> DNAList = new ArrayList<DNA>();
	public static int Id=0;
	
	
	public static Color[] Colors = new Color[10];
	public Vector2f Loc;
	public Vector2f LastLoc;
	public Vector2f Vel;
	public Vector2f Acc;
	
	
	public Seeker(float x, float y)
	{
		Colors[0] = Color.cyan;
		Colors[1] = Color.orange;
		Colors[2] = Color.red;
		Colors[3] = Color.magenta;
		Colors[4] = Color.green;
		Colors[5] = Color.yellow;
		Colors[6] = Color.pink;
		Colors[7] = Color.blue;
		Colors[8] = Color.lightGray;
		Colors[9] = Color.darkGray;
		
		
		
		
		Loc = new Vector2f(x,y);
		LastLoc = Loc.copy();
		Vel = new Vector2f(0,0);
		Acc = new Vector2f(0,0);
		
		this.color = Colors[Id%10];
		Seekers.add(this);
		this.ObjId=Id;
		Id++;
	}
	
	public void GenerateDNA()
	{
		for(int i=0;i!=LifeLength;i++)
		{
			DNAList.add(new DNA());
		}
	}
	
	public void DNAInput(GameContainer Gc, int Delta)
	{
		if(DNAList.isEmpty() || DNAList.size()-1<=Age)
		{
			Vel.x=0;
			return;
		}
		DNA CurrentDNA = DNAList.get(Age);
		
		if(CurrentDNA.Jump && Vel.y==0)
		{
			Acc = new Vector2f(0,(float)(-0.6*Delta));
		}
		if(CurrentDNA.GoLeft)
		{
			Acc.x+=-0.6f;
		}
		if(CurrentDNA.GoRight)
		{
			Acc.x+=0.6f;
		}
		if(CurrentDNA.GoRight && CurrentDNA.GoLeft)
		{
			Vel.x=0;
		}
		if(CurrentDNA.GoRight==false && CurrentDNA.GoLeft==false)
		{
			Vel.x=0;
		}
	}
	
	public Rectangle getRectangle()
	{
		float xOffset = -this.width/2;
		float yOffset = height;
		Rectangle r = new Rectangle(Loc.x+xOffset, Loc.y-yOffset, width, height);
		return r;
	}
	
	public void Render(Graphics g)
	{
		g.setColor(this.color);
		float xOffset = -this.width/2;
		float yOffset = height;
		g.fillRect(Loc.x+xOffset, Loc.y-yOffset, width, height);
	}
	
	public void KeyInput(GameContainer Gc, int Delta)
	{
		if(Gc.getInput().isKeyDown(Input.KEY_W) && Vel.y==0)
		{
			Acc = new Vector2f(0,(float)(-0.6*Delta));
		}
		
		
		if(Gc.getInput().isKeyDown(Input.KEY_A))
		{
			Vel.x=-0.6f;
		}
		if(Gc.getInput().isKeyDown(Input.KEY_D))
		{
			Vel.x=0.6f;
		}
		if(Gc.getInput().isKeyDown(Input.KEY_D) && Gc.getInput().isKeyDown(Input.KEY_A))
		{
			Vel.x=0;
		}
		if(Gc.getInput().isKeyDown(Input.KEY_D)==false && Gc.getInput().isKeyDown(Input.KEY_A)==false)
		{
			Vel.x=0;
		}
	}
	
	public void Update(int Delta,GameContainer Gc)
	{
		float OldScore = Game.goal.CalculateScore(this, Gc);
		Acc = new Vector2f(0,0);
		
		KeyInput(Gc,Delta);
		DNAInput(Gc, Delta);

		Vector2f Gravity = new Vector2f(0,0.0050f);
		Acc.add(Gravity);
		Acc.scale(Delta);
		Vel.add(Acc);
		Vel.scale(Delta);
		if(Vel.y>15)//Downwards
		{
			Vel.y=15;
		}
		if(Vel.y<-20) //upwards
		{
			Vel.y=-20f;
		}
		if(Vel.x>6)
		{
			Vel.x=6;
		}
		if(Vel.x<-6)
		{
			Vel.x=-6;
		}
		LastLoc = Loc.copy();
		Loc.add(Vel);
		Vel.x/=Delta;
		Vel.y/=Delta;
		float ThisFrameScore = Game.goal.CalculateScore(this, Gc);
		Score+=ThisFrameScore;
		DNAList.get(Age).Score=ThisFrameScore-OldScore;
		this.Age++;
		
		if(Age==LifeLength)
		{
			IsDead=true;
		}
		
		
	}
	
	public void Mate(Seeker S)
	{
		S.Score=0;
		S.IsDead=false;

		for(int i=0;i!=DNAList.size();i++)
		{
			if(S.DNAList.get(i).Score>this.DNAList.get(i).Score)
			{
				DNA d = S.DNAList.get(i);
				S.DNAList.set(i, d);
			}
			else
			{
				DNA d = this.DNAList.get(i);
				S.DNAList.set(i, d);
			}
		}
		Mutate();
		S.Loc = new Vector2f(100,100); //Move to spawn point
		S.Age=0;
	}
	public void Mutate()
	{
		int Percent = 2;
		for(int i=0;i!=Percent;i++)
		{
			int Index = (int)(Math.random()*(double)DNAList.size());
			DNAList.set(Index, new DNA());
		}
	}
	
	//public Circle C = new Circle(0,0,5);
	public static void MegaUpdate(int Delta,GameContainer Gc)
	{
		for(int i=0;i!=Seekers.size();i++)
		{
			Seeker S = Seekers.get(i);
			S.Update(Delta, Gc);
			for(int ii =0;ii!=Platform.List.size();ii++)
			{
				S.ResolveCollision(Platform.List.get(ii));
			}
			
		}
	}
	
	public static void MegaRender(Graphics g)
	{
		for(int i=0;i!=Seekers.size();i++)
		{
			Seekers.get(i).Render(g);
		}
	}
	
	public void ResolveCollision(Platform r)
	{
		Rectangle SeekerRect = this.getRectangle();
		if(SeekerRect.intersects(r))
		{
			//this.Loc=LastLoc.copy();
			
			float NearestX =  Math.max(r.getX(), Math.min(SeekerRect.getCenterX(), r.getX() + r.getWidth()));
			float NearestY =  Math.max(r.getY(), Math.min(SeekerRect.getCenterY(), r.getY() + r.getHeight()));
			if(Loc.x==NearestX)
			{
				
				if(Loc.y<r.getCenterY() && Vel.y>=0)
				{
					Loc.y = NearestY;
					Vel.y = 0;
				}
				else
				{
					Loc.y = NearestY+height;
					Vel.y*=-1;
				}
			}
			else if(SeekerRect.getCenterY()==NearestY)
			{
				if(Loc.x>r.getCenterX())
				{
					Loc.x = NearestX+width/2;
				}
				else
				{
					Loc.x = NearestX-width/2;
				}
				Vel.x=0;
			}
			else
			{
				if(SeekerRect.getCenterY()<r.getCenterY())
				{
					Loc.y = NearestY;
					Vel.y=0;
				}
				else
				{
					Loc.y = NearestY+height;
					Vel.y*=-1;
				}
			}
			LastLoc = Loc.copy();
		}
		if(this.Loc.y>1080)
		{
			//this.Loc.y=0;
		}
	}

	
}
