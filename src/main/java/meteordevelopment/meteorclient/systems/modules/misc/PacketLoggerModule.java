
// This file is NOT a part of the Meteor Client distribution.

package meteordevelopment.meteorclient.systems.modules.misc;

import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import net.minecraft.network.packet.Packet;

public class PacketLoggerModule extends Module {

    public PacketLoggerModule() {
        super(Categories.Misc, "packet-logger", "Logs every packet sent from the client.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        Packet<?> packet = event.packet;

        // Log the packet information
        info("Sent packet: " + packet.getClass().getSimpleName() + " - " + packet.toString());
    }
}
