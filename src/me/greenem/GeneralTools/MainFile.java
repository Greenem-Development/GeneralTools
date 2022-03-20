package me.greenem.GeneralTools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class MainFile extends JavaPlugin implements Listener {
	
	Logger log = getLogger();
	public File invsSaves;
	
	@Override
	public void onDisable() {
		log.info(ChatColor.RED + "GeneralTools plugin has been disabled.");
	}

	@Override
	public void onEnable() {
		log.info(ChatColor.GREEN + "GeneralTools plugin has been enabled!");
		getServer().getPluginManager().registerEvents(this, this);
		try {
			invsSaves = new File(getServer().getWorldContainer().getCanonicalPath() + "\\files\\invsaves");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//for(Map.Entry<String, PlayerData>entity : players.entrySet()) {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player p = getServer().getPlayer(sender.getName());
		
		if(cmd.getName().equalsIgnoreCase("gm")){
			GmCommand(sender, args);
		}
		else if(cmd.getName().equalsIgnoreCase("heal")){
			HealCommand(p, args);
		}
		else if(cmd.getName().equalsIgnoreCase("loc")){
			//sendActionBar(p, p.getLocation().toString());
			p.sendMessage(ChatColor.ITALIC + "" + ChatColor.WHITE + "Your location: " + ChatColor.AQUA + "" + p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ() + " ");
			p.sendMessage(ChatColor.ITALIC + "" + ChatColor.WHITE + "Your direction: " + ChatColor.LIGHT_PURPLE + "" + GetPlayerFacing(p));
		}
		if(cmd.getName().equalsIgnoreCase("invsave")){
			try {
				saveInventory(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(cmd.getName().equalsIgnoreCase("invload")){
			try {
				loadInventory(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void GmCommand(CommandSender sender, String[] args) {
		Player p = getServer().getPlayer(sender.getName());
		if(p.isOp() == true){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "Please, insert gamemode number.");
			}
			else if(args.length == 1){
				if(args[0].equalsIgnoreCase("0")){
					p.setGameMode(GameMode.SURVIVAL);
				}
				else if(args[0].equalsIgnoreCase("1")){
					p.setGameMode(GameMode.CREATIVE);
				}
				else if(args[0].equalsIgnoreCase("2")){
					p.setGameMode(GameMode.ADVENTURE);
				}
				else if(args[0].equalsIgnoreCase("3")){
					p.setGameMode(GameMode.SPECTATOR);
				}
				else {
					sender.sendMessage(ChatColor.RED + "Please, insert correct gamemode number.");
				}
			}
			else if(args.length == 2){
				if(getServer().getPlayer(args[1]) != null){
					if(args[0].equalsIgnoreCase("0")){
						getServer().getPlayer(args[1]).setGameMode(GameMode.SURVIVAL);
					}
					else if(args[0].equalsIgnoreCase("1")){
						getServer().getPlayer(args[1]).setGameMode(GameMode.CREATIVE);
					}
					else if(args[0].equalsIgnoreCase("2")){
						getServer().getPlayer(args[1]).setGameMode(GameMode.ADVENTURE);
					}
					else if(args[0].equalsIgnoreCase("3")){
						getServer().getPlayer(args[1]).setGameMode(GameMode.SPECTATOR);
					}
					else {
						sender.sendMessage(ChatColor.RED + "Please, insert correct gamemode number.");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + "Such player doesn't exist.");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Please, type the command in such order: /gm <number 0-3> <player>.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You can't use this command.");
		}
	}
	
	public void HealCommand(Player p, String[] args) {
		if(args.length == 0){
			p.setHealth(20);
			p.setFoodLevel(20);
		}
		else if(args.length == 1){
			if(getServer().getPlayer(args[0]) != null){
				getServer().getPlayer(args[0]).setHealth(20);
				getServer().getPlayer(args[0]).setFoodLevel(20);
			}
			else {
				p.sendMessage(ChatColor.RED + "Such player doesn't exist.");
			}
		}
		else {
			p.sendMessage(ChatColor.RED + "Please, type the command in such order: /heal <player>.");
		}
	}

	public void AddItemStack(Player p, ItemStack ItemStack, int slot) {
		if(!p.getInventory().contains(ItemStack)) {
			if(p.getInventory().getItem(slot) == null) p.getInventory().setItem(slot, ItemStack);
			else p.getInventory().addItem(ItemStack);
		}
	}

	public void RemoveMaterial(Player p, Material material) {
		if(p.getInventory().contains(material)) {
			p.getInventory().remove(material);
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
	    // only got here if we didn't return false
	    return true;
	}
   
	private static String GetPlayerFacing(Player p) {
		double rot = (p.getLocation().getYaw() - 90) % 360-90;
		if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 22.5) {
            return "North (Север)";
        } else if (22.5 <= rot && rot < 67.5) {
            return "Northeast (Северо-восток)";
        } else if (67.5 <= rot && rot < 112.5) {
            return "East (Восток)";
        } else if (112.5 <= rot && rot < 157.5) {
            return "Southeast (Юго-восток)";
        } else if (157.5 <= rot && rot < 202.5) {
            return "South (Юг)";
        } else if (202.5 <= rot && rot < 247.5) {
            return "Southwest (Юго-запад)";
        } else if (247.5 <= rot && rot < 292.5) {
            return "West (Запад)";
        } else if (292.5 <= rot && rot < 337.5) {
            return "Northwest (Северо-Запад)";
        } else if (337.5 <= rot && rot < 360.0) {
            return "North (Север)";
        } else {
            return null;
        }
	}

	public void saveInventory (Player p) throws IOException {
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.armor", p.getInventory().getArmorContents());
		c.set("inventory.content", p.getInventory().getContents());
		c.save(new File(invsSaves, p.getName()+".yml"));
	}

	private void loadInventory(Player p) throws IOException {
//		if(!p.isOp()) {
//			p.sendMessage("At the moment you can only save your inventory. If you have lost your items because of some kind of autorization plugin bug, don't overwrite your invenroty save and contact the admins.");
//		}
		//File file = new File(inventoriesDirectory, p.getName()+".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(invsSaves, p.getName()+".yml"));
		ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
		p.getInventory().setArmorContents(content);
		content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
		p.getInventory().setContents(content);
	}
}