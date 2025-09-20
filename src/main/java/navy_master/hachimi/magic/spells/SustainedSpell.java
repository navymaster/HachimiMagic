package navy_master.hachimi.magic.spells;

import java.util.UUID;

public interface SustainedSpell {
    UUID uuid = UUID.randomUUID();
    abstract boolean tick() ;
}
