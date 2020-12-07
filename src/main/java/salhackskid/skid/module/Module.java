package salhackskid.skid.module;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;
import salhackskid.skid.SalHackSkid;
import salhackskid.skid.gui.managers.CommandManager;
import salhackskid.skid.managers.PresetsManager;

import java.util.ArrayList;
import java.util.List;

public class Module implements Listenable {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private boolean toggled = false;
    public boolean hidden = false;
    public String displayName;
    private String[] alias;
    private int color;
    private ModuleType type;
    public String key;
    private String desc;
    public List<Value> valueList = new ArrayList<Value>();

    private Module(String displayName, String[] alias, String key, int color, ModuleType type)
    {
        this.displayName = displayName;
        this.alias = alias;
        this.key = key;
        this.color = color;
        this.type = type;
    }


    public Module(String displayName, String[] alias, String desc, String key, int color, ModuleType type)
    {
        this(displayName, alias, key, color, type);
        this.desc = desc;
    }

    public enum ModuleType
    {
        COMBAT, EXPLOIT, MOVEMENT, RENDER, WORLD, MISC, HIDDEN, UI, BOT, SCHEMATICA, HIGHWAY, DONATE
    }

    public void toggle() {
        toggled = !toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (toggled) onEnable();
        else onDisable();
    }

    public void init() {

    }

    public void onEnable() {
        SalHackSkid.eventBus.subscribe(this);
    }

    public void onDisable() {
        try {
            SalHackSkid.eventBus.unsubscribe(this);
        }
        catch (Exception this_didnt_get_registered_hmm_weird) {
            this_didnt_get_registered_hmm_weird.printStackTrace();
        }
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void SignalValueChange(Value p_Val)
    {
        SaveSettings();
    }

    public void SaveSettings()
    {
        new Thread(() ->
        {
            PresetsManager.Get().getActivePreset().addModuleSettings(this);
        }).start();
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
        SaveSettings();
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
        CommandManager.Get().Reload();
        SaveSettings();
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public List<Value> getValueList()
    {
        return valueList;
    }

    public String getKey()
    {
        return key;
    }

    public void toggleNoSave()
    {
        this.setToggled(!this.isToggled());
        if (this.isToggled())
        {
            this.onEnable();
        }
        else
        {
            this.onDisable();
        }
        this.onEnable();
    }

}
