package salhackskid.skid.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import salhackskid.skid.SalHackSkid;

public class Command
{
    private String Name;
    private String Description;

    protected final MinecraftClient mc = MinecraftClient.getInstance();
    protected final List<String> CommandChunks = new ArrayList<String>();

    public Command(String p_Name, String p_Description)
    {
        Name = p_Name;
        Description = p_Description;
    }

    public String GetName()
    {
        return Name;
    }

    public String GetDescription()
    {
        return Description;
    }

    public void ProcessCommand(String p_Args)
    {
    }


    protected void SendToChat(String p_Desc)
    {
        SalHackSkid.SendMessage(String.format("%s[%s]: %s", Formatting.LIGHT_PURPLE, GetName(), Formatting.YELLOW + p_Desc));
    }

    public List<String> GetChunks()
    {
        return CommandChunks;
    }

    public String GetHelp()
    {
        return Description;
    }
}
