package me.ODINN.ASMBB;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ASManageGUI implements InventoryHolder {


    private Inventory inv;
    private final Player owner;
    private String state = "normal";
    private final Project project;

    private final int size = 54;
    private final String title = "Manage Armor Stands";
    private final Material deleteMat = Material.RED_STAINED_GLASS_PANE;
    private final Material editMat = Material.BLUE_STAINED_GLASS_PANE;
    private final Material blankMat = Material.BLACK_STAINED_GLASS_PANE;
    public final Material deleteItemMat = Material.BARRIER;
    public final Material editItemMat = Material.CRAFTING_TABLE;
    public final Material dupeMat = Material.WHITE_STAINED_GLASS_PANE;
    public final Material dupeItemMat = Material.PAINTING;

    private final int deleteItemSlot = 52;
    private final int editItemSlot = 46;
    private final int dupeItemSlot = 49;


    /**
     * creates a management gui for the armor stands of the given project
     * @param project a given project
     * @param state the state of the action (delete/edit/normal)
     */
    public ASManageGUI(Project project,String state){
        this.owner = project.getOwner();
        this.project = project;

        initializeInv();
    }


    /**
     * opens the gui to the owner of the gui
     */
    public void openManage(){
        owner.openInventory(getInventory());
    }

    /**
     * initializes the management inventory with the given values
     */
    public void initializeInv(){
        this.inv = getBlankInv();


        int counter = 0;
        for (int x = 1; x < (size/9)-1; x ++) {
            for (int i = 1; i < 8; i++) {
                int slot = i + (x * 9);
                inv.setItem(slot,standToItemStack(project.getArmorStandByIndex(counter)));
                counter++;
            }
        }
        inv.setItem(deleteItemSlot,getDeleteItem());
        inv.setItem(editItemSlot,getEditItem());
        inv.setItem(dupeItemSlot,getDupeItem());

        Bukkit.getScheduler().runTask(BuilderMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                openManage();
            }
        });

    }

    /**
     *
     * @param stand a given stand
     * @return an item stack display of the armor stand, showing it's settings.
     */
    private ItemStack standToItemStack(ArmorStand stand) {
        if(stand == null)
            return new ItemStack(Material.AIR);


        final ChatColor loreColor = ChatColor.GRAY;
        final ChatColor nameColor = ChatColor.GOLD;

        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();

        meta.setDisplayName(nameColor + "Armor Stand "+project.getArmorStandNumber(stand) + ChatColor.RESET);
        lore.add(loreColor + "Invisible: " + stand.isInvisible());
        lore.add(loreColor + "Has base plate: " + stand.hasBasePlate());
        lore.add(loreColor + "Small: " + stand.isSmall());
        lore.add(loreColor + "Invulnerable: " + stand.isInvulnerable());
        lore.add("");


        EntityEquipment eq = stand.getEquipment();
        String value;

        value = getNameOfItem(eq.getHelmet());
        lore.add(loreColor+"Helmet: "+ value);

        value = getNameOfItem(eq.getChestplate());
        lore.add(loreColor+"Chestplate: "+ value);

        value = getNameOfItem(eq.getLeggings());
        lore.add(loreColor+"Leggings: "+ value);

        value = getNameOfItem(eq.getBoots());
        lore.add(loreColor+"Boots: "+ value);

        value = getNameOfItem(eq.getItemInMainHand());
        lore.add(loreColor+"Main Hand: "+ value);

        value = getNameOfItem(eq.getItemInOffHand());
        lore.add(loreColor+"Off Hand: "+ value);

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;

    }

    /**
     * sets the state
     * @param state a given state
     */
    public void setState(String state){
        if (state.equals("normal") || state.equals("delete") || state.equals("edit") || state.equals("dupe"))
            this.state = state;

        initializeInv();

    }

    //gets

    /**
     *
     * @return the owner of the gui
     */
    public Player getOwner(){
        return this.owner;
    }

    /**
     * creates a "blank" inventory with the color of the given state
     * @return a blank inventory (full of blank items)
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
     * @return a blank item with the color of the current state
     */
    private ItemStack getBlankItem(){
        Material mat;
        switch (state){
            case "normal":
                mat = blankMat;
                break;

            case "delete":
                mat = deleteMat;
                break;

            case "edit":
                mat = editMat;
                break;

            case "dupe":
                mat = dupeMat;
                break;

            default:
                mat = blankMat;
                break;
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }

    /**
     *
     * @return the state of the action (delete/edit/normal)
     */
    public String getState(){
        return this.state;
    }

    /**
     *
     * @return the project which the gui is applied to
     */
    public Project getProject(){
        return  this.project;
    }

    /**
     *
     * @param slot a given slot
     * @return the armor stand number in order in the gui by the given slot
     */
    public int getArmorStandNumBySlot(int slot){
        int startRow = slot - 7;
        if(slot % 9 != 0) {
            return startRow - ( startRow/9 * 2) -2;
        }

        return startRow - ( startRow/9 * 2);


    }

    /**
     *
     * @return an item stack which displays the delete action
     */
    public ItemStack getDeleteItem(){

        final String title = "Delete";
        final ChatColor color = ChatColor.RED;

        ItemStack item = new ItemStack(deleteItemMat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color+title+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }

    /**
     *
     * @return an item stack which displays the edit action
     */
    public ItemStack getEditItem() {

        final String title = "Edit Armor Stand";
        final ChatColor color = ChatColor.BLUE;

        ItemStack item = new ItemStack(editItemMat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color+title+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }

    /**
     *
     * @return an item stack which displays the dupe action
     */
    public ItemStack getDupeItem(){
        final String title = "Duplicate Armor Stand";
        final ChatColor color = ChatColor.WHITE;

        ItemStack item = new ItemStack(dupeItemMat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color+title+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
    /**
     *
     * @param item a given item
     * @return the name of the item, if null will return "none".
     */
    @NotNull
    public String getNameOfItem(@Nullable ItemStack item){
        String value;
        if(item == null || item.getType() == Material.AIR)
            value = "none";
        else {
            String name = item.getType().name();
            name = name.replace("_"," ");
            value = name;
        }

        return value;
    }








}
