package me.ODINN.ASMBB;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private final String name;
    private final Player owner;

    private List<ArmorStand> stands;
    private final Location mainPos;
    private ArmorStand currentStand = null;
    private final Layout playerLayout;

    /**'
     *
     * @param name the project's name
     * @param owner the project's owner
     * @param mainPos the project's main position
     */
    public Project(String name, Player owner, Location mainPos){
        this.name = name;
        this.owner = owner;
        this.stands = new ArrayList<ArmorStand>();
        this.mainPos = mainPos;

        playerLayout = LayoutManager.getPlayerLayout(this.owner);
    }


    /**
     *
     * @return the project's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the project's owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     *
     * @return the amount of armor stands in the project
     */
    public int getArmorStandsAmount(){
        return stands.size();
    }


    /**
     * adds the given stand to the list of armor stands of the project
     * @param stand a given stand
     */
    public void addArmorStand(ArmorStand stand){
        stands.add(stand);
    }

    /**
     *
     * @param stand a given stand
     * @return the index of the armor stand in the project's list
     */
    public int getArmorStandNumber(ArmorStand stand){
        return stands.indexOf(stand);
    }

    /**
     *
     * @return the project's main position
     */
    public Location getMainPos(){
        return  this.mainPos;
    }

    /**
     *
     * @return the project's current armor stand
     */
    public ArmorStand getCurrentStand(){
        if(this.currentStand == null)
            return  null;

        return this.currentStand;
    }

    /**
     * sets the project's current armor stand to the given armor stand
     * @param stand a given armor stand
     */
    public void setCurrentStand(ArmorStand stand){

        this.currentStand = stand;
    }

    /**
     *
     * @param index a given index
     * @return an armor stand from the project's list with the given index
     */
    public ArmorStand getArmorStandByIndex(int index){
        if(stands.size() <= index)
            return null;

        return stands.get(index);
    }

    /**
     *
     * @return the project's armor stand list
     */
    public List<ArmorStand> getStands(){
        return this.stands;
    }

    /**
     *
     * @return the owner's inventory hotbar layout
     */
    public Layout getPlayerLayout(){
        return this.playerLayout;
    }
}
