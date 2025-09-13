package navy_master.hachimi.magic.utils;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static navy_master.hachimi.magic.HachimiMagic.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();

        // 注册 GLM Provider
        gen.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput, MODID));

        // 通常你还会注册其他Provider (BlockStates, ItemModels, LootTables等)
    }
}