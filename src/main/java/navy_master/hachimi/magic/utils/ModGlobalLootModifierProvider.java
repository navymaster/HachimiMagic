package navy_master.hachimi.magic.utils;

import navy_master.hachimi.magic.modifier.FishLootModifier;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void start() {
        LootItemCondition[] conditions = new LootItemCondition[]{
                LootTableIdCondition.builder(new ResourceLocation("minecraft", "gameplay/fishing")).build()
        };

        this.add("fish_loot",
                new FishLootModifier(
                        conditions,
                        ModItems.MANBO_LEVEL_1.get(), // 你的鱼物品
                        5
                )
        );
    }
}