package com.mik1ng.chat.ui.map;

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
import com.mik1ng.chat.util.DensityUtils;
import com.mik1ng.chat.util.DeviceUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageView ivLocation;
    private MyLocationStyle myLocationStyle;
    private UiSettings uiSettings;

    private int screenWidth;
    private int screenHeight;

    private Object[] result;

    //地址搜索
    private GeocodeSearch geocodeSearch;

    //我的定位
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        setContentView(R.layout.activity_map);

        screenWidth = DensityUtils.getScreenWidth(MapActivity.this);
        screenHeight = DensityUtils.getScreenHeight(MapActivity.this);

        setMapUI(savedInstanceState);
        setSearchUI();

        //初始化搜索功能
        try {
            geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        } catch (AMapException e) {
            e.printStackTrace();
        }

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

        //取消
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //发送
        findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result != null) {
                    finish();
                    EventBus.getDefault().post(new SendLocationEvent(result));
                }
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
     * 地址搜索返回经纬度
     */
    GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (i == 1000) {
                Object[] res = getNameAndAddress(regeocodeResult);
                updateResultUI((String) res[0], (String) res[1]);
                result = res;
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            if (i == 1000) {
                List<GeocodeAddress> addresses = geocodeResult.getGeocodeAddressList();
                if (addresses.size() > 0) {
                    GeocodeAddress address = addresses.get(0);

                    updateMapLocation(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude());
                    isScroll = true;
                }
            }
        }
    };

    /**
     * 我的定位监听
     */
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            updateMapLocation(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            isScroll = true;
            aMapLocationClient.stopLocation();
        }
    };

    private boolean isScroll = false;

    /**
     * 地图滑动监听
     */
    AMap.OnCameraChangeListener onCameraChangeListener = new AMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            if (isScroll) {
                isScroll = false;
                Point point = new Point();
                point.x = screenWidth / 2;
                point.y = screenHeight / 3;
                LatLng latLng = mapView.getMap().getProjection().fromScreenLocation(point);

                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
            }
        }
    };

    private View.OnApplyWindowInsetsListener applyWindowInsetsListener = new View.OnApplyWindowInsetsListener() {
        @Override
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            int height = DeviceUtils.getKeyboardHeight(MapActivity.this);
            if (height == 0) {
                etKey.clearFocus();
            }

            layoutSearch.setPadding(0, 0, 0, height);

            return windowInsets;
        }
    };

    /**
     * 软键盘按钮
     */
    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (TextUtils.isEmpty(etKey.getText().toString())) {
                return true;
            }else {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    GeocodeQuery query = new GeocodeQuery(etKey.getText().toString(), "");
                    geocodeSearch.getFromLocationNameAsyn(query);
                }
                return false;
            }
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

        ImageView ivCoordinate = (ImageView) findViewById(R.id.iv_coordinate);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivCoordinate.getLayoutParams();
        layoutParams.topMargin = screenHeight / 3 - layoutParams.height;
        ivCoordinate.setVisibility(View.VISIBLE);

        mapView.getMap().setOnCameraChangeListener(onCameraChangeListener);
        mapView.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isScroll = true;
            }
        });
    }

    private LinearLayout layoutSearch,layoutResult;
    private EditText etKey;
    private TextView tvSearchName, tvSearchAddress;

    /**
     * 设置搜索功能UI
     */
    private void setSearchUI() {
        layoutSearch = (LinearLayout) findViewById(R.id.layout_search);
        layoutResult = (LinearLayout) findViewById(R.id.layout_result);
        etKey = (EditText) findViewById(R.id.et_key);
        etKey.setOnEditorActionListener(onEditorActionListener);
        tvSearchName = (TextView) findViewById(R.id.tv_search_name);
        tvSearchAddress = (TextView) findViewById(R.id.tv_search_address);
        findViewById(R.id.layout_root).setOnApplyWindowInsetsListener(applyWindowInsetsListener);
    }

    /**
     * 更新搜索结果
     * @param name
     * @param address
     */
    private void updateResultUI(String name, String address) {
        layoutResult.setVisibility(View.VISIBLE);
        tvSearchName.setText(name);
        tvSearchAddress.setText(address);
        etKey.setText("");
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
        float offY = y - screenHeight / 3;

        mapView.getMap().animateCamera(CameraUpdateFactory.scrollBy(offX, offY), 300, null);
    }

    private Object[] getNameAndAddress(RegeocodeResult result) {
        String country = result.getRegeocodeAddress().getCountry();
        String province = result.getRegeocodeAddress().getProvince();
        String city = result.getRegeocodeAddress().getCity();
        String district = result.getRegeocodeAddress().getDistrict();
        String township = result.getRegeocodeAddress().getTownship();
        String street = result.getRegeocodeAddress().getStreetNumber().getStreet();
        String number = result.getRegeocodeAddress().getStreetNumber().getNumber();

        StringBuilder address = new StringBuilder();
        address.append(country).append(province).append(city).append(district).append(township).append(street).append(number);

        String name = result.getRegeocodeAddress().getFormatAddress()
                .replace(country, "")
                .replace(province, "")
                .replace(city, "")
                .replace(district, "")
                .replace(township, "")
                .replace(street, "")
                .replace(number, "");

        Object[] res = new Object[4];
        res[0] = name;
        res[1] = address.toString();
        res[2] = result.getRegeocodeQuery().getPoint().getLatitude();
        res[3] = result.getRegeocodeQuery().getPoint().getLongitude();
        return res;
    }
}
