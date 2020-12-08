package salhackskid.skid.module.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.util.math.MathHelper;
import salhackskid.skid.event.EventTick;
import salhackskid.skid.gui.managers.HudManager;
import salhackskid.skid.module.Module;
import salhackskid.skid.module.Value;

public final class HudModule extends Module
{
    public final Value<Boolean> CustomFOV = new Value<Boolean>("CustomFOV", new String[]
            { "CustomFOV" }, "Enables the option below", false);
    public final Value<Float> FOV = new Value<Float>("FOV", new String[]
            { "FOV" }, "Override the clientside FOV", (float) mc.options.fov, 0f, 179f, 10f);

    public HudModule()
    {
        super("HUD", new String[]
                { "HUD" }, "Displays the HUD", "NONE", 0xD1DB24, Module.ModuleType.UI);
    }

    public void toggleNoSave() {

    }

    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions options = mc.options;
        if (mc.world != null) {
            options.fov = options.fov * (1 + FOV.getValue());
        }
    }

    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions options = mc.options;
        options.fov = options.fov / (1 + FOV.getValue());
    }
}