package navy_master.hachimi.magic.spells;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class SpellManager {
    // TODO 还没确定是否要区分互斥和不互斥的持续性法术，这里先放个HASHMAP凑合一下
    private static final Map<UUID, SustainedSpell> activeSpells = new HashMap<>();

    public static void addSpell(UUID spelluuid, SustainedSpell spell) {
        activeSpells.put(spelluuid, spell);
    }

    public static void removeSpell(UUID spelluuid) {
        // 主动取消
        activeSpells.remove(spelluuid);
    }

    public static void tick() {
        Iterator<Map.Entry<UUID, SustainedSpell>> iterator = activeSpells.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, SustainedSpell> entry = iterator.next();
            SustainedSpell spell = entry.getValue();

            if (spell.tick()) {
                iterator.remove(); // 法术结束，移除
            }
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            SpellManager.tick();
        }
    }
}
