package pieman.caffeineaddon.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import pieman.caffeineaddon.blocks.BlockCoffeeTreeBranch;
import pieman.caffeineaddon.blocks.BlockCoffeeTreeLeaves;
import pieman.caffeineaddon.blocks.BlockCoffeeTreeSapling;
import pieman.caffeineaddon.blocks.BlockCoffeeTreeTrunk;
import pieman.caffeineaddon.blocks.BlockDryingMat;
import pieman.caffeineaddon.blocks.BlockLeafyBush;
import pieman.caffeineaddon.util.CoffeeTree;
import pieman.caffeineaddon.util.LeafyBush;

public class ModBlocks {

	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block DRYING_MAT_BLOCK = new BlockDryingMat("drying_mat");
	public static final Block SAPLING = new BlockCoffeeTreeSapling(CoffeeTree.COFFEE);
	public static final Block TRUNK = new BlockCoffeeTreeTrunk(CoffeeTree.COFFEE);
	public static final Block BRANCH = new BlockCoffeeTreeBranch(CoffeeTree.COFFEE);
	public static final Block LEAVES = new BlockCoffeeTreeLeaves(CoffeeTree.COFFEE);
	//public static final Block TEA = new BlockLeafyBush(LeafyBush.TEA);
	
}
