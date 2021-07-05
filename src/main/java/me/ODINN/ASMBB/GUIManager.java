package me.ODINN.ASMBB;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIManager implements Listener {

    BuilderMain main;
    BuilderUtility util;
    /**
     * creates a gui manager
     * @param main a main instance
     */
    public GUIManager(BuilderMain main){
        this.main = main;
        this.util = new BuilderUtility(main);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onInvClick(InventoryClickEvent event){
        if(event.getClick() != ClickType.UNKNOWN && event.getAction() != InventoryAction.UNKNOWN) {

            if (event.getClickedInventory() != null) {



                if(event.getInventory().getHolder() instanceof  ChangeNameGUI){
                    ChangeNameGUI gui = (ChangeNameGUI) event.getInventory().getHolder();
                    ItemStack item = event.getCursor();

                    if(event.getClickedInventory().getHolder() instanceof ChangeNameGUI){


                        if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){

                            if(event.getSlot() == gui.getNameItemSlot()) {
                                if (event.getCursor() == null || event.getCursor().getType() == Material.AIR){
                                    gui.getStand().setCustomName(null);
                                gui.getStand().setCustomNameVisible(false);
                            }else{
                                    gui.getStand().setCustomName(ChatColor.translateAlternateColorCodes(main.config.getString("Settings.ColorCodesKey").charAt(0),item.getItemMeta().getDisplayName()));
                                    event.getWhoClicked().sendMessage(ChatColor.GREEN+"Changed the armor stand's name to "+gui.getStand().getCustomName());
                                    gui.getStand().setCustomNameVisible(true);
                                }

                                return;
                            }
                            event.setCancelled(true);
                            return;

                        }


                        if(item == null || item.getType() == Material.AIR)
                            return;

                        if(item.getType() != Material.NAME_TAG){
                            event.setCancelled(true);
                            event.getWhoClicked().sendMessage(ChatColor.RED+"You can only set the name using a name tag.");
                            return;
                        }

                        if(!item.getItemMeta().hasDisplayName()){
                            event.setCancelled(true);
                            event.getWhoClicked().sendMessage(ChatColor.RED+"You can't set the name to empty name.");
                            return;
                        }


                        gui.getStand().setCustomName(ChatColor.translateAlternateColorCodes(main.config.getString("Settings.ColorCodesKey").charAt(0),item.getItemMeta().getDisplayName()));
                        event.getWhoClicked().sendMessage(ChatColor.GREEN+"Changed the armor stand's name to "+gui.getStand().getCustomName());
                        gui.getStand().setCustomNameVisible(true);
                        return;
                    }else{
                        if (ButtonType.isButton(event.getCurrentItem())) {
                        event.setCancelled(true);
                        return;
                        }
                    }



                }

                if (event.getInventory().getHolder() instanceof SettingsGUI) {
                    SettingsGUI gui = (SettingsGUI) event.getInventory().getHolder();
                    event.setCancelled(true);
                    if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                        ItemStack item = event.getCurrentItem();
                        if (ButtonType.isButton(item) && ButtonType.getType(item) == ButtonType.SETTF) {
                            gui.setSettingTF(ButtonType.getSettingType(event.getCurrentItem()));
                            return;
                        }
                        return;
                    }

                }

                if (event.getInventory().getHolder() instanceof EquipmentGUI) {
                    EquipmentGUI gui = (EquipmentGUI) event.getInventory().getHolder();

                    if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {

                        if (ButtonType.isButton(event.getCurrentItem())) {
                            event.setCancelled(true);
                            return;
                        }

                        if(event.getClickedInventory().getHolder() instanceof  EquipmentGUI)
                            event.setCancelled(true);
                    }


                    // checks if the slot is one of the equipment slots and then checks if the slot has an item or not which if has then gives the player back the item
                    if (gui.getEquipmentSlots().contains(event.getSlot())) {

                        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {

                            Player player = (Player) event.getWhoClicked();
                            event.setCancelled(true);
                            if (player.getOpenInventory().getBottomInventory().firstEmpty() == -1) {

                                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), event.getCurrentItem());
                                event.getInventory().setItem(event.getSlot(),Layout.AIR());
                                gui.setEquipmentBySlot(event.getSlot());
                                return;
                            }

                            player.getOpenInventory().getBottomInventory().addItem(event.getCurrentItem());
                            event.getInventory().setItem(event.getSlot(),Layout.AIR());

                        }
                        gui.setEquipmentBySlot(event.getSlot());
                        return;
                    }

                    return;

                }

                if (event.getInventory().getHolder() instanceof ASManageGUI) {
                    event.setCancelled(true);

                    if(event.getClickedInventory().getHolder() instanceof  ASManageGUI){
                        ASManageGUI gui = (ASManageGUI) event.getClickedInventory().getHolder();
                        ItemStack item = event.getCurrentItem();

                            if(item == null)
                                return;
                            if(item.getType() == Material.AIR)
                                return;

                            if(item.getType() == gui.deleteItemMat) {
                                if (gui.getState().equals("delete")) {
                                    gui.setState("normal");
                                } else {
                                    gui.setState("delete");
                                    return;
                                }
                            }

                                if(item.getType() == Material.ARMOR_STAND) {
                                    Project project = BuilderUtility.projects.get(event.getWhoClicked().getUniqueId());
                                    if (gui.getState().equals("edit")) {
                                        int index = gui.getArmorStandNumBySlot(event.getSlot()) -1;
                                        project.setCurrentStand(project.getArmorStandByIndex(index));
                                        project.getStands().remove(index);
                                        event.getWhoClicked().closeInventory();
                                        LayoutManager.getLayoutByStage("Start").setLayoutToPlayer((Player) event.getWhoClicked());
                                        event.getWhoClicked().sendMessage(ChatColor.BLUE+"You are now editing the armor stand number: "+gui.getArmorStandNumBySlot(event.getSlot()));
                                        return;
                                    }

                                    if (gui.getState().equals("delete")) {

                                        int index = gui.getArmorStandNumBySlot(event.getSlot()) - 1;
                                        ArmorStand stand = project.getArmorStandByIndex(index);
                                        project.getStands().remove(index);
                                        stand.remove();
                                        event.getWhoClicked().closeInventory();
                                        event.getWhoClicked().sendMessage(ChatColor.RED+"The armor stand has been removed from the project.");
                                        return;

                                    }

                                    if(gui.getState().equals("dupe")){
                                        int index = gui.getArmorStandNumBySlot(event.getSlot()) - 1;
                                        ArmorStand stand = project.getArmorStandByIndex(index);
                                        util.serializeArmorStand(project,index);
                                        ArmorStand dupe = util.deserializeStand(project.getName(), index, stand.getLocation().getBlock());
                                        main.config.set("ASMBB.Projects."+project.getName(),null);
                                        project.setCurrentStand(dupe);
                                        event.getWhoClicked().closeInventory();
                                        LayoutManager.getLayoutByStage("Start").setLayoutToPlayer((Player) event.getWhoClicked());
                                        event.getWhoClicked().sendMessage(ChatColor.WHITE+"You duplicated the armor stand number: "+gui.getArmorStandNumBySlot(event.getSlot()));
                                        return;
                                    }
                                }

                                if(item.getType() == gui.editItemMat) {
                                    if (gui.getState().equals( "edit"))
                                        gui.setState("normal");
                                    else gui.setState("edit");
                                    return;
                                }
                                if(item.getType() == gui.dupeItemMat) {
                                    if (gui.getState().equals("dupe"))
                                        gui.setState("normal");
                                    else gui.setState("dupe");
                                    return;

                                }

                        }


                    }





                }


                }


            }



            @EventHandler
            public void onCreativeInvClick(InventoryCreativeEvent e){
                if(ButtonType.isButton(e.getCurrentItem())){
                    Player player = (Player) e.getWhoClicked();
                    e.setCancelled(true);
                   player.closeInventory();
                    return;
                }
            }






        }





