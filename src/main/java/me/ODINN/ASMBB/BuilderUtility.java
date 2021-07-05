package me.ODINN.ASMBB;

import Events.MultiBlockCreationEvent;
import Events.MultiBlockDeleteEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class BuilderUtility {

    public static Map<UUID,Project> projects = new HashMap<UUID, Project>();

    BuilderMain main;

    /**
     *
     * @param main a main refrence
     */
    public BuilderUtility(BuilderMain main){
       this.main = main;
    }

    /**
     *
     * @param usage the usage of the message, this is used for the path of the message in the config.yml
     * @return the message for the usage
     */
    public String getMessage(ConfigUsages usage){
         return ChatColor.translateAlternateColorCodes('&',""+main.config.getString("Messages."+usage.getUsage()));
    }

    /**
     *
     * @param loc a given location
     * @return if the block at the location is compatible for building (if its air)
     */
    public boolean isAreaForBuilding(Location loc){
        Block block = loc.getBlock();
        return block.getType() == Material.AIR;
    }

    /**
     * creates a new project
     * @param name the project's name
     * @param player the project's owner
     */
    public void createNewProject(String name,Player player,Block block){

        Location loc = block.getLocation();
        Project project = new Project(name,player,loc);
        projects.put(player.getUniqueId(), project);

        if(!isAreaForBuilding(loc)){
            player.sendMessage(getMessage(ConfigUsages.IncompatibleArea));
            return;
        }
        Location newLoc = loc.clone();
        newLoc.setY(loc.getY() -1);
        Block secBlock = newLoc.getBlock();
        secBlock.setType(Material.QUARTZ_BLOCK);

        Location playerLoc = player.getLocation();
        playerLoc.setY(200);
        player.teleport(playerLoc);
        player.setGameMode(GameMode.CREATIVE);
        LayoutManager.getLayoutByStage("Menu").setLayoutToPlayer(player);






    }

    /**
     * saves all the projects
     */
    public void saveProjects() {

        if(!projects.isEmpty())
        projects.forEach((uuid,project) -> {
            Project project1 = project;
            saveProject(project1, uuid);
        });
    }

    /**
     * saves the project into the file and setting the player's hotbar back to normal
     * @param project a given project
     * @param playerID a given player uuid
     */
    public void saveProject(Project project, UUID playerID){



            //saving the project
            if(project.getCurrentStand() != null) {
                project.addArmorStand(project.getCurrentStand());
                project.setCurrentStand(null);
            }

            main.config.set("ASMBB.Projects."+project.getName()+".ArmorStands",null);

            for(int i = 0 ; i < project.getArmorStandsAmount(); i ++){
                serializeArmorStand(project,i);
                project.getArmorStandByIndex(i).remove();
            }
        Location block = project.getMainPos().clone();
            block.setY(block.getY()-1);
            block.getBlock().setType(Material.AIR);

        // settings the player hotbar back to normal
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerID);
        project.getPlayerLayout().setLayoutToPlayer(player);

        projects.remove(playerID);

    }




    /**
     * serializes the armor stand in the file for the given project
     * @param project a given project
     * @param indexOfStand the index of the armor stand in the project
     */
    public void serializeArmorStand(Project project,int indexOfStand) {

        FileConfiguration file = main.config;
        ArmorStand stand = project.getArmorStandByIndex(indexOfStand);
        if(stand == null){
            onError("Failed to deserialize Armor Stand: the armor stand from the index in the project is null.");
            return;
        }

        String path = "ASMBB.Projects." + project.getName() + ".ArmorStands." + indexOfStand + ".";

        // saving each value to config
        // GENERAL SETTINGS:
        file.set(path+"posX",calculateTrueDistance(project.getMainPos().getX(),stand.getLocation().getX()));
        file.set(path+"posY",calculateTrueDistance(project.getMainPos().getY(),stand.getLocation().getY()));
        file.set(path+"posZ",calculateTrueDistance(project.getMainPos().getZ(),stand.getLocation().getZ()));
        file.set(path+"yaw",stand.getLocation().getYaw());
        file.set(path+"pitch",stand.getLocation().getPitch());
        file.set(path+"BasePlate",stand.hasBasePlate());
        file.set(path+"Invisible",stand.isVisible());
        file.set(path+"Invulnerable",stand.isInvulnerable());
        file.set(path+"name",stand.getName());
        file.set(path+"small",stand.isSmall());
        file.set(path+"Gravity",stand.hasGravity());
        file.set(path+"Marker",stand.isMarker());
        file.set(path+"Glowing",stand.isGlowing());

        EntityEquipment equip = stand.getEquipment();

        //EQUIPMENT
        serializeItem(path+"equipment.helmet",equip.getHelmet());
        serializeItem(path+"equipment.chestplate",equip.getChestplate());
        serializeItem(path+"equipment.leggings",equip.getLeggings());
        serializeItem(path+"equipment.boots",equip.getBoots());
        serializeItem(path+"equipment.offhand",equip.getItemInOffHand());
        serializeItem(path+"equipment.mainhand",equip.getItemInMainHand());



        //HEAD
        String headPath = path+"head.";
        file.set(headPath+"poseX",stand.getHeadPose().getX());
        file.set(headPath+"poseY",stand.getHeadPose().getY());
        file.set(headPath+"poseZ",stand.getHeadPose().getZ());

        //BODY
        String bodyPath = path+"body.";
        file.set(bodyPath+"poseX",stand.getBodyPose().getX());
        file.set(bodyPath+"poseY",stand.getBodyPose().getY());
        file.set(bodyPath+"poseZ",stand.getBodyPose().getZ());

        //LEFT ARM
        String LArmPath = path+"LeftArm.";
        file.set(LArmPath+"poseX",stand.getLeftArmPose().getX());
        file.set(LArmPath+"poseY",stand.getLeftArmPose().getY());
        file.set(LArmPath+"poseZ",stand.getLeftArmPose().getZ());

        //RIGHT ARM
        String RArmPath = path+"RightArm.";
        file.set(RArmPath+"poseX",stand.getRightArmPose().getX());
        file.set(RArmPath+"poseY",stand.getRightArmPose().getY());
        file.set(RArmPath+"poseZ",stand.getRightArmPose().getZ());

        //LEFT LEG
        String LLegPath = path+"LeftLeg.";
        file.set(LLegPath+"poseX",stand.getLeftLegPose().getX());
        file.set(LLegPath+"poseY",stand.getLeftLegPose().getY());
        file.set(LLegPath+"poseZ",stand.getLeftLegPose().getZ());

        //RIGHT LEG
        String RLegPath = path+"RightLeg.";
        file.set(RLegPath+"poseX",stand.getLeftLegPose().getX());
        file.set(RLegPath+"poseY",stand.getLeftLegPose().getY());
        file.set(RLegPath+"poseZ",stand.getLeftLegPose().getZ());

       main.saveConfig();
    }

    /**
     *
     * @param mainPos the main position, the starting point
     * @param newPos the second position
     * @return the distance between the main position to the second position
     */
        public double calculateTrueDistance(double mainPos, double newPos){
        return newPos - mainPos;
        }

    /**
     * sends an error msg when an error occurs and the stack trace of an exception
     * @param msg the error msg to send
     * @param e the Exception
     */
    public void onError(String msg,Exception e){
        Bukkit.getServer().getLogger().log(Level.SEVERE, ChatColor.RED + msg);
        e.printStackTrace();
        Bukkit.broadcastMessage(ChatColor.RED +"[ASMBB]: "+ msg);
    }

    /**
     * sends an error msg when an error occurs
     * @param msg the error msg to send
     */
    public void onError(String msg){
        Bukkit.getServer().getLogger().log(Level.SEVERE, ChatColor.RED + msg);
        Bukkit.broadcastMessage(ChatColor.RED +"[ASMBB]: "+ msg);

    }

    /**
     *
     * @param file a given file
     * @param path the path to the pose value
     * @return the pose value from the given path
     */
    public EulerAngle getPoseFromFile(FileConfiguration file, String path){
        try {
            EulerAngle pose = new EulerAngle(file.getDouble(path + "poseX"), file.getDouble(path + "poseY"), file.getDouble(path + "poseZ"));
            return pose;
        }catch (Exception e){
            onError("Could not retrieve data for EulerAngle from the file.",e);
            return null;
        }

    }

    /**
     *
     * @param projectName a project's name
     * @param num the armor stand number in the project
     * @param block the block which the armor stand will spawn on
     * @return the deserialization of the armor stand from file with data for the persistent data container of the armor stand from another plugin's Main
     */
    public ArmorStand deserializeStand(String projectName, int num, Block block, Plugin plugin, Map<String,Object> data){
        String path = "ASMBB." +"Projects." + projectName +".ArmorStands." +num+".";
        Location mainPos = block.getLocation();
        World world = mainPos.getWorld();

        FileConfiguration file = main.config;
        //LOCATION
        double x = mainPos.getX() + file.getDouble(path+"posX");
        double y = mainPos.getY() + file.getDouble(path + "posY");
        double z = mainPos.getZ() + file.getDouble(path+"posZ");
        float yaw = (float) file.getDouble(path +"yaw");
        float pitch = (float) file.getDouble(path+"pitch");

        ArmorStand stand = world.spawn(new Location(world,x,y,z,yaw,pitch), ArmorStand.class);


        //SETTINGS
        stand.setArms(true);
        stand.setGravity(false);
        stand.setBasePlate(file.getBoolean(path +"BasePlate"));
        stand.setVisible(file.getBoolean(path+"Invisible"));
        stand.setInvulnerable(file.getBoolean(path+"Invulnerable"));
        stand.setCustomName(file.getString(path+"name"));
        stand.setSmall(file.getBoolean(path+"small"));
        stand.setGravity(file.getBoolean(path+"Gravity"));
        stand.setMarker(file.getBoolean(path+"Marker"));
        stand.setGlowing(file.getBoolean(path+"Glowing"));

    //EQUIPMENT
        EntityEquipment equip = stand.getEquipment();
        equip.setBoots(deserializeItem(path+"equipment.boots"));
        equip.setHelmet(deserializeItem(path+"equipment.helmet"));
        equip.setLeggings(deserializeItem(path+"equipment.leggings"));
        equip.setChestplate(deserializeItem(path+"equipment.chestplate"));
        equip.setItemInMainHand(deserializeItem(path+"equipment.mainhand"));
        equip.setItemInOffHand(deserializeItem(path+"equipment.offhand"));

    //POSES
        stand.setHeadPose(getPoseFromFile(file,path+"head."));
        stand.setBodyPose(getPoseFromFile(file,path+"body."));
        stand.setLeftArmPose(getPoseFromFile(file,path+"LeftArm."));
        stand.setRightArmPose(getPoseFromFile(file,path+"RightArm."));
        stand.setLeftLegPose(getPoseFromFile(file,path+"LeftLeg."));
        stand.setRightLegPose(getPoseFromFile(file,path+"RightLeg."));

        stand.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"ASMBB-NAME"), PersistentDataType.STRING,projectName);


        if(data != null){

            data.forEach( (key,value) ->{

                if(plugin == null)
                    stand.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),key), new Data(value.getClass()),value);
                else
                    stand.getPersistentDataContainer().set(new NamespacedKey(plugin,key), new Data(value.getClass()),value);
            });

        }


        return stand;
    }

    /**
     *
     * @param projectName a given project
     * @param num the armor stand index
     * @param block the mainPos block
     * @param data the data to put into the Persistent Data Container of the armor stand
     * @return the deserialization of the armor stand from the file with data for the persistent data container of the armor stand
     */
    public ArmorStand deserializeStand(String projectName, int num, Block block,Map<String,Object> data){
        return deserializeStand(projectName, num, block, null, data);
    }

    /**
     *
     * @param projectName a given project
     * @param num the armor stand index
     * @param block the mainPos block
     * @return the armor stand from the file with the given index
     */
    public ArmorStand deserializeStand(String projectName, int num, Block block){
        return deserializeStand(projectName, num, block, null, null);
    }





    /**
     * serializes an item stack into the file with the given path
     * @param path the path inside the file to the item's location
     * @param item a given item
     */
    public void serializeItem( String path, ItemStack item){
        FileConfiguration file = main.config;
          if(item == null || item.getType() == Material.AIR)
              return;
        try{

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(byteOut);
            bukkitOut.writeObject(item);
            bukkitOut.flush();

            file.set(path,byteOut.toByteArray());
            main.saveConfig();
            return;


        }catch (Exception e){
            onError("Could not serialize itemstack to file: ",e);
            return;
        }



    }

    /**
     *
     * @param path the path to the item in file
     * @return the deserialization of the item from the file
     */
    public ItemStack deserializeItem( String path){
        FileConfiguration file = main.config;
        if(!file.contains(path))
            return new ItemStack(Material.AIR);

        try{

            ByteArrayInputStream byteIn = new ByteArrayInputStream((byte[]) file.get(path));
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(byteIn);

            return (ItemStack) bukkitIn.readObject();

        }catch (Exception e){
            onError("Could not deserialize itemstack from file: ",e);
            return null;
        }
    }





    /**
     * adds / subtracts to the pose of the given layout path body part and xyz value
     * @param stand a given armor stand
     * @param stage the layout path
     * @param value the value to add / subtract
     *
     */
    public void addValueToPose(ArmorStand stand,String stage,double value){

        if(!stage.contains("Pose"))
            return;

        String[] stageAsArray = getStageAsArray(stage);
        String xyz = stageAsArray[3];


        switch(stageAsArray[2]){

            case "HEAD":
                stand.setHeadPose(addToPose(stand.getHeadPose(),value,xyz));
                return;

            case "BODY":
                stand.setBodyPose(addToPose(stand.getBodyPose(),value,xyz));
                return;

            case "LeftArm":
                stand.setLeftArmPose(addToPose(stand.getLeftArmPose(),value,xyz));
                return;

            case "RightArm":
                stand.setRightArmPose(addToPose(stand.getRightArmPose(),value,xyz));
                return;

            case "LeftLeg":
                stand.setLeftLegPose(addToPose(stand.getLeftLegPose(),value,xyz));
                return;

            case "RightLeg":
                stand.setRightLegPose(addToPose(stand.getRightLegPose(),value,xyz));
                return;


            default:
                return;


        }
    }

    /**
     *
     * @param angle a given angle
     * @param value the value which is added to the angle
     * @param xyz x / y / z value
     * @return the given angle with an added value for the specified xyz value
     */
    public EulerAngle addToPose(EulerAngle angle,double value,String xyz){



        switch(xyz){
            case "x":
                return angle.setX(angle.getX() + value);


            case "y":
                return angle.setY(angle.getY() + value);


            case "z":
                return angle.setZ(angle.getZ() + value);


            default:
                onError("Could not recognize xyz value in an addToPose method. this is a coding mistake, pls fix it. xyz value: "+xyz);
                return null;


        }

    }

    /**
     *
     * @param player the owner of a project
     * @return the project which the given player is making
     */
    public Project getProject(Player player){
        return projects.get(player.getUniqueId());
    }

    /**
     *
     * @param stage a given stage value
     * @return an array which contains the stage values
     */
    public String[] getStageAsArray(String stage){
        return stage.split("@");
    }



    /**
     * adds the value to the position of the armor stand with the given stage path value (x, y or z)
     * @param stand a given armor stand
     * @param value a given value
     * @param stage the stage path to a certain layout
     */
    public void addToStandPos(ArmorStand stand, double value,String stage) {
        Location loc = stand.getLocation();

        if(!stage.contains("Pos"))
            return;

        String xyz = getStageAsArray(stage)[2];

        switch(xyz){
            case "x":
                loc.setX(loc.getX() + value);
                break;

            case "y":
                loc.setY(loc.getY() + value);
                break;

            case "z":
                loc.setZ(loc.getZ() + value);
                break;

            default:
                onError("Could not recognize xyz value in an addToStandPos method. this is a coding error, pls fix it. xyz value: "+xyz);
                return;
        }

        stand.teleport(loc);


    }

    public void addToStandRotation(ArmorStand stand, float value,String stage){

        if(!stage.contains("Rotation"))
            return;

        String pitchYaw = getStageAsArray(stage)[2];

                Location loc = stand.getLocation();

        switch (pitchYaw){

            case "pitch":
                loc.setPitch(loc.getPitch() + value);
                break;

            case "yaw":
                loc.setYaw(loc.getYaw() + value);
                break;

            default:
                onError("Could not set rotation to armor stand because the stage path was not pitch or yaw. Stage: "+stage);

        }

        stand.teleport(loc);


    }

    /**
     *
     * @return the AddValue from the config
     */
    public double getAddingValueFromConfig(String fromWhat){

        String path = "Settings.";
        switch (fromWhat){
            case "Pos":
                path += "PosAddValue";
                break;

            case "Pose":
                path += "PoseAddValue";
                break;

            case "Rotation":
                path += "RotationAddValue";
                break;

        }


                if(!main.config.contains(path)){
                    onError("Could not retrieve adding value from config because the path doesn't exist. Path: "+path);
                    return 0.1;
                }

                try {
                    double value = main.config.getDouble(path);


                    if (value < 0)
                        return value * -1;

                    return value;
                }catch (Exception e){
                    onError("The adding value in the config is not double!");
                    return 0.1;
                }

    }

    /**
     *
     * @return the BiggerADDMultiplier value from the config
     */
    public int getBiggerAddMultiplierFromConfig(){
        if(!main.config.contains("Settings.BiggerAddMultiplier")){
            onError("Could not retrieve adding value from config because the path doesn't exist.");
            return 10;
        }
        try {
            int value = main.config.getInt("Settings.BiggerAddMultiplier");

            if (value < 0)
                return value * -1;

            return value;
        }catch (Exception e){
            onError("The Bigger add multiplier in the config is not an Integer!");
            return 10;
        }

    }

    /**
     *
     * @return the BuildingY value from config
     */
    public int getBuildingYFromConfig(){
        if(!main.config.contains("Settings.BuildingY")){
            onError("Could not retrieve adding value from config because the path doesn't exist.");
            return 10;
        }
        int value = main.config.getInt("Settings.BuildingY");

        if(value < 0)
            value *= -1;

        if(value > 250 || value < 6){
            onError("The Building Y value in the config is not valid.");
            return 200;
        }
        return value;

    }


    /**
     * builds a project in the world as well as returning the project as a Project instance
     * @param projectName the project's name
     * @param block the block which the project gonna be built on
     * @param owner the project's owner
     * @return the project
     */
    public Project buildProject(String projectName,Block block,Player owner,Plugin plugin,Map<String,Object> data){

        if(!main.config.contains("ASMBB.Projects."+projectName)){
            onError("Could not build project because there is no valid project of this name, this is a coding error, pls fix it. Name: "+projectName);
            return null;
        }

        Project project = new Project(projectName, owner, block.getLocation());
        UUID id = UUID.randomUUID();

        MultiBlockCreationEvent event = new MultiBlockCreationEvent(id, project);

        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())
            return null;

        main.getConfig().getConfigurationSection("ASMBB.Projects."+projectName+".ArmorStands").getKeys(false).forEach(key ->{
            ArmorStand stand = deserializeStand(projectName,Integer.parseInt(key),block,plugin,data);

            stand.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"ASMBB-UUID"),new DataUUID(),id);

            project.addArmorStand(stand);
        });

        return project;

    }



    /**
     * destroys a multi block with the given id
     * @param id a given ASMBB UUID from an armor stand which belongs to the multi block
     * @param world a given world which the multi block is in
     */
    public void destroyStands(UUID id, World world){
        MultiBlockDeleteEvent event = new MultiBlockDeleteEvent(id);

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled())
            return;

        for(ArmorStand stand : world.getEntitiesByClass(ArmorStand.class)){

            if(isASMB(stand) && hasUUID(stand) && getUUIDFromStand(stand).equals(id))
                stand.remove();


        }
    }

    /**
     *
     * @param stand a given armor stand
     * @return if the armor stand is a part of a multi block structure
     */
    public boolean isASMB(ArmorStand stand){
        return stand.getPersistentDataContainer().has(new NamespacedKey(BuilderMain.getInstance(), "ASMBB-NAME"), PersistentDataType.STRING);
    }

    /**
     *
     * @param stand a given stand
     * @return if the persistent data type of the armor stand contains an ASMBB UUID value
     */
    public boolean hasUUID(ArmorStand stand){
        return stand.getPersistentDataContainer().has(new NamespacedKey(BuilderMain.getInstance(), "ASMBB-UUID"), new DataUUID());
    }

    /**
     *
     * @param stand a given stand
     * @return the ASMBB UUID of the armor stand from the persistent data container
     */
    public UUID getUUIDFromStand(ArmorStand stand){
        return stand.getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(),"ASMBB-UUID"),new DataUUID());
    }

    /**
     *
     * @param stand a given stand
     * @param type the type of the object
     * @param key the key for the NamespacedKey
     * @param plugin the plugin instance for the NamespacedKey
     * @return the value from the persistent data container of the stand with the given key and plugin instance
     */
    public Object getDataFromStand(ArmorStand stand,Class type, String key,Plugin plugin){
        return stand.getPersistentDataContainer().get(new NamespacedKey(plugin, key), new Data(type));
    }

    /**
     *
     * @param stand a given stand
     * @param type the type of the object
     * @param key the key for the NamespacedKey
     * @return the value from the persistent data container of the stand with the given key and this plugin's Main instance
     */
    public Object getDataFromStand(ArmorStand stand,Class type,String key){
        return getDataFromStand(stand, type, key, BuilderMain.getInstance());
    }

    /**
     * sets the given value with the given plugin instance into the persistent data container of the given armor stand
     * @param stand a given stand
     * @param value a given value
     * @param key a key for the NamespacedKey
     * @param instance a plugin instance
     */
    public void setDataToStand(ArmorStand stand,Object value, String key, Plugin instance){
        stand.getPersistentDataContainer().set(new NamespacedKey(instance,key), new Data(value.getClass()),value);
    }

    /**
     * sets the given value with this plugin's main instance into the persistent data container of the given armor stand
     * @param stand a given stand
     * @param value a given value
     * @param key a key for the NamespacedKey
     */
    public void setDataToStand(ArmorStand stand,Object value, String key){
        stand.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),key), new Data(value.getClass()),value);
    }


}


