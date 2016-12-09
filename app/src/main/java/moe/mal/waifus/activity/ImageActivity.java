package moe.mal.waifus.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import moe.mal.waifus.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * The activity for viewing a single image.
 * Created by Arshad on 23/10/2016.
 */

public class ImageActivity extends GenericActivity implements LoadImage.Listener {
    String waifu;
    Bitmap nextImage;
    boolean alreadyTapped;

    ImageView mImageView;
    PhotoViewAttacher mAttacher;

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            if (((BitmapDrawable) mImageView.getDrawable()).getBitmap().sameAs(nextImage)) {
                alreadyTapped = true;
                return;
            }
            updateImage();
            loadNewImage();
        }

        @Override
        public void onOutsidePhotoTap() {
            finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Bundle extras = getIntent().getExtras();
        waifu = ((String) extras.get("waifu"));
        waifu = (waifu == null) ? "lily" : waifu.toLowerCase().replace(' ', '_');

        mImageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());

//        mAttacher.setOnLongClickListener(new OnLongClickListener());

        alreadyTapped = true;
        loadNewImage();
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        if (alreadyTapped) {
            nextImage = bitmap;
            updateImage();
            loadNewImage();
        } else {
            nextImage = bitmap;
        }
    }

    @Override
    public void setLoadingState(int state) {
        (findViewById(R.id.loadingLabel)).setVisibility(state);
    }

    @Override
    public void onError() {
        showToast(getString(R.string.failed_to_load_image));
    }

    /**
     * Executes the Async task for loading a new image
     */
    private void loadNewImage() {
        new LoadImage(this).execute(waifu);
    }

    /**
     * Updates the image displayed in the imageview
     */
    private void updateImage() {
        mImageView.setImageBitmap(nextImage);
        mAttacher.update();
        alreadyTapped = false;
    }
}