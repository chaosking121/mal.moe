package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.WaifuImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * The activity for viewing a single image.
 * Created by Arshad on 23/10/2016.
 */

public class ImageActivity extends GenericActivity {
    String waifu;
    WaifuImage image;

    boolean loading;
    boolean tapped;

//    @BindView(R.id.loadingLabel) TextView loadingLabel;

    @BindView(R.id.imageProgressBar) ProgressBar progressBar;

    @BindView(R.id.imageView) PhotoView mImageView;
    PhotoViewAttacher mAttacher;

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener, ImageView.OnLongClickListener {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            tapped = true;
            requestImage();
        }

        @Override
        public void onOutsidePhotoTap() {
            finish();
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        waifu = ((String) extras.get("waifu"));
        waifu = (waifu == null) ? "lily" : waifu.toLowerCase().replace(' ', '_');

        progressBar.setIndeterminate(true);

        mAttacher = new PhotoViewAttacher(mImageView);

        PhotoTapListener listener = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(listener);
        mAttacher.setOnLongClickListener(listener);

        tapped = true;
        requestImage();
    }

    private void requestImage() {
        if (loading) {
            return;
        }

        setLoading(true);

        Call<WaifuImage> call = Ougi.getInstance().getWaifuAPI()
                .getImage(waifu,
                        Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<WaifuImage>() {
            @Override
            public void onResponse(Call<WaifuImage> call, Response<WaifuImage> response) {
                handleImageResponse(response);
            }

            @Override
            public void onFailure(Call<WaifuImage> call, Throwable t) {
                handleImageResponse(null);
            }
        });
    }

    private void handleImageResponse(Response<WaifuImage> response) {
        if ((response == null) || (response.body() == null)) {
            showToast("Error loading image.");
            return;
        }

        image = response.body();

        Glide
                .with(this)
                .load(image.getImageUrl())
                .fitCenter().dontAnimate()
                .placeholder(mImageView.getDrawable())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        showToast("Error loading image.");
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mAttacher.update();
                        setLoading(false);
                        return false;
                    }
                })
                .into(mImageView);
        tapped = false;
    }

    private void setLoading(boolean status) {
        loading = status;

        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}