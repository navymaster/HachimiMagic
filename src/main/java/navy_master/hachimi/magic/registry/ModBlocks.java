package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blocks.MixingTankBlock;
import navy_master.hachimi.magic.blocks.MusicMultiblockBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HachimiMagic.MODID);

    public static final RegistryObject<Block> MIXING_TANK = BLOCKS.register("mixing_tank",
            () -> new MixingTankBlock(BlockBehaviour.Properties.of()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}