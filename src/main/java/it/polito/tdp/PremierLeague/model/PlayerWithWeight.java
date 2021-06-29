package it.polito.tdp.PremierLeague.model;

public class PlayerWithWeight implements Comparable<PlayerWithWeight>{
	
	private Player p;
	private int peso;
	
	public PlayerWithWeight(Player p, int peso) {
		super();
		this.p = p;
		this.peso = peso;
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(PlayerWithWeight other) {
		return other.getPeso()-this.peso;
	}
	
	

}
