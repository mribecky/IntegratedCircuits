package hea3ven.integratedcircuits.client.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class CircuitComponentModelBase extends ModelBase {
	private ModelRenderer cover = new ModelRenderer(this, 0, 0);
	private List<ModelRenderer> torches = new ArrayList<ModelRenderer>();
	
	public CircuitComponentModelBase() {
		cover.setTextureSize(128, 64);
		cover.addBox(-16.0f, -4.0f, -16.0f, 32, 4, 32, 0.0f);

	}
	
	protected void addTorch(float x, float z, boolean lighted) {
		ModelRenderer torch = new ModelRenderer(this, (lighted?16:0), 36);
		torch.setTextureSize(128,64);
		torch.addBox(x, -14.0f, z, 4, 10, 4, 0.0f);
		torches.add(torch);
	}

	public void render(float par1)
	{
		cover.render(par1);
		for (ModelRenderer torch : torches) {
			torch.render(par1);
		}
	}
}
