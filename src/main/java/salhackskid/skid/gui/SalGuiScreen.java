package salhackskid.skid.gui;

import java.io.IOException;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;

public class SalGuiScreen extends Screen
{
    private boolean InventoryMoveEnabled()
    {
        return true;
    }

    public SalGuiScreen() {
        super(Text.of("hax"));
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
    public void render(MatrixStack matrix, int mouseX, int mouseY, float tickDelta)
    {
        super.render(matrix, mouseX, mouseY, tickDelta);
    }

    @Override
    public void mouse() {

    }

}
