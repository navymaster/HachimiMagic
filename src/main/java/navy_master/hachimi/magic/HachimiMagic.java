package navy_master.hachimi.magic;

import com.mojang.logging.LogUtils;
import navy_master.hachimi.magic.registry.*;
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

    public HachimiMagic(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        MinecraftForge.EVENT_BUS.register(this);
        ModDisc.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModMenuType.register(modEventBus);
        LootModifier.register(modEventBus);
    }
}
