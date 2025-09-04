package navy_master.hachimi.magic.registry;

// 在 ModItems.java 文件中
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "hachimimagic");


    public static final RegistryObject<Item> MUSIC_DISC_Hachimi_Legacy = ITEMS.register("hachimi_legacy_music_disc",
            () -> new RecordItem(15,() -> ModDisc.DISC_SOUND_Hachimi_Legacy.get(), new Item.Properties().stacksTo(1), 600));

    public static final RegistryObject<Item> HACHIMIMAGIC_TAB_ICON = ITEMS.register("tab_icon",
            () -> new Item(new Item.Properties()) // 这是一个普通的物品，无特殊属性
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}