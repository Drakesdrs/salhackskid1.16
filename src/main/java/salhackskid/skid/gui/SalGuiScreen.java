package salhackskid.skid.gui;

import java.io.IOException;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public class SalGuiScreen extends Screen
{
    // private NoSlowModule NoSlow = null;

    private boolean InventoryMoveEnabled()
    {
        /*if (NoSlow == null)
            NoSlow = (NoSlowModule)SalHack.INSTANCE.getModuleManager().find(NoSlowModule.class);

        return NoSlow != null && NoSlow.InventoryMove.getValue();*/
        return true;
    }

    public static void UpdateRotationPitch(float p_Amount)
    {
        final MinecraftClient mc = MinecraftClient.getInstance();

        float l_NewRotation = mc.player.pitch + p_Amount;

        l_NewRotation = Math.max(l_NewRotation, -90.0f);
        l_NewRotation = Math.min(l_NewRotation, 90.0f);

        mc.player.pitch = l_NewRotation;
    }

    public static void UpdateRotationYaw(float p_Amount)
    {
        final MinecraftClient mc = MinecraftClient.getInstance();

        float l_NewRotation = mc.player.yaw + p_Amount;

        // l_NewRotation = Math.min(l_NewRotation, -360.0f);
        // l_NewRotation = Math.max(l_NewRotation, 360.0f);

        mc.player.yaw = l_NewRotation;
    }

    @Override
    public void openScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.openScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, int prevMouseX, int prevMouseY)
    {
        super.mouseDragged(mouseX, mouseY, clickedMouseButton, prevMouseX, prevMouseY);
    }
}
