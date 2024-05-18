package com.example.guardiango.screen;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class find_destination extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private TextView statusBar;
    private boolean isDestinationSetMode = false;
    private boolean isEditMode = false;
    private Marker destinationMarker;
    private Polyline currentPolyline;
    private List<LatLng> routePoints;
    private List<LatLng> waypoints;
    private static final String API_KEY = "Qo2Dzd0MGI2AyknkLTB8U6jqfAz5UwUA3gaqwxjj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_destination);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        statusBar = findViewById(R.id.statusBar);

        waypoints = new ArrayList<>();

        Button setDestinationButton = findViewById(R.id.setDestinationButton);
        setDestinationButton.setOnClickListener(v -> {
            if (destinationMarker != null) {
                destinationMarker.remove();
            }
            statusBar.setText("목적지의 위치를 터치해주세요");
            isDestinationSetMode = true;
            waypoints.clear(); // Clear waypoints when setting a new destination
        });

        Button editRouteButton = findViewById(R.id.editRouteButton);
        editRouteButton.setOnClickListener(v -> {
            if (currentPolyline != null) {
                isEditMode = !isEditMode;
                statusBar.setText(isEditMode ? "편집 모드 활성화" : "편집 모드 비활성화");
            } else {
                statusBar.setText("먼저 경로를 설정해주세요");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title("출발지")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        googleMap.setOnMapClickListener(latLng -> {
            if (isDestinationSetMode) {
                if (destinationMarker != null) {
                    destinationMarker.remove();
                }
                destinationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("목적지")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                statusBar.setText("경로를 검색중입니다...");
                isDestinationSetMode = false;
                fetchRoute(location, latLng, waypoints);
            } else if (isEditMode) {
                waypoints.add(latLng);
                fetchRoute(new LatLng(latitude, longitude), destinationMarker.getPosition(), waypoints);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchRoute(LatLng start, LatLng end, List<LatLng> waypoints) {
        new AsyncTask<LatLng, Void, PolylineOptions>() {
            protected PolylineOptions doInBackground(LatLng... latLngs) {
                try {
                    URL url = new URL("https://apis.openapi.sk.com/tmap/routes/pedestrian");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("appKey", API_KEY);
                    connection.setDoOutput(true);

                    // 데이터를 POST Request의 Body로 전송
                    String postData = buildPostData(latLngs[0], latLngs[1], waypoints);
                    byte[] postDataBytes = postData.getBytes("UTF-8");
                    connection.getOutputStream().write(postDataBytes);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder jsonStringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonStringBuilder.append(line);
                    }

                    String response = jsonStringBuilder.toString();
                    Log.d("API Response", response); // 로그에 응답 출력
                    return parseRoute(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("API Error", "Error fetching the route", e);
                    return null;
                }
            }

            protected void onPostExecute(PolylineOptions result) {
                if (result != null) {
                    if (currentPolyline != null) {
                        currentPolyline.remove();
                    }
                    currentPolyline = googleMap.addPolyline(result);
                    routePoints = result.getPoints();
                    statusBar.setText("경로 표시 완료");
                } else {
                    statusBar.setText("경로를 불러오는 데 실패했습니다.");
                }
            }
        }.execute(start, end);
    }

    private String buildPostData(LatLng start, LatLng end, List<LatLng> waypoints) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        postData.append("startX=").append(start.longitude)
                .append("&startY=").append(start.latitude)
                .append("&endX=").append(end.longitude)
                .append("&endY=").append(end.latitude)
                .append("&reqCoordType=WGS84GEO")
                .append("&resCoordType=WGS84GEO")
                .append("&startName=").append(URLEncoder.encode("출발지", "UTF-8"))
                .append("&endName=").append(URLEncoder.encode("목적지", "UTF-8"))
                .append("&searchOption=0")  // 경로 탐색 옵션: 0은 추천 경로
                .append("&appKey=").append(API_KEY);

        for (int i = 0; i < waypoints.size(); i++) {
            LatLng waypoint = waypoints.get(i);
            postData.append("&passList=").append(URLEncoder.encode(waypoint.longitude + "," + waypoint.latitude, "UTF-8"));
        }

        return postData.toString();
    }

    private PolylineOptions parseRoute(String json) {
        PolylineOptions options = new PolylineOptions();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        // 경로 데이터는 features 배열 안의 geometry 객체 안에 있음
        JsonArray features = jsonObject.getAsJsonArray("features");
        if (features != null) {
            for (JsonElement featureElement : features) {
                JsonObject feature = featureElement.getAsJsonObject();
                JsonObject geometry = feature.getAsJsonObject("geometry");
                if (geometry != null && "LineString".equals(geometry.get("type").getAsString())) {
                    JsonArray coordinates = geometry.getAsJsonArray("coordinates");
                    if (coordinates != null) {
                        for (JsonElement coordElement : coordinates) {
                            JsonArray coord = coordElement.getAsJsonArray();
                            double lng = coord.get(0).getAsDouble();
                            double lat = coord.get(1).getAsDouble();
                            options.add(new LatLng(lat, lng));
                        }
                    }
                }
            }
        }

        return options.width(10).color(Color.BLUE);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
