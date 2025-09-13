package navy_master.hachimi.magic.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class MusicMultiblockRecipeSerializer implements RecipeSerializer<MusicMultiblockRecipe> {
    public static final MusicMultiblockRecipeSerializer INSTANCE = new MusicMultiblockRecipeSerializer();

    @Override
    public MusicMultiblockRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        // 解析物品配方
        JsonArray pattern = json.getAsJsonArray("pattern");
        JsonObject key = json.getAsJsonObject("key");

        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        int index = 0;

        for (JsonElement line : pattern) {
            String patternLine = line.getAsString();
            for (char c : patternLine.toCharArray()) {
                Ingredient ingredient = Ingredient.fromJson(key.get(String.valueOf(c)));
                ingredients.set(index++, ingredient);
            }
        }

        // 解析结果物品
        ItemStack result = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("result"));

        // 解析流体要求
        JsonArray tanks = json.getAsJsonArray("tanks");
        int[] minFluidLevels = new int[4];
        int[] consumeFluidAmounts = new int[4];
        boolean[] requiredActiveTanks = new boolean[4];

        for (int i = 0; i < tanks.size(); i++) {
            JsonObject tank = tanks.get(i).getAsJsonObject();
            minFluidLevels[i] = tank.get("min").getAsInt();
            consumeFluidAmounts[i] = tank.get("consume").getAsInt();
            requiredActiveTanks[i] = tank.get("active").getAsBoolean();
        }

        // 解析经验值和烹饪时间
        int experience = GsonHelper.getAsInt(json, "experience", 0);
        int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);

        return new MusicMultiblockRecipe(recipeId, ingredients, result,
                minFluidLevels, consumeFluidAmounts, requiredActiveTanks,
                experience, cookingTime);
    }

    @Override
    public MusicMultiblockRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();

        int[] minFluidLevels = new int[4];
        int[] consumeFluidAmounts = new int[4];
        boolean[] requiredActiveTanks = new boolean[4];

        for (int i = 0; i < 4; i++) {
            minFluidLevels[i] = buffer.readInt();
            consumeFluidAmounts[i] = buffer.readInt();
            requiredActiveTanks[i] = buffer.readBoolean();
        }

        int experience = buffer.readInt();
        int cookingTime = buffer.readInt();

        return new MusicMultiblockRecipe(recipeId, ingredients, result,
                minFluidLevels, consumeFluidAmounts, requiredActiveTanks,
                experience, cookingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, MusicMultiblockRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.result);

        for (int i = 0; i < 4; i++) {
            buffer.writeInt(recipe.minFluidLevels[i]);
            buffer.writeInt(recipe.consumeFluidAmounts[i]);
            buffer.writeBoolean(recipe.requiredActiveTanks[i]);
        }

        buffer.writeInt(recipe.getExperience());
        buffer.writeInt(recipe.getCookingTime());
    }
}