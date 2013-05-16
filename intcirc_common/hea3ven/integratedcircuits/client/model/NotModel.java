package hea3ven.integratedcircuits.client.model;


public class NotModel extends CircuitComponentModelBase {
	public NotModel()
	{
		super();

		addTorch(-2.0f, 8.0f, true);
		addTorch(-2.0f, -8.0f, false);
	}

}
