package navy_master.hachimi.magic.registry;
// 在 ModSoundEvents.java 文件中
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModDisc {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "hachimimagic");

    public static final RegistryObject<SoundEvent> DISC_SOUND_Hachimi_Legacy =
            SOUND_EVENTS.register("music_disc.hachimi_legacy",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("hachimimagic", "music_disc.hachimi_legacy")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}