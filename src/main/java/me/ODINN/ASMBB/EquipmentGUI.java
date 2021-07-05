package me.ODINN.ASMBB;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class EquipmentGUI implements InventoryHolder {

    private Inventory inv;
    private final Player owner;
    private final ArmorStand stand;

    private final int size = 36;
    private final String title = "Equipment";
    private final List<Integer> equipmentSlots = Arrays.asList(10,11,12,13,15,16);

    /**
     *
     * @param stand the gui's armor stand which the equipment is presented from
     * @param owner the owner of the gui
     */
    public EquipmentGUI(ArmorStand stand, Player owner){
        this.stand = stand;
        this.owner = owner;

        initializeInv();

    }

    /**
     * opens the gui to the owner
     */
    public void openEquipment(){
        owner.openInventory(getInventory());
    }

    /**
     * initializes the inventory with the given item values from the armor stand
     */
    public void initializeInv(){
        this.inv = getBlankInv();


        EntityEquipment equip = stand.getEquipment();
        inv.setItem(10,equip.getHelmet());
        inv.setItem(11,equip.getChestplate());
        inv.setItem(12,equip.getLeggings());
        inv.setItem(13,equip.getBoots());
        inv.setItem(15,equip.getItemInMainHand());
        inv.setItem(16,equip.getItemInOffHand());

        inv.setItem(19,ButtonType.getEquipItemButton("HELMET"));
        inv.setItem(20,ButtonType.getEquipItemButton("CHESTPLATE"));
        inv.setItem(21,ButtonType.getEquipItemButton("LEGGINGS"));
        inv.setItem(22,ButtonType.getEquipItemButton("BOOTS"));
        inv.setItem(24,ButtonType.getEquipItemButton("MAIN_HAND"));
        inv.setItem(25,ButtonType.getEquipItemButton("OFF_HAND"));

    }

    /**
     * sets the equipment to the armor stand by the slot which presents an armor part
     * @param slot a given slot
     */
    public void setEquipmentBySlot(int slot){

        Bukkit.getScheduler().runTask(BuilderMain.instance,new Runnable(){

            @Override
            public void run() {
                EntityEquipment equip = stand.getEquipment();
                switch (slot){
                    case 10:
                        equip.setHelmet(inv.getItem(slot));
                        break;

                    case 11:
                        equip.setChestplate(inv.getItem(slot));
                        break;

                    case 12:
                        equip.setLeggings(inv.getItem(slot));
                        break;

                    case 13:
                        equip.setBoots(inv.getItem(slot));
                        break;

                    case 15:
                        equip.setItemInMainHand(inv.getItem(slot));
                        break;

                    case 16:
                        equip.setItemInOffHand(inv.getItem(slot));

                }
            }
        });
    }

    //gets
    public Player getOwner(){
        return this.owner;
    }

    /**
     *
     * @return a "blank" inventory , full of the "blank" item
     */
    public Inventory getBlankInv(){
        Inventory inventory = Bukkit.createInventory(this,this.size,title);

        for(int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i,getBlankItem());
        }

        return inventory;
    }

    @NotNull
    @Override
    public Inventory getInventory(){
        return inv;
    }

    /**
     *
     * @return a "blank" item, for a background color of an inventory
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

    /**
     *
     * @return the slots in the gui which presents the equipment slots
     */
    public List<Integer> getEquipmentSlots(){
        return equipmentSlots;
    }
}
