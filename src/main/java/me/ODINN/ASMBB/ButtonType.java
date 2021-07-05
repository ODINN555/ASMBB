package me.ODINN.ASMBB;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public enum ButtonType {

    ADD("ADD"), // add 1
    SUB("SUB"), // subtract 1
    SETTINGS("SETTINGS"), // settings
    SETTF("SETTF"), // set true / false
    LAYOUT("LAYOUT"),
    SAVESTAND("SAVESTAND"),
    SAVEPROJECT("SAVEPROJECT"),
    EQUIPMENT("EQUIPMENT"),
    ADDSTAND("ADDSTAND"),
    ASMANAGER("ASMANAGER"),
    CHANGENAME("CHANGENAME")
    ;


    private final String name;



 ButtonType(String name ){
     this.name = name;

 }


 public String getName(){
     return this.name;

    }

    /**
     *
     * @param button the itemstack which is checked
     * @return whether the itemstack is a button or not.
     */
    public static boolean isButton(ItemStack button){

        if(button == null || button.getType() == Material.AIR)
            return false;

     if(!button.hasItemMeta())
         return false;

     return button.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(BuilderMain.getInstance(),"buttonType"), PersistentDataType.STRING);
    }

    /**
     *
     * @param item the button
     * @return the button's type
     */
    public static ButtonType getType(ItemStack item){

     return valueOf(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING));

    }


    /**
     *
     * @param amount the subtract amount
     * @return the item stack presentation of a subtraction button
     */
    public static ItemStack getSubButton(double amount,String path){

     final Material MAT = Material.RED_STAINED_GLASS_PANE;
     final ChatColor COLOR = ChatColor.RED;

     ItemStack item = new ItemStack(MAT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(COLOR+""+amount+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING,"SUB");
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"path"),PersistentDataType.STRING,path);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"addAmount"),PersistentDataType.DOUBLE,amount);

        item.setItemMeta(meta);
        return item;

    }

    /**
     *
     * @param amount the adding amount
     * @return an item stack presentation of an adding button
     */
    public static ItemStack getAddButton(double amount,String path){
        final Material MAT = Material.GREEN_STAINED_GLASS_PANE;
        final ChatColor COLOR = ChatColor.GREEN;

        ItemStack item = new ItemStack(MAT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(COLOR+"+"+amount+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING,"ADD");
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"path"),PersistentDataType.STRING,path);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"addAmount"),PersistentDataType.DOUBLE,amount);

        item.setItemMeta(meta);
        return item;

    }

    /**
     *
     * @return an item stack presentation of a back button
     */
    public static ItemStack getBackButton(String layout){
        final Material MAT = Material.ARROW;
        final ChatColor COLOR = ChatColor.GOLD;

        ItemStack item = new ItemStack(MAT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(COLOR+"Back"+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, ButtonType.LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, layout);

        item.setItemMeta(meta);
        return item;
    }


    /**
     *
     * @param setting the setting which the button presents
     * @param TF if the setting is true or false
     * @return an item stack presentation of a set true / false button
     */
    public static ItemStack getSetTFButton(String setting,boolean TF){

     Material MAT;
     ChatColor COLOR;
     int value;
     if(TF) {
         MAT = Material.GREEN_STAINED_GLASS_PANE;
         COLOR = ChatColor.GREEN;
         value = 1;
     }
     else {
         MAT = Material.RED_STAINED_GLASS_PANE;
         COLOR = ChatColor.RED;
         value = 0;
     }

        ItemStack item = new ItemStack(MAT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(COLOR+setting+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, SETTF.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"settingType"),PersistentDataType.STRING, setting);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"settingValue"),PersistentDataType.INTEGER,value );

        item.setItemMeta(meta);
        return item;

    }

    /**
     *
     * @param item a setting button
     * @return  the setting's type
     */
    public static String getSettingType(ItemStack item){
     return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(),"settingType"),PersistentDataType.STRING);
    }



    /**
     *
     * @return an item stack presentation of the settings button
     */
    public static ItemStack getSettingsButton(){
        final Material MAT = Material.ACTIVATOR_RAIL;
        final ChatColor COLOR = ChatColor.GOLD;

        ItemStack item = new ItemStack(MAT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(COLOR+"Settings"+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, SETTINGS.getName());

        item.setItemMeta(meta);
        return item;
    }


    /**
     *
     * @param part a given body part
     * @return an item stack body part button
     */
    public static ItemStack getBodyPartButton(String part){

        String title;
        Material mat;
        ChatColor color = ChatColor.GOLD;
        switch(part){

            case "HEAD":
                title = "Head";
                mat = Material.IRON_HELMET;
                break;

            case "BODY":
                title = "Body";
                mat = Material.IRON_CHESTPLATE;
                break;

            case "LeftArm":
                title = "Left Arm";
                mat = Material.SHIELD;
                break;

            case "RightArm":
                title = "Right Arm";
                mat = Material.IRON_SWORD;
                break;

            case "LeftLeg":
                title = "Left Leg";
                mat = Material.IRON_LEGGINGS;
                break;

            case "RightLeg":
                title = "Right Leg";
                mat = Material.IRON_BOOTS;
                break;

            default:
                title = "ERROR";
                mat = Material.BARRIER;
        }

        ItemStack item = new ItemStack(mat);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color+title);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING,ButtonType.LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING,"Start@Pose@"+part);

        item.setItemMeta(meta);
        return item;

    }


    /**
     *
     * @param armorPart a given armor part
     * @return an item stack display of the armor part
     */
    public static ItemStack getEquipItemButton(String armorPart) {


        String title;
        Material mat;
        ChatColor color = ChatColor.GOLD;

        switch (armorPart) {

            case "HELMET":
                mat = Material.IRON_HELMET;
                title = "Helmet";
                break;
            case "CHESTPLATE":
                mat = Material.IRON_CHESTPLATE;
                title = "Chestplate";
                break;
            case "LEGGINGS":
                mat = Material.IRON_LEGGINGS;
                title = "Leggings";
                break;
            case "BOOTS":
                mat = Material.IRON_BOOTS;
                title = "Boots";
                break;
            case "MAIN_HAND":
                mat = Material.IRON_SWORD;
                title = "Main Hand";
                break;
            case "OFF_HAND":
                mat = Material.SHIELD;
                title = "Off Hand";
                break;

            default:
                mat = Material.BARRIER;
                title = "ERROR";
                break;
        }

        ItemStack item = new ItemStack(mat);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color + title);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        return item;

    }

    /**
     *
     * @param xyz an x / y / z value
     * @param path a given stage path
     * @return an item stack xyz button
     */
    public static ItemStack getXYZButtons(String xyz,String path){
        Material mat = Material.YELLOW_STAINED_GLASS_PANE;
        ChatColor color = ChatColor.GOLD;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color + xyz + ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"ButtonType"),PersistentDataType.STRING,ButtonType.LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING,path+"@"+xyz);

        item.setItemMeta(meta);
        return item;
    }

    /**
     *
     * @param type the type of the save button
     * @return an item stack save button
     */
    public static ItemStack getSaveButton(ButtonType type){
        final Material mat = Material.GREEN_STAINED_GLASS_PANE;
        final ChatColor color = ChatColor.GREEN;

        ItemStack save = new ItemStack(mat);
        ItemMeta meta = save.getItemMeta();

        meta.setDisplayName(color+"Save"+ChatColor.RESET);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, type.getName());

        save.setItemMeta(meta);

        return save;
    }


    /**
     *
     * @return an item stack add armor stand button
     */
    public static ItemStack getAddASButton(){

        final Material mat = Material.ARMOR_STAND;
        final ChatColor color = ChatColor.GOLD;

        ItemStack stand = new ItemStack(mat);
        ItemMeta meta = stand.getItemMeta();

        meta.setDisplayName(color+"Add Armor Stand");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, ADDSTAND.getName());
        stand.setItemMeta(meta);

        return stand;

    }

    /**
     *
     * @return an item stack armor stand manage button
     */
    public static ItemStack getASManagerButton(){
        final Material mat = Material.FILLED_MAP;
        final ChatColor color = ChatColor.GOLD;

        ItemStack stand = new ItemStack(mat);
        ItemMeta meta = stand.getItemMeta();

        meta.setDisplayName(color+"Manage Armor Stands");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, ASMANAGER.getName());
        stand.setItemMeta(meta);

        return stand;
    }

    /**
     *
     * @return an item stack position button
     */
    public static ItemStack getPositionButton(){

            final Material mat = Material.ENDER_PEARL;
            final ChatColor color = ChatColor.GOLD;

            ItemStack button = new ItemStack(mat);
            ItemMeta meta = button.getItemMeta();

            meta.setDisplayName(color+"Position");
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, LAYOUT.getName());
            meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, "Start@Pos");
            button.setItemMeta(meta);

            return button;



    }

    /**
     *
     * @return an item stack pose button
     */
    public static ItemStack getPoseButton(){
        final Material mat = Material.STICK;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Poses");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, "Start@Pose");
        button.setItemMeta(meta);

        return button;
    }


    /**
     *
     * @return an item stack equipment button
     */
    public static ItemStack getEquipmentButton(){
        final Material mat = Material.IRON_CHESTPLATE;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Equipment");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, EQUIPMENT.getName());
        button.setItemMeta(meta);

        return button;
    }

    /**
     *
     * @return an item stack rotation button
     */
    public static ItemStack getRotationButton(){
        final Material mat = Material.COMPASS;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Rotation");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, "Start@Rotation");
        button.setItemMeta(meta);

        return button;
    }

    /**
     *
     * @return an item stack yaw button
     */
    public static ItemStack getYawButton(){
        final Material mat = Material.YELLOW_STAINED_GLASS_PANE;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Yaw");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, "Start@Rotation@yaw");
        button.setItemMeta(meta);

        return button;
    }

    /**
     *
     * @return an item stack pitch button
     */
    public static ItemStack getPitchButton(){
        final Material mat = Material.YELLOW_STAINED_GLASS_PANE;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Pitch");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, LAYOUT.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"layoutSet"),PersistentDataType.STRING, "Start@Rotation@pitch");
        button.setItemMeta(meta);

        return button;
    }


    public static ItemStack getChangeNameButton(){
        final Material mat = Material.NAME_TAG;
        final ChatColor color = ChatColor.GOLD;

        ItemStack button = new ItemStack(mat);
        ItemMeta meta = button.getItemMeta();

        meta.setDisplayName(color+"Change Name");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(new NamespacedKey(BuilderMain.getInstance(),"buttonType"),PersistentDataType.STRING, CHANGENAME.getName());
        button.setItemMeta(meta);

        return button;
    }

    /**
     *
     * @param button a given button
     * @return the stage path of the button (note, not all buttons has this value)
     */
    public static String getPathFromButton(ItemStack button){
        return button.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(), "path"), PersistentDataType.STRING);
    }

    /**
     *
     * @param button a given layout button
     * @return the layout setting of the button (which layout will be set when clicking the button)
     */
    public static String getLayoutSetFromButton(ItemStack button){
        return button.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(), "layoutSet"), PersistentDataType.STRING);

    }

    /**
     *
     * @param button a given add button
     * @return the adding amount of the button
     */
    public static double getAddAmountFromButton(ItemStack button){
        return button.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(BuilderMain.getInstance(), "addAmount"), PersistentDataType.DOUBLE);
    }




}
