package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blocks.MusicAltarCoreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HachimiMagic.MODID);
    public static final RegistryObject<Block> MUSIC_ALTAR_CORE = BLOCKS.register("music_altar_core",
            () -> new MusicAltarCoreBlock(Block.Properties.copy(Blocks.JUKEBOX)));
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}