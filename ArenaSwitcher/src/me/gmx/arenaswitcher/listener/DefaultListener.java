package me.gmx.arenaswitcher.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.spawn.IEssentialsSpawn;

import me.gmx.arenaswitcher.ArenaBoard;
import me.gmx.arenaswitcher.ArenaSwitcher;
import net.md_5.bungee.api.ChatColor;

public class DefaultListener implements Listener{

	private ArenaSwitcher ins;
	public DefaultListener(ArenaSwitcher i){
		ins=i;
	}
	
	
	@EventHandler
	public void inventoryInteract(InventoryClickEvent e){
		if (!e.getClickedInventory().getName().equals(ins.getConfig().get("gui-name"))){
			return;
		}
		e.setCancelled(true);
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem().getType().equals(Material.BLAZE_ROD)){
			p.closeInventory();
			p.sendMessage(ins.prefix + ChatColor.RED + " You have voted for: " + ChatColor.RED + e.getCurrentItem().getItemMeta().getDisplayName());
			if (p.hasPermission("arenaswitcher.voteweight.1")){
				ins.ahandler.alreadyVoted.add(p.getName());
			ins.ahandler.mapVote(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), 1);
			}else if (p.hasPermission("arenaswitcher.voteweight.2")){
				ins.ahandler.alreadyVoted.add(p.getName());
				ins.ahandler.mapVote(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), 2);
				}else if (p.hasPermission("arenaswitcher.voteweight.3")){
					ins.ahandler.alreadyVoted.add(p.getName());
					ins.ahandler.mapVote(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), 3);
				}else if (p.hasPermission("arenaswitcher.voteweight.4")){
					ins.ahandler.alreadyVoted.add(p.getName());
					ins.ahandler.mapVote(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), 4);
				}
			}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void playerRespawn(PlayerRespawnEvent e){
		final Player p = e.getPlayer();
		if (ins.ahandler.getCurrentArena() != null){
			new BukkitRunnable(){
				
				public void run(){
					p.teleport(ins.ahandler.spawn);
				}
			}.runTaskLater(ins,50);
		}
	}
	
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerMove(PlayerMoveEvent e){
		if (ins.ahandler.getCurrentArena() == null){
			return;
		}
		Player p = e.getPlayer();
		Location to = e.getTo();
		if (ins.getConfig().getInt("teleport-region.min.x") <= to.getBlockX()){
			
			
		 if (ins.getConfig().getInt("teleport-region.min.z") <= to.getBlockZ()){
		 
			
			

		if (ins.getConfig().getInt("teleport-region.max.x") >= to.getBlockX()){
			
			
			if (ins.getConfig().getInt("teleport-region.max.z") >= to.getBlockZ()){
					p.teleport(ins.ahandler.spawn);
			 }
		}
		 }

		 

		}
		
	}
	
	
	 @EventHandler
	    public void onPlayerJoin(PlayerJoinEvent e) {
	        e.getPlayer().teleport(((IEssentialsSpawn)Bukkit.getPluginManager().getPlugin("EssentialsSpawn")).getSpawn("default"));
	        new ArenaBoard(ins);
			e.getPlayer().setScoreboard(ArenaBoard.aBoard());
	    }
	
	
}
