package navy_master.hachimi.magic.menu;

import com.ibm.icu.impl.Pair;
import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.registry.ModMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MusicAltarMenu extends AbstractContainerMenu {
    private final MusicAltarBlockEntity blockEntity;
    private final Player player;
    private final List<Pair<ResourceLocation, Integer>> syncedFluids = new ArrayList<>();

    public MusicAltarMenu(int containerId, Inventory playerInventory, MusicAltarBlockEntity blockEntity) {
        super(ModMenuType.MUSIC_ALTAR.get(), containerId);
        this.blockEntity = blockEntity;
        this.player = playerInventory.player;

        // 添加3x3合成网格
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(blockEntity, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        this.addSlot(new ResultSlot(
                playerInventory.player, // Player
                blockEntity, // CraftingContainer
                blockEntity, // Container
                9, // slotIndex
                148, // x
                35 // y
        ));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
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
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 9) {
                // 结果槽位
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10) {
                // 玩家物品栏 -> 合成网格
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                // 合成网格 -> 玩家物品栏
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    public BlockPos getPos() {
        return blockEntity.getBlockPos();
    }
}