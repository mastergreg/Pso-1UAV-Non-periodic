package pso;

public class Sensor {
	private int x;
	private int y;
	private int z;
	public Sensor(){
		this.x=(int)(Math.random()*100);
		this.y=(int)(Math.random()*100);
		this.z=(int)(Math.random()*100);
	}
	public Sensor(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getZ(){
		return this.z;
	}
	public double getDis(Sensor sensor){
		double distance;
		int xaxis=getX()-sensor.getX();
		int yaxis=getY()-sensor.getY();
		int zaxis=getZ()-sensor.getZ();
		distance=Math.sqrt(xaxis*xaxis+yaxis*yaxis+zaxis*zaxis);
		return distance;
	}
	public String toString(){
		return "["+getX()+","+getY()+","+getZ()+"]";
	}
}

