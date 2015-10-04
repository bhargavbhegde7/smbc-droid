package com.smbc.droid;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComicManager {
    private static final String LOGTAG = ComicManager.class.getSimpleName();
    private static final String endpoint = "http://192.168.1.3:9001/";
    private static ComicManager mInstance;

    private Map<Integer, Comic> mComics;
    private Integer latest;

    private ComicManager() {
        mComics = new HashMap<>(100);
        setLatest();
    }

    public static ComicManager getInstance() {
        if (mInstance == null) {
            mInstance = new ComicManager();
        }
        return mInstance;
    }

    public ListenableFuture<Comic> getComic(final Integer index) {
        if (mComics.containsKey(index)) {
            return Futures.immediateFuture(mComics.get(index));
        }

        final SettableFuture<Comic> future = SettableFuture.create();
        Request req = new Request.Builder().url(endpoint + index).build();
        Call call = new OkHttpClient().newCall(req);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                future.setException(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Type listType = new TypeToken<List<Comic>>() {
                }.getType();
                List<Comic> comics = new Gson().fromJson(body, listType);
                for (Comic comic : comics) {
                    mComics.put(comic.mIndex, comic);
                }
                future.set(mComics.get(index));
            }
        });

        return future;
    }

    private void setLatest() {
        Request req = new Request.Builder().url(endpoint + "latest").build();
        Call call = new OkHttpClient().newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(LOGTAG, "request failed - " + endpoint + "latest");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Comic latestComic = new Gson().fromJson(body, Comic.class);
                Log.d(LOGTAG, body + " - " + latestComic.mIndex);
                latest = latestComic.mIndex;
                mComics.put(latest, latestComic);
            }
        });
    }

}
