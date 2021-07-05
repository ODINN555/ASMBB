package me.ODINN.ASMBB;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BuilderMain extends JavaPlugin {

     public FileConfiguration config;
     public BuilderUtility util;

     public static BuilderMain instance = (BuilderMain) Bukkit.getPluginManager().getPlugin("ASMBB");

    @Override
    public void onEnable(){

     saveDefaultConfig();
     this.config = getConfig();
     this.util = new BuilderUtility(this);
     getServer().getPluginManager().registerEvents(new EventsHandler(this),this);
     getServer().getPluginManager().registerEvents(new GUIManager(this),this);
     LayoutManager.initializeLayouts();
      instance = (BuilderMain) Bukkit.getPluginManager().getPlugin("ASMBB");
      this.getCommand("ASMBB").setTabCompleter(new ASMBBTabCompleter());

    }

    @Override
    public void onDisable(){

       util.saveProjects();

    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("asmbb")){
            // checking if its a player
            if(!(sender instanceof Player)){
                System.out.println(util.getMessage(ConfigUsages.NotPlayer));
                return true;
            }

            Player player = (Player) sender;
            if(args.length == 0){
                player.sendMessage(util.getMessage(ConfigUsages.NotEnoughArguments));
                return true;
            }




                // Command: /asmbb new , creates a new project
            if(args[0].equalsIgnoreCase("new")){

                // checks if the amount of arguments is right

                if(args.length < 2){
                    player.sendMessage(util.getMessage(ConfigUsages.NotEnoughArguments));
                    return true;
                }

                if(args.length > 2){
                    player.sendMessage(util.getMessage(ConfigUsages.TooManyArguments));
                    return true;
                }

                if(config.contains("ASMBB.Projects."+args[1])){
                    player.sendMessage(util.getMessage(ConfigUsages.NameAlreadyExists));
                    return true;
                }

                Location loc = player.getLocation();
                loc.setY(util.getBuildingYFromConfig());


                util.createNewProject(args[1],player,loc.getBlock());
                return true;




            }

            // Command: /asmbb load , loads a project from the config
            if(args[0].equalsIgnoreCase("load")){

                if(args.length < 2){
                    player.sendMessage(util.getMessage(ConfigUsages.NotEnoughArguments));
                    return true;
                }

                if(args.length > 2){
                    player.sendMessage(util.getMessage(ConfigUsages.TooManyArguments));
                    return true;
                }

                String name = args[1];


                if(!getConfig().contains("ASMBB.Projects."+name)){
                    player.sendMessage(ChatColor.RED+"There is no project with this name!");
                    return true;
                }

                if(BuilderUtility.projects.containsKey(player.getUniqueId()) && !util.getProject(player).getName().equalsIgnoreCase(name)) {
                    player.sendMessage(ChatColor.RED + "You are already working on a project!");
                    return true;
                }

                Location loc = player.getLocation();
                loc.setY(util.getBuildingYFromConfig());
                util.createNewProject(name,player,loc.getBlock());

                if(!BuilderUtility.projects.containsKey(player.getUniqueId()))
                    return true;

                Project project = util.getProject(player);

                getConfig().getConfigurationSection("ASMBB.Projects."+name+".ArmorStands").getKeys(false).forEach(key ->{
                    project.addArmorStand(util.deserializeStand(name,Integer.parseInt(key),loc.getBlock()));
                });

                player.sendMessage(ChatColor.GREEN+"Loaded project successfully!");
                return true;

            }

            // Command /asmbb help/commands , sends the list of commands to the player
            if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("commands")){

                String msg = ChatColor.GOLD+"ASMBB Command List" + ChatColor.RESET;
                msg += "\n";
                msg += ChatColor.BLUE + "----------" + ChatColor.RESET;
                msg += "\n";
                msg += "\n";
                msg += ChatColor.translateAlternateColorCodes('&',"&6/ASMBB new [name] &9- creates a new project for you to work on.");
                msg += "\n";
                msg += " ";
                msg += "\n";
                msg += ChatColor.translateAlternateColorCodes('&',"&6/ASMBB load [name] &9- Loads a project from the file system for you to work on.");
                msg += "\n";
                msg += " ";
                msg += "\n";
                msg += ChatColor.translateAlternateColorCodes('&', "&6/ASMBB delete [name] &9- Deletes a project.");
                msg += "\n";
                msg += " ";
                msg += "\n";
                msg += ChatColor.translateAlternateColorCodes('&', "&6/ASMBB build [name] &9- Builds the multi block where your standing. for more building actions you need to code using this plugin as instructed.");
                msg += "\n";
                msg += " ";
                msg += "\n";
                msg += ChatColor.translateAlternateColorCodes('&', "&6/ASMBB help &9- shows this command list");
                msg += "\n";
                msg += " ";
                msg += "\n";
                msg += ChatColor.BLUE + "----------" + ChatColor.RESET;

                player.sendMessage(msg);
                return true;
            }

            if(args[0].equalsIgnoreCase("delete")){

                if(args.length < 2){
                    player.sendMessage(util.getMessage(ConfigUsages.NotEnoughArguments));
                    return true;
                }

                if(args.length > 2){
                    player.sendMessage(util.getMessage(ConfigUsages.TooManyArguments));
                    return true;
                }

                if(BuilderUtility.projects.containsKey(player.getUniqueId())){
                    player.sendMessage(ChatColor.RED+"You must finish your project before deleting one.");
                    return true;
                }

                String name = args[1];

                if(!getConfig().contains("ASMBB.Projects."+name)){
                    player.sendMessage(ChatColor.RED+"There is no project with this name!");
                    return true;
                }

                config.set("ASMBB.Projects."+name,null);
                saveConfig();

                player.sendMessage(ChatColor.GREEN+"Project deleted successfully!");
                return true;

            }


            if(args[0].equalsIgnoreCase("build")){
                if(args.length < 2){
                    player.sendMessage(util.getMessage(ConfigUsages.NotEnoughArguments));
                    return true;
                }

                if(args.length > 2){
                    player.sendMessage(util.getMessage(ConfigUsages.TooManyArguments));
                    return true;
                }

                String name = args[1];

                if(!getConfig().contains("ASMBB.Projects."+name)){
                    player.sendMessage(ChatColor.RED+"There is no project with this name!");
                    return true;
                }

                util.buildProject(args[1], player.getLocation().getBlock(), player,null,null);
                player.sendMessage(ChatColor.GREEN+"The project has been built!");

            }



        }



        return false;
    }


    /**
     *
     * @return the instance of this main class
     */
    public static BuilderMain getInstance(){
        return (BuilderMain) Bukkit.getPluginManager().getPlugin("ASMBB");
    }













}
