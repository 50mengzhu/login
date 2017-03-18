package com.gyh.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyh.login.banner.ViewPagerIndicator;
import com.gyh.login.db.Route;
import com.gyh.login.db.User;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import java.io.File;
import java.io.FileNotFoundException;
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

    private Marker cilckMarker;

    // 0 为设置起点， 1 为设置过程， 2 为设置终点
    private int flag = 0;
    // 限制起点终点只能有一个
    private int sum = 0;
    public static List<com.gyh.login.db.Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_set_up);

        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        mIndicator.setItemCount(6);
        mIndicator.setBackground(getDrawable(R.drawable.add_button));

        showDialog("请设置您的起点，完成后通过下方按钮进行下一步，我们期待您的路线:)");

        // 改变底部按钮
        mStepButton = (Button) findViewById(R.id.step_btn);
        mStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStepButton.getText() == getText(R.string.add_start)) {
                    if (sum == 0) {
                        Toast.makeText(MapSetUp.this, "必须制定一个起点", Toast.LENGTH_LONG).show();
                    } else {
                        mStepButton.setText(getText(R.string.add_btw));
                        mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_btw), null);
                        mIndicator.setPositionAndOffset(1, 0);
                        flag = 1;
                        sum = 0;
                    }
                } else if (mStepButton.getText() == getText(R.string.add_btw)) {
                    mStepButton.setText(getText(R.string.add_final));
                    mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_final), null);
                    mIndicator.setPositionAndOffset(2, 0);
                    flag = 2;
                    sum = 0;
                } else if (mStepButton.getText() == getText(R.string.add_final)) {
                    if (sum == 0 ) {
                        Toast.makeText(MapSetUp.this, "必须制定一个终点", Toast.LENGTH_LONG).show();
                    } else {
                        mStepButton.setText(getText(R.string.add_done));
                        mStepButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_add_done), null);
                        mStepButton.setBackground(getDrawable(R.drawable.add_done_button));
                        mIndicator.setBackground(getDrawable(R.drawable.add_done_button));
                        mIndicator.setPositionAndOffset(3, 0);
                        flag = 3;
                        sum = 0;

                        double left = 200;
                        double right = 0;
                        double top = 0;
                        double bottom = 200;

                        for (com.gyh.login.db.Marker marker : markers) {
                            if (marker.getLongitude() > right) {
                                right = marker.getLongitude();
                            }

                            if (marker.getLongitude() < left) {
                                left = marker.getLongitude();
                            }

                            if (marker.getLatitude() > top) {
                                top = marker.getLatitude();
                            }

                            if (marker.getLatitude() < bottom) {
                                bottom = marker.getLatitude();
                            }
                        }

                        // left 下纬度 right 上纬度 bottom 左经度 top 右经度
                        // Log.d("Test", "left: " + left + " right: " + right + " bottom: " + bottom + " top: " + top);

                        double f = top - bottom;
                        double tmpF = 1;
                        if (f < 1) {
                            while (f <= tmpF) {
                                tmpF /= 10;
                            }
                        } else if (f >= 1) {
                            while (f >= tmpF) {
                                tmpF *= 10;
                            }
                        }

                        // 优化图片放缩
                        tencentMap.zoomToSpan(new LatLng(left, bottom - tmpF*5), new LatLng(right, top + tmpF*5));
                    }
                } else if (mStepButton.getText() == getText(R.string.add_done)) {
                    mIndicator.setPositionAndOffset(4, 0);

                    // 打开总结页面
                    Intent intent = new Intent(MapSetUp.this, MapSet.class);
                    startActivityForResult(intent, 2);
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

        // 地图点击增加标记
        tencentMap.setOnMapClickListener(new TencentMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (flag == 0 && sum == 0) {
                    Intent intent = new Intent(MapSetUp.this, MarkerSet.class);
                    intent.putExtra("edit", false);
                    intent.putExtra("latitude", latLng.getLatitude());
                    intent.putExtra("longitude", latLng.getLongitude());
                    startActivityForResult(intent, 0);
                } else if (flag == 1) {
                    Intent intent = new Intent(MapSetUp.this, MarkerSet.class);
                    intent.putExtra("edit", false);
                    intent.putExtra("latitude", latLng.getLatitude());
                    intent.putExtra("longitude", latLng.getLongitude());
                    startActivityForResult(intent, 0);
                } else if (flag == 2 && sum == 0) {
                    Intent intent = new Intent(MapSetUp.this, MarkerSet.class);
                    intent.putExtra("edit", false);
                    intent.putExtra("latitude", latLng.getLatitude());
                    intent.putExtra("longitude", latLng.getLongitude());
                    startActivityForResult(intent, 0);
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

        // 详细信息框的点击事件，编辑信息
        tencentMap.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                cilckMarker = marker;
                Intent intent = new Intent(MapSetUp.this, MarkerSet.class);

                intent.putExtra("edit", true);

                // 分隔出名称与介绍
                String titleSnippet = marker.getTitle();
                String[] temp = titleSnippet.split(" - ");
                intent.putExtra("title", temp[0]);
                intent.putExtra("snippet", temp[1]);
                // 分隔出方式与时间
                String methodTime = marker.getSnippet();
                String[] tep = methodTime.split(" ");
                intent.putExtra("method", tep[0]);
                intent.putExtra("time", tep[1].substring(1, tep[1].length() - 1));

                intent.putExtra("latitude", marker.getPosition().getLatitude());
                intent.putExtra("longitude", marker.getPosition().getLongitude());

                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode != 2) {
            String title = data.getStringExtra("title");
            String intro = data.getStringExtra("intro");
            String method = data.getStringExtra("method");
            String tag = data.getStringExtra("tag");
            String time = data.getStringExtra("time");

            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);

            switch (requestCode) {
                case 0:
                    setMarker(new LatLng(latitude, longitude), title, intro, method, time, tag);
                    sum += 1;
                    break;
                case 1:
                    cilckMarker.setTitle(title + " - " + intro);
                    cilckMarker.setSnippet(method + " (" + time + ")");

                    // 更新相应的markers数组值
                    for (com.gyh.login.db.Marker marker : markers) {
                        if (marker.getLatitude() == cilckMarker.getPosition().getLongitude()
                                && marker.getLongitude() == cilckMarker.getPosition().getLatitude()) {
                            marker.setTitle(title);
                            marker.setSnippet(intro);
                            marker.setMethod(method);
                            marker.setTime(time);
                            marker.setTag(tag);
                        }
                    }
                    break;
            }
        } else if (resultCode == RESULT_OK && requestCode == 2) {
            String routeTitle = data.getStringExtra("routeTitle");
            String routeIntro = data.getStringExtra("routeIntro");
            saveRoute(routeTitle, routeIntro);
            finish();
        }
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("开始制作你的路线吧！");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void setMarker(LatLng latLng, String title, String snippet, String method, String time, String tag) {
        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title + " - " + snippet)
                .anchor(.5f, .5f)
                .snippet(method + " (" + time + ")")
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(false));
        marker.showInfoWindow();
        com.gyh.login.db.Marker m = new com.gyh.login.db.Marker(latLng.getLatitude(), latLng.getLongitude(), title, snippet, method, time, tag);
        markers.add(m);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Route");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    private void saveRoute(String routeTitle, String routeIntro) {
        Route route = new Route(R.drawable.route_1, routeTitle, routeIntro, 300, user.getId(), tencentMap.getZoomLevel());
        route.setId(allRoutes.size() + 1);
        route.setMarkers(markers);

        // 截图
        tencentMap.getScreenShot(new TencentMap.OnScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                saveImageToGallery(MapSetUp.this, bitmap);
            }
        });

        // 更新制作者制作路线信息
        User tmp = new User();
        tmp.setMakeRoutes(user.getMakeRoutes() + "," + route.getId());
        tmp.update(user.getId());

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
    }
}
