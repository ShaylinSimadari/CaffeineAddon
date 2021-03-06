package pieman.caffeineaddon.recipes;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.objects.fluids.FluidsTFC.FRESH_WATER;
import static net.dries007.tfc.objects.fluids.FluidsTFC.HOT_WATER;

import net.dries007.tfc.api.recipes.barrel.BarrelRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.heat.HeatRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingType;
import net.dries007.tfc.api.recipes.quern.QuernRecipe;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.dries007.tfc.objects.items.food.ItemFoodTFC;
import net.dries007.tfc.util.agriculture.Food;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import pieman.caffeineaddon.Reference;
import pieman.caffeineaddon.init.ModFluids;
import pieman.caffeineaddon.init.ModItems;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class Recipes {

    @SubscribeEvent
    public static void onRegisterHeatRecipeEvent(RegistryEvent.Register<HeatRecipe> event){
        IForgeRegistry<HeatRecipe> r = event.getRegistry();
        r.registerAll(
                new HeatRecipeSimple(IIngredient.of(ModItems.GreenCoffeeBeans), new ItemStack(ModItems.CoffeeBeans), 200, 480).setRegistryName("coffee_beans"),
                HeatRecipe.destroy(IIngredient.of(ModItems.CoffeeBeans), 480).setRegistryName("burned_coffee_beans"),
                new HeatRecipeSimple(IIngredient.of(ModItems.MugUnfired), new ItemStack(ModItems.Mug), 1599f, Metal.Tier.TIER_I).setRegistryName("unfired_mug"),
                new HeatRecipeSimple(IIngredient.of(ModItems.Mug), new ItemStack(ModItems.Mug), 1599f, Metal.Tier.TIER_I).setRegistryName("fired_jug")
                
        );
    }

	@SubscribeEvent
	public static void onRegisterQuernRecipeEvent(RegistryEvent.Register<QuernRecipe> event) {
		IForgeRegistry<QuernRecipe> r = event.getRegistry();
		r.registerAll(
				new QuernRecipe(IIngredient.of(ModItems.DriedCoffeeCherries), new ItemStack(ModItems.GreenCoffeeBeans, 1)).setRegistryName("green_coffee_beans"),
				new QuernRecipe(IIngredient.of(ModItems.CoffeeBeans), new ItemStack(ModItems.GroundCoffee, 1)).setRegistryName("ground_coffee")
		);
	}
	
    @SubscribeEvent
    public static void onRegisterBarrelRecipeEvent(RegistryEvent.Register<BarrelRecipe> event){
        event.getRegistry().registerAll(
        		new BarrelRecipe(IIngredient.of(HOT_WATER.get(), 500), IIngredient.of(ModItems.GroundCoffee), new FluidStack(ModFluids.COFFEE.get(), 500), ItemStack.EMPTY, 0).setRegistryName("coffee_hot"),
        		new BarrelRecipe(IIngredient.of(FRESH_WATER.get(), 500), IIngredient.of(ModItems.GroundCoffee), new FluidStack(ModFluids.COFFEE.get(), 500), ItemStack.EMPTY, 16 * ICalendar.TICKS_IN_HOUR).setRegistryName("coffee_cold")
        );
    }
    
    @SubscribeEvent
    public static void onRegisterDryingMatRecipeEvent(RegistryEvent.Register<DryingMatRecipe> event){
        IForgeRegistry<DryingMatRecipe> r = event.getRegistry();
        r.registerAll(
                new DryingMatRecipe(IIngredient.of(new ItemStack(ModItems.CoffeeCherries)), new ItemStack(ModItems.DriedCoffeeCherries), 72000).setRegistryName("dried_coffee_cherries")
        );
    }
    
    @SubscribeEvent
    public static void onRegisterKnappingRecipeEvent(RegistryEvent.Register<KnappingRecipe> event){
        event.getRegistry().registerAll(
            new KnappingRecipeSimple(KnappingType.CLAY, true, new ItemStack(ModItems.MugUnfired), 
            		"     ", 
            		"XXXX ", 
            		"XXX X", 
            		"XXXX ", 
            		"XXX  ").setRegistryName("clay_mug")
        );
    }
}
