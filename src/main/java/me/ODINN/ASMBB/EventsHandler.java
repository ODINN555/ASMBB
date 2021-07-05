package me.ODINN.ASMBB;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class EventsHandler implements Listener {

    private final BuilderMain main;
    BuilderUtility util;


    /**
     * creates an event handler
     * @param main a given main instance
     */
    public EventsHandler(BuilderMain main){
        this.main = main;
        util = new BuilderUtility(main);
    }




    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(PlayerInteractEvent e){ // handles the click of layout buttons


        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(ButtonType.isButton(e.getPlayer().getInventory().getItemInMainHand())) {
                e.setCancelled(true);
                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

                Project project = util.getProject(e.getPlayer());
                Player player = e.getPlayer();
                ArmorStand stand;


                // Handling buttons
                switch(ButtonType.getType(item)){
                    // adding / subtracting from pose/pos
                    case ADD:
                    case SUB:
                        stand = project.getCurrentStand();
                        util.addValueToPose(stand,ButtonType.getPathFromButton(item),ButtonType.getAddAmountFromButton(item));
                        util.addToStandPos(stand,ButtonType.getAddAmountFromButton(item),ButtonType.getPathFromButton(item));
                        util.addToStandRotation(stand,(float) ButtonType.getAddAmountFromButton(item),ButtonType.getPathFromButton(item) );
                        return;


                    // layout setting
                    case LAYOUT:

                        Layout layout = LayoutManager.getLayoutByStage(ButtonType.getLayoutSetFromButton(item));
                        layout.setLayoutToPlayer(player);
                        return;
                    // adding armor stand
                    case ADDSTAND:
                        if(project.getCurrentStand() != null){
                            project.addArmorStand(project.getCurrentStand());
                        }

                        ArmorStand newStand = project.getMainPos().getWorld().spawn(project.getMainPos(),ArmorStand.class);
                        newStand.setGravity(false);
                        newStand.setArms(true);
                        project.setCurrentStand(newStand);
                        LayoutManager.getLayoutByStage("Start").setLayoutToPlayer(player);
                        return;
                    // settings
                    case SETTINGS:
                        stand = project.getCurrentStand();
                        SettingsGUI gui = new SettingsGUI(stand,player);
                        gui.openSettings();
                        return;
                    // Armor stand manage
                    case ASMANAGER:
                        ASManageGUI ASManage = new ASManageGUI(project,"normal");
                        ASManage.openManage();
                        return;
                    // equipment
                    case EQUIPMENT:
                        stand = project.getCurrentStand();
                        EquipmentGUI equipmentGUI = new EquipmentGUI(stand, player);
                        equipmentGUI.openEquipment();
                        return;

                    case SAVESTAND:
                        stand = project.getCurrentStand();
                        if(!project.getStands().contains(stand))
                            project.addArmorStand(stand);

                        project.setCurrentStand(null);
                        LayoutManager.getLayoutByStage("Menu").setLayoutToPlayer(player);
                        player.sendMessage(ChatColor.GREEN+"Armor Stand has been saved.");
                        return;


                    case SAVEPROJECT:
                        util.saveProject(project,player.getUniqueId());
                        player.sendMessage(ChatColor.GREEN+"The project has been saved in the file successfully.");
                        return;

                    case CHANGENAME:
                        stand = project.getCurrentStand();
                        ChangeNameGUI changeNameGUI = new ChangeNameGUI(player, stand);
                        changeNameGUI.openGUI();
                        return;


                    default:
                        util.onError("Could not recognize this button. this is a coding error, pls fix it. Button Type: "+ButtonType.getType(item));
                        break;
                }





            }
        }
    }



    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(BuilderUtility.projects.containsKey(player.getUniqueId())){
            Project project = util.getProject(player);
            util.saveProject(project,player.getUniqueId());
        }
    }


    @EventHandler
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent e){
        if(e.getRightClicked().getPersistentDataContainer().has(new NamespacedKey(BuilderMain.getInstance(),"ASMBB-NAME"), PersistentDataType.STRING))
            e.setCancelled(true);

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){

        if(e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {

            Player player = (Player) e.getDamager();
            ArmorStand stand = (ArmorStand) e.getEntity();
            if (util.isASMB(stand)){
                e.setCancelled(true);

                if(!BuilderUtility.projects.containsKey(player.getUniqueId())) {
                    util.destroyStands(util.getUUIDFromStand(stand), player.getWorld());
                }

                return;
            }
        }
    }


}
