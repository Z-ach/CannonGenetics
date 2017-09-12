package com.zach.genetics.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.zach.genetics.Cannon;
import com.zach.genetics.Trajectory;
import com.zach.genetics.algorithms.Evolution;
import com.zach.genetics.algorithms.Evolution.SelectionMethod;

public class GUI extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	private final int SIZE = 50;
	private final int ELITE_CLONES = 3;
	private final double DISTANCE = 650;
	private final SelectionMethod CHOICE = SelectionMethod.RANK;
	private boolean color = false;
	private boolean[] done;
	private int modifier = 8;
	private int count = 0;
	private int limit = 2500;

	private Random rand;
	private Evolution evolve;
	private List<Cannon> population;
	private List<Ball> ball;
	private List<Color> ballColor = new ArrayList<Color>();

	Timer timer;

	public GUI(int WIDTH, int HEIGHT) {
		this.requestFocus();
		this.addKeyListener(this);
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
			} else {
				done[i] = true;
			}
		}
	}

	private boolean checkDone() {
		for (boolean b : done) {
			if (!b) {
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
	
	public void toggleTime(){
		if(modifier == 8){
			modifier = 80;
			limit = 250;
		}else if(modifier == 80){
			modifier = 800;
			limit = 25;
		}else if(modifier == 800){
			modifier = 20000;
			limit = 1;
		}
		else{
			modifier = 8;
			limit = 2500;
		}
	}

	long startTime = System.currentTimeMillis();
	long time = System.currentTimeMillis() - startTime;
	Ball ball1 = new Ball(new Cannon(50, 45));

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect((int) DISTANCE - 12, 560, 13, 40);
		g.setColor(Color.GREEN);
		g.fillRect(0, 600, 800, 40);
		
		
		time = System.currentTimeMillis() - startTime;
		/*if (!color) {
			ballColor.clear();
			for (int i = 0; i < ball.size(); i++)
				ballColor.add(randColor());
			color = true;
		}
		for (Ball b : ball) {
			if (b.getY() <= (600 - 25))
				b.updateCoords(time*modifier);
		}
		for (int i = 0; i < ball.size(); i++) {
			g.setColor(ballColor.get(i));
			ball.get(i).paintComponent(g);
		}*/
		g.setColor(Color.BLUE);
		if(ball.get(0).getY() <= (600 - 25)){
			ball.get(0).updateCoords(time*modifier);
		}
		ball.get(0).paintComponent(g);
		//shotDone();
		addStats(g);
	}

	public void addStats(Graphics g) {
		g.setColor(Color.BLACK);
		Cannon temp = population.get(0);
		g.drawString("     Current max fitness:    " + temp.getFitness(), this.getWidth() / 2 - 150, 50);
		g.drawString("Current distance delta:    " + Trajectory.calculateDistance(temp.getPower(), temp.getAngle()), this.getWidth() / 2 - 150, 65);
		g.drawString("Generation: " + count, this.getWidth() / 2 - 150, 80);
		g.drawString("Modifier: " + modifier, this.getWidth() / 2 - 150, 95);
	}

	private Color randColor() {
		return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		time = System.currentTimeMillis() - startTime;
		if (time > limit) {
			count++;
			run();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_SPACE){
			toggleTime();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
