package one.theaq.handsmanager.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import one.theaq.handsmanager.HandsManagerMain;

public class CommonTags {
	public static TagKey<Item> CROSSBOWS = TagKey.create(Registries.ITEM, HandsManagerMain.commonLocation("tools/crossbow"));
	
	public static void init() {}
}
