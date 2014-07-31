1UAV-Non-periodic using PSO(particle swarm optimization algorithm)
==================
introduction:
every particle could be regarded as a bird in the space, every particle has a speed(w*p1+c1*r*(p1->p2)+c2*r1*(p1->p3)),

1、 every particle has a path choice at some time point
2、 get the shortcut of every path, then compute the distance of the shortcut
4、 "get more food" means the bird can get all the data via less distance
3、 every partical has its current path p1 and past best path p2(p2 is the past best path of current bird based on which the bird gets the most "food")
4、 compute the distance p3 of the best particle in the group(the path of the bird which gets the most "food" in the group)
5、 w*p1+c1*r*(p1->p2)+c2*r1*(p1->p3)

fitniss function: the total distance of shortcut 

limitation: it's easy to trap into local minimum. The GA-Sugihara is better than pso

(1、Concorde TSP solver is not suitable for the 3D situation, there are some existing interfaces, i will look whether it's ok to adopt it into 3D.
2、Improve the 1-LCT planning to Bio-inspired model, sugihara's online semi algorithm may be helpful. There are some articles online about bio-inspired model. (Bacteria foraging algo)
3、Consider about the periodic thing)
