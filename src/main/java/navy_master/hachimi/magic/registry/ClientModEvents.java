package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.walkman.WalkmanScreen;
import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.Item.WalkmanBase;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HachimiMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册菜单屏幕
            MenuScreens.register(ModMenuType.WALKMAN_MENU.get(), WalkmanScreen::new);

            // 注册物品属性
            WalkmanBase.registerProperties();
        });
    }
}