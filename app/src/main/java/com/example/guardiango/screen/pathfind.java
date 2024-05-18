package com.example.guardiango.screen;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.guardiango.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.List;

public class pathfind extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private TextView startLocationText, destinationLocationText;

    // Geocoding 및 Directions API 요청에 사용할 API 키
    private static final String API_KEY = "YOUR_API_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathfind);

        mapView = findViewById(R.id.mapView);
        startLocationText = findViewById(R.id.startLocationText);
        destinationLocationText = findViewById(R.id.destinationLocationText);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // 인텐트에서 데이터 가져오기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String startLocation = extras.getString("startLocation");
            String destinationLocation = extras.getString("destinationLocation");
            startLocationText.setText(startLocation);
            destinationLocationText.setText(destinationLocation);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear(); // 이전 마커나 경로가 있을 경우 클리어

        String startAddress = startLocationText.getText().toString();
        String destinationAddress = destinationLocationText.getText().toString();

        new GeocodeAsyncTask().execute(startAddress, destinationAddress);
    }

    private class GeocodeAsyncTask extends AsyncTask<String, Void, List<LatLng>> {
        protected List<LatLng> doInBackground(String... addresses) {
            List<LatLng> latLngs = new ArrayList<>();
            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

            try {
                for (String address : addresses) {
                    GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
                    if (results.length > 0) {
                        double lat = results[0].geometry.location.lat;
                        double lng = results[0].geometry.location.lng;
                        latLngs.add(new LatLng(lat, lng));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                context.shutdown();
            }

            return latLngs;
        }

        protected void onPostExecute(List<LatLng> latLngs) {
            if (latLngs.size() >= 2) {
                LatLng startLatLng = latLngs.get(0);
                LatLng destinationLatLng = latLngs.get(1);

                // 마커 추가
                map.addMarker(new MarkerOptions().position(startLatLng).title("Start"));
                map.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));

                // 카메라 이동
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 10));

                // 경로 그리기
                new RouteDrawingAsyncTask().execute(startLatLng, destinationLatLng);
            }
        }
    }

    private class RouteDrawingAsyncTask extends AsyncTask<LatLng, Void, PolylineOptions> {
        protected PolylineOptions doInBackground(LatLng... latLngs) {
            LatLng start = latLngs[0];
            LatLng end = latLngs[1];
            PolylineOptions options = new PolylineOptions();

            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
            try {
                DirectionsResult result = DirectionsApi.newRequest(context)
                        .origin(new com.google.maps.model.LatLng(start.latitude, start.longitude))
                        .destination(new com.google.maps.model.LatLng(end.latitude, end.longitude))
                        .await();

                for (DirectionsRoute route : result.routes) {
                    for (DirectionsLeg leg : route.legs) {
                        for (DirectionsStep step : leg.steps) {
                            List<LatLng> decodedPath = PolyUtil.decode(step.polyline.getEncodedPath());
                            for (LatLng pathLatLng : decodedPath) {
                                options.add(new com.google.android.gms.maps.model.LatLng(pathLatLng.latitude, pathLatLng.longitude));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                context.shutdown();
            }

            return options.width(10).color(Color.RED);
        }

        protected void onPostExecute(PolylineOptions options) {
            if (options != null) {
                map.addPolyline(options);
            }
        }
    }
}

