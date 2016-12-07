/*
MIT License

Copyright (c) 2016 Learn2Crack

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package moe.mal.waifus.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import moe.mal.waifus.Ougi;

/**
 * A tool for fetching images from mal.moe.
 * Modified and redistributed in accordance with the MIT License.
 * Based off of code from Learn2Crack
 * Created by Arshad on 23/10/2016.
 */

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    public static final String DEFAULT_WAIFU = "lily";

    public LoadImage(Listener listener) {
        mListener = listener;
    }

    public interface Listener{
        void onImageLoaded(Bitmap bitmap);
        void onError();
        void setLoadingState(int state);
    }

    private Listener mListener;

    @Override
    protected void onPreExecute() {
        mListener.setLoadingState(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String ... args) {
        String url = "https://mal.moe/api/waifus/"
                + ("".equals(args[0]) ? DEFAULT_WAIFU : args[0]);
        try {
            return BitmapFactory.decodeStream(getInputStream(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream getInputStream(String url_string) throws IOException {
        URL url = new URL(url_string);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", Ougi.getInstance().getUser().getAuth());
        return urlConnection.getInputStream();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mListener.setLoadingState(View.INVISIBLE);
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }
}