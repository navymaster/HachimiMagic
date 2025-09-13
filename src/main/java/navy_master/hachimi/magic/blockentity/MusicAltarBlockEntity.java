package navy_master.hachimi.magic.blockentity;

import navy_master.hachimi.magic.menu.MusicAltarMenu;
import navy_master.hachimi.magic.music_altar.MultiblockStructureChecker;
import navy_master.hachimi.magic.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class MusicAltarBlockEntity extends BlockEntity implements WorldlyContainer, CraftingContainer,MenuProvider{
    private Set<BlockPos> tankPositions = new HashSet<>();
    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);
    private final int[] tankLevels = new int[4];
    private final boolean[] tankActive = new boolean[4];
    private ItemStack record = ItemStack.EMPTY;

    public MusicAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MUSIC_ALTAR.get(), pos, state);
    }

    public void initializeStructure() {
        setChanged();
        findTankPositions();
    }

    private void findTankPositions() {
        BlockPos[] corners = {
                this.worldPosition.offset(-2, 0, -2),
                this.worldPosition.offset(-2, 0, 2),
                this.worldPosition.offset(2, 0, -2),
                this.worldPosition.offset(2, 0, 2)
        };

        for (BlockPos corner : corners) {
            if (level.getBlockEntity(corner) instanceof MixingTankBlockEntity) {
                tankPositions.add(corner);
            }
        }
    }


    public static void serverTick(Level level, BlockPos pos, BlockState state, MusicAltarBlockEntity be) {
            be.validateStructure();
            be.monitorTanks();
    }

        //TODO  检查配方
        /*
        Optional<MusicMultiblockRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.MUSIC_MULTIBLOCK, be, level);

        if (recipe.isPresent()) {
            // 检查是否满足合成条件
            if (recipe.get().matches(be, level)) {
                // 执行合成
                be.craftItem(recipe.get());
            }
        }
*/


    /*
    private void craftItem(MusicMultiblockRecipe recipe) {
        // 消耗物品
        for (int i = 0; i < 9; i++) {
            items.get(i).shrink(1);
        }

        // 消耗流体
        for (int i = 0; i < 4; i++) {
            if (tankActive[i]) {
                BlockPos tankPos = getTankPosition(worldPosition, i);
                BlockEntity tank = level.getBlockEntity(tankPos);
                if (tank instanceof FluidTankBlockEntity fluidTank) {
                    fluidTank.drain(recipe.getConsumeFluidAmount(i), FluidAction.EXECUTE);
                }
            }
        }

        // 设置结果物品
        items.set(9, recipe.getResultItem().copy());

    }
    */



    public int getTankLevel(int tankIndex) {
        return tankLevels[tankIndex];
    }

    public boolean isTankActive(int tankIndex) {
        return tankActive[tankIndex];
    }
    public void setTankActive(int tankIndex,boolean active) {
        tankActive[tankIndex] = active;
        setChanged();
    }



    public void toggleTankActive(int tankIndex) {
        tankActive[tankIndex] = !tankActive[tankIndex];
        setChanged();
    }

    private static BlockPos getTankPosition(BlockPos center, int direction) {
        // 根据方向获取储罐位置（北、东、南、西）
        return switch (direction) {
            case 0 -> center.offset(-2, -1, -2); // 西北
            case 1 -> center.offset(2, -1, -2);  // 东北
            case 2 -> center.offset(2, -1, 2);   // 东南
            case 3 -> center.offset(-2, -1, 2);  // 西南
            default -> center;
        };
    }

    private void validateStructure() {
        if (!MultiblockStructureChecker.isStructureComplete(level, worldPosition)) {
            revertToJukebox();
        }
    }

    private void monitorTanks() {
        for (BlockPos tankPos : tankPositions) {
            BlockEntity be = level.getBlockEntity(tankPos);
            if (be instanceof MixingTankBlockEntity tank) {
                FluidStack fluid=tank.getFluidTank().getFluid();
                // TODO tankLevels[tankPositions]=fluid.getAmount();
            }
        }
    }

    // 添加恢复为唱片机的方法
    public void revertToJukebox() {
        if (level == null || level.isClientSide) return;

        // 将方块替换为唱片机
        level.setBlockAndUpdate(worldPosition, Blocks.JUKEBOX.defaultBlockState());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!record.isEmpty()) {
            tag.put("Record", record.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Record")) {
            record = ItemStack.of(tag.getCompound("Record"));
        }
    }


    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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