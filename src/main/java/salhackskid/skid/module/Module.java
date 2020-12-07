package salhackskid.skid.module;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;
import salhackskid.skid.SalHackSkid;

public class Module implements Listenable {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private boolean toggled = false;
    private boolean hidden = false;
    public String displayName;

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

}
