package navy_master.hachimi.magic.blocks;

import navy_master.hachimi.magic.blockentity.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;

public abstract class TankBase extends BaseEntityBlock {
    protected TankBase(Properties p_49224_) {
        super(p_49224_);
    }
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BaseTankBlockEntity basetank) {
                // 尝试处理流体交互
                InteractionResult fluidResult = handleFluidInteraction(player, hand, basetank);
                if (fluidResult.consumesAction()) {
                    return fluidResult;
                }
            }
            if (entity instanceof MenuProvider menuProvider){
                // 如果流体交互未成功，打开GUI
                NetworkHooks.openScreen(((ServerPlayer) player), menuProvider, pos);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private InteractionResult handleFluidInteraction(Player player, InteractionHand hand, BaseTankBlockEntity blockEntity) {
        if (FluidUtil.interactWithFluidHandler(player, hand, blockEntity.getFluidTank())) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
