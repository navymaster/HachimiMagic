package navy_master.hachimi.magic.menu;


import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.registry.ModBlocks;
import navy_master.hachimi.magic.registry.ModMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MixingTankMenu extends BaseTankMenu {

    private final Level level;

    public MixingTankMenu(int containerId, Inventory inv, MixingTankBlockEntity entity) {
        super(ModMenuType.MIXING_TANK.get(), containerId,entity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        IItemHandler handler = entity.getItemHandler();

        // 乳化材料槽 (左边)
        this.addSlot(new SlotItemHandler(handler, 0, 56, 35));

        // 表面活性剂槽 (右边)
        this.addSlot(new SlotItemHandler(handler, 1, 104, 35));
    }

    public MixingTankMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (MixingTankBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.MIXING_TANK.get());
    }
}