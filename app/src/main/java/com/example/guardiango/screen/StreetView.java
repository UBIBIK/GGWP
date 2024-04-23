package com.example.guardiango.screen;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view);

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        // 스트리트뷰 파노라마 프래그먼트 설정
        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.street_view);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(panorama -> {
            panorama.setPosition(new LatLng(latitude, longitude));
        });

        // 뒤로가기 버튼 설정
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // 현재 액티비티 종료
            finish();
        });
    }
}
