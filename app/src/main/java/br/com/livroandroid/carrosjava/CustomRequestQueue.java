package br.com.livroandroid.carrosjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

public class CustomRequestQueue {
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private static  CustomRequestQueue instance;
    private static Context context;

    private CustomRequestQueue(Context context){
        this.context = context;
        queue = getRequestQueue();
        imageLoader = new ImageLoader(this.queue, new ImageLoader.ImageCache() {
            private final LruCache<String,Bitmap> mcache = new LruCache<String,Bitmap>(10);
            public void putBitmap (String url, Bitmap bitmap){mcache.put(url, bitmap);}
            public Bitmap getBitmap(String url){return mcache.get(url);}
        });
    }

    public ImageLoader getImageLoader(){
        return this.imageLoader;
    }

    public static CustomRequestQueue getInstance(Context context){
        if(instance == null){
            instance = new CustomRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (queue == null){
            Cache cache = new DiskBasedCache(context.getCacheDir(),10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            queue = new RequestQueue(cache, network);
            queue.start();
        }
        return queue;
    }
}
