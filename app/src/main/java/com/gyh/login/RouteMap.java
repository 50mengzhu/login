package com.gyh.login;

import android.os.Bundle;

import com.gyh.login.db.Marker;
import com.gyh.login.db.Route;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import static com.gyh.login.Index.allRoutes;

public class RouteMap extends MapActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);


        Route route = null;
        // 读取路线信息
        for (Route r : allRoutes) {
            if (r.getId() == getIntent().getIntExtra("route", 0)) {
                route = r;
                break;
            }
        }

        mMapView = (MapView) findViewById(R.id.mapview);
        TencentMap tencentMap = mMapView.getMap();
        tencentMap.setZoom(17);

        double initLongitude = 0;
        double initLatitude = 0;

        for (Marker marker : route.getMarkers()) {
            LatLng startPos = new LatLng(marker.getLongitude(), marker.getLatitude());
            com.tencent.mapsdk.raster.model.Marker m = tencentMap.addMarker(new MarkerOptions()
                    .position(startPos)
                    .title(marker.getTitle())
                    .snippet(marker.getSnippet())
                    .anchor(.5f, .5f)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker()));
            m.showInfoWindow();

            initLatitude += marker.getLatitude();
            initLongitude += marker.getLongitude();
        }

        tencentMap.setCenter(new LatLng(initLongitude / route.getMarkers().size(), initLatitude / route.getMarkers().size()));

        UiSettings uiSettings = mMapView.getUiSettings();
        // 启动缩放手势
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setAnimationEnabled(true);
    }
}
