package pieman.caffeineaddon.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.te.TEPlacedItemFlat;
import net.dries007.tfc.objects.te.TEQuern;
import net.dries007.tfc.objects.te.TETickCounter;
import net.dries007.tfc.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import pieman.caffeineaddon.CaffeineAddon;
import pieman.caffeineaddon.client.GUIHandler;
import pieman.caffeineaddon.init.ModBlocks;
import pieman.caffeineaddon.init.ModItems;
import pieman.caffeineaddon.util.IHasModel;

public class BlockDryingMat extends Block implements IHasModel {

	public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

	public BlockDryingMat(String name) {
		super(Material.GRASS, MapColor.YELLOW);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabsTFC.CT_MISC);

		setHardness(0.5f);
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));

	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand.equals(EnumHand.MAIN_HAND)){
			TEDryingMat te = Helpers.getTE(worldIn, pos, TEDryingMat.class);
			if (te != null && !worldIn.isRemote) {
				if(playerIn.isSneaking()) {
					ItemStack stack = te.getStack();
					if (stack.isEmpty()) {
						ItemStack is = playerIn.getHeldItem(hand);
						if (te.isItemValid(te.SLOT, is)) {
							te.setStack(is.copy());
							is.setCount(0);
						}
					} else {
						ItemHandlerHelper.giveItemToPlayer(playerIn, stack);
						te.setStack(ItemStack.EMPTY);
					}
					te.setAndUpdateSlots(TEDryingMat.SLOT);
				}else {
					playerIn.openGui(CaffeineAddon.instance, GUIHandler.DRYINGMATGUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			}
			worldIn.notifyBlockUpdate(pos, state, state, 3);
			return true;
        }
        return false;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		TEDryingMat te = Helpers.getTE(worldIn, pos, TEDryingMat.class);
		if (te != null && !worldIn.isRemote) {
			if(worldIn.isRainingAt(pos.up())) {
				te.resetCounter();
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

    @Override
    public boolean isTopSolid(IBlockState state){
        return false;
    }

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!canPlaceBlockAt(worldIn, pos)) {
			this.breakBlock(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
		}
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isTopSolid();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		// Set the initial counter value
		TEDryingMat tile = Helpers.getTE(worldIn, pos, TEDryingMat.class);
		if (tile != null) {
			tile.resetCounter();
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TEDryingMat te = Helpers.getTE(worldIn, pos, TEDryingMat.class);
		if (te != null) {
			te.onBreakBlock(worldIn, pos, state);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TEDryingMat();
	}

	@Override
	public void registerModels() {
		CaffeineAddon.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

}
