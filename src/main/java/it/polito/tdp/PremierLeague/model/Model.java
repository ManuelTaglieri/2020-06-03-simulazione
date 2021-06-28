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
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	
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

}
