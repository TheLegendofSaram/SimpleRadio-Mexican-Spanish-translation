package com.codinglitch.simpleradio.core.central;

import com.codinglitch.simpleradio.CommonSimpleRadio;
import com.codinglitch.simpleradio.lexiconfig.annotations.LexiconPage;
import com.codinglitch.simpleradio.lexiconfig.classes.LexiconPageData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemHolder<I extends Item> {
    private final I item;
    private final ResourceLocation location;
    public boolean enabled = true;

    protected ItemHolder(I item, ResourceLocation location) {
        this.item = item;
        this.location = location;
    }

    public static <I extends Item> ItemHolder<I> of(I item, ResourceLocation location) {
        ItemHolder<I> holder = new ItemHolder<>(item, location);

        LexiconPageData configData = CommonSimpleRadio.SERVER_CONFIG.getPage(location.getPath());
        if (configData != null) {
            Object field = configData.getEntry("enabled");
            holder.enabled = field == null || (boolean) field;
        }

        return holder;
    }

    public Item get() {
        return this.item;
    }
}
