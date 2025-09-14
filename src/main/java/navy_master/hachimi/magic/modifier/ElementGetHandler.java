package navy_master.hachimi.magic.modifier;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HachimiMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElementGetHandler {
    // 哈气掉落
    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Enemy && event.getSource().getDirectEntity() instanceof Player) {
            Player player = (Player) event.getSource().getEntity();
            Level level = player.level();

            // 10%概率掉落
            if (level.random.nextFloat() < 0.2f) {
                ItemEntity drop = new ItemEntity(level,
                        event.getEntity().getX(),
                        event.getEntity().getY(),
                        event.getEntity().getZ(),
                        new ItemStack(ModItems.HA_LEVEL_1.get()));
                level.addFreshEntity(drop);
            }
        }
    }

    // 基米掉落
    @SubscribeEvent
    public static void onFeedAnimal(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Animal animal &&
                !event.getLevel().isClientSide()) { // 只在服务端处理

            ItemStack itemStack = event.getItemStack();

            // 检查是否是食物且动物可以吃这个食物
            if (itemStack.getItem().isEdible() && animal.isFood(itemStack)) {

                if (animal.getAge() == 0 && animal.getInLoveTime() == 0) {
                    Level level = animal.level();

                    // 10%概率刷出
                    if (level.random.nextFloat() < 0.1f) {
                        ItemEntity spawn = new ItemEntity(level,
                                animal.getX(),
                                animal.getY() + 1.0,
                                animal.getZ(),
                                new ItemStack(ModItems.JIMI_LEVEL_1.get()));
                        level.addFreshEntity(spawn);
                    }
                }
            }
        }
    }
}
