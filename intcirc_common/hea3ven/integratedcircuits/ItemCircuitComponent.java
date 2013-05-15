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

import java.lang.reflect.InvocationTargetException;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCircuitComponent extends Item {

	private int componentID;

	public ItemCircuitComponent(int itemID, int componentID) {
		super(itemID);
		setCreativeTab(CreativeTabs.tabRedstone);

		this.componentID = componentID;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer,
			World world, int x, int y, int z, int side, float xOffset,
			float yOffset, float zOffset) {
		int blockID = IntegratedCircuitsMod.circuitComponent.blockID;
		Block block = IntegratedCircuitsMod.circuitComponent;
		int id = world.getBlockId(x, y, z);

		if (id == Block.snow.blockID) {
			side = 1;
		} else if (id != Block.vine.blockID
				&& id != Block.tallGrass.blockID
				&& id != Block.deadBush.blockID
				&& (Block.blocksList[id] == null || !Block.blocksList[id]
						.isBlockReplaceable(world, x, y, z))) {
			if (side == 0) {
				y--;
			}
			if (side == 1) {
				y++;
			}
			if (side == 2) {
				z--;
			}
			if (side == 3) {
				z++;
			}
			if (side == 4) {
				x--;
			}
			if (side == 5) {
				x++;
			}
		}

		if (itemStack.stackSize == 0)
			return false;
		if (entityPlayer.canCurrentToolHarvestBlock(x, y, z)
				&& world.canPlaceEntityOnSide(blockID, x, y, z, false, side,
						entityPlayer, itemStack)) {

			if (world.isRemote)
				return true;

			boolean placed = world.setBlock(x, y, z, blockID, 0, 1);

			if (placed) {

				TileCircuitComponentLogic tile = (TileCircuitComponentLogic) world
						.getBlockTileEntity(x, y, z);
				try {
					tile.setLogic(IntegratedCircuitsMod.componentLogicsClass.get(this.componentID).getDeclaredConstructor(TileCircuitComponentLogic.class).newInstance(this));
				} catch (InstantiationException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				} catch (SecurityException e) {
					e.printStackTrace();
					world.setBlockToAir(x, y, z);
					return false;
				}

				Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z,
						entityPlayer, itemStack);
				
				world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F,
						block.stepSound.getPlaceSound(),
						(block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);
				itemStack.stackSize--;
				return true;
			}
		}
		return false;

	}
}
