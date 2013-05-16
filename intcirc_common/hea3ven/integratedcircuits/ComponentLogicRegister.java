package hea3ven.integratedcircuits;

import java.lang.reflect.InvocationTargetException;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import hea3ven.integratedcircuits.componentlogic.ComponentLogic;

public class ComponentLogicRegister {

	public int itemID;
	public int componentID;
	public Class<? extends ComponentLogic> klass;
	public ItemCircuitComponent item;

	public ComponentLogicRegister(int itemID, int componentID,
			Class<? extends ComponentLogic> klass) {
		this.itemID = itemID;
		this.componentID = componentID;
		this.klass = klass;
		this.item = new ItemCircuitComponent(itemID, componentID);
	}

	public void registerItem() {

		String itemName;
		try {
			itemName = (String) klass.getMethod("getItemName").invoke(null,
					(Object) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			return;
		}
		String itemDisplayName;
		try {
			itemDisplayName = (String) klass.getMethod("getItemDisplayName")
					.invoke(null, (Object) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			return;
		}
		GameRegistry.registerItem(this.item, itemName);
		LanguageRegistry.addName(this.item, itemDisplayName);

	}

}
