package salhackskid.skid.settings.base;

import salhackskid.skid.gui.hud.ComponentItem;
import salhackskid.skid.managers.Preset;
import salhackskid.skid.settings.listeners.ComponentItemListener;

public class ComponentPresetItem extends ComponentItem
{
    private Preset _preset;

    public ComponentPresetItem(Preset preset, int flags, int state, ComponentItemListener listener, float width,
                               float height)
    {
        super(preset.getName(), "", flags, state, listener, width, height);
        
        _preset = preset;
    }

    @Override
    public boolean HasState(int p_State)
    {
        if ((p_State & ComponentItem.Clicked) != 0)
            return _preset.isActive();
        
        return super.HasState(p_State);
    }

    public Preset getPreset()
    {
        return _preset;
    }
}
