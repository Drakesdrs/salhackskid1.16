package salhackskid.skid.gui.hud;

import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.util.math.MatrixStack;
import salhackskid.skid.module.gui.ClickGuiModule;
import salhackskid.skid.module.gui.ColorsModule;
import salhackskid.skid.utils.Render;
import salhackskid.skid.utils.ScaledResolution;

public class MenuComponent
{
    private String DisplayName;
    protected ArrayList<ComponentItem> Items = new ArrayList<ComponentItem>();

    private float DefaultX;
    private float DefaultY;
    private float X;
    private float Y;
    private float Height;
    private float Width;
    private boolean Dragging = false;
    private float DeltaX = 0;
    private float DeltaY = 0;
    private ComponentItem HoveredItem = null;
    private boolean Minimized = false;
    private boolean IsMinimizing = false;
    private float RemainingMinimizingY;
    private boolean IsMaximizing = false;
    private float RemainingMaximizingY;
    private int MousePlayAnim;
    private SalDynamicTexture BarTexture = null;
    private ColorsModule Colors;
    private ClickGuiModule ClickGUI;
    MinecraftClient mc = MinecraftClient.getInstance();
    private TextRenderer textRenderer = mc.textRenderer;

    public MenuComponent(String p_DisplayName, float p_X, float p_Y, float p_Height, float p_Width, String p_Image, ColorsModule p_Colors, ClickGuiModule p_ClickGui)
    {
        DisplayName = p_DisplayName;
        DefaultX = p_X;
        DefaultY = p_Y;
        X = p_X;
        Y = p_Y;
        Height = p_Height;
        Width = p_Width;
        RemainingMinimizingY = 0;
        RemainingMaximizingY = 0;
        MousePlayAnim = 0;

        if (p_Image != null)
        {
            BarTexture = ImageManager.Get().GetDynamicTexture(p_Image);
        }

        Colors = p_Colors;
        ClickGUI = p_ClickGui;
    }

    public void AddItem(ComponentItem p_Item)
    {
        Items.add(p_Item);
    }

    final float BorderLength = 15.0f;
    final float Padding = 3;

    public boolean Render(int p_MouseX, int p_MouseY, boolean p_CanHover, boolean p_AllowsOverflow, float p_OffsetY)
    {
        if (Dragging)
        {
            X = p_MouseX - DeltaX;
            Y = p_MouseY - DeltaY;
        }

        if (!p_AllowsOverflow)
        {
            ScaledResolution l_Res = new ScaledResolution(MinecraftClient.getInstance());

            /// Don't allow too much to right, or left
            if (X+GetWidth() >= l_Res.getScaledWidth())
                X = l_Res.getScaledWidth() - GetWidth();
            else if (X < 0)
                X = 0;

            if (Y+GetHeight() >= l_Res.getScaledHeight())
                Y = l_Res.getScaledHeight() - GetHeight();
            else if (Y < 0)
                Y = 0;
        }

        for (ComponentItem l_Item : Items)
            l_Item.OnMouseMove(p_MouseX, p_MouseY, GetX(), GetY()-p_OffsetY);

        if (IsMinimizing)
        {
            if (RemainingMinimizingY > 0)
            {
                RemainingMinimizingY -= 20;

                RemainingMinimizingY = Math.max(RemainingMinimizingY, 0);

                if (RemainingMinimizingY == 0)
                {
                    Minimized = true;
                    IsMinimizing = false;
                    Height = 0;
                }
            }
        }
        else if (IsMaximizing)
        {
            if (RemainingMaximizingY < 500)
            {
                RemainingMaximizingY += 20;

                RemainingMaximizingY = Math.min(RemainingMaximizingY, 500);

                if (RemainingMaximizingY == 500)
                {
                    IsMaximizing = false;
                    Height = 0;
                }
            }
        }

        //RenderUtil.drawOutlineRect(GetX()+10, GetY()+17-p_OffsetY+10, GetX()+GetWidth()-10, GetY()+GetHeight()-10, 10, 0x99333cc4);
        //RenderUtil.drawRect(GetX(), GetY()+17-p_OffsetY, GetX()+GetWidth(), GetY()+GetHeight(), 0x99333cc4);
        Render.drawRect(GetX(), GetY() + 13 - p_OffsetY, GetX() + 1, GetY() + GetHeight(), 0x333cc4, 0xff); //vertical left
        Render.drawRect(GetX() + GetWidth() - 1, GetY() + 13 - p_OffsetY, GetX() + GetWidth(), GetY() + GetHeight(), 0x333cc4, 0xff); //vertical right
        Render.drawRect(GetX(), GetY() + 13 - p_OffsetY, GetX() + GetWidth(), GetY() + 14 - p_OffsetY, 0x333cc4, 0xff); //horizontal top
        Render.drawRect(GetX(), GetY() + GetHeight() - 1, GetX() + GetWidth(), GetY() + GetHeight(), 0x333cc4, 0xff); //horizontal bottom
        Render.drawRect(GetX() + 1, GetY() + 1, GetX() + GetWidth() - 1, GetY() + GetHeight() - 1, 0x333cc4, 0x70); //complete
        DrawableHelper.fill(matrix, GetX(), GetY() - p_OffsetY, GetX() + GetWidth(), GetY() + 13 - p_OffsetY, 0xff333cc4, 0xff333cc4); /// top

        if (!Minimized)
        {
            float l_Y = GetY() + 5-p_OffsetY;

            HoveredItem = null;

            boolean l_Break = false;

            for (ComponentItem l_Item : Items)
            {
                l_Y = DisplayComponentItem(l_Item, l_Y, p_MouseX, p_MouseY, p_CanHover, false, IsMinimizing ? RemainingMinimizingY : (IsMaximizing ? RemainingMaximizingY : 0));

                float l_MenuY = Math.abs(Y - l_Y - BorderLength);

                if (IsMinimizing && l_MenuY >= RemainingMinimizingY)
                    l_Break = true;
                else if (IsMaximizing && l_MenuY >= RemainingMaximizingY)
                    l_Break = true;

                if (l_Break)
                    break;
            }

            if (!l_Break)
            {
                IsMinimizing = false;
                IsMaximizing = false;
            }

            if (HoveredItem != null && (ClickGUI != null ? ClickGUI.HoverDescriptions.getValue() : true))
            {
                if (HoveredItem.GetDescription() != null && HoveredItem.GetDescription() != "")
                {
                    Render.drawRect(p_MouseX+15, p_MouseY, p_MouseX+19+Render.getStringWidth(HoveredItem.GetDescription()), p_MouseY + RenderUtil.getStringHeight(HoveredItem.GetDescription())+3, 0x90000000);
                    RenderUtil.drawStringWithShadow(HoveredItem.GetDescription(), p_MouseX+17, p_MouseY, 0xFFFFFF);
                }
            }

            Height = Math.abs(Y - l_Y - 12);
        }

        if (MousePlayAnim > 0)
        {
            MousePlayAnim--;

            RenderUtil.DrawPolygon(p_MouseX, p_MouseY, MousePlayAnim, 360, 0x99FFFFFF);
        }

        return p_CanHover && p_MouseX > GetX() && p_MouseX < GetX() + GetWidth() && p_MouseY > GetY()-p_OffsetY && p_MouseY < GetY()+GetHeight()-p_OffsetY;
    }

    public float DisplayComponentItem(ComponentItem p_Item, float p_Y, int p_MouseX, int p_MouseY, boolean p_CanHover, boolean p_DisplayExtendedLine, final float p_MaxY)
    {
        p_Y += p_Item.GetHeight();

        p_Item.OnMouseMove(p_MouseX, p_MouseY, GetX(), GetY());
        p_Item.Update();

        if (p_Item.HasState(ComponentItem.Extended))
        {
            RenderUtil.drawRect(X+1,p_Y,X+p_Item.GetWidth()-3,p_Y + RenderUtil.getStringHeight(p_Item.GetDisplayText()) + 3,0x080808);
        }

        int l_Color = 0xFFFFFF;

        boolean l_Hovered = p_CanHover && p_MouseX > X && p_MouseX < X+p_Item.GetWidth() && p_MouseY > p_Y && p_MouseY < p_Y+p_Item.GetHeight();

        boolean l_DropDown = p_Item.HasState(ComponentItem.Extended);

        if (l_Hovered)
        {
            if (!l_DropDown)
                RenderUtil.drawGradientRect(GetX(), p_Y, GetX()+p_Item.GetWidth(), p_Y+11, 0x99040404, 0x99000000);
            l_Color = (p_Item.HasState(ComponentItem.Clicked) && !p_Item.HasFlag(ComponentItem.DontDisplayClickableHighlight)) ? GetTextColor() : l_Color;// - commented for issue #27
            HoveredItem = p_Item;

            p_Item.AddState(ComponentItem.Hovered);
        }
        else
        {
            if (p_Item.HasState(ComponentItem.Clicked) && !p_Item.HasFlag(ComponentItem.DontDisplayClickableHighlight))
                l_Color = GetTextColor();

            p_Item.RemoveState(ComponentItem.Hovered);
        }

        if (l_DropDown)
            DrawableHelper.fill(matrix, GetX(), p_Y, GetX()+p_Item.GetWidth(), p_Y+11, 0x99040404, 0x99000000);

        if (p_Item.HasFlag(ComponentItem.RectDisplayAlways) || (p_Item.HasFlag(ComponentItem.RectDisplayOnClicked) && p_Item.HasState(ComponentItem.Clicked)))
            RenderUtil.drawRect(GetX(), p_Y, GetX()+p_Item.GetCurrentWidth(), p_Y+11, p_Item.HasState(ComponentItem.Clicked) || p_Item.HasFlag(ComponentItem.DontDisplayClickableHighlight) ? GetColor() : GetColor());

        DrawableHelper.drawStringWithShadow(matrix, ,p_Item.GetDisplayText(), X + Padding, p_Y, l_Color);

        /*if (p_Item.HasFlag(ComponentItem.HasValues))
        {
            RenderUtil.drawLine(X + p_Item.GetWidth() - 1, p_Y, X + p_Item.GetWidth() - 1, p_Y + 11, 5, 0x9945B5E4);
        }*/

        if (p_Item.HasState(ComponentItem.Extended) || p_DisplayExtendedLine)
        {
            RenderUtil.drawLine(X + p_Item.GetWidth() - 1, p_Y, X + p_Item.GetWidth() - 1, p_Y + 11, 3, GetColor());
        }

        if (p_Item.HasState(ComponentItem.Extended))
        {
            for (ComponentItem l_ValItem : p_Item.DropdownItems)
            {
                p_Y = DisplayComponentItem(l_ValItem, p_Y, p_MouseX, p_MouseY, p_CanHover, true, p_MaxY);

                if (p_MaxY > 0)
                {
                    float l_MenuY = Math.abs(Y - p_Y - BorderLength);

                    if (l_MenuY >= p_MaxY)
                        break;
                }
            }
        }

        return p_Y;
    }

    public boolean MouseClicked(int p_MouseX, int p_MouseY, int p_MouseButton, float offsetY)
    {
        if (p_MouseX > GetX() && p_MouseX < GetX() + GetWidth() && p_MouseY > GetY()-offsetY && p_MouseY < GetY()+BorderLength-offsetY)
        {
            /// Dragging (Top border)
            if (p_MouseButton == 0)
            {
                Dragging = true;
                DeltaX = p_MouseX-X;
                DeltaY = p_MouseY-Y;
            }
            else if (p_MouseButton == 1)
            {
                /// Right click
                if (!Minimized)
                {
                    IsMinimizing = true;
                    RemainingMinimizingY = Height;

                    IsMaximizing = false;
                    RemainingMaximizingY = 0;
                }
                else
                {
                    Minimized = false;

                    IsMinimizing = false;
                    RemainingMinimizingY = 0;

                    IsMaximizing = true;
                    RemainingMaximizingY = 0;
                }
            }
        }

        if (HoveredItem != null)
        {
            HoveredItem.OnMouseClick(p_MouseX, p_MouseY, p_MouseButton);

            if (p_MouseButton == 0)
                MousePlayAnim = 20;
            return true;
        }

        return Dragging;
    }

    public void MouseReleased(int p_MouseX, int p_MouseY, int p_State)
    {
        if (Dragging)
            Dragging = false;

        for (ComponentItem l_Item : Items)
        {
            HandleMouseReleaseCompItem(l_Item, p_MouseX, p_MouseY);
        }
    }

    public void HandleMouseReleaseCompItem(ComponentItem p_Item, int p_MouseX, int p_MouseY)
    {
        p_Item.OnMouseRelease(p_MouseX, p_MouseY);

        for (ComponentItem l_Item : p_Item.DropdownItems)
        {
            l_Item.OnMouseRelease(p_MouseX, p_MouseY);
        }
    }

    public void MouseClickMove(int p_MouseX, int p_MouseY, int p_ClickedMouseButton, long p_TimeSinceLastClick)
    {
        for (ComponentItem l_Item : Items)
        {
            HandleMouseClickMoveCompItem(l_Item, p_MouseX, p_MouseY, p_ClickedMouseButton);
        }
    }

    private void HandleMouseClickMoveCompItem(ComponentItem l_Item, int p_MouseX, int p_MouseY, int p_ClickedMouseButton)
    {
        l_Item.OnMouseClickMove(p_MouseX, p_MouseY, p_ClickedMouseButton);

        for (ComponentItem l_Item2 : l_Item.DropdownItems)
        {
            l_Item2.OnMouseClickMove(p_MouseX, p_MouseY, p_ClickedMouseButton);
        }
    }

    public String GetDisplayName()
    {
        return DisplayName;
    }

    public float GetX()
    {
        return X;
    }

    public float GetY()
    {
        return Y;
    }

    public float GetWidth()
    {
        return Width;
    }

    public float GetHeight()
    {
        return Height;
    }

    public void SetX(float p_X)
    {
        X = p_X;
    }

    public void SetY(float p_Y)
    {
        Y = p_Y;
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        for (ComponentItem l_Item : Items)
            HandleKeyTypedForItem(l_Item, typedChar, keyCode);
    }

    public void HandleKeyTypedForItem(ComponentItem p_Item, char typedChar, int keyCode)
    {
        p_Item.keyTyped(typedChar, keyCode);

        for (ComponentItem l_Item : p_Item.DropdownItems)
            HandleKeyTypedForItem(l_Item, typedChar, keyCode);
    }

    private int GetColor()
    {
        return (Colors.Alpha.getValue() << 24) & 0xFF000000 | (Colors.Red.getValue() << 16) & 0x00FF0000 | (Colors.Green.getValue() << 8) & 0x0000FF00 | Colors.Blue.getValue() & 0x000000FF;
    }

    public int GetTextColor()
    {
        return (Colors.Red.getValue() << 16) & 0x00FF0000 | (Colors.Green.getValue() << 8) & 0x0000FF00 | Colors.Blue.getValue() & 0x000000FF;
    }

    public void Default()
    {
        X = DefaultX;
        Y = DefaultY;

        Items.forEach(comp ->
        {
            if (comp.HasState(ComponentItem.Extended))
                comp.RemoveState(ComponentItem.Extended);
        });
    }
}
