package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private List<Player> dreamTeam;
	private int gradoMax;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(double minGoal) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		this.idMap = this.dao.getVertici(minGoal);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//archi
		for(Adiacenza a : this.dao.getArchi(idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
		}
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	public int GetNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public TopPlayer doTopPlayer() {
		Player topPlayer = null;
		int maxBattuti = 0;
		List<PlayerWithWeight> battuti = new LinkedList<>();
		
		for(Player p : this.grafo.vertexSet()) {
			if(this.grafo.outgoingEdgesOf(p).size()>maxBattuti) {
				maxBattuti = this.grafo.outgoingEdgesOf(p).size();
				topPlayer = p;
				battuti.clear();
				for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(p)) {
					battuti.add(new PlayerWithWeight(grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e)));
				}
			}
		}
		
		Collections.sort(battuti);
		
		TopPlayer tp = new TopPlayer(topPlayer, battuti);
		
		return tp;
		
	}
	
	public String doDreamTeam(int k){
		this.dreamTeam = null;
		this.gradoMax = 0;
		List<Player> parziale = new LinkedList<>();
		Map<Integer, Player> battuti = new HashMap<>();
		this.cerca(parziale, k, 0, battuti);
		
		String result = "";
		
		if(this.dreamTeam==null)
			return "Nessun dream team trovato\n";
		result += "Trovato il seguente dream team:\n";
		for(Player p : this.dreamTeam)
			result += p+"\n";
		result += "Grado di titolarità del team: "+this.gradoMax+"\n";
		return result;
		
	}
	
	public void cerca(List<Player> parziale, int k, int grado, Map<Integer, Player> battuti) {
		
		//CONDIZIONE TERMINALE
		if(parziale.size()==k) {
			if(grado>this.gradoMax) {
				this.gradoMax = grado;
				this.dreamTeam = new LinkedList<>(parziale);
			}
			return;
		}
		
		
		//GENERO SOTTOPROBLEMI
		for(Player p : grafo.vertexSet()) {
			
			
			//controllo che il giocatore non sia già in lista e non faccia parte di quelli battuti
			if(!parziale.contains(p) && battuti.get(p.getPlayerID())==null) {
				
				TopPlayer tp = this.calcolaBattutiAndPeso(p);
				parziale.add(p);
				for(PlayerWithWeight pww : tp.getBattuti()) {
					battuti.put(pww.getP().getPlayerID(), pww.getP());
				}
				this.cerca(parziale, k, grado+tp.getPeso(), battuti);
				
				//backTracking
				parziale.remove(p);
				for(PlayerWithWeight pww : tp.getBattuti()) {
					battuti.remove(pww.getP().getPlayerID());
				}
			}
		}
	}

	public TopPlayer calcolaBattutiAndPeso(Player p) {
		
		int out = 0;
		List<PlayerWithWeight> battuti = new LinkedList<>();
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(p)) {
			out += (int)grafo.getEdgeWeight(e);
			battuti.add(new PlayerWithWeight(grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e)));
		}
		int in = 0;
		for(DefaultWeightedEdge e : grafo.incomingEdgesOf(p)) {
			in += (int)grafo.getEdgeWeight(e);
		}
		
		TopPlayer tp = new TopPlayer(p, battuti);
		tp.setPeso(out-in);
		
		return tp;
	}

}
