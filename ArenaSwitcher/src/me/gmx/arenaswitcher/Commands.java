package me.gmx.arenaswitcher;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.gmx.arenaswitcher.util.InvenUtil;
import me.gmx.arenaswitcher.util.RegionUtil;
import net.md_5.bungee.api.ChatColor;


public class Commands implements CommandExecutor{
private ArenaSwitcher ins;


public Commands(ArenaSwitcher i){
	ins=i;
}
	

	@Override
	    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	        if(!(sender instanceof Player)) {
	            return true;
	        }
	        World world = ((Player)sender).getWorld();
	        if (cmd.getName().equalsIgnoreCase("wzarena")){
	        	switch(args.length){
	        	case 0:
	        		sender.sendMessage(ins.prefix + ChatColor.DARK_RED + "Warzone Arena - by " + ChatColor.DARK_AQUA + "GMX");
	        		sender.sendMessage(ins.prefix + "Command usage is as follows:");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena list" + ChatColor.DARK_RED + " - Lists current arenas");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena addarena [protectedregion]" + ChatColor.DARK_RED + " - Adds arena" + ChatColor.GREEN + " [Requires reload]");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena currentarena" + ChatColor.DARK_RED + " - " + ChatColor.GREEN + " [Requires reload]");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena removearena" + ChatColor.DARK_RED + " - Removes existing arena" + ChatColor.GREEN + " [Requires reload]");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena setspawn [arena]" + ChatColor.DARK_RED + " - changes spawn for arena" + ChatColor.GREEN + " [Requires reload]");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena vote [add/remove] [arena] [amount]" + ChatColor.DARK_RED + " - adds and removes votes for maps");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/map vote" + ChatColor.DARK_RED + " - opens voting GUI");
	        		sender.sendMessage(ins.prefix + ChatColor.RED + "/wzarena setteleportregion [protectedregion]" + ChatColor.DARK_RED + " - set a region to teleport players" );

	        		return true;
	        		case 1:
	        			if (args[0].equalsIgnoreCase("listarenas") || args[0].equalsIgnoreCase("list")){
	        				if (!sender.hasPermission("arenaswitcher.list")){
	        					sender.sendMessage(ins.prefix + ins.getCommand("no-permission"));
	        					return true;
	        				}
	        				for (String s : ins.ahandler.arenaList){
	        					sender.sendMessage(ins.prefix + " " + s);
	        				}
	        				return true;
	        			}else if (args[0].equalsIgnoreCase("currentarena")){
	        				sender.sendMessage(ins.ahandler.getCurrentArena());
	        			}
	        		case 2:
	        			if (args[0].equalsIgnoreCase("addarena")){
	        				if (!sender.hasPermission("arenaswitcher.addarena")){
	        					sender.sendMessage(ins.prefix + ins.getCommand("no-permission"));
	        					return true;
	        				}
	        				try{
	        				ProtectedRegion r = WGBukkit.getRegionManager(world).getRegion(args[1]);
	        				RegionUtil.addArena(r,((Player)sender).getLocation());
	        				sender.sendMessage(ins.prefix + ChatColor.GREEN + "Region " + args[1] + " added.");
	        				}catch(NullPointerException e){
	        					sender.sendMessage(ins.prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + " region not found.");
	        					return false;
	        				}
	        				return true;

	        			}else if (args[0].equalsIgnoreCase("setteleportregion")){
	        				if (!sender.hasPermission("arenaswitcher.setteleportregion")){
	        					sender.sendMessage(ins.prefix + ins.getCommand("no-permission"));
	        					return true;
	        				}
	        				try{
		        				ProtectedRegion r = WGBukkit.getRegionManager(world).getRegion(args[1]);
		        				RegionUtil.setTeleportRegion(r);
		        				sender.sendMessage(ins.prefix + ChatColor.GREEN + "Region " + args[1] + " set to teleport players.");
		        				return true;
	        				}catch(NullPointerException e){
		        					e.printStackTrace();
		        					sender.sendMessage(ins.prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + " region not found.");
		        					return false;
		        				}
	        				
	        			}else if(args[0].equalsIgnoreCase("removearena")){
	        				if (!sender.hasPermission("arenaswitcher.removearena")){
	        					sender.sendMessage(ins.prefix + ins.getCommand("no-permission"));
	        					return true;
	        				}
	        				try{
		        				ProtectedRegion r = WGBukkit.getRegionManager(world).getRegion(args[1]);
		        				RegionUtil.removeArena(r);
		        				sender.sendMessage(ins.prefix + ChatColor.GREEN + "Region " + args[1] + " removed.");
		        				}catch(NullPointerException e){
		        					sender.sendMessage(ins.prefix + ChatColor.DARK_GREEN + args[1] + ChatColor.GREEN + " region not found.");
		        					return false;
		        				}
		        				return true;
	        			}else if (args[0].equalsIgnoreCase("setspawn")){
	        				if (!sender.hasPermission("wzarena.arena.setspawn")){
	        					sender.sendMessage(ins.prefix + ins.getConfig().getString("no-permission"));
	        					return true;
	        				}
	        					for (String s : ins.ahandler.arenaList){
	        						if (s.equals(args[1])){
	        							Bukkit.broadcastMessage(s);
	        							break;
	        						}
	        					}
	        					ConfigurationSection c = ins.getConfig().getConfigurationSection("arenas." + args[1]);
	        					Player p = (Player)sender;
	        					c.set("spawn.x",p.getLocation().getBlockX());
	        					c.set("spawn.y",p.getLocation().getBlockY());
	        					c.set("spawn.z",p.getLocation().getBlockZ());
	        					sender.sendMessage(ins.prefix + " Spawn set");
	        					ins.saveConfig();
	        					return true;

	        					
	        			}
	        		case 4:
	        			if (args[0].equalsIgnoreCase("vote")){
	        				if (args[1].equalsIgnoreCase("add")){
	        					if (!sender.hasPermission("wzarena.vote.add")){
	        						sender.sendMessage(ins.getConfig().getString("no-permission"));
	        						return true;
	        					}
	        					if (ins.ahandler.arenaList.contains(args[2])){
	        						if (RegionUtil.isInteger(args[3])){
	        							if (VoteTimer.votingEnabled){
	        								ins.ahandler.mapVote(args[2],Integer.parseInt(args[3]));
	        								sender.sendMessage(ins.prefix + " Added votes to map!");
	        								return true;
	        							}else{
	        								sender.sendMessage(ins.prefix + ins.getConfig().getString("voting-not-enabled"));
	        								return true;
	        							}
	        						}else{
	        							sender.sendMessage(ins.prefix + "Please type a valid integer");
	        							return true;
	        						}
	        						
	        					}
	        					
	        				}else if (args[1].equalsIgnoreCase("remove")){
	        					if (!sender.hasPermission("wzarena.vote.remove")){
	        						sender.sendMessage(ins.getConfig().getString("no-permission"));
	        						return true;
	        					}
	        					if (ins.ahandler.arenaList.contains(args[2])){
	        						if (RegionUtil.isInteger(args[3])){
	        							if (VoteTimer.votingEnabled){
	        								ins.ahandler.mapVote(args[2],Integer.parseInt(args[3])*-1);
	        								sender.sendMessage(ins.prefix + " Removes votes to map!");
	        								return true;
	        							}else{
	        								sender.sendMessage(ins.prefix + ins.getConfig().getString("voting-not-enabled"));
	        								return true;
	        							}
	        						}else{
	        							sender.sendMessage(ins.prefix + "Please type a valid integer");
	        							return true;
	        						}
	        						
	        					}
	        					
	        				}
	        				
	        				
	        			}
	        		
	        	}
	        	
	        	//done
	        }else if(cmd.getName().equalsIgnoreCase("map")){
	        	switch(args.length){
	        	case 0:

	        		sender.sendMessage(ins.prefix + ChatColor.RED + " Please type /map vote.");
	        		return true;
	        	case 1:
	        		if (!sender.hasPermission("wzarena.vote")){
    					sender.sendMessage(ins.prefix + ins.getCommand("no-permission"));
    					return true;
    				}
	        		
	        		if (args[0].equalsIgnoreCase("vote")){
	        			if (!VoteTimer.votingEnabled){
		        			sender.sendMessage(ins.prefix + ChatColor.DARK_RED + ins.getConfig().getString("voting-not-enabled"));
		        			return true;
		        		}
	        			if (ins.ahandler.alreadyVoted.contains(((Player)sender).getName())){
	        				sender.sendMessage(ins.prefix + ChatColor.RED + ins.getConfig().getString("already-voted"));
	        				return true;
	        			}
	        			if (ins.getConfig().getBoolean("book-enabled")){
	        			InvenUtil.openBook( InvenUtil.createBook(), (Player)sender);
	        			}else{
	        			((Player)sender).openInventory(InvenUtil.createInventory());
	        			}
	        			return true;
	        		}
	        		
	        		
	        	
	        	}
	        	
	        }else if (cmd.getName().equalsIgnoreCase("play")){
	        	if (!sender.hasPermission("wzarena.play")){
	        		sender.sendMessage(ins.prefix + ins.getConfig().getString("no-permission"));
	        		return true;
	        	}
	        	sender.sendMessage(ins.prefix + ins.getConfig().getString("arena-teleport"));
	        	ConfigurationSection c = ins.getConfig().getConfigurationSection("arenas." + ins.ahandler.getCurrentArena());
	    		int x,y,z;

	    		x = c.getInt("spawn.x");
	    		y = c.getInt("spawn.y");
	    		z = c.getInt("spawn.z");

	    		Location l = new Location(ins.getServer().getWorld(ins.getConfig().getString("map-world")),x,y,z);
	    		try{
	        	((LivingEntity)sender).teleport(l);
	    		}catch(Exception e){
	    			
	    		}
	        }
	        
	        
	        return false;
	 }
	
}
