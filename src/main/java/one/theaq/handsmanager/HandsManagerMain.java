package one.theaq.handsmanager;

//~identifier
import net.minecraft.resources.Identifier;
import one.theaq.handsmanager.config.HandsManagerConfig;
import one.theaq.handsmanager.tag.CommonTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandsManagerMain {
	public static final String MOD_ID = "mchandsmanager";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final HandsManagerConfig CONFIG = new HandsManagerConfig();
	
	public static void initialize() {
		CommonTags.init();
		LOGGER.info(MOD_ID + " initialized");
	}

	public static Identifier location(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	
	public static Identifier commonLocation(String path) {
		return Identifier.fromNamespaceAndPath("c", path);
	}
}
