package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HachimiMagic.MODID);

    public static final RegistryObject<BlockEntityType<MusicAltarBlockEntity>> MUSIC_ALTAR =
            BLOCK_ENTITIES.register("music_altar", () ->
                    BlockEntityType.Builder.of(MusicAltarBlockEntity::new, ModBlocks.MUSIC_ALTAR_CORE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MixingTankBlockEntity>> MIXING_TANK =
            BLOCK_ENTITIES.register("mixing_tank", () ->
                    BlockEntityType.Builder.of(MixingTankBlockEntity::new, ModBlocks.MIXING_TANK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}