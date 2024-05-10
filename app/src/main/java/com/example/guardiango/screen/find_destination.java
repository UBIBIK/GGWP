package com.example.guardiango.screen;

import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardiango.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class find_destination extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private EditText destinationLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_destination);

        EditText startLocation = findViewById(R.id.startLocation);
        Intent intent = getIntent();
        startLocation.setText(intent.getStringExtra("location"));

        destinationLocation = findViewById(R.id.destinationLocation);

        // Places 초기화
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAB_0ZJm5BrHx1bbpOwSjmc-dXvglILKNI");
        }

        destinationLocation.setFocusable(false);
        destinationLocation.setOnClickListener(v -> startAutocompleteActivity());
    }

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                destinationLocation.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // Operation was canceled.
            }
        }
    }
}
