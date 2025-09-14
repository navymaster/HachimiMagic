package navy_master.hachimi.magic.blockentity;

import navy_master.hachimi.magic.menu.MixingTankMenu;
import navy_master.hachimi.magic.registry.ModBlockEntities;
import navy_master.hachimi.magic.registry.ModFluids;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class MixingTankBlockEntity extends BaseTankBlockEntity implements MenuProvider {
    public static Set<Item> surfactant=Set.of(
            Items.HONEY_BOTTLE,
            Items.SLIME_BALL,
            Items.INK_SAC
            );
    public static Map<Item, Fluid> emul_recipe =Map.of(
            ModItems.HA_LEVEL_1.get(),
            ModFluids.HA_EMULSION.source().get(),
            ModItems.MANBO_LEVEL_1.get(),
            ModFluids.MANBO_EMULSION.source().get(),
            ModItems.JIMI_LEVEL_1.get(),
            ModFluids.JIMI_EMULSION.source().get()
    );
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };

    private int progress = 0;
    private final int maxProgress = 100; // 100 ticks (5 seconds)

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MixingTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIXING_TANK.get(), pos, state);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
   }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MixingTankBlockEntity entity) {
        if (level.isClientSide) return;

        if (entity.hasRecipe()) {
            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                entity.craftEmulsion();
            }
        } else {
            entity.resetProgress();
        }
    }

    private boolean hasRecipe() {
        // 检查是否有水和表面活性剂
        if (fluidTank.getFluidAmount() < 1000) return false;
        if (itemHandler.getStackInSlot(1).isEmpty()) return false;
        if (itemHandler.getStackInSlot(0).isEmpty()) return false;


        // 获取材料槽和表面活性剂槽的物品
        FluidStack fluidStack =fluidTank.getFluid();
        ItemStack materialStack = itemHandler.getStackInSlot(0);
        ItemStack surfactantStack = itemHandler.getStackInSlot(1);

        return fluidStack.getFluid().isSame(Fluids.WATER) && surfactant.contains(surfactantStack.getItem()) && emul_recipe.keySet().contains(materialStack.getItem());
    }

    private void craftEmulsion() {
        // 消耗材料
        ItemStack materialStack = itemHandler.getStackInSlot(0);
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);

        // 转换流体：消耗所有水，转化为乳液
        int amount=fluidTank.getFluidAmount();
        fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
        Fluid outfluid= emul_recipe.get(materialStack.getItem());
        fluidTank.fill(new FluidStack(outfluid, amount), IFluidHandler.FluidAction.EXECUTE);

        resetProgress();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.hachimimagic.mixing_tank");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MixingTankMenu(containerId, playerInventory, this);
    }
}