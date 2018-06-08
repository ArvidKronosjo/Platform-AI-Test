package se.kronosjo;

public class DNA 
{
	public Boolean GoLeft;
	public Boolean GoRight;
	public Boolean Jump;
	public float Score=0;
	
	public DNA()
	{
		double Random = Math.random();
		
		Random = Math.random();
		GoLeft = Random>0.5;
		Random = Math.random();
		GoRight = Random>0.5;
		Random = Math.random();
		Jump = Random>0.5;
	}
}
