package com.zach.genetics.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.zach.genetics.Cannon;
import com.zach.genetics.CannonComparator;
import com.zach.genetics.Trajectory;

public class Evolution {

	private final double MUTATION_RATE = 0.05; // how often they should mutate
	private final int SIZE; // size of population

	private final double DISTANCE; // distance to target from start

	public enum SelectionMethod { // how to pick from matingpool
		ROULETTE, RANK
	};

	private Random rand;

	private final double UPPER_BOUND_POWER = 1;
	private final double LOWER_BOUND_POWER = 100;

	private final double UPPER_BOUND_ANGLE = 1;
	private final double LOWER_BOUND_ANGLE = 89;

	private SelectionMethod pick;

	private List<Cannon> population, matingPool;

	public Evolution(SelectionMethod pick, int size, double distance) {
		this.DISTANCE = distance;
		this.SIZE = size;
		this.pick = pick;
		rand = new Random();
		new Trajectory(distance);
		initializePopulation();
		nextGeneration();
	}

	public void nextGeneration() {
		while (!checkForDone()) {
			fitness();
			printList(population);
			System.out.println("----------------------------------------MATING POOL:");
			generateMatingPool();
			crossover();
			mutation();
		}
		printList(population);
	}

	private boolean checkForDone() {
		boolean done = false;
		for (Cannon cannon : population) {
			if (cannon.getFitness() >= 10000){
				return true;
			}
		}
		if (done) {
			System.out.println("COMPLETE!");
		}
		return done;
	}

	private void printList(List<Cannon> list) {
		Collections.sort(list, new CannonComparator());
		for (Cannon cannon : list) {
			System.out.println("Angle: " + cannon.getAngle());
			System.out.println("Power: " + cannon.getPower());
			System.out.println("Distance: " + Trajectory.calculateDistance(cannon.getPower(), cannon.getAngle()));
			System.out.println("Fitness: " + cannon.getFitness());
			System.out.println("---------------------------");
		}
	}

	private void initializePopulation() {
		population = new ArrayList<Cannon>();
		double power, angle;
		for (int i = 0; i < SIZE; i++) {
			power = LOWER_BOUND_POWER + (UPPER_BOUND_POWER - LOWER_BOUND_POWER) * rand.nextDouble();
			angle = LOWER_BOUND_ANGLE + (UPPER_BOUND_ANGLE - LOWER_BOUND_ANGLE) * rand.nextDouble();
			population.add(new Cannon(power, angle));
		}
	}

	private void fitness() {
		double totalFitness = 0;
		for (Cannon cannon : population) {
			cannon.setFitness(100 / (Trajectory.calculateDistance(cannon.getPower(), cannon.getAngle() + .000000001)));
			totalFitness += cannon.getFitness();
		}
		for (Cannon cannon : population) {
			cannon.setNormalizedFitness(cannon.getFitness() / totalFitness);
		}
	}

	private void crossover() {
		population.clear();
		double power = 1, angle = 1;
		while (population.size() < SIZE) {
			Cannon[] selection = { selection(), selection() };
			if (rand.nextBoolean()) { // change power
				if (rand.nextBoolean()) { // parent 1
					power = selection[0].getPower();
				} else { // parent 2
					power = selection[1].getPower();
				}
			} else {
				if (rand.nextBoolean()) { // change angle
					angle = selection[0].getAngle();
				} else {
					angle = selection[1].getAngle();
				}
			}
			population.add(new Cannon(power, angle));
		}
	}

	private Cannon selection() {
		int choice = rand.nextInt(matingPool.size());
		return matingPool.get(choice);
	}

	private Cannon select() {
		double sum = 0;
		double spin = rand.nextDouble();
		for (Cannon c : matingPool) {
			sum += c.getFitness();
			if (sum >= spin) {
				return c;
			}
		}
		return null;
	}

	private void mutation() {
		for (Cannon cannon : population) {
			if (rand.nextDouble() <= MUTATION_RATE) {
				cannon.setPower(LOWER_BOUND_POWER + (UPPER_BOUND_POWER - LOWER_BOUND_POWER) * rand.nextDouble());
			} else if (rand.nextDouble() <= MUTATION_RATE) {
				cannon.setAngle(LOWER_BOUND_ANGLE + (UPPER_BOUND_ANGLE - LOWER_BOUND_ANGLE) * rand.nextDouble());
			}
		}
	}

	private void generateMatingPool() {
		matingPool = new ArrayList<Cannon>();
		if (pick == SelectionMethod.ROULETTE) {
			rouletteWheel();
			System.out.println("ROULETTE");
		} else if (pick == SelectionMethod.RANK) {
			ranking();
			System.out.println("RANK");

		}
	}

	private void rouletteWheel() {
		for (Cannon cannon : population) {
			System.out.println((cannon.getNormalizedFitness() * SIZE));

			if (cannon.getNormalizedFitness() > 0) {
				System.out.println((cannon.getNormalizedFitness() * SIZE));
				System.out.println(
						"this cannon will take up approx " + (cannon.getNormalizedFitness() * SIZE) + "% of the pool");
				for (int i = 0; i < (int) (cannon.getNormalizedFitness() * SIZE); i++) {
					System.out.println("ADDING CANNON " + i + "!");
					matingPool.add(cannon);
				}
			}
		}
		//printList(matingPool);

		/*
		 * matingPool = new ArrayList<Cannon>(); List<Cannon> temp = new
		 * ArrayList<Cannon>(); for(int i = 0; i < SIZE; i++){
		 * if(population.get(i).getNormalizedFitness() > 0)
		 * temp.add(population.get(i)); } double[] fit = new
		 * double[temp.size()]; for(int i = 0; i < temp.size(); i++){ fit[i] =
		 * temp.get(i).getNormalizedFitness() * SIZE; }
		 * //System.out.println("fit length " + fit.length);
		 * 
		 * for(int i = 0; i < fit.length; i++){
		 * //System.out.println("fit length" + fit[i]); for(int j = 0; j <
		 * (int)fit[i]; j++){ //System.out.println("wearwawawrawr");
		 * matingPool.add(temp.get(i)); //System.out.println("matingPool[" + i +
		 * "].getFitness() = " + temp.get(i).getFitness()); } } printList();
		 */
	}

	private void ranking() {
		Collections.sort(population, new CannonComparator());
		for (int i = 0; i < SIZE; i++) {
			for (int j = SIZE; j > i; j--) {
				matingPool.add(population.get(i));
			}
		}
		//printList(matingPool);
	}

	public List<Cannon> getPopulation() {
		return population;
	}

}
