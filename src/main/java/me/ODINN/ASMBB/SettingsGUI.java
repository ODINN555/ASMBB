package me.ODINN.ASMBB;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SettingsGUI implements InventoryHolder {


    private Inventory inv;
    private final Player owner;
    private final ArmorStand stand;
    private final int size = 27;
    private final String title = "Armor Stand Settings";
    private final int backButtonSlot = 22;


    /**
     *
     * @param stand the gui's armor stand which the settings are applied to
     * @param owner the gui's owner
     */
    public SettingsGUI(ArmorStand stand, Player owner){
        this.owner = owner;
        this.stand = stand;

        initializeInv();
    }

    /**
     * initializes the inventory with the given armor stand settings
     */
    private void initializeInv(){
        inv = getBlankInv();

        // back button
        inv.setItem(this.backButtonSlot,ButtonType.getBackButton("Start"));

        // settings
        inv.setItem(10,ButtonType.getSetTFButton("Invisible",getStand().isInvisible()));
        inv.setItem(11,ButtonType.getSetTFButton("Marker",getStand().isMarker()));
        inv.setItem(12,ButtonType.getSetTFButton("BasePlate",getStand().hasBasePlate()));
        inv.setItem(13,ButtonType.getSetTFButton("Glowing",getStand().isGlowing()));
        inv.setItem(14,ButtonType.getSetTFButton("Invulnerable",getStand().isInvulnerable()));
        inv.setItem(15,ButtonType.getSetTFButton("Gravity",getStand().hasGravity()));
        inv.setItem(16,ButtonType.getSetTFButton("small",getStand().isSmall()));
    }

    /**
     * sets a setting to the opposite boolean value
     * @param setting a given setting's name
     */
    public void setSettingTF(String setting) {
        switch (setting) {

            case "Invisible":
                stand.setInvisible(!stand.isInvisible());
                break;

            case "BasePlate":
                stand.setBasePlate(!stand.hasBasePlate());
                break;

            case "Invulnerable":
                stand.setInvulnerable(!stand.isInvulnerable());
                break;

            case "small":
                stand.setSmall(!stand.isSmall());
                break;

            case "Marker":
                stand.setMarker(!stand.isMarker());
                break;

            case "Glowing":
                stand.setGlowing(!stand.isGlowing());
                break;

            case "Gravity":
                stand.setGravity(!stand.hasGravity());
                break;

            default:
                return;
        }
        initializeInv();
        openSettings();
    }

    /**
     * opens the settings to the gui's owner
     */
    public void openSettings(){
        owner.openInventory(getInventory());
    }

    // gets

    /**
     *
     * @return a "blank" inventory
     */
    private Inventory getBlankInv(){
        Inventory inventory = Bukkit.createInventory(this,this.size,title);

        for(int i = 0; i < inventory.getSize(); i++){
            inventory.setItem(i,getBlankItem());
        }

        return inventory;
    }

    /**
     *
     * @return the gui's owner
     */
    public Player getOwner(){
        return this.owner;
    }

    /**
     *
     * @return the gui's armor stand which the settings are applied to
     */
    public ArmorStand getStand(){
        return this.stand;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
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
}
