package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	
	private Player g1;
	private Player g2;
	private int differenza;
	public Adiacenza(Player g1, Player g2, int differenza) {
		super();
		this.g1 = g1;
		this.g2 = g2;
		this.differenza = differenza;
	}
	public Player getG1() {
		return g1;
	}
	public void setG1(Player g1) {
		this.g1 = g1;
	}
	public Player getG2() {
		return g2;
	}
	public void setG2(Player g2) {
		this.g2 = g2;
	}
	public int getDifferenza() {
		return differenza;
	}
	public void setDifferenza(int differenza) {
		this.differenza = differenza;
	}

}
