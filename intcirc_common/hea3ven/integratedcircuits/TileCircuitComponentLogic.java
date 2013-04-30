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

import java.util.Map.Entry;

import hea3ven.integratedcircuits.componentlogic.ComponentLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileCircuitComponentLogic extends TileEntity {

	private ComponentLogic logic;
	private int direction;

	public TileCircuitComponentLogic() {
		logic = null;
	}

	public int getSignalOutput(int side) {
		if (logic != null)
			return logic.getSignalOutput(side);
		else
			return 0;
	}

	public int updateTime(World world, int x, int y, int z, int neighborBlockID) {
		if (logic != null)
			return logic.getUpdateTime(world, x, y, z, neighborBlockID);
		else
			return 0;
	}

	public void onUpdate(World world, int x, int y, int z) {
		if (logic != null)
			logic.onUpdate(world, x, y, z);
	}

	public void SetLogic(ComponentLogic componentLogic) {
		this.logic = componentLogic;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		
		nbtTagCompound.setInteger("componentID", getComponentID());
		nbtTagCompound.setInteger("direction", direction);
		logic.writeToNBT(nbtTagCompound);
	}	
	
	private int getComponentID() {
		for (Entry<Integer, Class<? extends ComponentLogic>> entry : IntegratedCircuitsMod.componentLogicsClass.entrySet()) {
			if(entry.getValue().isInstance(logic))
				return entry.getKey();
		}
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		
		try {
			logic = IntegratedCircuitsMod.componentLogicsClass.get(nbtTagCompound.getInteger("componentID")).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDirection(nbtTagCompound.getInteger("direction"));
		
		logic.readFromNBT(nbtTagCompound);
	}

	public void setDirection(int direction) {
		this.direction = direction;
		logic.setDirection(direction);
	}
}
