package navy_master.hachimi.magic.registry;

import com.mojang.serialization.Codec;
import navy_master.hachimi.magic.modifier.FishLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static navy_master.hachimi.magic.HachimiMagic.MODID;

public class LootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FISH_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("fish_loot", FishLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
