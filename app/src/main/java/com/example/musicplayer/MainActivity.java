package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static int ontTimeOnly = 0;
    Button playBtn, rewindBtn, forwardBtn;
    TextView timeLeftText, timeRightText, songTitle;
    SeekBar seekBar;
    // Media Player
    MediaPlayer mediaPlayer;
    // Handlers
    Handler handler = new Handler();
    // Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    //     Creating a runnable
    private Runnable updateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            timeLeftText.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playBtn = findViewById(R.id.playBtn);
        rewindBtn = findViewById(R.id.rewindBtn);
        forwardBtn = findViewById(R.id.fowardBtn);
        timeLeftText = findViewById(R.id.timeLeftText);
        timeRightText = findViewById(R.id.timeRightText);
        songTitle = findViewById(R.id.songTitle);
        seekBar = findViewById(R.id.seekBar);


        // media player
        mediaPlayer = MediaPlayer.create(this, R.raw.test);
        seekBar.setClickable(false);
        songTitle.setText(getResources().getIdentifier(
                "test",
                "raw",
                getPackageName()
        ));

        playBtn.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playBtn.setBackgroundResource(R.drawable.play_circle);
            } else {
                mediaPlayer.start();
                playMusic();
                playBtn.setBackgroundResource(R.drawable.pause_circle);
            }
        });

        forwardBtn.setOnClickListener(v -> {
            int temp = (int) startTime;
            System.out.println(startTime);
            if ((temp + forwardTime) <= finalTime) {
                startTime = startTime + forwardTime;
                System.out.println(startTime);
                mediaPlayer.seekTo((int) startTime);
            } else {
                Toast.makeText(MainActivity.this, "Cant forward anymore", Toast.LENGTH_SHORT).show();
            }
        });

        rewindBtn.setOnClickListener(v -> {
            int temp = (int) startTime;
            if ((temp - backwardTime) > 0) {
                startTime = startTime - backwardTime;
                mediaPlayer.seekTo((int) startTime);
            } else {
                Toast.makeText(MainActivity.this, "Cant backward anymore", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void playMusic() {
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (ontTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            ontTimeOnly = 1;
        }
        timeRightText.setText(String.format(
                "%d : %d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
        ));
        seekBar.setProgress((int) startTime);
        handler.postDelayed(updateSongTime, 100);
    }
}