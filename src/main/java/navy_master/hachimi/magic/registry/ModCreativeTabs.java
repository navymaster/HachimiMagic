package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HachimiMagic.MODID); // 替换"你的modid"

    public static final RegistryObject<CreativeModeTab> HACHIMI_TAB = CREATIVE_MODE_TABS.register("hachimimagic",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.HACHIMIMAGIC_TAB_ICON.get())) // 设置标签页图标，使用你的唱片
                    .title(Component.translatable("creativetab.hachimimagic.hachimimagic")) // 设置标签页的翻译键
                    .displayItems((parameters, output) -> {
                        for (RegistryObject<Item> item : ModItems.TAB_ITEMS) {
                            output.accept(item.get());
                        }
                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}