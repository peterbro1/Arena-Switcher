package me.gmx.arenaswitcher;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.synth.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.gmx.arenaswitcher.listener.DefaultListener;
import me.gmx.arenaswitcher.util.InvenUtil;
import me.gmx.arenaswitcher.util.PlayerUtil;
import me.gmx.arenaswitcher.util.RegionUtil;

public class ArenaSwitcher extends JavaPlugin{
	  Logger log = Logger.getLogger("Minecraft");
	  public String prefix;
	   public ArenaHandler ahandler;
	  private ArenaSwitcher ins;
	  public VoteTimer vtimer;
	  public Location min,max;
	public void onEnable(){
	    prefix = colorize(getConfig().getString("prefix"));
		ins=this;
		 ahandler = new ArenaHandler(ins);
	    ahandler.refreshArenas();
		new DefaultListener(ins);
		new RegionUtil(ins);
		new PlayerUtil(ins);
		vtimer = new VoteTimer(ins);
		new InvenUtil(ins);
	    Bukkit.getPluginManager().registerEvents(new DefaultListener(ins), this);
	    getCommand("wzarenavote").setExecutor(new PrivateCommands(ins));
	    getCommand("wzarena").setExecutor(new Commands(ins));
	    getCommand("map").setExecutor(new Commands(ins));
	    getCommand("play").setExecutor(new Commands(ins));    
	 		createConfig();
			new ArenaBoard(ins);
			ahandler.selectRandom();
	    log.log(Level.INFO, String.format("[%s] Successfully enabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));
	     min = new Location(Bukkit.getServer().getWorld(ins.getConfig().getString("teleport-region-world")),ins.getConfig().getInt("teleport-region.min.x"),ins.getConfig().getInt("teleport-region.min.y"),ins.getConfig().getInt("teleport-region.min.z"));
	     max = new Location(Bukkit.getServer().getWorld(ins.getConfig().getString("teleport-region-world")),ins.getConfig().getInt("teleport-region.max.x"),ins.getConfig().getInt("teleport-region.max.y"),ins.getConfig().getInt("teleport-region.max.z"));
	    
	}
	
	public void onDisable(){
	    log.log(Level.INFO, String.format("[%s] Successfully disabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));
	}

	
	private static String colorize(String message){
	    return message = ChatColor.translateAlternateColorCodes('&', message);
	  }
	
	private void createConfig() {
	      try {
	          if (!getDataFolder().exists()) {
	              getDataFolder().mkdirs();
	          }
	          File file = new File(getDataFolder(), "config.yml");
	          if (!file.exists()) {
	              saveDefaultConfig();
	              getConfig().options().copyDefaults();
	              saveConfig();
	          } else {
	              getLogger().info("Config.yml found, loading!");
	          }

	          
	      } catch (Exception e) {
	          e.printStackTrace();

	      }
	      
	      if (getConfig().getConfigurationSection("arenas") == null){
	      	  getConfig().createSection("arenas");
		    }
	      if (getConfig().getConfigurationSection("teleport-region") == null){
	      	  getConfig().createSection("teleport-region");
		    }

	      if (!getConfig().isSet("arena-teleport")){
	    	  getConfig().set("arena-teleport", "Teleporting to arena...");
	      }

	      if (!getConfig().isSet("gui-rows")){
	    	  getConfig().set("gui-rows", 3);
	      }
	      
	      if (!getConfig().isSet("arena-win-message")){
	    	  getConfig().set("arena-win-message", "Arena %ARENA% has won with %VOTES% votes");
	      }
	      if (!getConfig().isSet("already-voted")){
	    	  getConfig().set("already-voted", "You have already voted!");
	      }
	      if (!getConfig().isSet("debug")){
	    	  getConfig().set("debug", false);
	      }
	      if (!getConfig().isSet("teleport-region-world")){
	    	  getConfig().set("teleport-region-world", "warzone");
	      }
	      if (!getConfig().isSet("text-hover-event")){
	    	  getConfig().set("text-hover-event", "Vote for an arena");
	      }
	      
	      if(!getConfig().isSet("voting-not-enabled")){
		    	getConfig().set("voting-not-enabled", "You are unable to vote at this time!");
		    }
	      if(!getConfig().isSet("book-enabled")){
		    	getConfig().set("book-enabled", false);
		    }
	      if(!getConfig().isSet("random-map-select")){
		    	getConfig().set("random-map-select", "A random map has been chosen!");
		    }
	      if(!getConfig().isSet("vote-start-message")){
		    	getConfig().set("vote-start-message", "Voting is now open, click here to vote!");
		    }
	      if(!getConfig().isSet("vote-book-1st-line")){
		    	getConfig().set("vote-book-1st-line", "Vote selection menu");
		    }
	      if (!getConfig().isSet("map-switch-message")){
	    	  getConfig().set("map-switch-message", "Arena switching in %SECONDS% seconds!");
	      }
	      if(!getConfig().isSet("insufficient-votes-message")){
	    	  getConfig().set("insufficient-votes-message", "Insufficient votes to start!");
	      }
	      if(!getConfig().isSet("open-vote-timer")){
	    	  getConfig().set("open-vote-timer", 1);
	      }
		    if (!getConfig().isSet("switch-timer")){
		    	getConfig().set("switch-timer", 120);
		    }
		    if(!getConfig().isSet("gui-name")){
		    	getConfig().set("gui-name", "Please vote for the next map");
		    }
		    if (!getConfig().isSet("no-permission")){
		    	getConfig().set("no-permission", "Insufficient permission!");
		    }
		    if (!getConfig().isSet("prefix")){
		    	getConfig().set("prefix", "&4[&8ArenaSwitcher&4]");
		    }
		    if (!getConfig().isSet("map-world")){
		    	getConfig().set("map-world", "warzone");
		    }
		    if (!getConfig().isSet("lobby-world")){
		    	getConfig().set("lobby-world", "WZLobby");
		    }
		    
		    
		    
		    saveConfig();

	  }
	
}
