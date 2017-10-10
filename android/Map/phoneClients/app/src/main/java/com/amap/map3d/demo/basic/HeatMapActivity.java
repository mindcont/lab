package com.amap.map3d.demo.basic;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Gradient;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TileOverlayOptions;
import com.mindcont.map.R;

import java.util.Arrays;

public class HeatMapActivity extends Activity {

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {0.0f,
            0.10f, 0.20f, 0.60f, 1.0f};
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),
            Color.argb(255 / 3 * 2, 0, 255, 0),
            Color.rgb(125, 191, 0),
            Color.rgb(185, 71, 0),
            Color.rgb(255, 0, 0)
    };
    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(
            ALT_HEATMAP_GRADIENT_COLORS, ALT_HEATMAP_GRADIENT_START_POINTS);
    private MapView mMapView;
    private AMap mAMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heatmap_activity);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        initDataAndHeatMap();
    }

    private void initDataAndHeatMap() {
        LatLng[] latlngs = new LatLng[500];
        double x = 39.904979;
        double y = 116.40964;

        for (int i = 0; i < 500; i++) {
            double x_ = 0;
            double y_ = 0;
            x_ = Math.random() * 0.5 - 0.25;
            y_ = Math.random() * 0.5 - 0.25;
            latlngs[i] = new LatLng(x + x_, y + y_);
        }
        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(Arrays.asList(latlngs)).gradient(ALT_HEATMAP_GRADIENT)

                .build();
        mAMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
