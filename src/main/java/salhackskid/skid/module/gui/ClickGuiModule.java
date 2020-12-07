package salhackskid.skid.module.gui;

import salhackskid.skid.module.Module;
import salhackskid.skid.module.ModuleManager;
import salhackskid.skid.module.Value;

public final class ClickGuiModule extends Module
{
    public final Value<Boolean> AllowOverflow = new Value<Boolean>("AllowOverflow", new String[]
            { "AllowOverflow" }, "Allows the GUI to overflow", true);
    public final Value<Boolean> Watermark = new Value<Boolean>("Watermark", new String[]
            { "Watermark" }, "Displays the watermark on the GUI", true);
    public final Value<Boolean> HoverDescriptions = new Value<Boolean>("HoverDescriptions", new String[] {"HD"}, "Displays hover descriptions over values and modules", true);
    public final Value<Boolean> Snowing = new Value<Boolean>("Snowing", new String[] {"SN"}, "Play a snowing animation in ClickGUI", true);

    public ClickGuiScreen m_ClickGui;

    public ClickGuiModule()
    {
        super("ClickGui", new String[]
                { "ClickGui", "ClickGui" }, "Displays the click gui", "LEFT", 0xDB9324, ModuleType.UI);
    }

    @Override
    public void toggleNoSave()
    {

    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        if (m_ClickGui == null)
            m_ClickGui = new ClickGuiScreen(this, (ColorsModule) ModuleManager.getModule(ColorsModule.class));

        if (mc.world != null)
        {
            mc.openScreen(m_ClickGui);
        }
    }
}
