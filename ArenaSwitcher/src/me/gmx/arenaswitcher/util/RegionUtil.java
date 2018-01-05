package me.gmx.arenaswitcher.util;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.gmx.arenaswitcher.ArenaSwitcher;

public class RegionUtil {
	private static ArenaSwitcher ins;
	public RegionUtil(ArenaSwitcher instance){
		ins=instance;
	}
	
	public static void addArena(ProtectedRegion r, Location l){
		ConfigurationSection config = ins.getConfig().getConfigurationSection("arenas").createSection(r.getId());
		config.set("min.x", r.getMinimumPoint().getBlockX());
		config.set("min.y", r.getMinimumPoint().getBlockY());
		config.set("min.z", r.getMinimumPoint().getBlockZ());
		config.set("max.x", r.getMaximumPoint().getBlockX());
		config.set("max.y", r.getMaximumPoint().getBlockY());
		config.set("max.z", r.getMaximumPoint().getBlockZ());
		config.set("spawn.x", l.getBlockX());
		config.set("spawn.y", l.getBlockY());
		config.set("spawn.z", l.getBlockZ());
		config.set("gui-block", "BLAZE_ROD");
		config.set("lore", "This is an arena!");
		config.set("book-text", r.getId() + " arena");
		config.set("gui-item-slot",ins.ahandler.arenaList.size()+1);

		
		ins.saveConfig();
		ins.ahandler.refreshArenas();
	}
	
	public static void setTeleportRegion(ProtectedRegion r){
		ConfigurationSection config = ins.getConfig().getConfigurationSection("teleport-region");
		config.set("min.x", r.getMinimumPoint().getBlockX());
		config.set("min.y", r.getMinimumPoint().getBlockY());
		config.set("min.z", r.getMinimumPoint().getBlockZ());
		config.set("max.x", r.getMaximumPoint().getBlockX());
		config.set("max.y", r.getMaximumPoint().getBlockY());
		config.set("max.z", r.getMaximumPoint().getBlockZ());
		ins.saveConfig();
		
	}
	
	public static void removeArena(ProtectedRegion r){
		if (ins.getConfig().getConfigurationSection("arenas." + r.getId()) != null){
			ins.getConfig().set("arenas." + r.getId(), null);
			ins.saveConfig();
		}
	}
	
	
	public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
	
}
