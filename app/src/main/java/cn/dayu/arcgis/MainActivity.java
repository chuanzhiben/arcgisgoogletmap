package cn.dayu.arcgis;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dayu.arcgis.common.ConStant;
import cn.dayu.arcgis.map.google.GoogleFactory;
import cn.dayu.arcgis.map.google.GoogleMapLayer;
import cn.dayu.arcgis.map.google.GoogleMapLayerTypes;
import cn.dayu.arcgis.map.tianditu.TianDiTuLayer;
import cn.dayu.arcgis.map.tianditu.TianDiTuLayerTypes;

public class MainActivity extends Activity {
    Activity context;
    @Bind(R.id.r_msg)
    RelativeLayout rMsg;
    @Bind(R.id.l_mine)
    LinearLayout lMine;
    @Bind(R.id.l_map_type)
    LinearLayout lMapType;
    @Bind(R.id.l_location)
    LinearLayout lLocation;
    @Bind(R.id.map_arcgis)
    MapView mapArcgis;
    @Bind(R.id.img_map_type)
    ImageView imgMapType;
    TianDiTuLayer t_vector_Layer, t_imge_layer, t_terrain_layer;
    TianDiTuLayer t_vectora_Layer, t_imgea_layer, t_terraina_layer;
    GoogleMapLayer g_layer;
    Layer tmapLayer, tannotationLayer, gmaplayer;
    View v;
    SpatialReference mSR;
    LocationDisplayManager ldm;
    boolean hasGps = true;
    boolean mting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
        addlayer();
        listens();
    }

    private void addlayer() {
        mSR = SpatialReference.create(GoogleFactory.SRID_MERCATOR);
        Point p = GeometryEngine.project(ConStant.locx, ConStant.locy, mSR);
        mapArcgis.zoomToResolution(p, ConStant.Rzoom);
        mapArcgis.setMapBackground(0x000000, 0xcccccc, 10, 1);
        t_vector_Layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_MERCATOR);
        t_imge_layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_IMAGE_MERCATOR);
        t_terrain_layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_TERRAIN_MERCATOR);
        t_vectora_Layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR);
        t_imgea_layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR);
        t_terraina_layer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR);
        g_layer = new GoogleMapLayer(GoogleMapLayerTypes.GOOGLE_IMAGE_MAP);
        tmapLayer = t_vector_Layer;
        tannotationLayer = t_vectora_Layer;
        gmaplayer = new GraphicsLayer();
        mapArcgis.addLayer(tmapLayer);
        mapArcgis.addLayer(tannotationLayer);
        mapArcgis.addLayer(gmaplayer);
    }

    void listens() {
        mapArcgis.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if ((status == STATUS.INITIALIZED) && (o instanceof MapView)) {
                    ldm = mapArcgis.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
                    ldm.setShowLocation(false);
                    ldm.setLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
//                            Log.d("DUGLOCATION", "lat: " + location.getLatitude() + " long: " + location.getLongitude());

                            if (location != null) {
                                hasGps = true;
//                                Log.d("DUGLOCATION", "lat: " + location.getLatitude() + " long: " + location.getLongitude());
                                ldm.setShowLocation(true);
                                ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
//                                Log.d("DUGLOCATION", "Location found....");
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.d("DUGLOCATION", "onStatusChanged");
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText(context, provider + "定位可用", Toast.LENGTH_SHORT);

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                Toast.makeText(context, provider + "定位不可用", Toast.LENGTH_SHORT);
                            } else {
                                ldm.setShowLocation(false);
                            }
                        }
                    });

                    ldm.start();
                }
            }
        });

    }

    PopupWindow popWindow;
    View popView;
    ImageView imgEndGsat, imgEndSat, imgEndTra;
    LinearLayout lPop, lGoogleSatl, lSatellite, lTrail;
    TextView tvGs, tvSat, tvTraffic;

    private void showPop() {
        if (popWindow != null) {
            if (popWindow.isShowing()) {
                imgMapType.setImageResource(R.drawable.icon_map_type);
                mting = false;
                popWindow.dismiss();
            } else {
                mting = true;
                int[] location = new int[2];
                popView.getLocationOnScreen(location);
                popWindow.showAtLocation(popView, Gravity.NO_GRAVITY, location[0], location[1] - popWindow.getHeight());
            }
        } else {
            mting = true;
            initPop();
            int[] location = new int[2];
            popView.getLocationOnScreen(location);
            popWindow.showAtLocation(popView, Gravity.NO_GRAVITY, location[0], location[1] - popWindow.getHeight());
        }
        if (mting) {
            imgMapType.setImageResource(R.drawable.icon_map_typei);
        } else {
            imgMapType.setImageResource(R.drawable.icon_map_type);
        }
    }

    void initPop() {
        popView = getLayoutInflater().inflate(R.layout.pop_map_type, null, false);
        popWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        lPop = (LinearLayout) popView.findViewById(R.id.l_pop);
        lGoogleSatl = (LinearLayout) popView.findViewById(R.id.l_google_satl);
        lSatellite = (LinearLayout) popView.findViewById(R.id.l_satellite);
        lTrail = (LinearLayout) popView.findViewById(R.id.l_trail);
        imgEndGsat = (ImageView) popView.findViewById(R.id.img_end_gsat);
        imgEndSat = (ImageView) popView.findViewById(R.id.img_end_sat);
        imgEndTra = (ImageView) popView.findViewById(R.id.img_end_tra);
        tvGs = (TextView) popView.findViewById(R.id.tv_gs);
        tvSat = (TextView) popView.findViewById(R.id.tv_sat);
        tvTraffic = (TextView) popView.findViewById(R.id.tv_traffic);
        lPop.setOnClickListener(onClickListener);
        lGoogleSatl.setOnClickListener(onClickListener);
        lSatellite.setOnClickListener(onClickListener);
        lTrail.setOnClickListener(onClickListener);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        lPop.setAnimation(slide);
        popWindow.setOutsideTouchable(true);
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closePop();
            }
        });
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popWindow != null && popWindow.isShowing()) {
                    closePop();
                }
                return false;
            }
        });
    }

    private void closePop() {
        if (popWindow != null) {
            if (popWindow.isShowing()) {
                popWindow.dismiss();
                mting = false;
            }
        }
        if (mting) {
            imgMapType.setImageResource(R.drawable.icon_map_typei);
        } else {
            imgMapType.setImageResource(R.drawable.icon_map_type);
        }
    }

    private void setMapType(int mtype) {
        mapArcgis.removeLayer(tmapLayer);
        mapArcgis.removeLayer(tannotationLayer);
        mapArcgis.removeLayer(gmaplayer);
        tvGs.setTextColor(getResources().getColor(R.color.tv_maptype2));
        tvSat.setTextColor(getResources().getColor(R.color.tv_maptype2));
        tvTraffic.setTextColor(getResources().getColor(R.color.tv_maptype2));
        imgEndGsat.setVisibility(View.INVISIBLE);
        imgEndSat.setVisibility(View.INVISIBLE);
        imgEndTra.setVisibility(View.INVISIBLE);
        switch (mtype) {
            case 1:
                tmapLayer = new GraphicsLayer();
                tannotationLayer = new GraphicsLayer();
                gmaplayer = g_layer;
                tvGs.setTextColor(getResources().getColor(R.color.tv_maptype1));
                imgEndGsat.setVisibility(View.VISIBLE);
                break;
            case 2:
                tmapLayer = t_imge_layer;
                tannotationLayer = t_imgea_layer;
                gmaplayer = new GraphicsLayer();
                tvSat.setTextColor(getResources().getColor(R.color.tv_maptype1));
                imgEndSat.setVisibility(View.VISIBLE);
                break;
            case 3:
                tmapLayer = t_vector_Layer;
                tannotationLayer = t_vectora_Layer;
                gmaplayer = new GraphicsLayer();
                tvTraffic.setTextColor(getResources().getColor(R.color.tv_maptype1));
                imgEndTra.setVisibility(View.VISIBLE);
                break;
        }
        mapArcgis.addLayer(tmapLayer);
        mapArcgis.addLayer(tannotationLayer);
        mapArcgis.addLayer(gmaplayer);
    }

    @OnClick({R.id.r_msg, R.id.l_mine, R.id.l_map_type, R.id.l_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_msg:
                break;
            case R.id.l_mine:
                break;
            case R.id.l_map_type:
                showPop();
                break;
            case R.id.l_location:
                ldm.setShowLocation(true);
                ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                break;
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.l_google_satl:
                    setMapType(ConStant.MTYPE_SAT);
                    break;
                case R.id.l_satellite:
                    setMapType(ConStant.MTYPE_GSAT);
                    break;
                case R.id.l_trail:
                    setMapType(ConStant.MTYPE_TRA);
                    break;
                case R.id.l_pop:
                    break;
            }
        }
    };
}
