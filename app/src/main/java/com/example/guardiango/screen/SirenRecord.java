package com.example.guardiango.screen;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.guardiango.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SirenRecord extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    private MediaRecorder mediaRecorder;
    private Uri fileUri;
    private boolean isRecording = false;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String TAG = "SirenRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siren_record);

        Button buttonPlayStop = findViewById(R.id.Siren);
        Button buttonRecord = findViewById(R.id.Record);
        Button back = findViewById(R.id.siren_record_back);

        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer.create(this, R.raw.siren);

        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    isPlaying = false;
                    buttonPlayStop.setText("사이렌 재생");
                } else {
                    mediaPlayer.start();
                    isPlaying = true;
                    buttonPlayStop.setText("사이렌 정지");
                }
            }
        });

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRecording();
                }
                else {
                    stopRecording();
                }
            }
        });

        // 앱 실행 시 권한 확인
        if (!checkPermission()) {
            requestPermission();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startRecording() {
        try {
            fileUri = createFileUri(); // 녹음 파일의 URI를 생성합니다.
            setupMediaRecorder(); // MediaRecorder를 설정합니다.

            // 녹음을 준비하고 시작합니다.
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true; // 녹음 상태를 true로 변경합니다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MediaRecorder를 설정하는 메소드입니다.
    private void setupMediaRecorder() throws IOException {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 소스 설정
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 출력 형식 설정
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 오디오 인코더 설정

        // ParcelFileDescriptor를 통해 얻은 FileDescriptor를 사용
        ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(fileUri, "w");
        if (pfd == null) {
            throw new IOException("Cannot open file descriptor for URI: " + fileUri);
        }
        mediaRecorder.setOutputFile(pfd.getFileDescriptor());
    }


    // MediaStore를 사용하여 녹음 파일의 URI를 생성하는 메소드입니다.
    private Uri createFileUri() throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, createFileName()); // 파일의 이름을 설정합니다.
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/3gpp"); // 파일의 MIME 타입을 설정합니다.
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/RecordExample"); // 파일의 저장 경로를 설정합니다.

        // 생성된 정보를 바탕으로 파일 URI를 MediaStore에 추가합니다.
        Uri uri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            throw new IOException("Failed to create new MediaStore record.");
        }
        return uri;
    }

    // 현재 시간을 기반으로 파일 이름을 생성하는 메소드입니다.
    private String createFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "REC_" + timeStamp + ".3gpp";
    }

    // 파일 URI에 대한 파일 디스크립터를 반환하는 메소드입니다.
    private ParcelFileDescriptor getFileDescriptor(Uri uri) throws IOException {
        ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
        if (pfd == null) {
            throw new IOException("Cannot open file descriptor for URI: " + uri);
        }
        return pfd;
    }

    // 녹음을 중지하고 MediaRecorder 리소스를 해제하는 메소드입니다.
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop(); // 녹음을 중지합니다.
            mediaRecorder.release(); // MediaRecorder 리소스를 해제합니다.
            mediaRecorder = null; // MediaRecorder 객체를 null로 설정합니다.
            isRecording = false; // 녹음 상태를 false로 변경합니다.
        }
    }



    private boolean checkPermission() {
        int recordPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return recordPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

}