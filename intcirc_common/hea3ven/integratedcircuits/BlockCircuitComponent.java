/**
 * Copyright (c) 2013 Matias Ribecky.
 * 
 * This file is part of IntegratedCircuits.
 * 
 * IntegratedCircuits is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * IntegratedCircuits is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with IntegratedCircuits.  If not, see <http://www.gnu.org/licenses/>.
 */

package hea3ven.integratedcircuits;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCircuitComponent extends Block implements ITileEntityProvider {

	private boolean sendInitialData;

	public BlockCircuitComponent(int blockID) {
		super(blockID, Material.circuits);

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the
	 * specified side. Args: World, X, Y, Z, side. Note that the side is
	 * reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z,
			int side) {
		return this.isProvidingWeakPower(world, x, y, z, side);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the
	 * specified side. If isBlockNormalCube returns true, standard redstone
	 * propagation rules will apply instead and this will not be called. Args:
	 * World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up)
	 * when checking the bottom of the block.
	 */
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z,
			int side) {
		if (side < 2)
			return 0;
		return getTileEntity(world, x, y, z).getSignalOutput(side);
	}

	private TileCircuitComponentLogic getTileEntity(IBlockAccess world, int x,
			int y, int z) {
		return ((TileCircuitComponentLogic) world.getBlockTileEntity(x, y, z));
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this
	 * change based on its state.
	 */
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return !world.doesBlockHaveSolidTopSurface(x, y - 1, z) ? false : super
				.canPlaceBlockAt(world, x, y, z);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	public boolean canBlockStay(World world, int x, int y, int z) {
		return !world.doesBlockHaveSolidTopSurface(x, y - 1, z) ? false : super
				.canBlockStay(world, x, y, z);
	}

	/**
	 * Called right before the block is destroyed by a player. Args: world, x,
	 * y, z, metaData
	 */
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z,
			int metadata) {
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);

		super.onBlockDestroyedByPlayer(world, x, y, z, metadata);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileCircuitComponentLogic();
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor blockID
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z,
			int neighborBlockID) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z,
					world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		} else {
			int updateRequired = this.getTileEntity(world, x, y, z).updateTime(
					world, x, y, z, neighborBlockID);

			if (updateRequired > 0
					&& !world.isBlockTickScheduled(x, y, z,
							IntegratedCircuitsMod.circuitComponent.blockID)) {
				world.func_82740_a(x, y, z,
						IntegratedCircuitsMod.circuitComponent.blockID,
						updateRequired, -1);
			}
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World world, int x, int y, int z, Random random) {

		TileCircuitComponentLogic tile = this.getTileEntity(world, x, y, z);
		if (this.sendInitialData) {
			// TODO: this should be done by the tile entity
			this.sendInitialData = false;
			Packet packet = tile.getDescriptionPacket();
			if (packet != null)
				PacketDispatcher.sendPacketToAllPlayers(packet);
		}

		tile.onUpdate(world, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);

	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving entityLiving, ItemStack itemStack) {
		int l = ((MathHelper
				.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
		this.getTileEntity(world, x, y, z).setDirection(l);
		world.setBlockMetadataWithNotify(x, y, z, 0, 3);
		this.sendInitialData = true;

		world.scheduleBlockUpdate(x, y, z, this.blockID, 1);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
			int z, int metadata, int fortune) {
		TileCircuitComponentLogic te = (TileCircuitComponentLogic) world
				.getBlockTileEntity(x, y, z);
		IntegratedCircuitsMod.getComponentId(te.getLogic().getClass());

		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(
				IntegratedCircuitsMod.componentItemStrengthDetector, 1,
				damageDropped(metadata)));

		return ret;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityPlayer, int side, float hitVecX, float hitVecY,
			float hitVecZ) {
			TileCircuitComponentLogic te = (TileCircuitComponentLogic)world.getBlockTileEntity(x, y, z);
		return te.onBlockActivated(world, x, y, z, entityPlayer, side, hitVecX, hitVecY, hitVecZ);
	}

	@Override
	public int getRenderType() {
		return IntegratedCircuitsMod.renderCircuitComponentID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int metadata, int side) {
		if (metadata == 0) {
			return Block.torchRedstoneIdle.getBlockTextureFromSide(side);
		} else {
			if (side == 1)
				return null;
			else
				return Block.stoneDoubleSlab.getBlockTextureFromSide(1);
		}
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if
	 * the adjacent block is at the given coordinates. Args: blockAccess, x, y,
	 * z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y,
			int z, int side) {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

}
