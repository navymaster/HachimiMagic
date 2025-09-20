package navy_master.hachimi.magic.blockentity;

import navy_master.hachimi.magic.menu.MusicAltarMenu;
import navy_master.hachimi.magic.music_altar.MultiblockStructureChecker;
import navy_master.hachimi.magic.registry.ModBlocks;
import navy_master.hachimi.magic.registry.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MusicAltarBlockEntity extends BlockEntity implements WorldlyContainer, CraftingContainer,MenuProvider{
    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

    public static final Map<ResourceLocation,Integer> typecode= Map.of(
            ModFluids.HA_EMULSION.source().getId(),0,
            ModFluids.JIMI_EMULSION.source().getId(),1,
            ModFluids.MANBO_EMULSION.source().getId(),2,
            ModFluids.MUSIC_EMULSION.source().getId(),3
            );
    private final BlockPos[] corners = {
            this.worldPosition.offset(0, 2, -3),
            this.worldPosition.offset(-3, 2, 0),
            this.worldPosition.offset(0, 2, 3),
            this.worldPosition.offset(3, 2, 0)
    };

    public MusicAltarBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModBlocks.MUSIC_ALTAR.block_entity().get(), pos, state);
    }

    public void initializeStructure() {
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MusicAltarBlockEntity be) {
            be.validateStructure();
    }


    private void validateStructure() {
        if (!MultiblockStructureChecker.isStructureComplete(level, worldPosition)) {
            revertToJukebox();
        }
    }

    public int[] monitorTanks() {
        int tanklevel[]=new int[4];
        for (BlockPos corner : corners) {
            BlockEntity be = null;
            if (level != null) {
                be = level.getBlockEntity(corner);
            }
            if (be instanceof BaseTankBlockEntity tank) {
                FluidStack fluid=tank.getFluidTank().getFluid();
                ResourceLocation fluidID=ForgeRegistries.FLUIDS.getKey(fluid.getFluid());
                if (typecode.containsKey(fluidID)){
                    tanklevel[typecode.get(fluidID)]=fluid.getAmount();
                }
            }
        }
        return tanklevel;
    }


    // 添加恢复为唱片机的方法
    public void revertToJukebox() {
        if (level == null || level.isClientSide) return;

        // 将方块替换为唱片机
        level.setBlockAndUpdate(worldPosition, Blocks.JUKEBOX.defaultBlockState());
    }

    // 实现WorldlyContainer接口
    @Override
    public int[] getSlotsForFace(Direction side) {
        // 定义可访问的槽位
        return IntStream.range(0, 10).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        // 只允许在合成槽放置物品
        return index < 9;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        // 只允许取出结果物品
        return index == 9;
    }

    // 实现 Container 方法
    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    //实现CraftingContainer
    @Override
    public int getWidth() {
        return 3; // 3x3 合成网格
    }

    @Override
    public int getHeight() {
        return 3; // 3x3 合成网格
    }

    @Override
    public List<ItemStack> getItems() {
        return items.subList(0, 9); // 只返回合成网格的物品
    }

    @Override
    public void fillStackedContents(StackedContents contents) {
        // 只处理合成网格中的物品（前9个槽位）
        for (int i = 0; i < 9; i++) {
            ItemStack stack = getItem(i);
            contents.accountStack(stack);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.hachimimagic.music_multiblock");
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MusicAltarMenu(containerId, playerInventory, this);
    }
}