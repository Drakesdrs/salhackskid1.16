package salhackskid.skid.command;

public interface ModuleCommandListener
{
    public void OnHide();
    public void OnToggle();
    public void OnRename(String p_NewName);
}
