package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.Item.WalkmanBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HachimiMagic.MODID);
    // 创造模式物品栏icon
    public static final RegistryObject<Item> HACHIMIMAGIC_TAB_ICON = ITEMS.register("tab_icon",
            () -> new Item(new Item.Properties())
    );



    public static final RegistryObject<Item> MUSIC_DISC_Hachimi_Legacy = ITEMS.register("hachimi_legacy_music_disc",
            () -> new RecordItem(15,() -> ModDisc.DISC_SOUND_Hachimi_Legacy.get(),
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), 600));



    public static final RegistryObject<Item> WALKMAN = ITEMS.register("walkman",
            () -> new WalkmanBase(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));



    public static final RegistryObject<Item> HA_LEVEL_1 = ITEMS.register("ha_level_1",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> MANBO_LEVEL_1 = ITEMS.register("manbo_level_1",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> JIMI_LEVEL_1 = ITEMS.register("jimi_level_1",
            () -> new Item(new Item.Properties())
    );
    public static final RegistryObject<Item> HA_LEVEL_2 = ITEMS.register("ha_level_2",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    public static final RegistryObject<Item> MANBO_LEVEL_2 = ITEMS.register("manbo_level_2",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    public static final RegistryObject<Item> JIMI_LEVEL_2 = ITEMS.register("jimi_level_2",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    public static final RegistryObject<Item> HA_LEVEL_3 = ITEMS.register("ha_level_3",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE))
    );
    public static final RegistryObject<Item> MANBO_LEVEL_3 = ITEMS.register("manbo_level_3",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE))
    );
    public static final RegistryObject<Item> JIMI_LEVEL_3 = ITEMS.register("jimi_level_3",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE))
    );
    public static final RegistryObject<Item> HA_LEVEL_4 = ITEMS.register("ha_level_4",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC))
    );
    public static final RegistryObject<Item> MANBO_LEVEL_4 = ITEMS.register("manbo_level_4",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC))
    );
    public static final RegistryObject<Item> JIMI_LEVEL_4 = ITEMS.register("jimi_level_4",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC))
    );

    public static final List<RegistryObject<Item>> TAB_ITEMS = List.of(
            MUSIC_DISC_Hachimi_Legacy,
            HA_LEVEL_1,
            MANBO_LEVEL_1,
            JIMI_LEVEL_1,
            WALKMAN,
            ModBlocks.NORMAL_TANK.item(),
            ModBlocks.MIXING_TANK.item(),
            ModBlocks.MUSIC_TANK.item(),
            ModFluids.HA_EMULSION.bucket(),
            ModFluids.JIMI_EMULSION.bucket(),
            ModFluids.MANBO_EMULSION.bucket(),
            ModFluids.MUSIC_EMULSION.bucket()
    );
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}