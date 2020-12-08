package salhackskid.skid.module.movement;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import salhackskid.skid.event.EventTick;
import salhackskid.skid.module.Module;

public class Speed extends Module {

    public Speed() {
        super("Speed", new String[]
                { "Speed", "Speed" }, "speeeed", "NONE", -1, ModuleType.MOVEMENT);
    }

     @EventHandler
     private Listener<EventTick> onTick = new Listener<>(p_Event -> {
         double speedstrafe = 1.09 / 3;
         double forward = mc.player.forwardSpeed;
         double strafe = mc.player.sidewaysSpeed;
         float yaw = mc.player.yaw;
         if ((forward == 0.0D) && (strafe == 0.0D)) {
             mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
         }
         else {
             if (forward != 0.0D) {
                 if (strafe > 0.0D) {
                     yaw += (forward > 0.0D ? -45 : 45);
                 } else if (strafe < 0.0D) yaw += (forward > 0.0D ? 45 : -45);
                 strafe = 0.0D;
                 if (forward > 0.0D) {
                     forward = 1.0D;
                 } else if (forward < 0.0D) forward = -1.0D;
             }
             mc.player.setVelocity((forward * speedstrafe * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speedstrafe * Math.sin(Math.toRadians(yaw + 90.0F))), mc.player.getVelocity().y,
                     forward * speedstrafe * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speedstrafe * Math.cos(Math.toRadians(yaw + 90.0F)));
         }
     });
}
