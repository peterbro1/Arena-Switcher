package me.gmx.arenaswitcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.earth2me.essentials.spawn.IEssentialsSpawn;

import me.gmx.arenaswitcher.util.json.JSONChatMessage;
import net.md_5.bungee.api.ChatColor;

public class ArenaHandler {
	private  String currentArena;
	public List<String> arenaList;
	public Map<String, Integer> votes;
	 private  ArenaSwitcher ins;
	 public List<String> alreadyVoted;
	 public Location spawn;
	public ArenaHandler(ArenaSwitcher inst){
		ins=inst;
		arenaList = new ArrayList<String>();
		votes = new HashMap<String, Integer>();
		alreadyVoted = new ArrayList<String>();
		refreshArenas();
	}
	
	public void refreshArenas(){
		if (ins.getConfig().getConfigurationSection("arenas") != null){
			if (ins.getConfig().getBoolean("debug")){
				Bukkit.broadcastMessage(String.valueOf(ins.prefix + arenaList.size()) + " arenas loaded");
			}
			
			for (String s : ins.getConfig().getConfigurationSection("arenas").getKeys(false)){
				if (ins.getConfig().get("arenas." + s + ".enabled") == null){
					ins.getConfig().set("arenas." + s + ".enabled", true);
				}
				if (ins.getConfig().getBoolean("arenas." + s + ".enabled")){
					
				if (!arenaList.contains(ChatColor.stripColor(s.toString()))){
				arenaList.add(ChatColor.stripColor(s.toString()));
				}
				if (ins.getConfig().getBoolean("debug")){
				Bukkit.broadcastMessage(ins.prefix + "Loaded arena '" + s + "'");
				}
				votes.put(s, 0);
				}else{
					if (ins.getConfig().getBoolean("debug")){
					Bukkit.broadcastMessage(ins.prefix + "Loaded arena '" + s + "' --Disabled");
				}
					}
				
				
			}
			}else{
				Bukkit.broadcastMessage("Failed to refresh arenas.");
			}
	}
	
	public void selectRandom(){
		Random c = new Random();
		currentArena = arenaList.get(c.nextInt(arenaList.size()));
		Bukkit.broadcastMessage(ins.prefix + ins.getConfig().getString("random-map-select"));
		spawn=new Location(ins.getServer().getWorld(ins.getConfig().getString("map-world")),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.x"),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.y"),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.z"));

	}
	
	public void mapVote(String map, int weight){
		if (votes.containsKey(map)){
			votes.replace(map, votes.get(map)+weight);
		}else{
			votes.put(map, weight);
		}
	}
	
	
	public String getCurrentArena(){
		return this.currentArena;
	}
	
	public void setCurrentArena(String s){
		for (String a : arenaList){
			if (a.equalsIgnoreCase(s)){
				currentArena=s;
			}
		}
		
	}
	
	public Map.Entry<String, Integer> calculateVotes(){
		Map.Entry<String, Integer> top = null;
		for (Map.Entry<String, Integer> entry : votes.entrySet()){
			if (top==null||entry.getValue().compareTo(top.getValue()) > 0){
			top=entry;
			}
		}
		return top;
		
	}
	
	public void switchArena(){
		if (votes.isEmpty()){
			Bukkit.broadcastMessage(ins.prefix + ChatColor.DARK_RED + ins.getConfig().getString("insufficient-votes-message"));
			votes.clear();
			alreadyVoted.clear();
			selectRandom();
			Bukkit.broadcastMessage(ins.prefix + ins.getConfig().getString("random-map-select"));
			return;
		}else{
		Map.Entry<String, Integer> entry = calculateVotes();
			Bukkit.broadcastMessage(ins.prefix + ins.getConfig().getString("arena-win-message").replace("%ARENA%", entry.getKey()).replace("%VOTES%", String.valueOf(entry.getValue())));
			setCurrentArena(entry.getKey());
		}
		for (Player p : ins.getServer().getWorld(ins.getConfig().getString("map-world")).getPlayers()){
			p.teleport(((IEssentialsSpawn)Bukkit.getPluginManager().getPlugin("EssentialsSpawn")).getSpawn("default"));
		}
		votes.clear();
		alreadyVoted.clear();
		spawn=new Location(ins.getServer().getWorld(ins.getConfig().getString("map-world")),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.x"),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.y"),ins.getConfig().getInt("arenas." + getCurrentArena() + ".spawn.z"));
	}
	
}
