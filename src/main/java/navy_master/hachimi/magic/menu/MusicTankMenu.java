package navy_master.hachimi.magic.menu;

import navy_master.hachimi.magic.blockentity.MusicTankEntity;
import navy_master.hachimi.magic.registry.ModBlocks;
import navy_master.hachimi.magic.registry.ModMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;

public class MusicTankMenu extends BaseTankMenu{
    public MusicTankMenu(int containerId, Inventory inv, MusicTankEntity entity) {
        super(ModMenuType.MUSIC_TANK.get(), containerId,inv,entity);
    }
    public MusicTankMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (MusicTankEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, (Block) ModBlocks.MUSIC_TANK.block().get());
    }
}
