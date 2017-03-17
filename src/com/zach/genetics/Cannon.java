package com.zach.genetics;

public class Cannon {
	
	private double power, angle, fitness, normalizedFitness;
	
	public Cannon(double power, double angle){
		this.power = power;
		this.angle = angle;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getNormalizedFitness() {
		return normalizedFitness;
	}

	public void setNormalizedFitness(double normalizedFitness) {
		this.normalizedFitness = normalizedFitness;
	}
}
