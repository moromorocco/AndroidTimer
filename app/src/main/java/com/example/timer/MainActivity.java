package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    enum BUTTON_TEXT {
        START("スタート"),
        STOP("一時停止"),
        RESET("リセット");

        private String text;

        BUTTON_TEXT(String text) {
            this.text = text;
        }

        public String getText() { return text; }
    }

    private static final long START_TIME = 10000;  // タイマーをセットする時間 10秒
    private static final long TIME_INTERVAL = 1000;  // タイマーをセットする時間 10秒

    private long timeLeftInMillis = START_TIME;
    private boolean isTimerRunning;  // タイマー起動中フラグ

    private TextView timerView;
    private Button buttonStartPause;
    private Button buttonStop;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerView = findViewById(R.id.timer);
        buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStop = findViewById(R.id.buttonStop);

        // ボタン名設定
        buttonStartPause.setText(BUTTON_TEXT.START.getText());
        buttonStop.setText(BUTTON_TEXT.RESET.getText());

        // イベント登録
        buttonStartPause.setOnClickListener(this::onClick);
        buttonStop.setOnClickListener(v -> resetTimer());

        updateCountDownText();
    }

    // スタート・ポーズボタン押した時の処理
    private void onClick(View v) {
        System.out.println(isTimerRunning);

        if (isTimerRunning) {
            pauseTimer();
        } else {
            resetTimer();
            startTimer();
        }
    }

    // タイマー実行
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, TIME_INTERVAL) {
            @Override
            public void onTick(long millisUnitFinished) {
                timeLeftInMillis = millisUnitFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                buttonStartPause.setText(BUTTON_TEXT.START.getText());
                buttonStop.setVisibility(View.INVISIBLE);
                // 00:00表示
                timeLeftInMillis = 0;
                updateCountDownText();
            }
        }.start();

        isTimerRunning = true;
        buttonStartPause.setText(BUTTON_TEXT.STOP.getText());
        buttonStop.setVisibility(View.INVISIBLE);
    }

    // 一時停止
    private void pauseTimer() {
        System.out.println("一時停止処理前：" + isTimerRunning);
        countDownTimer.cancel();
        isTimerRunning = false;

        System.out.println("一時停止処理後：" + isTimerRunning);
        buttonStartPause.setText(BUTTON_TEXT.START.getText());
        buttonStop.setVisibility(View.VISIBLE);
    }

    // リセット
    private void resetTimer() {
        timeLeftInMillis = START_TIME;
        updateCountDownText();
        buttonStartPause.setVisibility(View.VISIBLE);
        buttonStop.setVisibility(View.INVISIBLE);
    }

    // 時刻の表示
    private void updateCountDownText() {
        int m = (int)(timeLeftInMillis / TIME_INTERVAL) / 60;
        int s = (int)(timeLeftInMillis / TIME_INTERVAL) % 60;
        String str = String.format(Locale.getDefault(), "%02d:%02d", m, s);

        timerView.setText(str);
    }
}
