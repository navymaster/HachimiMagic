package navy_master.hachimi.magic.Item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

public abstract class MagicRecord extends RecordItem {
    int magicCost;

    public MagicRecord(Supplier<SoundEvent> soundSupplier, Properties builder, int lengthInTicks,int magicCost) {
        super(15, soundSupplier, builder, lengthInTicks);
        this.magicCost=magicCost;
    }

    public abstract void castMagic(ServerLevel level, Player player);
}
