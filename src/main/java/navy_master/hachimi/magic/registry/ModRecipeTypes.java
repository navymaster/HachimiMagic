package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.recipe.MusicMultiblockRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, HachimiMagic.MODID);

    public static final RegistryObject<RecipeType<MusicMultiblockRecipe>> MUSIC_MULTIBLOCK_RECIPE =
            RECIPE_TYPES.register("music_multiblock_recipe", () -> new RecipeType<MusicMultiblockRecipe>() {
                @Override
                public String toString() {
                    return "hachimimagic:music_multiblock_recipe";
                }
            });
    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}