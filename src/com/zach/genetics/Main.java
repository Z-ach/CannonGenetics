package com.zach.genetics;

import javax.swing.JFrame;

import com.zach.genetics.algorithms.Evolution;
import com.zach.genetics.algorithms.Evolution.SelectionMethod;
import com.zach.genetics.visualization.GUI;

public class Main extends JFrame{
	private static final long serialVersionUID = 3368592664286654592L;

	public static void main(String[] args) {
/*		JFrame frame = new JFrame("Cannon Genetics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 640);
		frame.add(new GUI());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);*/
		new Evolution(SelectionMethod.RANK, 10, 1, 100);
	}

}
