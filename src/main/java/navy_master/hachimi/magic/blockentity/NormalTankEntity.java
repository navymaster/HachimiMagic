package navy_master.hachimi.magic.blockentity;

import navy_master.hachimi.magic.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NormalTankEntity extends BaseTankBlockEntity{
    public NormalTankEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModBlocks.NORMAL_TANK.block_entity().get(), pos, state);
    }

}
