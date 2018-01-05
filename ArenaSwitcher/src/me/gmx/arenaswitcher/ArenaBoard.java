package me.gmx.arenaswitcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ArenaBoard {
	private static ArenaSwitcher ins;
	private static ScoreboardManager m;
	private static Scoreboard b;
	private static Objective o;
	public ArenaBoard(ArenaSwitcher inst){
		m = Bukkit.getScoreboardManager();
		ins=inst;
		refreshBoards(aBoard());
	}
	
	@SuppressWarnings("static-access")
	public static Scoreboard aBoard(){
		b = (Scoreboard) m.getNewScoreboard();
		o = b.registerNewObjective("board", "dummy");
		o.setDisplayName(ChatColor.DARK_RED.BOLD + "Arena Timer");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		
		Score s = o.getScore(ChatColor.GREEN + "Current Arena: ");
		s.setScore(9);
		//Score sep = o.getScore(ChatColor.RED + "-----------");
		//sep.setScore(9);
		Score seps = o.getScore(ChatColor.DARK_RED + ins.ahandler.getCurrentArena());
		seps.setScore(8);
		Score sepse = o.getScore(" ");
		sepse.setScore(7);
		Score sepser = o.getScore(" ");
		sepser.setScore(6);
		Score sepsert = o.getScore(ChatColor.GREEN + "Next switch in: ");
		sepsert.setScore(5);
		int time = ins.getConfig().getInt("switch-timer")-VoteTimer.reset;
		Score sepserts = o.getScore(ChatColor.DARK_RED + String.valueOf(time) + ChatColor.RED + " minutes");
		sepserts.setScore(4);
		/*Score owner = o.getScore(ChatColor.DARK_GRAY + "");
		owner.setScore(0);*/
		return b;
	}
	
	public static void refreshBoards(Scoreboard b){
		for (Player p : Bukkit.getServer().getOnlinePlayers()){
			Bukkit.getPlayer(p.getUniqueId()).setScoreboard(aBoard());
		}
	}
	
	
	
	
	

}
