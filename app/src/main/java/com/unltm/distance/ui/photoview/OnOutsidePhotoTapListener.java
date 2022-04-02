package com.unltm.distance.ui.photoview;

import android.widget.ImageView;

/**
 * Callback when the user tapped outside the photo
 */
public interface OnOutsidePhotoTapListener {

    /**
     * The outside of the photo has been tapped
     */
    void onOutsidePhotoTap(ImageView imageView);
}
