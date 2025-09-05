package navy_master.hachimi.magic.walkman;

import navy_master.hachimi.magic.registry.ModMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;

public class WalkmanMenu extends AbstractContainerMenu {
    private final WalkmanInventory inventory;
    private final ItemStack walkmanStack;
    private final InteractionHand hand;

    // 客户端构造函数
    public WalkmanMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, extraData.readEnum(InteractionHand.class), playerInventory.player.getItemInHand(extraData.readEnum(InteractionHand.class)));
    }

    // 服务器端构造函数
    public WalkmanMenu(int containerId, Inventory playerInventory, InteractionHand hand, ItemStack walkmanStack) {
        super(ModMenuType.WALKMAN_MENU.get(), containerId);
        this.walkmanStack = walkmanStack;
        this.hand = hand;
        this.inventory = new WalkmanInventory(walkmanStack);

        checkContainerSize(inventory, inventory.getContainerSize()); // 检查容器大小

        // 添加随身听的9个槽位，只允许放入唱片
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot((Container) inventory, i, 8 + i * 18, 20) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof RecordItem;
                }
            });
        }

        // 添加玩家物品栏（3行9列）
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        // 添加玩家快捷栏
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(hand).equals(walkmanStack);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 9) {
                // 从随身听移动到玩家物品栏
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack1.getItem() instanceof RecordItem) {
                // 从玩家物品栏移动到随身听
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 36) {
                // 从主物品栏移动到快捷栏
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 45) {
                // 从快捷栏移动到主物品栏
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public WalkmanInventory getInventory() {
        return inventory;
    }
}