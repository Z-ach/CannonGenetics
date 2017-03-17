package com.zach.genetics.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.zach.genetics.Cannon;
import com.zach.genetics.algorithms.Evolution;
import com.zach.genetics.algorithms.Evolution.SelectionMethod;

public class GUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final int SIZE = 25;
	private final int ELITE_CLONES = 10;
	private final double DISTANCE = 650;
	private final SelectionMethod CHOICE = SelectionMethod.RANK;
	private boolean color = false;
	private boolean[] done;
	Random rand;

	private Evolution evolve;
	private List<Cannon> population;
	private List<Ball> ball;
	private List<Color> ballColor = new ArrayList<Color>();
	
	Timer timer;

	public GUI(int WIDTH, int HEIGHT) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		evolve = new Evolution(CHOICE, SIZE, ELITE_CLONES, DISTANCE);
		timer = new Timer(5, this);
		rand = new Random();
		run();
	}

	public void run() {
		color = false;
		startTime = System.currentTimeMillis();
		timer.stop();
		done = new boolean[SIZE];
		evolve.nextGeneration();
		population = evolve.getPopulation();
		initBalls();
		timer.start();
	}

	private void shotDone() {
		for (int i = 0; i < ball.size(); i++) {
			if (ball.get(i).getY() >= 600) {
				done[i] = false;
			}
			else{
				done[i] = true;
			}
		}
	}
	
	private boolean checkDone(){
		for(boolean b : done){
			if(!b){
				return false;
			}
		}
		System.out.println("completely done");
		return true;
	}

	private void initBalls() {
		ball = new ArrayList<Ball>();
		for (Cannon cannon : population) {
			ball.add(new Ball(cannon));
		}
	}

	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	long startTime = System.currentTimeMillis();
	long time = System.currentTimeMillis() - startTime;
	Ball ball1 = new Ball(new Cannon(50, 45));

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect((int)DISTANCE - 12, 560, 13, 40);
		g.setColor(Color.GREEN); 
		g.fillRect(0, 600, 800, 40);
		time = System.currentTimeMillis() - startTime;
		if(!color){
			ballColor.clear();
			for(int i = 0; i < ball.size(); i++)
				ballColor.add(randColor());
			color = true;
		}
		for (Ball b : ball) {
			if (b.getY() <= (600-25))
				b.updateCoords(time);
		}
		for (int i = 0; i < ball.size(); i++) {
			g.setColor(ballColor.get(i));
			ball.get(i).paintComponent(g);
		}
		shotDone();
	}
	
	private Color randColor(){
		return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		time = System.currentTimeMillis() - startTime;
		if(time > 250){
			run();
		}
	}

}
