package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.blockentity.MusicTankEntity;
import navy_master.hachimi.magic.blockentity.NormalTankEntity;
import navy_master.hachimi.magic.blocks.MixingTankBlock;
import navy_master.hachimi.magic.blocks.MusicTank;
import navy_master.hachimi.magic.blocks.NormalTank;
import navy_master.hachimi.magic.utils.EntityBlockRegHelper;
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

    public static final EntityBlockRegHelper.EntityBlockRegistration NORMAL_TANK =EntityBlockRegHelper.registerBlockEntities(
            "normal_tank",
            NormalTankEntity.class,
            NormalTank.class
    );

    public static final EntityBlockRegHelper.EntityBlockRegistration MIXING_TANK =EntityBlockRegHelper.registerBlockEntities(
            "mixing_tank",
            MixingTankBlockEntity.class,
            MixingTankBlock.class
    );

    public static final EntityBlockRegHelper.EntityBlockRegistration MUSIC_TANK =EntityBlockRegHelper.registerBlockEntities(
            "music_tank",
            MusicTankEntity.class,
            MusicTank.class
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}