package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.menu.WalkmanMenu;
import navy_master.hachimi.magic.menu.MixingTankMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModMenuType {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HachimiMagic.MODID);

    public static final RegistryObject<MenuType<WalkmanMenu>> WALKMAN_MENU = MENUS.register("walkman_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> {
                InteractionHand hand = data.readEnum(InteractionHand.class);
                ItemStack stack = inv.player.getItemInHand(hand);
                return new WalkmanMenu(windowId, inv, hand, stack);
            }));

    public static final RegistryObject<MenuType<MixingTankMenu>> MIXING_TANK =
            MENUS.register("mixing_tank_menu", () -> IForgeMenuType.create(MixingTankMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}