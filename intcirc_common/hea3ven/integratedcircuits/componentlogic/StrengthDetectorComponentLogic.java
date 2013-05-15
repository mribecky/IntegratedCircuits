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
import hea3ven.integratedcircuits.client.StrengthDerectorModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StrengthDetectorComponentLogic extends ComponentLogic {

	public StrengthDetectorComponentLogic(TileCircuitComponentLogic tileEntity) {
		super(tileEntity);
	}

	boolean powered;
	int targetStrength = 10;

	private static final StrengthDerectorModel[] models = new StrengthDerectorModel[] {
			new StrengthDerectorModel(1), new StrengthDerectorModel(2),
			new StrengthDerectorModel(3), new StrengthDerectorModel(4),
			new StrengthDerectorModel(5), new StrengthDerectorModel(6),
			new StrengthDerectorModel(7), new StrengthDerectorModel(8),
			new StrengthDerectorModel(9), new StrengthDerectorModel(10),
			new StrengthDerectorModel(11), new StrengthDerectorModel(12),
			new StrengthDerectorModel(13), new StrengthDerectorModel(14),
			new StrengthDerectorModel(15) };

	@Override
	public int getUpdateTime(World world, int x, int y, int z,
			int neighborBlockID) {
		if (powered != (getInputStrength(world, x, y, z, getInputSide()) == targetStrength))
			return 2;
		else
			return 0;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		powered = getInputStrength(world, x, y, z, getInputSide()) == targetStrength;
		sendUpdatePacket();
	}

	@Override
	public int getSignalOutput(int side) {
		if (powered && side == getOutputSide())
			return 15;
		else
			return 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setInteger("targetStrength", targetStrength);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		targetStrength = nbtTagCompound.getInteger("targetStrength");

	}

	@Override
	public boolean isLit() {
		return powered;
	}

	@Override
	public void addToDescriptionPacket(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("powered", powered);
	}

	@Override
	public void readFromDescriptionPacket(NBTTagCompound nbttagcompound) {
		powered = nbttagcompound.getBoolean("powered");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer entityPlayer, int side, float hitVecX, float hitVecY,
			float hitVecZ) {
		if (targetStrength >= 15)
			targetStrength = 1;
		else
			targetStrength++;
		sendUpdatePacket();
		return true;
	}

	@Override
	public String getTextureName() {
		if (this.getSignalOutput(this.getOutputSide()) > 0)
			return "/mods/integratedcircuits/textures/blocks/strength_detector_lit.png";
		else
			return "/mods/integratedcircuits/textures/blocks/strength_detector.png";
	}

	@Override
	public StrengthDerectorModel getModel() {
		return models[targetStrength - 1];
	}
}
