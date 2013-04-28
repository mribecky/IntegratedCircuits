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

import java.util.HashMap;
import java.util.Map;

import hea3ven.integratedcircuits.componentlogic.ComponentLogic;
import hea3ven.integratedcircuits.componentlogic.StrengthDetectorComponentLogic;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "IntegratedCircuits", name = "Integrated Circuits", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class IntegratedCircuitsMod {
	@Instance("IntegratedCircuits")
	public static IntegratedCircuitsMod instance;

	@SidedProxy(clientSide = "hea3ven.integratedcircuits.client.IntegratedCircuitsClientProxy", serverSide = "hea3ven.integratedcircuits.IntegratedCircuitsCommonProxy")
	public static IntegratedCircuitsCommonProxy proxy;

	public static BlockCircuitComponent circuitComponent;

	public static ItemRedstoneReader redstoneReader;
	public static ItemCircuitComponent componentItemStrengthDetector;
	
	public static Map<Integer, Class<? extends ComponentLogic>> componentLogicsClass = new HashMap<>(); 

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Init
	public void load(FMLInitializationEvent event) {

		circuitComponent = new BlockCircuitComponent(2004);
		GameRegistry.registerBlock(circuitComponent, "circuitComponent");
		LanguageRegistry.addName(circuitComponent, "Circuit Component");

		redstoneReader = new ItemRedstoneReader(5000);
		GameRegistry.registerItem(redstoneReader, "RedstoneReader");
		LanguageRegistry.addName(redstoneReader, "Redstone Reader");
		
		addComponentLogic(5001, 1, StrengthDetectorComponentLogic.class);
	}

	private void addComponentLogic(int itemID, int componentID,
			Class<StrengthDetectorComponentLogic> klass) {
		componentLogicsClass.put(componentID, klass);
		componentItemStrengthDetector = new ItemCircuitComponent(itemID, componentID);
		GameRegistry.registerItem(componentItemStrengthDetector,
				"circuitComponentStrengthDetector");
		LanguageRegistry.addName(redstoneReader, "Strength Detector");
		
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
}
