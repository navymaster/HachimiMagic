package navy_master.hachimi.magic.music_altar;

import navy_master.hachimi.magic.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MultiblockStructureChecker {
    public static boolean isStructureComplete(Level level, BlockPos center) {
        if(!checkBlockType(level, new BlockPos[]{
                center.offset(-2, 0, -2),
                center.offset(-2, 0, 2),
                center.offset(2, 0, -2),
                center.offset(2, 0, 2)
        },Blocks.HONEY_BLOCK)){
            return false;
        }
        if(!checkBlockType(level, new BlockPos[]{
                center.offset(0, -1, 0),
                center.offset(-1, -1, -1),
                center.offset(1, -1, 1),
                center.offset(1, -1, -1),
                center.offset(-1, -1, 1),
                center.offset(-2, -1, 2),
                center.offset(2, -1, -2),
                center.offset(-2, -1, -2),
                center.offset(2, -1, 2)
        },Blocks.CHISELED_STONE_BRICKS)){
            return false;
        }
        if(!checkBlockType(level, new BlockPos[]{
                center.offset(2, -1, -1),
                center.offset(2, -1, 1),
                center.offset(-2, -1, -1),
                center.offset(-2, -1, 1),
                center.offset(1, -1, 2),
                center.offset(1, -1, -2),
                center.offset(-1, -1, -2),
                center.offset(-1, -1, 2)
        },Blocks.STONE_BRICK_WALL)){
            return false;
        }
        if(!checkBlockType(level, new BlockPos[]{
                center.offset(0, -1, 3),
                center.offset(0, 0, 3),
                center.offset(0, 1, 3),
                center.offset(0, -1, -3),
                center.offset(0, 0, -3),
                center.offset(0, 1, -3),
                center.offset(3, -1, 0),
                center.offset(3, 0, 0),
                center.offset(3, 1, 0),
                center.offset(-3, -1, 0),
                center.offset(-3, 0, 0),
                center.offset(-3, 1, 0)
        },Blocks.STONE_BRICKS)){
            return false;
        }
        if(!checkBlockTag(level, new BlockPos[]{
                center.offset(2, -1, 0),
                center.offset(1, -1, 0),
                center.offset(-1, -1, 0),
                center.offset(-2, -1, 0),
                center.offset(0, -1, 2),
                center.offset(0, -1, -1),
                center.offset(0, -1, 1),
                center.offset(0, -1, -2)
        }, BlockTags.PLANKS)){
            return false;
        }
        return (checkBlockType(level, new BlockPos[]{
                center.offset(0, 0, 0),
        }, Blocks.JUKEBOX)) ||
                (checkBlockType(level, new BlockPos[]{
                center.offset(0, 0, 0),
        }, ModBlocks.MUSIC_ALTAR_CORE.get()));
    }

    private static boolean checkBlockType(Level level, BlockPos[] range, Block type){
        for(BlockPos place:range){
            if (!level.getBlockState(place).is(type)){
                return false;
            }
        }
        return true;
    }
    private static boolean checkBlockTag(Level level, BlockPos[] range, TagKey<Block> type){
        for(BlockPos place:range){
            if (!level.getBlockState(place).is(type)){
                return false;
            }
        }
        return true;
    }
}