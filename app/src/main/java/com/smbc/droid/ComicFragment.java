package com.smbc.droid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class ComicFragment extends Fragment {
    private static final String LOGTAG = ComicFragment.class.getSimpleName();
    public static final String INDEX = "index";
    private static final String endpoint = "http://smbc-comics.com/";
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.comic_view);
        mImageLoader = VolleyHelper.getInstance(getContext()).getImageLoader();
        Integer index = getArguments().getInt(INDEX);
        setComic(index);
        return rootView;
    }

    private void setComic(final Integer index) {
        Log.d(LOGTAG, "ComicIndex - " + index);
        ListenableFuture<Comic> future = ComicManager.getInstance().getComic(index);
        Futures.addCallback(future, new FutureCallback<Comic>() {
            @Override
            public void onSuccess(@Nullable final Comic result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            Log.d(LOGTAG, "About to read - " + index);
                            mNetworkImageView.setImageUrl(endpoint + result.mImageUrl, mImageLoader);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
