package salhackskid.skid.gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;
import net.java.games.input.Mouse;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import salhackskid.skid.gui.hud.MenuComponentModList;
import salhackskid.skid.managers.PresetsManager;
import salhackskid.skid.module.Module;
import salhackskid.skid.module.gui.ClickGuiModule;
import salhackskid.skid.module.gui.ColorsModule;
import salhackskid.skid.utils.MenuComponentPresetsList;
import salhackskid.skid.utils.ScaledResolution;
import salhackskid.skid.utils.Snow;
import salhackskid.skid.gui.hud.MenuComponent;

public class ClickGuiScreen extends SalGuiScreen
{
    private ArrayList<MenuComponent> MenuComponents = new ArrayList<MenuComponent>();
    private ArrayList<Snow> _snowList = new ArrayList<Snow>();

    private float OffsetY = 0;

    public ClickGuiScreen(ClickGuiModule p_Mod, ColorsModule p_Colors)
    {
        // COMBAT, EXPLOIT, MOVEMENT, RENDER, WORLD, MISC, HIDDEN, UI
        MenuComponents.add(new MenuComponentModList("Combat", Module.ModuleType.COMBAT, 10, 3, "Shield", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("Exploit", Module.ModuleType.EXPLOIT, 120, 3, "skull", p_Colors, p_Mod));
        // MenuComponents.add(new MenuComponentModList("Hidden", Module.ModuleType.HIDDEN, 320,
        // 3));
        MenuComponents.add(new MenuComponentModList("Misc", Module.ModuleType.MISC, 230, 3, "questionmark", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("Movement", Module.ModuleType.MOVEMENT, 340, 3, "Arrow", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("Render", Module.ModuleType.RENDER, 450, 3, "Eye", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("UI", Module.ModuleType.UI, 560, 3, "mouse", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("World", Module.ModuleType.WORLD, 670, 3, "blockimg", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("Highway", Module.ModuleType.HIGHWAY, 780, 3, "Highwayimg", p_Colors, p_Mod));
        //   MenuComponents.add(new MenuComponentModList("Bot", Module.ModuleType.BOT, 780, 3, "robotimg", p_Colors));
        MenuComponents.add(new MenuComponentModList("Donate!", Module.ModuleType.DONATE, 230, 203, "Highwayimg", p_Colors, p_Mod));
        MenuComponents.add(new MenuComponentModList("Schematica", Module.ModuleType.SCHEMATICA, 10, 203, "robotimg", p_Colors, p_Mod));

        MenuComponentPresetsList presetList = null;

        MenuComponents.add(presetList = new MenuComponentPresetsList("Presets", Module.ModuleType.SCHEMATICA, 120, 203, "robotimg", p_Colors, p_Mod));

        PresetsManager.Get().InitalizeGUIComponent(presetList);

        ClickGuiMod = p_Mod;

        /// Load settings
        for (MenuComponent l_Component : MenuComponents)
        {
            File l_Exists = new File("SalHack/GUI/" + l_Component.GetDisplayName() + ".json");
            if (!l_Exists.exists())
                continue;

            try
            {
                // create Gson instance
                Gson gson = new Gson();

                // create a reader
                Reader reader = Files
                        .newBufferedReader(Paths.get("SalHack/GUI/" + l_Component.GetDisplayName() + ".json"));

                // convert JSON file to map
                Map<?, ?> map = gson.fromJson(reader, Map.class);

                for (Map.Entry<?, ?> entry : map.entrySet())
                {
                    String l_Key = (String) entry.getKey();
                    String l_Value = (String) entry.getValue();

                    if (l_Key.equals("PosX"))
                        l_Component.SetX(Float.parseFloat(l_Value));
                    else if (l_Key.equals("PosY"))
                        l_Component.SetY(Float.parseFloat(l_Value));
                }

                reader.close();
            }
            catch (Exception e)
            {

            }
        }

        Random random = new Random();

        for (int i = 0; i < 100; ++i)
        {
            for (int y = 0; y < 3; ++y)
            {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2)+1);
                _snowList.add(snow);
            }
        }
    }

    private ClickGuiModule ClickGuiMod;

//    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        for (MenuComponent l_Menu : MenuComponents)
        {
            if (l_Menu.MouseClicked(mouseX, mouseY, mouseButton, OffsetY))
                break;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

//    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        for (MenuComponent l_Menu : MenuComponents)
        {
            l_Menu.MouseReleased(mouseX, mouseY, state);
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

//    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick, int prevMouseX, int prevMouseY)
    {
        for (MenuComponent l_Menu : MenuComponents)
        {
            l_Menu.MouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, prevMouseX, prevMouseY);
    }

//    @Override
    public void drawScreen(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrix, mouseX, mouseY, partialTicks);

        final ScaledResolution res = new ScaledResolution(client);

        if (!_snowList.isEmpty() && ClickGuiMod.Snowing.getValue())
        {
            _snowList.forEach(snow -> snow.Update(res));
        }

        GlStateManager.pushMatrix();

        GlStateManager.disableRescaleNormal();

        MenuComponent l_LastHovered = null;

        for (MenuComponent l_Menu : MenuComponents)
            if (l_Menu.Render(matrix, mouseX, mouseY, true, AllowsOverflow(), OffsetY))
                l_LastHovered = l_Menu;

        if (l_LastHovered != null)
        {
            /// Add to the back of the list for rendering
            MenuComponents.remove(l_LastHovered);
            MenuComponents.add(l_LastHovered);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.popMatrix();
    }

//    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyPressed(typedChar, keyCode, 0);

        for (MenuComponent l_Menu : MenuComponents)
        {
            l_Menu.keyTyped(typedChar, keyCode);
        }
    }

//    @Override
    public void onGuiClosed()
    {
        super.onClose();

        if (ClickGuiMod.isToggled())
            ClickGuiMod.toggle();

        /// Save Settings

        for (MenuComponent l_Component : MenuComponents)
        {
            try
            {
                GsonBuilder builder = new GsonBuilder();

                Gson gson = builder.setPrettyPrinting().create();

                Writer writer = Files.newBufferedWriter(Paths.get("SalHack/GUI/" + l_Component.GetDisplayName() + ".json"));
                Map<String, String> map = new HashMap<>();

                map.put("PosX", String.valueOf(l_Component.GetX()));
                map.put("PosY", String.valueOf(l_Component.GetY()));

                gson.toJson(map, writer);
                writer.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean AllowsOverflow()
    {
        return ClickGuiMod.AllowOverflow.getValue();
    }

    public void ResetToDefaults()
    {
        MenuComponents.forEach(comp -> comp.Default());
    }
}
