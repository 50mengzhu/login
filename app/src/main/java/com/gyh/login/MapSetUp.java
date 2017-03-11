package com.gyh.login;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyh.login.banner.ViewPagerIndicator;
import com.gyh.login.db.Route;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.gyh.login.Index.allRoutes;
import static com.gyh.login.Index.user;

public class MapSetUp extends MapActivity {

    private MapView mMapView;
    private Button mStepButton;
    private ViewPagerIndicator mIndicator;

    private TencentMap tencentMap;

    // 0 为设置起点， 1 为设置过程， 2 为设置终点
    private int flag = 0;
    // 限制起点终点只能有一个
    private int sum = 0;
    private List<com.gyh.login.db.Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_set_up);

        mIndicator = new ViewPagerIndicator(this);
        mIndicator.setItemCount(6);
        mIndicator.setBackground(getDrawable(R.drawable.add_button));
        FrameLayout.LayoutParams indicatorlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        indicatorlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        indicatorlp.bottomMargin = 180;
        mIndicator.setLayoutParams(indicatorlp);

        addContentView(mIndicator, indicatorlp);

        showDialog("请设置您的起点，完成后通过下方按钮进行下一步，我们期待您的路线:)");

        mStepButton = (Button) findViewById(R.id.step_btn);
        mStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepButton.getText() == getText(R.string.add_start)) {
                    mStepButton.setText(getText(R.string.add_btw));
                    mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_btw), null);
                    mIndicator.setPositionAndOffset(1, 0);
                    flag = 1;
                    sum = 0;
                } else if (mStepButton.getText() == getText(R.string.add_btw)) {
                    mStepButton.setText(getText(R.string.add_final));
                    mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_final), null);
                    mIndicator.setPositionAndOffset(2, 0);
                    flag = 2;
                    sum = 0;
                } else if (mStepButton.getText() == getText(R.string.add_final)) {
                    mStepButton.setText(getText(R.string.add_done));
                    mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_done), null);
                    mStepButton.setBackground(getDrawable(R.drawable.add_done_button));
                    mIndicator.setBackground(getDrawable(R.drawable.add_done_button));
                    mIndicator.setPositionAndOffset(3, 0);
                    flag = 3;
                    sum = 0;
                } else if (mStepButton.getText() == getText(R.string.add_done)) {
                    mIndicator.setPositionAndOffset(4, 0);
                    Route route = new Route(R.drawable.route_1, "BBBBiker", "带上女朋友来骑车", 300, user.getId());
                    route.setId(allRoutes.size() + 1);
                    route.setMarkers(markers);

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(route);

                    // 新建路线文件
                    try {
                        String filename = route.getTitle() + "_route.json";
                        File file = new File(getFilesDir(), filename);
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(jsonString.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
            }
        });

        mMapView = (MapView) findViewById(R.id.mapview);
        tencentMap = mMapView.getMap();
        // 设置中心点
        tencentMap.setCenter(new LatLng(39.9, 116.3));
        // 设置缩放
        tencentMap.setZoom(17);

        UiSettings uiSettings = mMapView.getUiSettings();
        // 启动缩放手势
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setAnimationEnabled(true);

        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (flag == 0 && sum == 0) {
                    setMarker(latLng, "起点", "");
                    sum = 1;
                } else if (flag == 1) {
                    setMarker(latLng, "经过", "好吃的地方");
                } else if (flag == 2 && sum == 0) {
                    setMarker(latLng, "终点", "");
                    sum = 1;
                }
            }
        });

        // 标记的点击事件
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        // 详细信息框的点击事件
        tencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapSetUp.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("开始制作你的路线吧！");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void setMarker(LatLng latLng, String title, String snippet) {
        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .anchor(.5f, .5f)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(false));
        marker.showInfoWindow();
        com.gyh.login.db.Marker m = new com.gyh.login.db.Marker(latLng.getLatitude(), latLng.getLongitude(), title, snippet);
        markers.add(m);
    }
}
