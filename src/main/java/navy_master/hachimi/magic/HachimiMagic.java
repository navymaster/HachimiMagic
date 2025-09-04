package navy_master.hachimi.magic;

import com.mojang.logging.LogUtils;
import navy_master.hachimi.magic.registry.ModCreativeTabs;
import navy_master.hachimi.magic.registry.ModDisc;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(HachimiMagic.MODID)
public class HachimiMagic {
    public static final String MODID = "hachimimagic";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);


    public HachimiMagic(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModDisc.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
