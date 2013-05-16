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

package hea3ven.integratedcircuits.componentlogic;

import hea3ven.integratedcircuits.TileCircuitComponentLogic;
import hea3ven.integratedcircuits.client.model.CircuitComponentModelBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public abstract class ComponentLogic {

	private int direction = 0;
	private TileCircuitComponentLogic tileEntity = null;

	public ComponentLogic(TileCircuitComponentLogic tileEntity) {
		this.tileEntity = tileEntity;

	}

	abstract public int getUpdateTime(World world, int x, int y, int z,
			int neighborBlockID);

	abstract public void onUpdate(World world, int x, int y, int z);

	abstract public int getSignalOutput(int side);

	abstract public void writeToNBT(NBTTagCompound nbtTagCompound);

	abstract public void readFromNBT(NBTTagCompound nbtTagCompound);

	abstract public boolean isLit();

	abstract public void addToDescriptionPacket(NBTTagCompound nbttagcompound);

	abstract public void readFromDescriptionPacket(NBTTagCompound nbttagcompound);

	public abstract String getTextureName();

	public abstract CircuitComponentModelBase getModel();

	/**
	 * Returns the signal strength at one input of the block. Args: world, X, Y,
	 * Z, side
	 */
	protected int getInputStrength(World world, int x, int y, int z, int side) {
		int inX = x + Direction.offsetX[side & 3];
		int inZ = z + Direction.offsetZ[side & 3];
		int power = world.getIndirectPowerLevelTo(inX, y, inZ,
				Direction.directionToFacing[side & 3]);
		return power >= 15 ? power
				: Math.max(
						power,
						world.getBlockId(inX, y, inZ) == Block.redstoneWire.blockID ? world
								.getBlockMetadata(inX, y, inZ) : 0);
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getInputSide() {
		return direction;
	}

	public int getOutputSide() {
		return Direction.directionToFacing[direction];
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityPlayer, int side, float hitVecX, float hitVecY,
			float hitVecZ) {
		return false;
	}

	protected void sendUpdatePacket() {
		this.tileEntity.sendUpdatePacket();
	}

}
