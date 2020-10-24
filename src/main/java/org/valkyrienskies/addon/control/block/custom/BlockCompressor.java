package org.valkyrienskies.addon.control.block.custom;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.joml.Vector3dc;
import org.valkyrienskies.addon.control.util.BaseBlock;
import org.valkyrienskies.mod.common.block.IBlockForceProvider;
import org.valkyrienskies.mod.common.ships.ship_world.PhysicsObject;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockCompressor extends BaseBlock implements IBlockForceProvider, ITileEntityProvider {

	// The maximum thrust in newtons that each compressor block can provide.
	public final double COMPRESSOR_MAX_THRUST;
	
    //public static final double COMPRESSOR_MAX_THRUST = 1300000;
	
	public BlockCompressor(String key, double max_thrust) {
		super(key, Material.WOOD, 0.0F, true);
		this.setHardness(6.0F);
		this.COMPRESSOR_MAX_THRUST = max_thrust;
	}
	
	@Override
    public void addInformation(ItemStack stack, @Nullable World player,
        List<String> itemInformation, ITooltipFlag advanced) {
		Collections.addAll(itemInformation, new String[]{
            "" + TextFormatting.GRAY + TextFormatting.ITALIC + TextFormatting.BOLD +
                "Force:", "  "+COMPRESSOR_MAX_THRUST+" Newtons"});
    }

	@Override
	public Vector3dc getBlockForceInShipSpace(World world, BlockPos pos, IBlockState state, PhysicsObject physicsObject,
			double secondsToApply) {
		
		TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCompressor) {
        	TileEntityCompressor tileCompressorPart = (TileEntityCompressor) tileEntity;
            return tileCompressorPart.getForceOutputUnoriented(secondsToApply, physicsObject);
        }
        return null;
	}

	@Override
	public boolean shouldLocalForceBeRotated(World world, BlockPos pos, IBlockState state, double secondsToApply) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		 return new TileEntityCompressor(COMPRESSOR_MAX_THRUST);
	}
}
