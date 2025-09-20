package navy_master.hachimi.magic.music_altar;

import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class MultiblockActiveEventHandler {
    @SubscribeEvent
    public void onJukeboxInteract(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (level.getBlockState(pos).is(Blocks.JUKEBOX) &&
                stack.getItem() instanceof RecordItem) {

            if (!level.isClientSide()) {
                level.getServer().execute(() -> {

                    if (MultiblockStructureChecker.isStructureComplete(level, pos)) {
                        activateStructure(level, pos, player);
                    }
                });
            }
        }

    }

    private void activateStructure(Level level, BlockPos pos, Player player) {
        MusicAltarBlockEntity controller = getOrCreateController(level, pos);
        controller.initializeStructure();
        player.displayClientMessage(Component.translatable("message.multiblock.activated"), true);
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private MusicAltarBlockEntity getOrCreateController(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MusicAltarBlockEntity) {
            return (MusicAltarBlockEntity) be;
        }

        level.setBlock(pos, ((Block)ModBlocks.MUSIC_ALTAR.block().get()).defaultBlockState(), Block.UPDATE_ALL);
        return (MusicAltarBlockEntity) level.getBlockEntity(pos);
    }

}