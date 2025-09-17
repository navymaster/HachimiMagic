package navy_master.hachimi.magic.blocks;

import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MixingTankBlock extends TankBase {
    public MixingTankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MixingTankBlockEntity) {
                ((MixingTankBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixingTankBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        BlockEntityType<MixingTankBlockEntity> typeB= (BlockEntityType<MixingTankBlockEntity>) ModBlockEntities.MIXING_TANK.block_entity().get();
        return createTickerHelper(type, typeB,
                MixingTankBlockEntity::tick);
    }
}