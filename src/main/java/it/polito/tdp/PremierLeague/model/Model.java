package it.polito.tdp.PremierLeague.model;

import java.util.Collection;
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
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	private Map<Player, Integer> prestazioni;
	private List<Player> dreamTeam;
	private int migliorTit;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(double gol) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		this.dao.getPlayerByGoals(idMap, gol);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		for (Adiacenza a : this.dao.getArchi(idMap)) {
			if (a.getDifferenza()>0) {
				Graphs.addEdge(grafo, a.getG1(), a.getG2(), a.getDifferenza());
			} else if (a.getDifferenza()<0) {
				Graphs.addEdge(grafo, a.getG2(), a.getG1(), -a.getDifferenza());
			}
		}
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Player getMigliore() {
		int massimo = 0;
		Player best = null;
		for (Player p : idMap.values()) {
			if (this.grafo.outDegreeOf(p)>massimo) {
				best = p;
				massimo = this.grafo.outDegreeOf(p);
			}
		}
		return best;
	}
	
	public List<Sfidante> getSfidanti() {
		List<Sfidante> sfidanti = new LinkedList<>();
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(getMigliore())) {
			sfidanti.add(new Sfidante(this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e)));
		}
		Collections.sort(sfidanti);
		return sfidanti;
	}
	
	public List<Player> getDreamTeam(int k) {
		
		this.prestazioni = new HashMap<>();
		for (Player p : idMap.values()) {
			int uscenti = 0;
			for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				uscenti += this.grafo.getEdgeWeight(e);
			}
			int entranti = 0;
			for (DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				entranti += this.grafo.getEdgeWeight(e);
			}
			int titolarita = uscenti - entranti;
			this.prestazioni.put(p, titolarita);
		}
		this.migliorTit = 0;
		ricorsiva(k, idMap.values(), new LinkedList<Player>(), 0);
		return this.dreamTeam;
		
	}

	private void ricorsiva(int k, Collection<Player> giocatori, List<Player> team, int forza) {
		
		if(team.size()==k) {
			if (forza>this.migliorTit) {
				this.dreamTeam = new LinkedList<>(team);
				this.migliorTit = forza;
			}
		} else {
			for (Player p : giocatori) {
				boolean ok = true;
				for (Player corrente : team) {
					if (Graphs.neighborSetOf(grafo, corrente).contains(p)) {
						ok = false;
					}
				}
				if (!team.contains(p) && ok) {
					team.add(p);
					ricorsiva(k, giocatori, team, forza + this.prestazioni.get(p));
					team.remove(p);
				}
			}
		}
	}

	public int getMigliorTit() {
		return migliorTit;
	}
	

}
