package pso;

import java.util.*;

public class PsoAlgo { 
	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	private final static int EPOCHES = 1000;
	private int particleNum;	
	private int sensorNum;
	private int range;
	double[] pb;
	ArrayList<ArrayList<Sensor>> best = new ArrayList<ArrayList<Sensor>>(); 
	double pgb=0;
	int pgbidx = 0;
	int Vmax = 0;
	private final static double w = 0.75;
	private final static double c1 = 2;
	private final static double c2 = 2;
	
	public PsoAlgo(){
		sensorNum = getSensorNum();
		particleNum = getParticleNum();
		range = getRange();
		pb = new double[particleNum];
		Vmax=(int)((sensorNum + 1.0)/2);
		for(int i = 0; i < sensorNum; i++){
			Sensor sensor = new Sensor();
			sensors.add(sensor);
		}
		for(int j = 0; j < particleNum; j++){
			Particle particle = new Particle(sensors);
			particles.add(particle);
			getShortcut(particles.get(j));
			System.out.println(particle.getPbest());
			initVlist(j);
			pb[j] = particles.get(j).getPbest();
			best.add(sensors);
		}
		getPgb();
	}
	public void initVlist(int index){
		int count = 0;
		int x = 0;
		int y = 0;
		ArrayList<SW> list = new ArrayList<SW>();
		count = (int)(Math.random()*sensorNum);
		while(count >= sensorNum || count == 0)
			count = (int)(Math.random()*sensorNum);
		for(int i = 0; i < count; i++){
			x = (int)(Math.random()*sensorNum);
			y = (int)(Math.random()*sensorNum);
			while(x >= sensorNum)
				x = (int)(Math.random()*sensorNum);
			while(y >= sensorNum)
				y = (int)(Math.random()*sensorNum);
			while(x == y){
				y = (int)(Math.random()*sensorNum);
			}
			SW sw=new SW(x,y);
			list.add(sw);
		}
		particles.get(index).setV(list);
	}
	public int checkVe(int length){
		if(length >= Vmax)
			length = Vmax;
		return length;
	}
	public void getPgb(){
		int com = -1;
		for(int i = 0; i < particleNum; i++){
			com = particles.get(pgbidx).compare(particles.get(i));
			if(com == 1){
				pgb = particles.get(i).getPbest();
				pgbidx = i;
			}else{
				continue;
			}
		}
		//System.out.println(particles.get(pgbidx).getPbest());
	}
	public int getSensorNum(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the size of the sensors:");
		int size = sc.nextInt();
		return size;
	}
	public int getParticleNum(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the scale of the particles:");
		int size = sc.nextInt();
		return size;
	}
	public int getRange(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter range:");
		int size = sc.nextInt();
		return size;
	}
	public double pointToSeg(Sensor ss1, Sensor ss2,Sensor ss3){
		double line1 = ss1.getDis(ss2);
		double line2 = ss1.getDis(ss3);
		double line3 = ss2.getDis(ss3);
		double disSeg = 0;
		if(line1 + line2 == line3)
			disSeg = 0;
		else if(line1 * line1 >= line2 * line2 + line3 * line3)
			disSeg = line2;
		else if(line2 * line2 >= line1 * line1 + line3 * line3)
			disSeg = line1;
		else{
			double p = (line1 + line2 + line3)/2;
			double area = Math.sqrt(p * (p - line3) * (p - line2) * (p - line1));
			disSeg = 2 * area/line3;
		}
		return disSeg;
	}
	public void getShortcut(Particle p){
		ArrayList<Sensor> slist = (ArrayList<Sensor>) p.sensors.clone();
		for(int i = 0; i < slist.size()-1; ){
			int j = i+1;
			for(; j < slist.size(); j++){
				double lengthSeg = ((Sensor) p.sensors.get(i)).getDis((Sensor)p.sensors.get(j));
				int k = i;
				for(; k <= j; k++){
					double disSeg = pointToSeg((Sensor)p.sensors.get(k),(Sensor)p.sensors.get(i),(Sensor)p.sensors.get(j));
					if(disSeg >= range){
						break;
					}
				}
				if(k == j){
					for(int m = i + 1; m < k; m++){
						slist.remove(m);
					}
				}
			}
			i = j;
		}
		p.setPbest(getTotalDistance(slist));
	}
	public double getTotalDistance(ArrayList<Sensor> slist){
		double tourDis = 0;
		for(int i = 0; i < slist.size(); i++){
			Sensor next;
			Sensor last = (Sensor) slist.get(i);
			if(i + 1 < slist.size()){
				next = (Sensor) slist.get(i + 1);
			}
			else 
				next = (Sensor) slist.get(0);
			tourDis += last.getDis(next);
		}
		return tourDis;
	}
	
	public void exchange(ArrayList<Sensor> current, int from, int to){
		if(from == to)
			return;
		Sensor temp = current.get(from);
		current.set(from, current.get(to));
		current.set(to, temp);
	}
	public ArrayList<SW> towardBest(ArrayList<Sensor> current, ArrayList<Sensor> best){
		ArrayList<Sensor> temp = (ArrayList<Sensor>) current.clone();
		SW sw;
		ArrayList<SW> swlist3 = new ArrayList<SW>();
		for(int i = 0; i < sensorNum-1; i++){
			if(!best.get(i).equals(temp.get(i))){
				for(int j = i + 1; j < sensorNum; j++){
					if(temp.get(j).equals(best.get(i))){
						exchange(current, j, i);
						sw = new SW(j, i);
						swlist3.add(sw);
					}
				}
			}
		}
		return swlist3;
	}
	public void updateParticle(ArrayList<SW> swlist3, Particle p){
		for(int i = 0; i < swlist3.size(); i++){
			int x = swlist3.get(i).getX();
			int y = swlist3.get(i).getY();
			exchange(p.sensors, x, y);
		}
	}
	public void psoAlgo(){
		int times = 0;
		int length = 0;
		int flag = 0;
		ArrayList<SW> vi;
	    while(times < EPOCHES){
	    	double r1 = Math.random();
			double r2 = Math.random();
	    	for(int m = 0; m < particleNum; m++){
	    		ArrayList<SW> vii = new ArrayList<SW>();
	    		Particle aparticle = particles.get(m);
	    		//System.out.println("ee"+aparticle.getPbest());
	    		double old = pb[m];
	    		//if(m == pgbidx) continue;
	    		vi = aparticle.getSwlist();
	    		length = checkVe((int)(vi.size() * w));
	    		for(int j = 0; j < vi.size(); j++){
	    			if(new Random().nextBoolean()){
	    					vii.add(vi.get(j));
	    					flag++;
	    			}
	    			if(flag == length){
	    				flag = 0;
	    				break;
	    			}
	    		}
	    		updateParticle(vii, aparticle);
	    		
	    		ArrayList<SW> tempSw = towardBest(aparticle.sensors, best.get(m));
	    		length=checkVe((int)(c1 * r1 * tempSw.size()));
	    		for(int j = 0; j < tempSw.size(); j++){
	    			if(new Random().nextBoolean()){
	    					vii.add(tempSw.get(j));
	    					flag++;
	    			}
	    			if(flag == length){
	    				flag = 0;
	    				break;
	    			}
	    		}
	    		updateParticle(tempSw, aparticle);
	    		
	    		ArrayList<SW> tempSw1 = towardBest(aparticle.sensors, particles.get(pgbidx).sensors);
	    		length=checkVe((int)(c2 * r2 * tempSw1.size()));
	    		for(int j = 0; j < tempSw1.size(); j++){
	    			if(new Random().nextBoolean()){
	    					vii.add(tempSw1.get(j));
	    					flag++;
	    			}
	    			if(flag == length){
	    				flag = 0;
	    				break;
	    			}
	    		}
	    		updateParticle(tempSw1, aparticle);
	    		
	    		getShortcut(aparticle);
	    		aparticle.setV(vii);
	    		if(old > aparticle.getPbest()){
	    			pb[m] = aparticle.getPbest();
	    			best.set(m, aparticle.sensors);
	    		}
	    		//System.out.println(aparticle.getPbest()+"zz");
	    		getPgb();
	    	}
	    	times++;
		}
	}
	public static void main(String[] args){
		double res = 0;
		PsoAlgo pa=new PsoAlgo();
		pa.psoAlgo();
		res = pa.particles.get(pa.pgbidx).getPbest();
		for(int i = 0; i < pa.pb.length; i++)
			if(pa.pb[i] < res)
				res = pa.pb[i];
		System.out.println(res);
	}
}
