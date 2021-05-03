package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.util.Locale.getDefault;

public class MainActivity extends AppCompatActivity {
    enum BUTTON_TEXT {
        START("スタート"),
        PAUSE("一時停止");

        final String text;

        BUTTON_TEXT(String text) {
            this.text = text;
        }

        public String getText() { return text; }
    }

    enum STATUS {
        SETTING,
        PAUSE,
        RUNNING
    }

    private static final long TIME_INTERVAL = 1000;  // タイマーをセットする時間 10秒
    private long timeLeftInMillis = 0;
    private boolean isTimerRunning;  // タイマー起動中フラグ

    private TextView timerView;
    private Button buttonStartPause;
    private Button buttonStop;
    private CountDownTimer countDownTimer;
    private STATUS status = STATUS.SETTING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerView = findViewById(R.id.timer);
        buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStop = findViewById(R.id.buttonStop);

        // ボタン名設定
        buttonStartPause.setText(BUTTON_TEXT.START.getText());

        // イベント登録
        findViewById(R.id.buttonDel).setOnClickListener(this::onClickDel);
        findViewById(R.id.button1).setOnClickListener(this::onClickNum);
        findViewById(R.id.button2).setOnClickListener(this::onClickNum);
        findViewById(R.id.button3).setOnClickListener(this::onClickNum);
        findViewById(R.id.button4).setOnClickListener(this::onClickNum);
        findViewById(R.id.button5).setOnClickListener(this::onClickNum);
        findViewById(R.id.button6).setOnClickListener(this::onClickNum);
        findViewById(R.id.button7).setOnClickListener(this::onClickNum);
        findViewById(R.id.button8).setOnClickListener(this::onClickNum);
        findViewById(R.id.button9).setOnClickListener(this::onClickNum);
        findViewById(R.id.button10).setOnClickListener(this::onClickNum);
        buttonStartPause.setOnClickListener(this::onClick);
        buttonStop.setOnClickListener(v -> resetTimer());

        updateCountDownText();

        status = STATUS.SETTING;
    }

    // Delボタン押した時の処理
    private void onClickDel(View v) {
        if(status != STATUS.SETTING) return;

        timeLeftInMillis = timeLeftInMillis / 10;

        showCountDownText();
    }

    // 数字ボタン押した時の処理
    private void onClickNum(View v) {
        if(status != STATUS.SETTING) return;
        if(timeLeftInMillis * 10 > 1000000)  return;

        if(v != null) {
            if(     v.getId() == R.id.button1) addTime(1);
            else if(v.getId() == R.id.button2) addTime(2);
            else if(v.getId() == R.id.button3) addTime(3);
            else if(v.getId() == R.id.button4) addTime(4);
            else if(v.getId() == R.id.button5) addTime(5);
            else if(v.getId() == R.id.button6) addTime(6);
            else if(v.getId() == R.id.button7) addTime(7);
            else if(v.getId() == R.id.button8) addTime(8);
            else if(v.getId() == R.id.button9) addTime(9);
            else if(v.getId() == R.id.button10) addTime(0);
        }
    }

    // 値をセットする
    private void addTime(int n) {
        timeLeftInMillis = timeLeftInMillis * 10 + n;
        showCountDownText();
    }

    // 設定時間表示(ボタン押したときの処理)
    private void showCountDownText() {
        int h = (int)( timeLeftInMillis / 10000);
        int m = (int)((timeLeftInMillis % 10000) / 100);
        int s = (int)( timeLeftInMillis % 100);

        timerView.setText(String.format(getDefault(), "%02d H %02d M %02d S", h, m, s));
    }

    // スタート・ポーズボタン押した時の処理
    private void onClick(View v) {
        System.out.println(isTimerRunning);

        if (isTimerRunning) {
            pauseTimer();
            status = STATUS.PAUSE;
        } else {
            startTimer();
            status = STATUS.RUNNING;
        }
    }

    // タイマー実行
    private void startTimer() {
        // 初回のみ設定
        if(status == STATUS.SETTING) {
            // 入力された値を時間に直す
            int h = (int) (timeLeftInMillis / 10000);
            int m = (int) ((timeLeftInMillis % 10000) / 100);
            int s = (int) timeLeftInMillis % 100;

            if (s >= 60) {
                m++;
                s = s - 60;
            }
            if (m >= 60) {
                h++;
                m = m - 60;
            }
            if (h >= 100) {
                h = 99;
                m = 59;
                s = 59;
            }

            timeLeftInMillis = (h * 3600) + (m * 60) + s;
            System.out.println("timeLeftInMillis=" + timeLeftInMillis);
            System.out.println("time = " + h + ":" + m + ":" + s);

            timeLeftInMillis = timeLeftInMillis * TIME_INTERVAL;
        }

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
                buttonStop.setVisibility(View.VISIBLE);
                // 00:00表示
                timeLeftInMillis = 0;
                updateCountDownText();
                status = STATUS.SETTING;
            }
        }.start();

        isTimerRunning = true;
        buttonStartPause.setText(BUTTON_TEXT.PAUSE.getText());
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
        status = STATUS.PAUSE;
    }

    // リセット
    private void resetTimer() {
        timeLeftInMillis = 0;
        updateCountDownText();
        buttonStartPause.setVisibility(View.VISIBLE);
        buttonStop.setVisibility(View.VISIBLE);
        status = STATUS.SETTING;
    }

    // 動作中の時刻の表示
    private void updateCountDownText() {
        // ミリ秒を時間に変換
        int h = (int)(timeLeftInMillis / (TIME_INTERVAL * 60 * 60));
        int m = (int)(timeLeftInMillis / (TIME_INTERVAL * 60) % 60);
        int s = (int)((timeLeftInMillis / TIME_INTERVAL) % 60);

        String str = String.format(getDefault(), "%02d H %02d M %02d S", h, m, s);

        timerView.setText(str);
    }
}
