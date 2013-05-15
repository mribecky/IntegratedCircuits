package hea3ven.integratedcircuits.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class StrengthDerectorModel extends ModelBase {
	static final private float STEP = 20.0f / 16;
	private ModelRenderer cover = new ModelRenderer(this, 0, 0);
	private ModelRenderer torchOut = new ModelRenderer(this, 0, 36);
	private ModelRenderer torchIn = new ModelRenderer(this, 0, 36);
	
	public StrengthDerectorModel(int targetSignal)
	{
		cover.setTextureSize(128, 64);
		cover.addBox(-16.0f, -4.0f, -16.0f, 32, 4, 32, 0.0f);

		torchOut.setTextureSize(128,64);
		torchOut.addBox(-2.0f, -14.0f, 8.0f, 4, 10, 4, 0.0f);

		torchIn.setTextureSize(128,64);
		torchIn.addBox(-10.0f + STEP * targetSignal, -14.0f, -8.0f, 4, 10, 4, 0.0f);
	}
	
	public void render(float par1)
	{
		cover.render(par1);
		torchOut.render(par1);
		torchIn.render(par1);
	}

}
