package pieman.caffeineaddon.blocks;

import javax.annotation.Nonnull;

import net.dries007.tfc.Constants;
import net.dries007.tfc.api.recipes.quern.QuernRecipe;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.objects.te.TETickCounter;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.calendar.ICalendarFormatted;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pieman.caffeineaddon.init.ModItems;
import pieman.caffeineaddon.recipes.DryingMatRecipe;
import pieman.caffeineaddon.recipes.Registries;

public class TEDryingMat extends TEInventory implements ITickable{
	
    public static final int SLOT = 0;

	public TEDryingMat() {
		super(1);
	}

	public ItemStack insertOrSwapItem(int slot, ItemStack playerStack) {
		ItemStack quernStack = inventory.getStackInSlot(slot);

		if (quernStack.isEmpty() || (playerStack.isStackable() && quernStack.isStackable()
				&& quernStack.getItem() == playerStack.getItem()
				&& (!playerStack.getHasSubtypes() || playerStack.getMetadata() == quernStack.getMetadata())
				&& ItemStack.areItemStackTagsEqual(playerStack, quernStack))) {
			return inventory.insertItem(slot, playerStack, false);
		}
		inventory.setStackInSlot(slot, playerStack);
		return quernStack;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		switch (slot) {
		case SLOT:
			return DryingMatRecipe.get(stack) != null;
		default:
			return false;
		}
	}
	
    @Override
    public void setAndUpdateSlots(int slot)
    {
        this.resetCounter();
        super.setAndUpdateSlots(slot);
    }
	
	//tickcounter
    private long lastUpdateTick;
    private long sealedCalendarTick;

	public long getTicksSinceUpdate() {
		return CalendarTFC.PLAYER_TIME.getTicks() - lastUpdateTick;
	}

	public void resetCounter() {
		lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
        sealedCalendarTick = CalendarTFC.CALENDAR_TIME.getTicks();
		markDirty();
	}

	public void reduceCounter(long amount) {
		lastUpdateTick += amount;
		markDirty();
	}
    //-----
	
    @Nonnull
    public String getSealedDate(){
        return getStack().isEmpty()?"":ICalendarFormatted.getTimeAndDate(sealedCalendarTick, CalendarTFC.CALENDAR_TIME.getDaysInMonth());
    }
    
    @Nonnull
    public String getOutputName(){
    	return DryingMatRecipe.get(getStack())==null?"":DryingMatRecipe.get(getStack()).getOutputStack().getDisplayName();
    }
    @Nonnull
    public float getProgress(){
    	return DryingMatRecipe.get(getStack())==null?0:((float)getTicksSinceUpdate())/((float)DryingMatRecipe.get(getStack()).getDuration());
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        lastUpdateTick = nbt.getLong("tick");
        sealedCalendarTick = nbt.getLong("date");
		super.readFromNBT(nbt);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("tick", lastUpdateTick);
        nbt.setLong("date", sealedCalendarTick);
		return super.writeToNBT(nbt);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 1024.0D;
	}

	@Override
	@Nonnull
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1D, 1D, 1D));
	}

	public ItemStack getStack() {
		return inventory.getStackInSlot(SLOT);
	}

	public void setStack(ItemStack stack) {
		inventory.setStackInSlot(SLOT, stack);
		markDirty();
	}

	public void checkDry() {
		if (!inventory.getStackInSlot(SLOT).isEmpty()) {
			ItemStack stack = inventory.getStackInSlot(SLOT);
			DryingMatRecipe recipe = DryingMatRecipe.get(stack);
            if (recipe != null && !world.isRemote && getTicksSinceUpdate() >= recipe.getDuration()) {
            	setStack(new ItemStack(recipe.getOutputItem(stack).getItem(), stack.getCount()));
            	resetCounter();
            	world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
		}
	}

	@Override
	public void update() {
		checkDry();
	}

}
