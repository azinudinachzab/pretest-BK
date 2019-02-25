package io.github.leonard04.recorder.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Objects;

import io.github.leonard04.recorder.MainActivity;
import io.github.leonard04.recorder.R;
import io.github.leonard04.recorder.utils.WavRecorder;
import io.github.leonard04.recorder.utils.WavRecorder2;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecorderFragment extends Fragment {
    FloatingActionButton btnRec, btnStop;
    File folder;
    private Chronometer chronometer;
    private boolean running = false;
    long date;
    SimpleDateFormat simpleDateFormat;
    String dateS;
    String pathSave;
    WavRecorder wavRecorder;
    WavRecorder2 wavRecorder2;
    private long pauseOffset;
    int link = 0;

    public RecorderFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recorder, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            link = intent.getIntExtra("srate", 0);
        }

        date = System.currentTimeMillis();
        chronometer = v.findViewById(R.id.chronometer);

        simpleDateFormat = new SimpleDateFormat("MMM_dd_yyyy_hh_mm_ss");
        dateS = simpleDateFormat.format(date);
        folder = new File(Environment.getExternalStorageDirectory() + "/AndroidBkTest");
        //buat folder
        if (!folder.exists()) {
            folder.mkdir();
        }
        pathSave = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/AndroidBkTest/"
                + dateS + "_audio_record.wav";
        wavRecorder = new WavRecorder(pathSave);
        wavRecorder2 = new WavRecorder2(pathSave);

        btnRec = v.findViewById(R.id.btnRec);
        btnStop = v.findViewById(R.id.btnStop);
        btnStop.setVisibility(View.GONE);


        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRec.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
                if (link == 0) {
                    wavRecorder.startRecording();
                    Toast.makeText(v.getContext(), "Recording.. (8kHz)", Toast.LENGTH_SHORT).show();
                } else {
                    wavRecorder2.startRecording();
                    Toast.makeText(v.getContext(), "Recording.. (16kHz)", Toast.LENGTH_SHORT).show();
                }



            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                btnStop.setVisibility(View.GONE);
                btnRec.setVisibility(View.VISIBLE);
                if (running) {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    pauseOffset = 0;
                    running = false;
                }

                if (link == 0) {
                    wavRecorder.stopRecording();
                } else {
                    wavRecorder2.stopRecording();
                }
                Toast.makeText(v.getContext(), "Stopped...", Toast.LENGTH_SHORT).show();
                gotoMainActivity();
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void gotoMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
        getActivity().finish();
    }

}
