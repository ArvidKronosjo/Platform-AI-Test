package se.kronosjo;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame
{

	public Game(String title)
	{
		super(title);
	}

	public static Goal goal;
	public static Seeker BestSeeker = null;
	public static float BestSeekerScore=0;
	public static ArrayList<DNA> BestDna;
	public static int Generation;
	
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer app = new AppGameContainer(new Game("NeuralNetwork"));
		app.setDisplayMode(1280, 670, false);
		app.setAlwaysRender(false);
		app.setTargetFrameRate(60);
		app.start();
	}
	
	public void init(GameContainer GC) throws SlickException
	{
		
		new Platform(0,0,1280,30);
		new Platform(0,640,1280,3000);
		//new Platform(650,690,700,30);
		
		new Platform(250,550,100,30);
		new Platform(400,500,200,30);
		new Platform(675,450,200,30);
		new Platform(940,420,500,30);
		new Platform(1050,310,500,100);
		//new Platform(1050,310,30,120);
		
		new Platform(0,0,30,720);
		new Platform(1250,0,30,720);
		restart();
	}
	
	public void restart()
	{
		Generation=0;
		BestSeekerScore = 0;
		BestDna = new ArrayList<DNA>();
		BestSeeker = null;
		
		Seeker.Seekers = new ArrayList<Seeker>();
		goal = new Goal(1100,240,30);
		for(int i=0;i!=25;i++)
		{
			Seeker S = new Seeker(100,100);
			S.GenerateDNA();
		}
	}
	
	public void update(GameContainer Gc, int Delta) throws SlickException
	{
		Seeker.MegaUpdate(Delta, Gc);
		
		if(Gc.getInput().isKeyDown(Input.KEY_ESCAPE))
		{
			Gc.exit();
		}
		if(Gc.getInput().isKeyDown(Input.KEY_R))
		{
			restart();
		}
		NeuralShit();
	}

	public void render(GameContainer GC, Graphics g) throws SlickException 
	{
		Platform.MegaRender(g);
		Seeker.MegaRender(g);
		g.setColor(Color.green);
		g.fill(goal);
		g.setColor(Color.darkGray);
		g.setLineWidth(1);
		g.draw(goal);
		g.drawString("Generation: " + Generation, 500, 200);
	}
	
	
	public void NeuralShit()
	{
		if(Seeker.Seekers.isEmpty()) 
		{
			return;
		}
		for(int i=0;i!=Seeker.Seekers.size();i++)
		{
			Seeker S = Seeker.Seekers.get(i);
			if(S.IsDead==false)
			{
				return;
			}
		}
		if(BestSeeker==null)
		{
			BestSeeker = Seeker.Seekers.get(0);
		}
		else
		{
			BestSeeker.Score = BestSeekerScore;
			BestSeeker.DNAList = BestDna;
		}
		
		for(int i=0;i!=Seeker.Seekers.size();i++)
		{
			Seeker S = Seeker.Seekers.get(i);
			if(S.Score>BestSeeker.Score)
			{
				BestSeeker=S;
			}
		}
		BestDna = new ArrayList<DNA>(BestSeeker.DNAList);
		System.out.println(BestSeeker.Score + " color: " + BestSeeker.color.toString());
		BestSeekerScore = BestSeeker.Score;
		for(int i=0;i!=Seeker.Seekers.size();i++)
		{
			BestSeeker.Mate(Seeker.Seekers.get(i));
			BestSeeker.IsDead=false;
		}
		Generation++;

		
	}

	

	

}
