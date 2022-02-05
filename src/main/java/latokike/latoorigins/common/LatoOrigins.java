package latokike.latoorigins.common;

import latokike.latoorigins.common.power.factory.action.*;
import latokike.latoorigins.common.registry.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.ModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LatoOrigins implements ModInitializer {
	public static final String MODID = "latoorigins";
	public static String VERSION = "";
	public static int[] SEMVER;
	public static final Logger LOGGER = LogManager.getLogger("Lato Origins");
	
	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(MODID).ifPresent(modContainer -> {
			VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
			if(VERSION.contains("+")) {
				VERSION = VERSION.split("\\+")[0];
			}
			if(VERSION.contains("-")) {
				VERSION = VERSION.split("-")[0];
			}
			String[] splitVersion = VERSION.split("\\.");
			SEMVER = new int[splitVersion.length];
			for(int i = 0; i < SEMVER.length; i++) {
				SEMVER[i] = Integer.parseInt(splitVersion[i]);
			}
		});
		LOGGER.info("Lato Origins " + VERSION + " is initializing. Have fun!");

		LOEvents.init();
		LOScaleTypes.init();
		LOPowers.init();
		LOInventory.init();
		BiEntityActions.init();
		EntityActions.init();
	}

	public static Identifier identifier(String path) {
		return new Identifier(LatoOrigins.MODID, path);
	}
}
