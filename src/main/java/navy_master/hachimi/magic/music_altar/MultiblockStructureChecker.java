package navy_master.hachimi.magic.music_altar;

import navy_master.hachimi.magic.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockStructureChecker {

    public static boolean isStructureComplete(Level level, BlockPos jukeboxPos) {
        return checkBaseLayer(level, jukeboxPos) && checkHoneyPillars(level, jukeboxPos);
    }

    private static boolean checkBaseLayer(Level level, BlockPos center) {
        BlockPos startPos = center.below().offset(-2, 0, -2);

        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                BlockPos checkPos = startPos.offset(x, 0, z);
                BlockState state = level.getBlockState(checkPos);

                if (!state.is(Blocks.POLISHED_DIORITE)) {
                    return false;
                }
            }
        }

        // 额外检查中心位置应为唱片机
        BlockPos checkPos = startPos.offset(2, 1, 2);
        BlockState state = level.getBlockState(checkPos);
        if (!state.is(Blocks.JUKEBOX) && !state.is(ModBlocks.MUSIC_ALTAR_CORE.get())) {
            return false;
        }

        return true;
    }

    private static boolean checkHoneyPillars(Level level, BlockPos center) {
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        BlockPos basePos = center.below();

        for (Direction dir : directions) {
            BlockPos pillarBase = basePos.relative(dir, 2).above();

            // 检查两格高的蜂蜜块柱子
            if (!level.getBlockState(pillarBase).is(Blocks.HONEY_BLOCK)) {
                return false;
            }

            if (!level.getBlockState(pillarBase.above()).is(Blocks.HONEY_BLOCK)) {
                return false;
            }
        }
        return true;
    }
}