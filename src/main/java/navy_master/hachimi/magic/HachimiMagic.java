package navy_master.hachimi.magic;

import navy_master.hachimi.magic.music_altar.MultiblockActiveEventHandler;
import navy_master.hachimi.magic.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        ModBlocks.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        LootModifier.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(new MultiblockActiveEventHandler());

        // 注册网络包
        PacketHandler.register();
    }
}
