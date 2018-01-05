package me.gmx.arenaswitcher;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrivateCommands implements CommandExecutor{
private ArenaSwitcher ins;


public PrivateCommands(ArenaSwitcher inst){
	ins=inst;
}
	
	



	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("wzarenavote")){
			if (ins.ahandler.alreadyVoted.contains(((Player)sender).getName())){
				return false;
			}
		switch(args.length){
		case 1:
			if (ins.ahandler.arenaList.contains(args[0])){
				Player p = (Player)sender;
				if (sender.hasPermission("wzarena.voteweight.1")){
				ins.ahandler.mapVote(args[0], 1);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				ins.ahandler.alreadyVoted.add(((Player)sender).getName());
				return true;
				}else
				if (sender.hasPermission("wzarena.voteweight.2")){
					ins.ahandler.mapVote(args[0], 2);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					ins.ahandler.alreadyVoted.add(((Player)sender).getName());
					return true;
				}else
				if (sender.hasPermission("wzarena.voteweight.3")){
					ins.ahandler.mapVote(args[0], 3);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					ins.ahandler.alreadyVoted.add(((Player)sender).getName());
					return true;
				}else
				if (sender.hasPermission("wzarena.voteweight.4")){
					ins.ahandler.mapVote(args[0], 4);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					ins.ahandler.alreadyVoted.add(((Player)sender).getName());
					return true;
				}else{
						ins.ahandler.mapVote(args[0], 1);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						ins.ahandler.alreadyVoted.add(((Player)sender).getName());
						return true;
				}
				
			}
			
		
		
		}
		
		
		}
		
		
		
		
		
		
		
		return false;
    }
}
