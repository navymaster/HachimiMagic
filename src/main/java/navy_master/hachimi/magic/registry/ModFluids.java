package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.utils.FluidRegistryHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, HachimiMagic.MODID);

    public static final FluidRegistryHelper.FluidRegistration HA_EMULSION =
            FluidRegistryHelper.registerFluid("ha_emulsion", 0xA100FFFF);

    public static final FluidRegistryHelper.FluidRegistration JIMI_EMULSION =
            FluidRegistryHelper.registerFluid("jimi_emulsion", 0xA1FF8800);

    public static final FluidRegistryHelper.FluidRegistration MANBO_EMULSION =
            FluidRegistryHelper.registerFluid("manbo_emulsion", 0xA100FF00);

    public static final FluidRegistryHelper.FluidRegistration MUSIC_EMULSION =
            FluidRegistryHelper.registerFluid("music_emulsion", 0xA100FFFF);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}