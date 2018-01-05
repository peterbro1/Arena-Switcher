package me.gmx.arenaswitcher;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import me.gmx.arenaswitcher.util.ActionBar;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteTimer{
private ArenaSwitcher ins;
private Map<String,Integer> votes;
public static boolean votingEnabled;
public static int reset;
private static Scoreboard board;
public VoteTimer(ArenaSwitcher inst){
	ins=inst;
	votes = new HashMap<String,Integer>();
	for (String s : ins.ahandler.arenaList){
		votes.put(s, 0);
	}
	startTimer();
}
public void startTimer(){
	 reset=0;
	
	new BukkitRunnable(){
		
		public void run(){
			board = ArenaBoard.aBoard();
			if (reset>= ins.getConfig().getInt("switch-timer")){
				minuteLeft();
				reset=0;
				votingEnabled=true;
				for (Player p : Bukkit.getServer().getOnlinePlayers()){
					TextComponent message = new TextComponent(ins.getConfig().getString("vote-start-message"));
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/map vote" ) );
					message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ins.getConfig().getString("text-hover-event")).create() ) );
					p.spigot().sendMessage( message );
				}
			}
			ArenaBoard.refreshBoards(board);
			reset++;
		}
		
	}.runTaskTimer(ins,50,1200);//1200 ticks in a minute

	
	
}

public void minuteLeft(){
	votingEnabled=true;
	new BukkitRunnable(){
		
		int seconds=ins.getConfig().getInt("open-vote-timer")*60;
		@SuppressWarnings("static-access")
		public void run(){
			ArenaBoard.refreshBoards(board);
			ActionBar ab = new ActionBar(ChatColor.BOLD.DARK_PURPLE + ins.getConfig().getString("map-switch-message").replace("%SECONDS%", String.valueOf(seconds)));
			for (Player p : ins.getServer().getOnlinePlayers()){
				ab.sendToPlayer(p);
				p.playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1, 1);
			}
			if (seconds == 0){
				if (!votes.isEmpty()){
					votingEnabled=false;
				ins.ahandler.switchArena();
				cancel();

				}
			}
			seconds-=5;
			
		}
		
		
	}.runTaskTimer(ins, 0, 100);
}

	

}
