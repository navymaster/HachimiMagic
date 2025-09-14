package navy_master.hachimi.magic.blocks;

import navy_master.hachimi.magic.blockentity.NormalTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NormalTank extends TankBase {
    public NormalTank(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NormalTankEntity(pos, state);
    }
}
