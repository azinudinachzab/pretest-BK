package io.github.leonard04.recorder.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import io.github.leonard04.recorder.MainActivity;
import io.github.leonard04.recorder.R;
import io.github.leonard04.recorder.utils.CustomList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileFragment extends Fragment {

    MediaPlayer mediaPlayer;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;
    String dirpath;
    public FileFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_file, container, false);
        listView = v.findViewById(R.id.list1);
        getAudio();
        return v;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getAudio(){
        arrayList = new ArrayList<>();
        dirpath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+"/AndroidBkTest/";
        File dir = new File(dirpath);
        File[] files = dir.listFiles();
        final String[] filenames = new String[files.length];
        Integer[] imgid = new Integer[files.length];

        for (int i = 0; i < filenames.length; i++) {
            filenames[i] = files[i].getName();
            imgid[i] = R.drawable.ic_audio;
        }
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1, filenames);
        CustomList listAdapter = new CustomList(getContext(),filenames,imgid);
        listView.setAdapter(listAdapter);
        listView.setSelected(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] dialogItem = {"Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Options");
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                String path = dirpath+filenames[position];
                                new File(path).delete();
                                Refresh();
                                Toast.makeText(getContext(),"File's deleted",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayer = new MediaPlayer();
                String mediaPath = dirpath+filenames[position];
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri uri = Uri.parse(mediaPath);
                try {
                    mediaPlayer.setDataSource(getContext(),uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    listView.setEnabled(false);
                    Toast.makeText(getContext(),"Playback Started",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        listView.setEnabled(true);
                        mediaPlayer.release();
                        mediaPlayer = null;
                        Toast.makeText(getContext(),"Playback Finished",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Refresh() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
        getActivity().finish();
    }
}
