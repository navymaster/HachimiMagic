package navy_master.hachimi.magic.blockentity;

import navy_master.hachimi.magic.menu.MusicTankMenu;
import navy_master.hachimi.magic.registry.ModBlockEntities;
import navy_master.hachimi.magic.registry.ModFluids;
import navy_master.hachimi.magic.utils.MusicDetectionSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MusicTankEntity extends BaseTankBlockEntity implements MenuProvider {
    private static final Fluid MUSIC_FLUID = ModFluids.MUSIC_EMULSION.source().get();

    public MusicTankEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NORMAL_TANK.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.hachimimagic.music_tank");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MusicTankMenu(containerId, playerInventory, this);
    }
    @Override
    protected FluidTank createFluidTank() {
        return new FluidTank(10000) {
            @Override
            public int fill(FluidStack resource, FluidAction action) {
                // 只允许注入music流体
                if (!resource.getFluid().isSame(MUSIC_FLUID)) {
                    return 0;
                }
                return super.fill(resource, action);
            }
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        };
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState,MusicTankEntity be) {
        if (level == null || level.isClientSide) return;
        // 每秒检测一次
        if (level.getGameTime() % 20 == 0) {
            if (MusicDetectionSystem.isMusicPlayingNear( be.worldPosition, 10)){
                // 每tick生成1mB music流体
                FluidStack toFill = new FluidStack(MUSIC_FLUID, 1);
                be.fluidTank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                be.setChanged();
                level.sendBlockUpdated(be.worldPosition, be.getBlockState(), be.getBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
