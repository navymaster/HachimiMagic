package navy_master.hachimi.magic.recipe;

import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.registry.ModRecipeSerializers;
import navy_master.hachimi.magic.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MusicMultiblockRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    final ItemStack result;
    final int[] minFluidLevels;
    final int[] consumeFluidAmounts;
    final boolean[] requiredActiveTanks;
    private final int experience;
    private final int cookingTime;

    public MusicMultiblockRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients,
                                 ItemStack result, int[] minFluidLevels,
                                 int[] consumeFluidAmounts, boolean[] requiredActiveTanks,
                                 int experience, int cookingTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.minFluidLevels = minFluidLevels;
        this.consumeFluidAmounts = consumeFluidAmounts;
        this.requiredActiveTanks = requiredActiveTanks;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        // 检查物品匹配
        for (int i = 0; i < 9; i++) {
            if (!ingredients.get(i).test(container.getItem(i))) {
                return false;
            }
        }

        // 检查流体条件
        if (container instanceof MusicAltarBlockEntity be) {
            for (int i = 0; i < 4; i++) {
                if (requiredActiveTanks[i] ) {
                    return false;
                }
                /*if (be.getTankLevel(i) < minFluidLevels[i]) {
                    return false;
                }*/
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MUSIC_MULTIBLOCK_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MUSIC_MULTIBLOCK_RECIPE.get();
    }

    // Getters for fluid data
    public int getMinFluidLevel(int tank) {
        return minFluidLevels[tank];
    }

    public int getConsumeFluidAmount(int tank) {
        return consumeFluidAmounts[tank];
    }

    public boolean isTankRequired(int tank) {
        return requiredActiveTanks[tank];
    }

    public int getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}