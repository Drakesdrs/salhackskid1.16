package salhackskid.skid.settings.base;

import salhackskid.skid.gui.hud.ComponentItem;
import salhackskid.skid.module.Module;
import salhackskid.skid.settings.listeners.ComponentItemListener;

public class ComponentItemHiddenMod extends ComponentItem
{
    final Module Mod;
    
    public ComponentItemHiddenMod(Module p_Mod, String p_DisplayText, String p_Description, int p_Flags, int p_State, ComponentItemListener p_Listener, float p_Width, float p_Height)
    {
        super(p_DisplayText, p_Description, p_Flags, p_State, p_Listener, p_Width, p_Height);
        Mod = p_Mod;
    }
    
    @Override
    public boolean HasState(int p_State)
    {
        if ((p_State & ComponentItem.Clicked) != 0)
            return Mod.isHidden();
        
        return super.HasState(p_State);
    }
}
