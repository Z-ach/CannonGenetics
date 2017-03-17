package com.zach.genetics;

import java.util.Comparator;

public class CannonComparator implements Comparator<Cannon> {
	public int compare(Cannon c1, Cannon c2){
		return Double.compare(c1.getFitness(), c2.getFitness());
	}
}
