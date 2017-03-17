package com.zach.genetics.visualization;

import java.awt.Graphics;

import com.zach.genetics.Cannon;
import com.zach.genetics.Trajectory;

public class Ball {

	public final int SIZE = 25;

	private int x, y;

	private final double velocity, angle;

	public Ball(Cannon cannon) {
		x = 0;
		y = 600 - (SIZE);
		velocity = cannon.getPower();
		angle = cannon.getAngle();
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void updateCoords(double time) {
		time *= 80;
		x = ((int) Trajectory.getX(velocity, angle, time / 1000));
		y = ((600 - SIZE) - (int) Trajectory.getY(velocity, angle, time / 1000));

		//System.out.println("BALL UPDATED X " + x);
		//System.out.println("BALL UPDATED Y " + y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void paintComponent(Graphics g) {
		g.fillOval(x, y, SIZE, SIZE);
	}

}
