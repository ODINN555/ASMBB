package me.ODINN.ASMBB;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LayoutManager {


    private static Map<String, Layout> layoutMap = new HashMap<String, Layout>();

    private static BuilderUtility util = new BuilderUtility(BuilderMain.getInstance());


    /**
     *
     * @param stage a given stage path
     * @return the stage path's layout
     */
    public static Layout getLayoutByStage(String stage){
        return layoutMap.get(stage);
    }

    /**
     * initializes the layouts
     */
    public static void initializeLayouts(){

        layoutMap.put("Menu", getMenuLayout());
        layoutMap.put("Start",getStartLayout());

        layoutMap.put("Start@Pose",new Layout( new ItemStack[]{ButtonType.getBackButton("Start"),Layout.AIR(),ButtonType.getBodyPartButton("LeftArm"),ButtonType.getBodyPartButton("LeftLeg"),ButtonType.getBodyPartButton("HEAD"), ButtonType.getBodyPartButton("BODY"),ButtonType.getBodyPartButton("RightLeg"),ButtonType.getBodyPartButton("RightArm"),Layout.AIR()},"Start@Pose"));
        layoutMap.put("Start@Pose@HEAD",getBodyPartLayout("HEAD"));
        layoutMap.put("Start@Pose@HEAD@x",getAddLayouts("Start@Pose@HEAD","x"));
        layoutMap.put("Start@Pose@HEAD@y",getAddLayouts("Start@Pose@HEAD","y"));
        layoutMap.put("Start@Pose@HEAD@z",getAddLayouts("Start@Pose@HEAD","z"));

        layoutMap.put("Start@Pose@BODY",getBodyPartLayout("BODY"));
        layoutMap.put("Start@Pose@BODY@x",getAddLayouts("Start@Pose@BODY","x"));
        layoutMap.put("Start@Pose@BODY@y",getAddLayouts("Start@Pose@BODY","y"));
        layoutMap.put("Start@Pose@BODY@z",getAddLayouts("Start@Pose@BODY","z"));

        layoutMap.put("Start@Pose@LeftArm",getBodyPartLayout("LeftArm"));
        layoutMap.put("Start@Pose@LeftArm@x",getAddLayouts("Start@Pose@LeftArm","x"));
        layoutMap.put("Start@Pose@LeftArm@y",getAddLayouts("Start@Pose@LeftArm","y"));
        layoutMap.put("Start@Pose@LeftArm@z",getAddLayouts("Start@Pose@LeftArm","z"));

        layoutMap.put("Start@Pose@RightArm",getBodyPartLayout("RightArm"));
        layoutMap.put("Start@Pose@RightArm@x",getAddLayouts("Start@Pose@RightArm","x"));
        layoutMap.put("Start@Pose@RightArm@y",getAddLayouts("Start@Pose@RightArm","y"));
        layoutMap.put("Start@Pose@RightArm@z",getAddLayouts("Start@Pose@RightArm","z"));

        layoutMap.put("Start@Pose@LeftLeg",getBodyPartLayout("LeftLeg"));
        layoutMap.put("Start@Pose@LeftLeg@x",getAddLayouts("Start@Pose@LeftLeg","x"));
        layoutMap.put("Start@Pose@LeftLeg@y",getAddLayouts("Start@Pose@LeftLeg","y"));
        layoutMap.put("Start@Pose@LeftLeg@z",getAddLayouts("Start@Pose@LeftLeg","z"));

        layoutMap.put("Start@Pose@RightLeg",getBodyPartLayout("RightLeg"));
        layoutMap.put("Start@Pose@RightLeg@x",getAddLayouts("Start@Pose@RightLeg","x"));
        layoutMap.put("Start@Pose@RightLeg@y",getAddLayouts("Start@Pose@RightLeg","y"));
        layoutMap.put("Start@Pose@RightLeg@z",getAddLayouts("Start@Pose@RightLeg","z"));

        layoutMap.put("Start@Pos",new Layout(new ItemStack[]{ButtonType.getBackButton("Start"),Layout.AIR(),ButtonType.getXYZButtons("x","Start@Pos"),Layout.AIR(),ButtonType.getXYZButtons("y","Start@Pos"),Layout.AIR(),ButtonType.getXYZButtons("z","Start@Pos"),Layout.AIR(),Layout.AIR()},"Start@Pos"));
        layoutMap.put("Start@Pos@x",getAddLayouts("Start@Pos","x"));
        layoutMap.put("Start@Pos@y",getAddLayouts("Start@Pos","y"));
        layoutMap.put("Start@Pos@z",getAddLayouts("Start@Pos","z"));

        layoutMap.put("Start@Rotation", new Layout(new ItemStack[]{ButtonType.getBackButton("Start"),Layout.AIR(),Layout.AIR(),ButtonType.getYawButton(),Layout.AIR(),ButtonType.getPitchButton(),Layout.AIR(),Layout.AIR(),Layout.AIR()},"Start@Rotation"));
        layoutMap.put("Start@Rotation@yaw",getAddLayouts("Start@Rotation","yaw"));
        layoutMap.put("Start@Rotation@pitch",getAddLayouts("Start@Rotation","pitch"));






    }


    /**
     *
     * @param path a given path
     * @param addedTo the value which is being added to
     * @return a layout with the given path and the add / subtract buttons
     */
    private static Layout getAddLayouts(String path,String addedTo){

        String str = util.getStageAsArray(path)[1];


        double amount = util.getAddingValueFromConfig(str);
        int multi = util.getBiggerAddMultiplierFromConfig();

        ItemStack[] buttons = new ItemStack[9];


        double biggerAmount = amount * multi;
        String newPath = path + "@" + addedTo;
        buttons[0] = ButtonType.getBackButton(path);
        buttons[1] = Layout.AIR();
        buttons[2] = ButtonType.getAddButton(amount,newPath);
        buttons[3] = ButtonType.getAddButton(biggerAmount,newPath);
        buttons[4] = Layout.AIR();
        buttons[5] = ButtonType.getSubButton(amount *-1,newPath);
        buttons[6] = ButtonType.getSubButton(biggerAmount * -1,newPath);
        buttons[7] = Layout.AIR();
        buttons[8] = Layout.AIR();


        return new Layout(buttons, newPath);


    }

    /**
     *
     * @param part a given body part
     * @return the body part layout
     */
    private static Layout getBodyPartLayout(String part){

        ItemStack[] buttons = new ItemStack[9];
        buttons[0] = ButtonType.getBackButton("Start@Pose");
        buttons[1] = Layout.AIR();
        buttons[2] = ButtonType.getXYZButtons("x", "Start@Pose@"+part);
        buttons[3] = Layout.AIR();
        buttons[4] = ButtonType.getXYZButtons("y", "Start@Pose@"+part);
        buttons[5] = Layout.AIR();
        buttons[6] = ButtonType.getXYZButtons("z", "Start@Pose@"+part);
        buttons[7] = Layout.AIR();
        buttons[8] = Layout.AIR();


        return new Layout(buttons, "Start@Pose"+part);
    }


    /**
     *
     * @return the main Menu layout
     */
    private static Layout getMenuLayout(){
        ItemStack[] buttons = new ItemStack[9];
        buttons[0] = ButtonType.getSaveButton(ButtonType.SAVEPROJECT);
        buttons[1] = Layout.AIR();
        buttons[2] = Layout.AIR();
        buttons[3] = ButtonType.getASManagerButton();
        buttons[4] = Layout.AIR();
        buttons[5] = ButtonType.getAddASButton();
        buttons[6] = Layout.AIR();
        buttons[7] = Layout.AIR();
        buttons[8] = Layout.AIR();


        return new Layout(buttons, "Menu");
    }

    /**
     *
     * @return the armor stand constructing start layout
     */
    private static Layout getStartLayout(){
        ItemStack[] buttons = new ItemStack[9];
        buttons[0] = ButtonType.getSaveButton(ButtonType.SAVESTAND);
        buttons[1] = ButtonType.getPositionButton();
        buttons[2] = ButtonType.getPoseButton();
        buttons[3] = ButtonType.getRotationButton();
        buttons[4] = Layout.AIR();
        buttons[5] = ButtonType.getChangeNameButton();
        buttons[6] = ButtonType.getEquipmentButton();
        buttons[7] = ButtonType.getSettingsButton();
        buttons[8] = Layout.AIR();


        return new Layout(buttons, "Start");
    }

    public static Layout getPlayerLayout(Player player){
        Inventory inv = player.getInventory();
        ItemStack[] buttons = new ItemStack[9];

        for(int i = 0; i < 9; i ++){
            if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
                buttons[i] = Layout.AIR();
            else buttons[i] = inv.getItem(i);

        }

        return new Layout(buttons, "Player");
    }



}
