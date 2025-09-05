package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.walkman.WalkmanMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
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
}