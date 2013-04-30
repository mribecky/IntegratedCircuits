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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StrengthDetectorComponentLogic extends ComponentLogic {

	boolean powered;
	int targetStrength = 10;

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

}
