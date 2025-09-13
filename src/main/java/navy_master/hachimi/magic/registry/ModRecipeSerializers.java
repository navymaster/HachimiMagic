package navy_master.hachimi.magic.registry;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.recipe.MusicMultiblockRecipe;
import navy_master.hachimi.magic.recipe.MusicMultiblockRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HachimiMagic.MODID);

    public static final RegistryObject<RecipeSerializer<MusicMultiblockRecipe>> MUSIC_MULTIBLOCK_RECIPE =
            SERIALIZERS.register("music_multiblock_recipe", () -> MusicMultiblockRecipeSerializer.INSTANCE);
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}