package com.codinglitch.simpleradio.lexiconfig.classes;

import com.codinglitch.simpleradio.CommonSimpleRadio;

import javax.annotation.Nullable;

public class LexiconHolding {
    @Nullable
    public LexiconPageData getPage(String name) {
        return (LexiconPageData) getEntry(name);
    }

    @Nullable
    public Object getEntry(String name) {
        try {
            return this.getClass().getField(name).get(this);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            CommonSimpleRadio.warn(e);
        }

        return null;
    }
}
