package navy_master.hachimi.magic.modifier;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;


public class FishLootModifier extends LootModifier {
    private final Item fishItem;
    private final int probability;

   public static final Supplier<Codec<FishLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(
           inst -> codecStart(inst) // 从父类获取条件
                   .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.fishItem))
                   .and(Codec.INT.fieldOf("probability").forGetter(m -> m.probability))
                   .apply(inst, FishLootModifier::new))
    );

    //public static final Supplier<Codec<FishLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, FishLootModifier::new)));

    public FishLootModifier(LootItemCondition[] conditions, Item fishItem, int probability) {
        super(conditions);
        this.fishItem = fishItem;
        this.probability = probability;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation lootTableId = context.getQueriedLootTableId();

        if (lootTableId.getPath().contains("fishing")) {
            // 翻车鱼在钓鱼战利品表
            if (context.getRandom().nextInt(100) < probability) {
                generatedLoot.add(new ItemStack((ModItems.MANBO_LEVEL_1.get())));
            }
        }
        return generatedLoot;
    }

    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

}