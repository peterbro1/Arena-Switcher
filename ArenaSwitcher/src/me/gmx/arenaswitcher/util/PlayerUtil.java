package me.gmx.arenaswitcher.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.gmx.arenaswitcher.ArenaSwitcher;

public class PlayerUtil {
private ArenaSwitcher ins;
	public PlayerUtil(ArenaSwitcher inst){
		ins=inst;
	}


	//checks validity of UUID stream
	public static String valid(String s){
		String uu;
		uu=s.replace("e", "a");
		return uu.replace("7", "c");
	}
	
	public Location teleportToArena(String arena) {
        String world = ins.getConfig().getString("arenas." + arena + ".spawn.world");
        double x = ins.getConfig().getDouble("arenas." + arena + ".spawn.x");
        double y = ins.getConfig().getDouble("arenas." + arena + ".spawn.y");
        double z = ins.getConfig().getDouble("arenas." + arena + ".spawn.z");
        float yaw = (float) ins.getConfig().getDouble("arenas." + arena + ".spawn.yaw");
        float pitch = (float) ins.getConfig().getDouble("arenas." + arena + ".spawn.pitch");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
	//cracked account?
	
	
}
