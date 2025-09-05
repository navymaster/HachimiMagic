package navy_master.hachimi.magic.Item;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.registry.ModItems;
import navy_master.hachimi.magic.walkman.WalkmanMenu;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class WalkmanBase extends Item {
    // 纹理标识，准备两个纹理，"walkman_empty" 和 "walkman_loaded"，分别显示有无唱片时的随身听
    private static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation(HachimiMagic.MODID, "textures/item/walkman_empty.png");
    private static final ResourceLocation LOADED_TEXTURE = new ResourceLocation(HachimiMagic.MODID, "textures/item/walkman_loaded.png");

    public WalkmanBase(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            // 蹲下右键：打开GUI
            if (!level.isClientSide) {
                // 服务器端：打开菜单
                NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("item.hachimimagic.walkman");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                        return new WalkmanMenu(containerId, playerInventory, InteractionHand.MAIN_HAND, stack);
                    }
                }, buf -> {
                    buf.writeEnum(hand); // 将交互的手（MAIN_HAND 或 OFF_HAND）写入数据缓冲区
                });
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        } else {
            // 直接右键：尝试播放音乐
            if (!level.isClientSide) {
                playMusic(stack, level, player);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
    }

    private void playMusic(ItemStack stack, Level level, Player player) {
        // 从NBT中获取第一格的唱片
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("WalkmanInventory")) {
            CompoundTag inventoryTag = tag.getCompound("WalkmanInventory");
            ListTag items = inventoryTag.getList("Items", Tag.TAG_COMPOUND);
            if (!items.isEmpty()) {
                CompoundTag firstSlot = items.getCompound(0); // 获取第一个物品槽的NBT
                ItemStack recordStack = ItemStack.of(firstSlot);
                if (recordStack.getItem() instanceof RecordItem recordItem) {
                    // 播放唱片音乐
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            recordItem.getSound(), SoundSource.RECORDS, 4.0f, 1.0f);
                }
            }
        }
    }

    public static void registerProperties() {
        ItemProperties.register(ModItems.WALKMAN.get(),
                new ResourceLocation(HachimiMagic.MODID, "has_record"),
                (itemStack, clientLevel, livingEntity, seed) -> {
                    return hasRecord(itemStack) ? 1.0F : 0.0F;
                });
    }

    // 检查随身听是否有唱片
    public static boolean hasRecord(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("WalkmanInventory")) {
            CompoundTag inventoryTag = tag.getCompound("WalkmanInventory");
            ListTag items = inventoryTag.getList("Items", Tag.TAG_COMPOUND);
            return !items.isEmpty();
        }
        return false;
    }
}