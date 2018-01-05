package me.gmx.arenaswitcher.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.gmx.arenaswitcher.ArenaSwitcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketDataSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutCustomPayload;

public class InvenUtil {
	private static ArenaSwitcher ins;
	public InvenUtil(ArenaSwitcher inst){
		ins=inst;
	}
	public static Inventory createInventory(){
		ItemStack[] itemstacks = new ItemStack[ins.ahandler.arenaList.size()];
		int c=0;
		for (String s : ins.ahandler.arenaList){
			ArrayList<String> lore = new ArrayList<String>();
			ConfigurationSection config = ins.getConfig().getConfigurationSection("arenas." + s);
			itemstacks[c] = new ItemStack(Material.getMaterial(config.getString("gui-block")));
			ItemMeta temp = itemstacks[c].getItemMeta();
			temp.setDisplayName(ChatColor.GREEN + s);
			lore.add(config.getString("lore"));
			lore.add(String.valueOf(ins.ahandler.votes.get(s)) + " votes");
			
			itemstacks[c].setItemMeta(temp);
					c++;
		}
		
		Inventory i = Bukkit.createInventory(null, ins.getConfig().getInt("gui-rows")*9,ins.getConfig().getString("gui-name"));
	
		for (ItemStack item : itemstacks){
			i.setItem(ins.getConfig().getInt("arenas." + ChatColor.stripColor(item.getItemMeta().getDisplayName()).toString()+".gui-item-slot"), item);
		}
		return i;
		
	}

	
	
	@SuppressWarnings("unchecked")
	public static ItemStack createBook() {
		ItemStack btoopen = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta)btoopen.getItemMeta();
		List<IChatBaseComponent> pages = null;
		bookMeta.setTitle("WarZone");
		bookMeta.setAuthor("GMX");
		
		//pages
		try{
			pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
		}catch(ReflectiveOperationException ex){
			ex.printStackTrace();
		}	
		//lines on a page
		ArrayList<TextComponent> lines = new ArrayList<TextComponent>();

		
		//list of page-text
		List<ArrayList<TextComponent>> pgs = new ArrayList<ArrayList<TextComponent>>();
		
		
		for (String s : ins.ahandler.arenaList){
			ConfigurationSection config = ins.getConfig().getConfigurationSection("arenas." + s);
			
			if (lines.size()<=10){
				
				
				//
				if (config.getString("book-text")!=null){
					lines.add(new TextComponent(ChatColor.BOLD + config.getString("book-text")));
					lines.add(new TextComponent("\n"));
					lines.add(new TextComponent("\n"));
				}else{
					lines.add(new TextComponent(ChatColor.BOLD + s));
					lines.add(new TextComponent("\n"));
					lines.add(new TextComponent("\n"));
				}
				//


			}else{
				if (config.getString("book-text")!=null){
					lines.add(new TextComponent(ChatColor.BOLD + config.getString("book-text")));
					lines.add(new TextComponent("\n"));
					lines.add(new TextComponent("\n"));
				}else{
					lines.add(new TextComponent(ChatColor.BOLD + s));
					lines.add(new TextComponent("\n"));
					lines.add(new TextComponent("\n"));
				}
				pgs.add(((ArrayList<TextComponent>) lines.clone()));
				lines.clear();
			} 
		}
		if (pgs.isEmpty()){
			pgs.add(lines);
		}
		//t.add(new TextComponent(sb.toString()));
	      TextComponent header = new TextComponent(ins.getConfig().getString("vote-book-1st-line")+"\n");
	      TextComponent headerSpacer = new TextComponent("\n");
	        //IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(header));
	        ArrayList<IChatBaseComponent> pageList = new ArrayList<IChatBaseComponent>();
	        
	        for (ArrayList<TextComponent> pg : pgs){
	        	IChatBaseComponent tPage = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(header));
	            tPage.a().add(IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(headerSpacer)));
		for (TextComponent text : pg){
			text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ChatColor.stripColor("/wzarenavote " + text.getText().toString())));
			text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote for this arena").create()));
			
            tPage.a().add(IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text)));
		
			
			//pages.add((ChatSerializer.a(ComponentSerializer.toString(text))));
			}
		pageList.add(tPage);
	        }
	        
	        for (IChatBaseComponent pa : pageList){
	    		pages.add(pa);

	        }
		bookMeta.setTitle("vote");
		bookMeta.setAuthor("GMX");
		btoopen.setItemMeta(bookMeta);        
		return btoopen;

		
	}
		public static void openBook(ItemStack itemStack, Player p) {
	        int slot = p.getInventory().getHeldItemSlot();
	        org.bukkit.inventory.ItemStack old = p.getInventory().getItem(slot);
	        p.getInventory().setItem(slot, CraftItemStack.asCraftCopy(itemStack));

	        ByteBuf buf = Unpooled.buffer(256);
	        buf.setByte(0, (byte)0);
	        buf.writerIndex(1);

	        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
	        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	        p.getInventory().setItem(slot, old);
	    }
	
	
}
