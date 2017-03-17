package com.zach.genetics;

public class Trajectory {
	
	private static double DISTANCE;
	private final static double GRAVITY = -9.8;
	
	static boolean runOnce = false;
	
	public Trajectory(double distance){
		this.DISTANCE = distance;
	}
	
	public static double calculateDistance(double velocity, double angle){
		if(!runOnce){
			System.out.println("distance123545645: " + DISTANCE);
			runOnce =true;
		}
		return Math.abs(DISTANCE + (velocity*velocity*Math.sin(Math.toRadians(2 * angle))/GRAVITY));
	}
	
}
