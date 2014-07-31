package pso;

import java.util.*;
public class Particle {
	ArrayList sensors=new ArrayList();
	private double pbest;
	ArrayList<SW> swlist = new ArrayList<SW>();
	public Particle(ArrayList<Sensor> sensors2){
		pbest=0;
		Collections.shuffle(sensors2);
		this.sensors=(ArrayList) sensors2.clone();
	}
	public ArrayList<Sensor> getParticle(){
		return this.sensors;
	}
	public double getPbest(){
		return pbest;
	}
	public void setPbest(double pbest){
		this.pbest=pbest;
	}
	public ArrayList<SW> getSwlist(){
		return swlist;
	}
	public void setV(ArrayList<SW> swlist2){
		this.swlist=swlist2;
	}
	public int compare(Particle p){
		if(getPbest()>p.getPbest()){
			return 1;
		}else if(getPbest()<p.getPbest()){
			return 0;
		}else
			return -1;
	}
}
