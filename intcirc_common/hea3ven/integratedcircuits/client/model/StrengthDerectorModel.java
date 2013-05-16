package hea3ven.integratedcircuits.client.model;


public class StrengthDerectorModel extends CircuitComponentModelBase {
	static final private float STEP = 20.0f / 14;
	public StrengthDerectorModel(int targetSignal)
	{
		addTorch(-2.0f, 8.0f, false);
		addTorch(-12.0f + STEP * (targetSignal - 1), -8.0f, false);
	}
}
