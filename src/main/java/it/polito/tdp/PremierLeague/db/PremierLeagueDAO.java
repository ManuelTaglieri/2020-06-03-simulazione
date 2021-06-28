package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getPlayerByGoals(Map<Integer, Player> idMap, double gol) {
		String sql = "SELECT p.PlayerID, p.Name "
				+ "FROM players p, actions a "
				+ "WHERE p.PlayerID = a.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name "
				+ "HAVING AVG(a.Goals)>=?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, gol);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if (idMap.get(res.getInt("PlayerID"))==null) {
					Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
					idMap.put(res.getInt("PlayerID"), player);
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Player> idMap) {
		String sql = "SELECT p1.PlayerID AS g1, p2.PlayerID AS g2, (SUM(a1.TimePlayed)-SUM(a2.TimePlayed)) AS differenza "
				+ "FROM players p1, players p2, actions a1, actions a2 "
				+ "WHERE p1.PlayerID = a1.PlayerID AND p2.PlayerID = a2.PlayerID AND a1.MatchID = a2.MatchID AND a1.TeamID != a2.TeamID AND p1.PlayerID > p2.PlayerID "
				+ "GROUP BY p1.playerID, p2.playerID "
				+ "HAVING COUNT( a1.`Starts`=1)>0 AND COUNT(a2.`Starts`=1)>0";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if (idMap.get(res.getInt("g1"))!=null && idMap.get(res.getInt("g2"))!=null) {
					Adiacenza ad = new Adiacenza(idMap.get(res.getInt("g1")), idMap.get(res.getInt("g2")), res.getInt("differenza"));
					result.add(ad);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
}
