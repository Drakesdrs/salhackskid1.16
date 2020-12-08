package salhackskid.skid.settings.base;

import me.ionar.salhack.util.Timer;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import salhackskid.skid.SalHackSkid;
import salhackskid.skid.gui.hud.ComponentItem;
import salhackskid.skid.module.Module;
import salhackskid.skid.settings.listeners.ComponentItemListener;
import salhackskid.skid.utils.Render;

public class ComponentItemKeybind extends ComponentItem
{
    public boolean Listening = false;
    final Module Mod;
    private String LastKey = "";
    private Timer timer = new Timer();
    private String DisplayString = "";
    MinecraftClient mc = MinecraftClient.getInstance();
    
    public ComponentItemKeybind(Module p_Mod, String p_DisplayText, String p_Description, int p_Flags, int p_State, ComponentItemListener p_Listener, float p_Width, float p_Height)
    {
        super(p_DisplayText, p_Description, p_Flags, p_State, p_Listener, p_Width, p_Height);
        Mod = p_Mod;

        Flags |= ComponentItem.RectDisplayAlways;
    }

    @Override
    public String GetDisplayText()
    {
        if (Listening)
            return "Press a Key...";

        String l_DisplayText = "Keybind " + Mod.getKey();
        
        if (HasState(ComponentItem.Hovered) && Render.getStringWidth(l_DisplayText) > GetWidth() - 3)
        {
            if (DisplayString == null)
                DisplayString = "Keybind " + Mod.getKey() + " ";

            l_DisplayText = DisplayString;
            float l_Width = Render.getStringWidth(l_DisplayText);

            while (l_Width > GetWidth() - 3)
            {
                l_Width = Render.getStringWidth(l_DisplayText);
                l_DisplayText = l_DisplayText.substring(0, l_DisplayText.length() - 1);
            }

            if (timer.passed(75) && DisplayString.length() > 0)
            {
                String l_FirstChar = String.valueOf(DisplayString.charAt(0));

                DisplayString = DisplayString.substring(1) + l_FirstChar;

                timer.reset();
            }

            return l_DisplayText;
        }
        else
            DisplayString = null;

        float l_Width = Render.getStringWidth(l_DisplayText);

        while (l_Width > GetWidth() - 3)
        {
            l_Width = Render.getStringWidth(l_DisplayText);
            l_DisplayText = l_DisplayText.substring(0, l_DisplayText.length() - 1);
        }

        return l_DisplayText;
    }

    @Override
    public String GetDescription()
    {
        return "Sets the key of the Module: " + Mod.getDisplayName();
    }
    
    @Override
    public void OnMouseClick(int p_MouseX, int p_MouseY, int p_MouseButton)
    {
        super.OnMouseClick(p_MouseX, p_MouseY, p_MouseButton);
        
        LastKey = "";
        
        if (p_MouseButton == 0)
        	Listening = !Listening;
        else if (p_MouseButton == 1)
        	Listening = false;
        else if (p_MouseButton == 2)
        {
        	Mod.setKey("NONE");
        	SalHackSkid.SendMessage("Unbinded the module: " + Mod.getDisplayName());
        	Listening = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        if (Listening)
        {
            String l_Key = String.valueOf(InputUtil.isKeyPressed(mc.getWindow().getHandle(), keyCode)).toUpperCase();

            if (l_Key.length() < 1)
            {
                Listening = false;
                return;
            }
            
            if (l_Key.equals("END") || l_Key.equals("BACK") || l_Key.equals("DELETE"))
            {
            	l_Key = "NONE";
            }
            
            if (!l_Key.equals("NONE") && !l_Key.contains("CONTROL") && !l_Key.contains("SHIFT") && !l_Key.contains("MENU"))
            {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 56))
                    l_Key = (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 56) ? "LMENU" : "RMENU") + " + " + l_Key;
                else if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 219) || InputUtil.isKeyPressed(mc.getWindow().getHandle(), 29) )
                {
                    String l_CtrlKey = "";
                    
                    if (MinecraftClient.IS_SYSTEM_MAC)
                        l_CtrlKey = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 219) ? "LCONTROL" : "RCONTROL";
                    else
                        l_CtrlKey = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 29) ? "LCONTROL" : "RCONTROL";
                    
                    l_Key = l_CtrlKey + " + " + l_Key;
                }
                else if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 42))
                    l_Key = (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 42) ? "LSHIFT" : "RSHIFT") + " + " + l_Key;
            }
            
            LastKey = l_Key;
        }
    }
    
    @Override
    public void Update()
    {
        if (Listening && LastKey != "")
        {
            Mod.setKey(LastKey);
            SalHackSkid.SendMessage("Set the key of " + Mod.getDisplayName() + " to " + LastKey);
            Listening = false;
        }
    }
}
