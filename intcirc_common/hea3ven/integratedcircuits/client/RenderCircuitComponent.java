package hea3ven.integratedcircuits.client;

import hea3ven.integratedcircuits.TileCircuitComponentLogic;
import hea3ven.integratedcircuits.componentlogic.ComponentLogic;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderCircuitComponent extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		this.renderCircuitComponentAt((TileCircuitComponentLogic) tileentity,
				d0, d1, d2, f);

	}

	private void renderCircuitComponentAt(
			TileCircuitComponentLogic par1TileEntitySign, double par2,
			double par4, double par6, float f) {

		ComponentLogic logic = par1TileEntitySign.getLogic();
		if (logic != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) par2 + 0.5F, (float) par4,
					(float) par6 + 0.5F);
			float angle = (float) (par1TileEntitySign.getDirection() * 360) / 4.0F;
			GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);

			this.bindTextureByName(logic.getTextureName());
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, -0.5f, -0.5f);

			logic.getModel().render(0.0625F);

			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

}
