package com.example.guardiango.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guardiango.Custom.LocationService;
import com.example.guardiango.R;
import com.example.guardiango.entity.Group;
import com.example.guardiango.entity.LocationData;
import com.example.guardiango.entity.SharedPreferencesGroup;
import com.example.guardiango.entity.SharedPreferencesHelper;
import com.example.guardiango.entity.UserInfo;
import com.example.guardiango.server.RetrofitClient;
import com.example.guardiango.server.UserRetrofitInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class your_new_home extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private SharedPreferencesGroup sharedPreferencesGroup;

    private GoogleMap mMap;

    private static final String TAG = "googlemap";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000; // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // OnRequestPermissionsResultCallback에서 수신된 결과에서 ActivityCompat.OnRequestPermissionsResultCallback를 사용한 퍼미션 요청을 구별하기 위함
    private static final int PERMISSION_REQUEST_CODE = 100;

    // 앱을 실행하기 위해 필요한 퍼미션 정의
    String[] REQUIRED_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };

    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest; // 주의
    private Location location;

    private View mLayout; // snackbar 사용하기 위함.

    private LocationService locationService;


    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_new_home);

        // 권한 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

        //위치 서비스 등록
        locationService = new LocationService(this);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //홈에서 그룹으로 가는 버튼 구현
        Button home_group_button = findViewById(R.id.home_group_button);

        home_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), home_group.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        sharedPreferencesGroup = new SharedPreferencesGroup(this);

        Log.d(TAG, "onMapReady: 들어옴 ");

        mMap = googleMap;

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 체크 로직
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // 사용자의 마지막 위치를 가져와 지도의 초기 위치로 설정
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    } else {
                        // 위치 정보가 없을 경우 기본 위치로 설정
                        setDefaultLocation();
                    }
                });

        //그룹원 위치 표시
        loadGroupMarkers(sharedPreferencesGroup.getGroupInfo());

        // 런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 확인합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            // 2. 이미 퍼미션을 가지고 있다면
            startLocationUpdates(); // 3. 위치 업데이트 실행
        }else{
            // 2. 퍼미션 요청을 허용한 적 없다면 퍼미션 요청하기
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION[0])){
                // 요청 진행하기 전에 퍼미션이 왜필요한지 설명
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 사용자에게 퍼미션 요청, 요청 결과는 onRequestPermisionResult에서 수신
                        ActivityCompat.requestPermissions(your_new_home.this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
                    }
                }).show();
            }else{
                // 사용자가 퍼미션 거부를 한적이 없는 경우 퍼미션 요청을 바로 함.
                // 요청 결과는 onRequestPermissionResult에서 수신된다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d(TAG, "onMapClick: ");
            }
        });

        //마커 클릭 리스너 설정
        mMap.setOnMarkerClickListener(marker -> {
            Log.w("상태메시지", "마커 클릭");
            Map<String, Object> member = (Map<String, Object>) marker.getTag();
            if (member != null) {
                showMemberOptionsDialog(member);
            }
            else {Log.w("상태메시지", "왜 안댐?");}
            return true;
        });

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if(locationList.size() > 0){
                location = locationList.get(locationList.size() -1);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                mCurrentLocation = location;
            }
        }
    };


    private String getCurrentAddress(LatLng currentPosition) {
        // 지오코더 gps를 주소로 변환

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(
                    currentPosition.latitude,
                    currentPosition.longitude,
                    1
            );
        }catch (IOException ioException){
            // 네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return  "지오코더 서비스 사용 불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this,"잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this,"주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }else{
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    private void startLocationUpdates() {
        if(!checkLocationServicesStatus()){
            showDiologForLocationServiceSetting();

        }else{
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ){

                Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if(checkPermission()){
                mMap.setMyLocationEnabled(true);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");

        if(checkPermission()){
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if(mMap!=null){
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mFusedLocationClient != null){
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private boolean checkPermission(){

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ){

            Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
            return false;

        }else{
            return true;
        }

    }

    private void showDiologForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(your_new_home.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. 위치설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setDefaultLocation() {

        // 기본 위치 설정
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);  // 서울 중심부 근처
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15));
    }

    private void loadGroupMarkers(Group group) {
        if (group != null && group.getGroupMember() != null) {
            for (Map<String, Object> member : group.getGroupMember()) {
                Double lat = (Double) member.get("latitude");
                Double lon = (Double) member.get("longitude");
                if (lat != null && lon != null) {
                    LatLng memberLocation = new LatLng(lat, lon);
                    String memberName = (String) member.get("groupMemberName");
                    Marker marker = mMap.addMarker(new MarkerOptions().position(memberLocation).title(memberName));
                    marker.setTag(member);//marker의 정보를 태그로 저장
                }
            }
        } else {
            Log.d(TAG, "No valid group data provided.");
        }
    }

    //마커 클릭 이벤트
    private void showMemberOptionsDialog(Map<String, Object> member) {
        Log.w("상태메시지", "마커 클릭");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String memberName = (String) member.get("groupMemberName");
        builder.setTitle(memberName + "의 정보");

        LatLng location = new LatLng((Double) member.get("latitude"), (Double) member.get("longitude"));
        String address = getAddressFromLocation(location); // 지오코딩을 통해 주소를 가져옵니다.

        String message = "역할: " + member.get("groupRole") + "\n주소: " + address;
        builder.setMessage(message);

        // 경로 설정 버튼
        builder.setPositiveButton("경로 설정", (dialog, id) -> {
            Intent intent = new Intent(your_new_home.this, find_destination.class);
            intent.putExtra("location", address);
            startActivity(intent);
        });

        builder.setNegativeButton("스트리트뷰 확인", (dialog, id) -> {
            Intent intent = new Intent(your_new_home.this, StreetView.class);
            intent.putExtra("latitude", location.latitude);
            intent.putExtra("longitude", location.longitude);
            startActivity(intent);
        });

        // 닫기 버튼
        builder.setNeutralButton("닫기", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 주소를 가져오는 메소드
    private String getAddressFromLocation(LatLng location) {
        // 한국어 주소 결과를 강제로 얻기 위해 Locale.KOREA 사용
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            } else {
                return "주소를 찾을 수 없습니다.";
            }
        } catch (IOException e) {
            Log.e("GeoCoder", "서비스 사용불가", e);
            return "지오코더 서비스 사용 불가";
        } catch (IllegalArgumentException e) {
            Log.e("GeoCoder", "잘못된 GPS 좌표", e);
            return "잘못된 GPS 좌표";
        }
    }

    //메뉴 항목 구현
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            // 새로고침 로직
            sendLocationToServer();
            loadUserGroups();

            // UI 업데이트 로직
            mMap.clear(); // 지도에 그려진 모든 마커 삭제
            loadGroupMarkers(sharedPreferencesGroup.getGroupInfo()); // 마커 다시 로드
            if (currentPosition != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition)); // 현재 위치로 카메라 이동
            }
            return true;
        } else if (id == R.id.menu_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //그룹정보 불러오기
    private void loadUserGroups() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();

        sharedPreferencesGroup = new SharedPreferencesGroup(this);
        String userGroupKey = user.getGroupKey();
        if(userGroupKey == null) {
            Toast.makeText(your_new_home.this, "그룹이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRetrofitInterface loadUserGroups = RetrofitClient.getUserRetrofitInterface();

        Call<Group> call = loadUserGroups.getUserGroups(user);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Group group = response.body();
                    Log.w("그룹 마스터", group.getGroupMaster());
                    String groupName = group.getGroupName();
                    if (groupName != null) { // null 체크 추가
                        sharedPreferencesGroup.saveGroupInfo(group);

                    }
                } else {
                    Toast.makeText(your_new_home.this, "그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Toast.makeText(your_new_home.this, "네트워크 문제로 그룹 정보를 불러오는데 실패했습니다: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //위치 전송
    private void sendLocationToServer() {
        //그룹 정보 가져오기
        sharedPreferencesGroup = new SharedPreferencesGroup(this);

        //사용자 정보 가져와서 위치 정보 삽입
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        UserInfo user = sharedPreferencesHelper.getUserInfo();
        LocationData currentLocation = locationService.getCurrentLocationData();

        user.getLocationInfo().put("latitude", currentLocation.getLatitude());
        user.getLocationInfo().put("longitude", currentLocation.getLongitude());

        if (user != null) {
            // 서버에 위치 정보 전송
            UserRetrofitInterface apiService = RetrofitClient.getUserRetrofitInterface();
            Call<Group> call = apiService.updateLocation(user);
            call.enqueue(new Callback<Group>() {
                @Override
                public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {
                    if (response.isSuccessful()) {
                        sharedPreferencesHelper.saveUserInfo(user);
                        sharedPreferencesGroup.saveGroupInfo(response.body());

                        Toast.makeText(getApplicationContext(), "위치 업데이트 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "위치 업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "위치 정보 없음", Toast.LENGTH_SHORT).show();
        }
    }

    //로그아웃
    private void logout() {
        // 모든 사용자 정보와 그룹 정보 삭제
        if (sharedPreferencesHelper != null) {
            sharedPreferencesHelper.clearUserInfo();
        }
        if (sharedPreferencesGroup != null) {
            sharedPreferencesGroup.clearGroupInfo();
        }

        // 로그아웃 후 로그인 화면으로 이동 또는 앱 재시작
        Intent restartIntent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert restartIntent != null;
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restartIntent);
    }

}