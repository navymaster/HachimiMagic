package navy_master.hachimi.magic.music_altar;

import navy_master.hachimi.magic.menu.MusicAltarMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleTankPacket {
    private final int tankIndex;

    public ToggleTankPacket(int tankIndex) {
        this.tankIndex = tankIndex;
    }

    public ToggleTankPacket(FriendlyByteBuf buffer) {
        this.tankIndex = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(tankIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null && player.containerMenu instanceof MusicAltarMenu menu) {
                menu.getBlockEntity().toggleTankActive(tankIndex);
            }
        });
        context.get().setPacketHandled(true);
    }
}