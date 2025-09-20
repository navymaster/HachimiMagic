package navy_master.hachimi.magic.blocks;

import navy_master.hachimi.magic.blockentity.MusicTankEntity;
import navy_master.hachimi.magic.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MusicTank extends TankBase {
    public MusicTank(Properties properties) {
        super(properties);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MusicTankEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {

        BlockEntityType<MusicTankEntity> typeB= (BlockEntityType<MusicTankEntity>) ModBlocks.MUSIC_TANK.block_entity().get();
        return createTickerHelper(type, typeB,
                MusicTankEntity::tick);
    }

}
