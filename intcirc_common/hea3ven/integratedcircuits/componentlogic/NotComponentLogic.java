package hea3ven.integratedcircuits.componentlogic;

import hea3ven.integratedcircuits.TileCircuitComponentLogic;
import hea3ven.integratedcircuits.client.model.CircuitComponentModelBase;
import hea3ven.integratedcircuits.client.model.NotModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class NotComponentLogic extends ComponentLogic {

	boolean powered;
	private static final NotModel model = new NotModel();

	public NotComponentLogic(TileCircuitComponentLogic tileEntity) {
		super(tileEntity);
	}

	@Override
	public int getUpdateTime(World world, int x, int y, int z,
			int neighborBlockID) {
		if (powered != (getInputStrength(world, x, y, z, getInputSide()) == 0))
			return 2;
		else
			return 0;
	}

	@Override
	public void onUpdate(World world, int x, int y, int z) {
		powered = getInputStrength(world, x, y, z, getInputSide()) == 0;
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
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
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
	public String getTextureName() {
		if (powered)
			return "/mods/integratedcircuits/textures/blocks/not.png";
		else
			return "/mods/integratedcircuits/textures/blocks/not_lit.png";
	}

	@Override
	public CircuitComponentModelBase getModel() {
		return model;
	}

}
