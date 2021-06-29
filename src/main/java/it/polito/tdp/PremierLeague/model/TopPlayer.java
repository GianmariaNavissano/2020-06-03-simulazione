package it.polito.tdp.PremierLeague.model;

import java.util.List;

public class TopPlayer {
	
	private Player p;
	private List<PlayerWithWeight> battuti;
	private int peso;
	
	public TopPlayer(Player p, List<PlayerWithWeight> battuti) {
		super();
		this.p = p;
		this.battuti = battuti;
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public List<PlayerWithWeight> getBattuti() {
		return battuti;
	}

	public void setBattuti(List<PlayerWithWeight> battuti) {
		this.battuti = battuti;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	
	
	
	

}
