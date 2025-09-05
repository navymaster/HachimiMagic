package navy_master.hachimi.magic.walkman;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;

// 这是一个辅助类，用于更方便地操作随身听的NBT库存
public class WalkmanInventory implements Container {
    private final ItemStack walkmanStack;
    private static final int SLOTS = 9;

    // 存储物品的列表
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public WalkmanInventory(ItemStack stack) {
        this.walkmanStack = stack;
        load(); // 从NBT加载物品
    }

    // 从NBT加载物品
    public void load() {
        CompoundTag tag = walkmanStack.getOrCreateTag();
        if (tag.contains("WalkmanInventory")) {
            ContainerHelper.loadAllItems(tag.getCompound("WalkmanInventory"), items);
        }
    }

    // 保存物品到NBT
    public void save() {
        CompoundTag tag = walkmanStack.getOrCreateTag();
        CompoundTag inventoryTag = new CompoundTag();
        ContainerHelper.saveAllItems(inventoryTag, items);
        tag.put("WalkmanInventory", inventoryTag);
    }

    // 实现Container接口的必需方法
    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < items.size()) {
            return items.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(items, slot);
        if (!stack.isEmpty()) {
            setChanged();
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < items.size()) {
            items.set(slot, stack);
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        save(); // 每次变更都保存到NBT
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    // 检查某个槽位是否可以放入物品（Container接口方法）
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof RecordItem;
    }
}