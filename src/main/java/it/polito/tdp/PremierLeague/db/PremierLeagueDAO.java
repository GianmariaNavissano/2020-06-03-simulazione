package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	public Map<Integer, Player> getVertici(double minGoal){
		String sql = "SELECT distinct a.PlayerID, p.Name "
				+ "FROM Actions a, Players p "
				+ "WHERE a.PlayerID = p.PlayerID "
				+ "GROUP BY a.PlayerID "
				+ "HAVING AVG(a.Goals)>?";
		Map<Integer, Player> result = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, minGoal);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("a.PlayerID"), res.getString("p.Name"));
				
				result.put(player.getPlayerID(), player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Player> idMap){
		
		String sql = "SELECT a1.PlayerID as p1, a2.PlayerID as p2, SUM(a1.TimePlayed)-SUM(a2.TimePlayed) as peso "
				+ "FROM Actions a1, Matches m1, Actions a2 "
				+ "WHERE a1.MatchID = m1.MatchID AND a2.MatchID = m1.MatchID "
				+ "AND a1.PlayerID<a2.PlayerID AND a1.TeamID<>a2.TeamID "
				+ "AND a1.Starts=1 AND a2.Starts=1 "
				+ "GROUP BY a1.PlayerID, a2.PlayerID "
				+ "HAVING SUM(a1.TimePlayed)-SUM(a2.TimePlayed)<>0";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("p1")) && idMap.containsKey(res.getInt("p2"))) {
					Player p1 = idMap.get(res.getInt("p1"));
					Player p2 = idMap.get(res.getInt("p2"));
					int peso = res.getInt("peso");
					
					if(peso>0) {
						//arco da p1 a p2
						result.add(new Adiacenza(p1, p2, peso));
					}else if(peso<0) //arco da p2 a p1
						result.add(new Adiacenza(p2, p1, (-1)*peso));
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
