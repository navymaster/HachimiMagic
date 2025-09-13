package navy_master.hachimi.magic.menu;


import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.registry.ModBlocks;
import navy_master.hachimi.magic.registry.ModMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MixingTankMenu extends AbstractContainerMenu {
    public final MixingTankBlockEntity blockEntity;
    private final Level level;

    public MixingTankMenu(int containerId, Inventory inv, MixingTankBlockEntity entity) {
        super(ModMenuType.MIXING_TANK.get(), containerId);
        this.blockEntity = entity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        IItemHandler handler = entity.getItemHandler();

        // 乳化材料槽 (左边)
        this.addSlot(new SlotItemHandler(handler, 0, 56, 35));

        // 表面活性剂槽 (右边)
        this.addSlot(new SlotItemHandler(handler, 1, 104, 35));
    }

    public MixingTankMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (MixingTankBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.MIXING_TANK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // 处理Shift+点击
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
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

    // 获取方块实体
    public MixingTankBlockEntity getBlockEntity() {
        return blockEntity;
    }
}