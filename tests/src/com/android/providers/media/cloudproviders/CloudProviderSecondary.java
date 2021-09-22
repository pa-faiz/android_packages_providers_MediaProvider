/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.providers.media.cloudproviders;

import static android.provider.CloudMediaProviderContract.MediaInfo;
import static com.android.providers.media.PickerProviderMediaGenerator.MediaGenerator;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.CloudMediaProvider;
import com.android.providers.media.MediaProvider;
import com.android.providers.media.PickerProviderMediaGenerator;

import java.io.FileNotFoundException;

/**
 * Implements a cloud {@link CloudMediaProvider} interface over items generated with
 * {@link MediaGenerator}
 */
public class CloudProviderSecondary extends CloudMediaProvider {
    private static final String AUTHORITY =
            "com.android.providers.media.photopicker.tests.cloud_secondary";

    private final MediaGenerator mMediaGenerator =
            PickerProviderMediaGenerator.getMediaGenerator(AUTHORITY);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor onQueryMedia(String mediaId) {
        throw new UnsupportedOperationException("onQueryMedia by id not supported");
    }

    @Override
    public Cursor onQueryMedia(Bundle extras) {
        return mMediaGenerator.getMedia(getGeneration(extras));
    }

    @Override
    public Cursor onQueryDeletedMedia(Bundle extras) {
        return mMediaGenerator.getDeletedMedia(getGeneration(extras));
    }

    @Override
    public AssetFileDescriptor onOpenThumbnail(String mediaId, Point size,
            CancellationSignal signal) throws FileNotFoundException {
        throw new UnsupportedOperationException("onOpenThumbnail not supported");
    }

    @Override
    public ParcelFileDescriptor onOpenMedia(String mediaId, CancellationSignal signal)
            throws FileNotFoundException {
        throw new UnsupportedOperationException("onOpenMedia not supported");
    }

    @Override
    public Bundle onGetMediaInfo(Bundle extras) {
        Bundle bundle = new Bundle();
        bundle.putString(MediaInfo.MEDIA_VERSION, mMediaGenerator.getVersion());
        bundle.putLong(MediaInfo.MEDIA_GENERATION, mMediaGenerator.getGeneration());
        bundle.putLong(MediaInfo.MEDIA_COUNT, mMediaGenerator.getCount());

        return bundle;
    }

    private static long getGeneration(Bundle extras) {
        return extras == null ? 0 : extras.getLong(MediaInfo.MEDIA_GENERATION, 0);
    }
}
