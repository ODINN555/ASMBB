package me.ODINN.ASMBB;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ChangeNameGUI implements InventoryHolder {



    private Inventory inv;
    private final Player owner;
    private final String standCurrName;
    private final ArmorStand stand;
    private final int nameItemSlot = 4;

    public ChangeNameGUI(Player owner, ArmorStand stand){
        this.owner = owner;
        this.stand = stand;
        standCurrName = stand.getCustomName();

        initializeInv();
    }

    /**
     * initializes the inventory
     */
    private void initializeInv(){

        inv = getBlankInv();
        inv.setItem(getNameItemSlot(),getNameItem());


    }

    /**
     * opens the GUI
     */
    public void openGUI(){

        getOwner().openInventory(getInventory());

    }

    //gets

    /**
     *
     * @return the GUI's owner
     */
    public Player getOwner(){
        return this.owner;
    }

    /**
     *
     * @return a "blank" item, this is for a background color of an inventory
     */
    private ItemStack getBlankItem(){
        final Material mat = Material.GRAY_STAINED_GLASS_PANE;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getNameItem(){
        if(standCurrName == null)
            return new ItemStack(Material.AIR);
        else{
             final Material nameItemMat = Material.NAME_TAG;

            ItemStack item = new ItemStack(nameItemMat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(standCurrName);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            item.setItemMeta(meta);
            return item;
        }

    }

    /**
     *
     * @return a "blank" inventory
     */
    private Inventory getBlankInv(){

        final int size = 9;
        final String title = "Change Name";

        Inventory inventory = Bukkit.createInventory(this,size,title);

        for(int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i,getBlankItem());
        }

        return inventory;
    }

    /**
     *
     * @return the armor stand which the changing is for
     */
    public ArmorStand getStand(){
        return this.stand;
    }

    /**
     *
     * @return the NameItem slot
     */
    public int getNameItemSlot(){
        return this.nameItemSlot;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
