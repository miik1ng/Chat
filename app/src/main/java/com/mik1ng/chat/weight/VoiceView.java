package com.mik1ng.chat.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mik1ng.chat.R;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
@SuppressLint("HandlerLeak")
public class VoiceView extends View {

    private float width;
    private float height;
    private float margin;
    private int zoom = 30;

    private final int START = 0;
    private final int INTERVAL = 1;
    private final int FINISHED = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (onPressedListener != null) {
                switch (msg.arg1) {
                    case START:
                        boolean b = onPressedListener.onStart();
                        if (!b) {
                            stopTimer();
                        }
                        break;
                    case INTERVAL:
                        onPressedListener.onInterval((float) msg.obj);
                        break;
                    case FINISHED:
                        onPressedListener.onFinished();
                        break;
                }
            }
        }
    };

    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = resolveSize(widthSize, widthMeasureSpec);
        height = resolveSize(heightSize, heightMeasureSpec);

        setMeasuredDimension((int) width, (int) height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(0, 0, width, height, bgPaint);

        margin = width / zoom;
        Shader shader = new RadialGradient(width / 2, height / 2, width / 2 - margin, Color.parseColor("#83C1FF"), Color.parseColor("#2793FF"), Shader.TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawOval(margin, margin, width - margin, height - margin, paint);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_send_voice);
        RectF rectF = new RectF();
        rectF.left = width / 4 + margin / 2;
        rectF.top = height / 4 + margin / 2;
        rectF.right = width * 3 / 4 - margin / 2;
        rectF.bottom = height * 3 / 4 - margin / 2;
        canvas.drawBitmap(bitmap, null, rectF, bgPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                startTimer();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isPressed = false;
                stopTimer();
                return true;
        }

        return super.onTouchEvent(event);
    }

    private Paint bgPaint;
    private Paint paint;

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#83C1FF"));

        paint = new Paint();
    }

    private boolean isPressed = false;
    private Timer timer;
    private int period = 1000;        //时间间隔
    private float time;
    private float maxTime = 10000;

    private void startTimer() {
        if (timer == null) {
            sendMessage(START, time);
            zoom = 10;
            invalidate();
            time = 0;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isPressed) {
                        time = time + period * 0.001f;
                        sendMessage(INTERVAL, time);
                        if (time >= maxTime * 0.001f) {
                            stopTimer();
                        }
                    } else {
                        stopTimer();
                    }
                }
            }, period, period);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            time = 0;
            zoom = 30;
            invalidate();
            sendMessage(FINISHED, time);
        }
    }

    private void sendMessage(int state, float time) {
        Message message = new Message();
        message.obj = time;
        message.arg1 = state;
        handler.sendMessage(message);
    }

    private OnPressedListener onPressedListener;

    public void setOnPressedListener(OnPressedListener listener) {
        this.onPressedListener = listener;
    }

    public interface OnPressedListener{
        boolean onStart();

        void onInterval(float time);

        void onFinished();
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
}
