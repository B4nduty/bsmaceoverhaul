package banduty.bsmaceoverhaul;

import banduty.bsmaceoverhaul.event.StartTickHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BsMaceOverhaul implements ModInitializer {
	public static final String MOD_ID = "bsmaceoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerTickEvents.START_SERVER_TICK.register(new StartTickHandler());
	}
}