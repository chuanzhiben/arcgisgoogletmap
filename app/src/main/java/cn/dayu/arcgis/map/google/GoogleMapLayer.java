package cn.dayu.arcgis.map.google;

import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by chuan on 16/12/13.
 */
public class GoogleMapLayer extends TiledServiceLayer {

    String _mapType = "s";

    public GoogleMapLayer(int layerType) {
        super(true);
        this._mapType = GoogleFactory.getGoogleMapType(layerType);
        this.init();
    }

    private void init() {
        try {
            getServiceExecutor().submit(new Runnable() {
                public void run() {
                    GoogleMapLayer.this.initLayer();
                }
            });
        } catch (RejectedExecutionException rejectedexecutionexception) {
            Log.e("Google Map Layer", "initialization of the layer failed.",
                    rejectedexecutionexception);
        }
    }

    protected byte[] getTile(int level, int col, int row) throws Exception {
        if (level > GoogleFactory.maxLevel || level < GoogleFactory.minLevel)
            return new byte[0];
        String subDomain = GoogleFactory.subDomains[(level + col + row) % GoogleFactory.subDomains.length];
        //构建待拼接字符串
        String url = GoogleFactory.http + subDomain
                + GoogleFactory.GoogleType + _mapType + GoogleFactory.GoogleX
                + col + GoogleFactory.GoogleY + row + GoogleFactory.GoogleZ + level + GoogleFactory.GoogleG;
//        Log.e("googleurl", url);
        Map<String, String> map = null;
        return com.esri.core.internal.io.handler.a.a(url, map);
    }

    protected void initLayer() {
        if (getID() == 0L) {
            nativeHandle = create();
            changeStatus(com.esri.android.map.event.OnStatusChangedListener.STATUS
                    .fromInt(-1000));
        } else {
            this.setDefaultSpatialReference(SpatialReference.create(102113));
            this.setFullExtent(new Envelope(-22041257.773878,
                    -32673939.6727517, 22041257.773878, 20851350.0432886));
            this.setTileInfo(new TileInfo(GoogleFactory.origin, GoogleFactory.scales, GoogleFactory.resolutions,
                    GoogleFactory.scales.length, GoogleFactory.dpi, GoogleFactory.tileWidth, GoogleFactory.tileHeight));
            super.initLayer();
        }
    }

}