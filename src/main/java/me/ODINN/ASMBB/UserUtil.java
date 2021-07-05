package me.ODINN.ASMBB;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class UserUtil {


     // this class contains all the methods the user will have, any other class variable will be private here.

    private BuilderUtility util;

    public UserUtil(){

        this.util = new BuilderUtility(BuilderMain.getInstance());

    }


    /**
     * serializes the armor stand in the file for the given project
     * @param project a given project
     * @param indexOfStand the index of the armor stand in the project
     */
    public void serializeArmorStand(Project project,int indexOfStand) {
        util.serializeArmorStand(project,indexOfStand);
    }

    /**
     *
     * @param projectName a project's name
     * @param num the armor stand number in the project
     * @param block the block which the armor stand will spawn on
     * @return the deserialization of the armor stand from file with data for the persistent data container of the armor stand from another plugin's Main
     */
    public ArmorStand deserializeStand(String projectName, int num, Block block, Plugin plugin, Map<String,Object> data){
        return util.deserializeStand(projectName,num,block,plugin,data);
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
     * builds a project in the world as well as returning the project as a Project instance
     * @param projectName the project's name
     * @param block the block which the project gonna be built on
     * @param owner the project's owner
     * @return the project
     */
    public Project buildProject(String projectName,Block block,Player owner,Plugin plugin,Map<String,Object> data){
        return util.buildProject(projectName, block, owner,plugin,data);
    }

    /**
     * destroys a multi block with the given id
     * @param id a given ASMBB UUID from an armor stand which belongs to the multi block
     * @param world a given world which the multi block is in
     */
    public void destroyMultiBlock(UUID id, World world){
       util.destroyStands(id, world);
    }

    /**
     *
     * @param stand a given armor stand
     * @return if the armor stand is a part of a multi block structure
     */
    public boolean isASMB(ArmorStand stand){
        return util.isASMB(stand);
    }

    /**
     *
     * @param stand a given stand
     * @return if the persistent data type of the armor stand contains an ASMBB UUID value
     */
    public boolean hasUUID(ArmorStand stand){
        return util.hasUUID(stand);
    }

    /**
     *
     * @param stand a given stand
     * @return the ASMBB UUID of the armor stand from the persistent data container
     */
    public UUID getUUIDFromStand(ArmorStand stand){
        return util.getUUIDFromStand(stand);
    }


    /**
     *
     * @param stand a given stand
     * @param type the type of the object
     * @param key the key for the NamespacedKey
     * @param plugin the plugin instance for the NamespacedKey
     * @return the value from the persistent data container of the stand with the given key and plugin instance
     */
    public Object getDataFromStand(ArmorStand stand, Class type, String key, Plugin plugin) {
        return util.getDataFromStand(stand,type,key,plugin);
    }

    /**
     *
     * @param stand a given stand
     * @param type the type of the object
     * @param key the key for the NamespacedKey
     * @return the value from the persistent data container of the stand with the given key and this plugin's Main instance
     */
    public Object getDataFromStand(ArmorStand stand, Class type, String key){
        return util.getDataFromStand(stand, type, key);
    }


    /**
     * sets the given value with the given plugin instance into the persistent data container of the given armor stand
     * @param stand a given stand
     * @param value a given value
     * @param key a key for the NamespacedKey
     * @param instance a plugin instance
     */
    public void setDataToStand(ArmorStand stand,Object value, String key, Plugin instance){
        util.setDataToStand(stand,value,key,instance);
    }

    /**
     * sets the given value with this plugin's main instance into the persistent data container of the given armor stand
     * @param stand a given stand
     * @param value a given value
     * @param key a key for the NamespacedKey
     */
    public void setDataToStand(ArmorStand stand,Object value, String key){
        util.setDataToStand(stand,value,key);
    }


    public String getNameFromStand(ArmorStand stand){

        return (String) util.getDataFromStand(stand, String.class, "ASMBB-NAME");

    }

}
