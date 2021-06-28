package it.polito.tdp.PremierLeague.model;

public class Sfidante implements Comparable<Sfidante>{
	
	private Player p;
	private int differenza;
	public Sfidante(Player p, int differenza) {
		super();
		this.p = p;
		this.differenza = differenza;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public int getDifferenza() {
		return differenza;
	}
	public void setDifferenza(int differenza) {
		this.differenza = differenza;
	}
	@Override
	public int compareTo(Sfidante o) {
		return -(this.differenza - o.differenza);
	}

}
