package com.mik1ng.chat.ui.map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.mik1ng.chat.R;
import com.mik1ng.chat.event.SendLocationEvent;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DensityUtils;
import com.mik1ng.chat.util.DeviceUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class ShowLocationMapActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageView ivLocation;
    private MyLocationStyle myLocationStyle;
    private UiSettings uiSettings;

    private int screenWidth;
    private int screenHeight;

    //我的定位
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        setContentView(R.layout.activity_show_location_map);

        screenWidth = DensityUtils.getScreenWidth(ShowLocationMapActivity.this);
        screenHeight = DensityUtils.getScreenHeight(ShowLocationMapActivity.this);

        setMapUI(savedInstanceState);

        //初始化我的定位
        try {
            aMapLocationClient = new AMapLocationClient(getApplicationContext());
            aMapLocationClient.setLocationListener(aMapLocationListener);

            aMapLocationClientOption = new AMapLocationClientOption();
            //设置高精度定位
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置单次定位
            aMapLocationClientOption.setOnceLocationLatest(true);
            //返回地址描述
            aMapLocationClientOption.setNeedAddress(true);
            aMapLocationClient.setLocationOption(aMapLocationClientOption);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            double lat = bundle.getDouble(Constant.BUNDLE_LAT);
            double log = bundle.getDouble(Constant.BUNDLE_LOG);
            addMark(lat, log);
        }

        //取消
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //定位
        findViewById(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMapLocationClient.startLocation();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 我的定位监听
     */
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            updateMapLocation(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            aMapLocationClient.stopLocation();
        }
    };

    /**
     * 设置地图UI
     */
    private void setMapUI(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        myLocationStyle = new MyLocationStyle();
        //我的位置刷新间隔 ms
        myLocationStyle.interval(1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.bg_map_my_location));
        myLocationStyle.radiusFillColor(Color.parseColor("#4D2793FF"));
        myLocationStyle.strokeColor(Color.parseColor("#4D2793FF"));
        mapView.getMap().setMyLocationStyle(myLocationStyle);
        mapView.getMap().setMyLocationEnabled(true);

        uiSettings = mapView.getMap().getUiSettings();
        //不显示缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //显示我的位置按钮
        uiSettings.setMyLocationButtonEnabled(false);
        //禁止地图旋转
        uiSettings.setRotateGesturesEnabled(false);
        //我的位置按钮可点击
        mapView.getMap().setMyLocationEnabled(true);
    }

    /**
     * 更新地图定位点
     * @param lat
     * @param log
     */
    private void updateMapLocation(double lat, double log) {
        LatLng latLng = new LatLng(lat, log);
        Point point = mapView.getMap().getProjection().toScreenLocation(latLng);

        float x = point.x;
        float y = point.y;
        float offX = x - screenWidth / 2;
        float offY = y - screenHeight / 2;

        mapView.getMap().animateCamera(CameraUpdateFactory.scrollBy(offX, offY), 300, null);
    }

    private void addMark(double lat, double log) {
        LatLng latLng = new LatLng(lat, log);
        mapView.getMap().addMarker(new MarkerOptions().position(latLng));
    }
}
