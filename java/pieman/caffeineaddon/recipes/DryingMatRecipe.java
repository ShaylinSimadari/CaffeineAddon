package pieman.caffeineaddon.recipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.recipes.quern.QuernRecipe;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.compat.jei.IJEISimpleRecipe;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DryingMatRecipe extends IForgeRegistryEntry.Impl<DryingMatRecipe> implements IJEISimpleRecipe {
	@Nullable
	public static DryingMatRecipe get(ItemStack item) {
		return Registries.DRYINGMAT.getValuesCollection().stream().filter(x -> x.isValidInput(item)).findFirst()
				.orElse(null);
	}

	private IIngredient<ItemStack> inputItem;
	private ItemStack outputItem;
    private int duration;

	public DryingMatRecipe(IIngredient<ItemStack> input, ItemStack output, int duration) {
		this.inputItem = input;
		this.outputItem = output;
        this.duration = duration;

		if (inputItem == null || outputItem == null) {
			throw new IllegalArgumentException("Input and output are not allowed to be empty");
		}
	}

    public int getDuration(){
        return duration;
    }
    /**
     * Only for GUI purposes - not intended as a crafting mechanic
     *
     * @return the output item stack
     */
    @Nonnull
    public ItemStack getOutputStack()
    {
        return outputItem;
    }

    @Nonnull
    public IIngredient<ItemStack> getItemIngredient()
    {
        return inputItem;
    }

	@Nonnull
	public ItemStack getOutputItem(ItemStack stack) {
		return CapabilityFood.updateFoodFromPrevious(stack, outputItem.copy());
	}

	@Override
	public NonNullList<IIngredient<ItemStack>> getIngredients() {
		return NonNullList.withSize(1, inputItem);
	}

	@Override
	public NonNullList<ItemStack> getOutputs() {
		return NonNullList.withSize(1, outputItem);
	}

	private boolean isValidInput(ItemStack inputItem) {
		return this.inputItem.testIgnoreCount(inputItem);
	}
}