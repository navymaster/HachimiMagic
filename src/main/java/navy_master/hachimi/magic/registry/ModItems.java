package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.Item.WalkmanBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HachimiMagic.MODID);


    public static final RegistryObject<Item> MUSIC_DISC_Hachimi_Legacy = ITEMS.register("hachimi_legacy_music_disc",
            () -> new RecordItem(15,() -> ModDisc.DISC_SOUND_Hachimi_Legacy.get(), new Item.Properties().stacksTo(1), 600));

    public static final RegistryObject<Item> HACHIMIMAGIC_TAB_ICON = ITEMS.register("tab_icon",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> HA_LEVEL_1 = ITEMS.register("ha_level_1",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> WALKMAN = ITEMS.register("walkman",
            () -> new WalkmanBase(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final List<RegistryObject<Item>> TAB_ITEMS = List.of(
            MUSIC_DISC_Hachimi_Legacy,
            HA_LEVEL_1,
            WALKMAN
    );
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}