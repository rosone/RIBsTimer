package com.gogovan.test.app.timetask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.shinetechina.demo.R;
import com.uber.rib.core.Initializer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * Top level view for {@link TimeTaskBuilder.TimeTaskScope}.
 */
class TimeTaskView extends LinearLayout implements TimeTaskInteractor.TimeTaskPresenter {

    public TimeTaskView(Context context) {
        this(context, null);
    }

    public TimeTaskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTaskView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private EditText mTaskNameEditText;
    private TextView mTimeTextView;

    private Button mStartButton;

    @Initializer
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTaskNameEditText = findViewById(R.id.task_name_edit);
        mTimeTextView = findViewById(R.id.time_count_text);
        mStartButton = findViewById(R.id.start_button);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.e("w", "ontouch = " + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public Observable<Object> upRequest() {

        return RxView.clicks(findViewById(R.id.up_button))
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> upLongRequest() {
        return RxView.longClicks(findViewById(R.id.up_button))
                .subscribeOn(AndroidSchedulers
                .mainThread());
    }

    public Observable<Object> upLongCancelRequest() {
        return RxView.touches(findViewById(R.id.up_button), new Predicate<MotionEvent>() {
            @Override
            public boolean test(MotionEvent motionEvent) throws Exception {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        }).map(e -> false);
    }


    @Override
    public Observable downRequest() {


        return RxView.clicks(findViewById(R.id.down_button))
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public String getTimeTaskName() {
        return mTaskNameEditText.getText().toString().trim();
    }

    @Override
    public Observable<Object> startRequest() {
        return RxView.clicks(mStartButton)
                .throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setTimeText(String text) {

        mTimeTextView.setText(text);
    }

    @Override
    public void chageStartButtonStatus(TimeTaskInteractor.TimerStatus timerStatus) {

        if (timerStatus == TimeTaskInteractor.TimerStatus.Idle) {

            mStartButton.setText(R.string.str_start);
        } else if (timerStatus == TimeTaskInteractor.TimerStatus.Running) {
            mStartButton.setText(R.string.str_stop);

        } else if (timerStatus == TimeTaskInteractor.TimerStatus.Pause) {

            mStartButton.setText(R.string.str_pause);

        }
    }

    @Override
    public void resetTimeTask() {

        mTaskNameEditText.setText("");

        chageStartButtonStatus(TimeTaskInteractor.TimerStatus.Idle);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
