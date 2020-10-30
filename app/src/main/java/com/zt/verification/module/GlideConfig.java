package com.zt.verification.module;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class GlideConfig extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

