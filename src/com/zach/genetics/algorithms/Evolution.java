package com.zach.genetics.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.zach.genetics.Cannon;
import com.zach.genetics.CannonComparator;
import com.zach.genetics.Trajectory;

public class Evolution {
	
	private final double DISTANCE; //distance to target from start
	private final int SIZE; //size of population
	
	public enum SelectionMethod{ //how to pick from matingpool
		ROULETTE, RANK
	};
	
	private Random rand;
	
	private final double UPPER_BOUND_POWER = 1;
	private final double LOWER_BOUND_POWER = 100;
	
	private final double UPPER_BOUND_ANGLE = 1;
	private final double LOWER_BOUND_ANGLE = 89;

	private SelectionMethod pick;
	
	private List<Cannon> population, matingPool;
	
	public Evolution(SelectionMethod pick, int size, double distance){
		this.DISTANCE = distance;
		this.SIZE = size;
		this.pick = pick;
		rand = new Random();
		new Trajectory(distance);
		run();
	}
	
	private void run(){
		initializePopulation();
		//printList();
		System.out.println("FITNESSING THEM");
		fitness();
		printList();
	}
	
	private void printList(){
		Collections.sort(population, new CannonComparator());
		for(Cannon cannon : population){
			System.out.println("Angle: " + cannon.getAngle());
			System.out.println("Power: " + cannon.getPower());
			System.out.println("Distance: " + Trajectory.calculateDistance(cannon.getPower(), cannon.getAngle()));
			System.out.println("Fitness: " + cannon.getFitness());
			System.out.println("---------------------------");
		}
	}
	
	private void initializePopulation(){
		population = new ArrayList<Cannon>();
		double power, angle;
		for(int i = 0; i < SIZE; i++){
			power = LOWER_BOUND_POWER + (UPPER_BOUND_POWER - LOWER_BOUND_POWER) * rand.nextDouble();
			angle = LOWER_BOUND_ANGLE + (UPPER_BOUND_ANGLE - LOWER_BOUND_ANGLE) * rand.nextDouble();
			population.add(new Cannon(power, angle));
		}
	}
	
	private void fitness(){
		double totalFitness = 0;
		for(Cannon cannon : population){
			cannon.setFitness(Trajectory.calculateDistance(cannon.getPower(), cannon.getAngle()));
			totalFitness += cannon.getFitness();
		}
		for(Cannon cannon : population){
			cannon.setFitness(cannon.getFitness()/totalFitness);
		}
	}
	
	private void crossover(){
		
	}
	
	private void mutation(){
		
	}
	
	private void mate(){
		
	}
	
	
}
