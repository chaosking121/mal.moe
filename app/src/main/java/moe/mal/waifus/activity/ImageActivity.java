package moe.mal.waifus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class ImageActivity extends AppCompatActivity {
    private String waifu;
    private WaifuImage image;

    boolean loading;
    boolean tapped;

    @BindView(R.id.imageProgressBar) ProgressBar progressBar;
    @BindView(R.id.imageView) PhotoView mImageView;

    private PhotoViewAttacher mAttacher;
    private Call<WaifuImage> call;

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

        if (getIntent().hasExtra("waifu")) {
            waifu = ((String) extras.get("waifu"));
            Log.e("poop", waifu);
        } else {
            handleBadResult();
            return;
        }

        call = Ougi.getInstance().getWaifuAPI()
                .getImage(waifu);

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

        call.clone().enqueue(new Callback<WaifuImage>() {
            @Override
            public void onResponse(Call<WaifuImage> call, Response<WaifuImage> response) {
                handleImageResponse(response);
            }

            @Override
            public void onFailure(Call<WaifuImage> call, Throwable t) {
                handleBadResult();
            }
        });
    }

    private void handleImageResponse(Response<WaifuImage> response) {

        image = response.body();

        Glide
            .with(this)
            .load(image.getUrl())
            .fitCenter().dontAnimate()
            .placeholder(mImageView.getDrawable())
            .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    handleBadResult();
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

    private void handleBadResult() {
        Toast.makeText(this, "Error loading image.", Toast.LENGTH_SHORT).show();
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