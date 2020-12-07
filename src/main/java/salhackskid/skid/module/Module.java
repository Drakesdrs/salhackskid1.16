package salhackskid.skid.module;

import me.zero.alpine.listener.Listenable;
import net.minecraft.client.MinecraftClient;
import salhackskid.skid.SalHackSkid;

public class Module implements Listenable {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private boolean toggled = false;

    public void toggle() {
        toggled = !toggled;
        if (toggled) onEnable();
        else onDisable();
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
}
