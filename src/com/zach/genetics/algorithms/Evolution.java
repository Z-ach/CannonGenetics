package com.zach.genetics.algorithms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.zach.genetics.Cannon;
import com.zach.genetics.CannonComparator;
import com.zach.genetics.Trajectory;

public class Evolution {

	private final double MUTATION_RATE = 0.33; // how often they should mutate
	private final int SIZE; // size of population
	private final int CLONES;

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

	public Evolution(SelectionMethod pick, int size, int clones, double distance) {
		this.DISTANCE = distance;
		this.SIZE = size;
		this.pick = pick;
		this.CLONES = clones;
		rand = new Random();
		new Trajectory(distance);
		initializePopulation();
		nextGeneration();

	}

	public void nextGeneration() {
		while (!checkForDone()) {
			fitness();
			System.out.println("----------------------------------------POPULATION POOL:");
			printList(population);
			//System.out.println("----------------------------------------MATING POOL:");
			generateMatingPool();
			//printList(matingPool);
			crossover();
			mutation();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

		}
		printList(population);
	}

	private boolean checkForDone() {
		boolean done = false;
		for (Cannon cannon : population) {
			if (cannon.getFitness() >= 10000) {
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
		DecimalFormat df = new DecimalFormat("#.##");
		int count = 1;
		for (Cannon cannon : list) {
			System.out.println("Cannon #\tAngle(°):\t" + df.format(cannon.getAngle()));
			System.out.println(count + "\t\tPower(m/s):\t" + df.format(cannon.getPower()));
			System.out.println("\t\tDistance(m):\t"
					+ df.format(Trajectory.calculateDistance(cannon.getPower(), cannon.getAngle())));
			System.out.println("\t\tFitness:\t" + df.format(cannon.getFitness()));
			System.out.println("-------------------------------------------");
			count++;
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
		clearAndKeepElite();
		double power = 1, angle = 1;
		// boolean success = false;
		while (population.size() < SIZE) {
			Cannon[] selection = { selection(), selection() };
			if (rand.nextBoolean()) { // parent 1
				power = selection[0].getPower();
				angle = selection[1].getAngle();
			} else { // parent 2
				power = selection[1].getPower();
				angle = selection[0].getAngle();
			}
			population.add(new Cannon(power, angle));
		}
	}

	private void clearAndKeepElite() {
		Collections.sort(population, new CannonComparator());
		List<Cannon> temp = new ArrayList<Cannon>();
		for (int i = 0; i < CLONES; i++)
			temp.add(population.get(i));
		population.clear();
		for (Cannon cannon : temp)
			population.add(cannon);
	}

	private Cannon selection() {
		int choice = rand.nextInt(matingPool.size());
		//System.out.println("Selection chose cannon # " + choice + ":" + matingPool.get(choice).getFitness());
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
		for (int i = 1; i < population.size(); i++) {
			if (rand.nextDouble() <= MUTATION_RATE) {
				population.get(i).setPower(LOWER_BOUND_POWER + (UPPER_BOUND_POWER - LOWER_BOUND_POWER) * rand.nextDouble());
			} else if (rand.nextDouble() <= MUTATION_RATE) {
				population.get(i).setAngle(LOWER_BOUND_ANGLE + (UPPER_BOUND_ANGLE - LOWER_BOUND_ANGLE) * rand.nextDouble());
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
			System.out.println((cannon.getNormalizedFitness()));

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
		// printList(matingPool);

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
		// printList(matingPool);
	}

	public List<Cannon> getPopulation() {
		return population;
	}

}
