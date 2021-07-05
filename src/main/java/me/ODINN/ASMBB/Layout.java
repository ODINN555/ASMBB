package me.ODINN.ASMBB;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Layout {




    private final ItemStack[] buttonList;
    private final String stageValue;


    /**
     * creates a hotbar buttons Layout
     * @param buttonList the button list for the hotbar
     * @param stageValue the stage path of the layout
     */
    public Layout(ItemStack[] buttonList,String stageValue){
        this.buttonList = buttonList;
        this.stageValue = stageValue;
    }

    /**
     * sets the layout to the player
     * @param player a given player
     */
    public void setLayoutToPlayer(Player player){

        for(int i = 0; i < 9; i++){
            player.getOpenInventory().getBottomInventory().setItem(i,buttonList[i]);
        }
    }

    public void setLayoutToPlayer(OfflinePlayer player){

        setLayoutToPlayer(player.getPlayer());
    }

    /**
     *
     * @return an air item stack, used to construct the button layout. the layout array cannot have null.
     */
    public static ItemStack AIR(){
        return new ItemStack(Material.AIR);
    }
}
