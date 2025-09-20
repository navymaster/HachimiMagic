package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.blockentity.MusicTankEntity;
import navy_master.hachimi.magic.blockentity.NormalTankEntity;
import navy_master.hachimi.magic.blocks.MixingTankBlock;
import navy_master.hachimi.magic.blocks.MusicAltarCoreBlock;
import navy_master.hachimi.magic.blocks.MusicTank;
import navy_master.hachimi.magic.blocks.NormalTank;
import navy_master.hachimi.magic.utils.BlockRegHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HachimiMagic.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HachimiMagic.MODID);
    public static final BlockRegHelper.BlockRegistration MUSIC_ALTAR = BlockRegHelper.registerBlockEntities(
            "music_altar",
            MusicAltarBlockEntity.class,
            MusicAltarCoreBlock.class
    );
    public static final BlockRegHelper.BlockRegistration NORMAL_TANK = BlockRegHelper.registerBlockEntities(
            "normal_tank",
            NormalTankEntity.class,
            NormalTank.class
    );
    public static final BlockRegHelper.BlockRegistration MIXING_TANK = BlockRegHelper.registerBlockEntities(
            "mixing_tank",
            MixingTankBlockEntity.class,
            MixingTankBlock.class
    );
    public static final BlockRegHelper.BlockRegistration MUSIC_TANK = BlockRegHelper.registerBlockEntities(
            "music_tank",
            MusicTankEntity.class,
            MusicTank.class
    );

     public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}