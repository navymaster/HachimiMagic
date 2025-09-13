package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.music_altar.ToggleTankPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                    new ResourceLocation(HachimiMagic.MODID, "main"))
            .networkProtocolVersion(() -> "1")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    public static void register() {
        INSTANCE.registerMessage(0, ToggleTankPacket.class,
                ToggleTankPacket::encode, ToggleTankPacket::new,
                ToggleTankPacket::handle);
    }
}