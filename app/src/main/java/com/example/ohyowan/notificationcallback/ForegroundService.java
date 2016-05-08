package com.example.ohyowan.notificationcallback;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.ohyowan.notificationcallback.util.CommonUtil;
import com.example.ohyowan.notificationcallback.util.JSONParserUtil;
import com.example.ohyowan.notificationcallback.util.OkHttpUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ohyowan on 16. 5. 5..
 */
public class ForegroundService extends Service {
    private final String TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = true;                                                //서비스 플래그

    // default dateSet
    private int[] dayArray = {0, 0, 0, 0};                                                          // 시간 기본값 Set
    private NotificationManager mNm;

    // notification
    private Notification.Builder mBuilder;
    private RemoteViews contentView;

    // notification - remoteView Data
    private String ssid;                                                                            // 접속 AP SSID
    private String day;                                                                             // 00일
    private String time;                                                                            // 00시, 00분 00초
    private String kok;                                                                             // 적립콕


    public ForegroundService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        IS_SERVICE_RUNNING = true;
        switch (intent.getAction()) {
            // 포그라운드 서비스 시작
            case Constants.ACTION.STARTFOREGROUND_ACTION:
                Log.i(TAG, "Received Start Foreground Intent ");
                defaultNotification();                                                              //기본노티피케이션 생성
                // setRemainTimer(intent.getStringExtra("mac"));                                    //남은시간 & 콕적립정보 요청

                /* 임시코드 !!*/
                Message msg = serverTimeHandler.obtainMessage();
                msg.obj = "{\"result\":SUCCESS,\"days1\":1,\"days2\":8,\"days3\":30,\"days4\":30,\"totalKokCount\":15}";
                serverTimeHandler.sendMessage(msg);
                ssid = "hwoh";
                /* 임시코드 !!*/

                Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();
                break;

            case Constants.ACTION.PREV_ACTION:
                break;

            case Constants.ACTION.PLAY_ACTION:
                break;

            // 콕 적립 업데이트
            case Constants.ACTION.KOK_SAVE_ACTION:
                kok = intent.getStringExtra("kok");
                break;

            // 포그라운드 서비스 종료
            case Constants.ACTION.STOPFOREGROUND_ACTION:
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
                break;
        }
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        IS_SERVICE_RUNNING = false;
        mNm.cancel(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE);
        mInCountHandler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 기본 Notification 생성시 호출
     */
    private void defaultNotification() {
        // notificaion Intnent는 Splash로 보내는게 좋을 듯 함
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // remoteView에 기본 데이터 셋팅
        contentView = new RemoteViews(getPackageName(), R.layout.wifiinfo_notification);
        contentView.setTextViewText(R.id.txt_conn_wifi, "none");
        contentView.setTextViewText(R.id.txt_conn_day, "00일");
        contentView.setTextViewText(R.id.txt_conn_time, "00:00:00");
        contentView.setTextViewText(R.id.txt_conn_kok, "0");

        // notifcation Builder
        mBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContent(contentView);

        // foreground Service Start
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                mBuilder.build());

    }


    /**
     * Notification 데이터 변경시 호출
     *  - remoteView의 경우 별도로 메모리를 관리하는 부분이 없기때문에 내용이 변경될 때마다 새로 생성(중요!)
     */
    private void updateNotification() {
        contentView = new RemoteViews(getPackageName(), R.layout.wifiinfo_notification);
        contentView.setTextViewText(R.id.txt_conn_wifi, ssid);
        contentView.setTextViewText(R.id.txt_conn_day, day);
        contentView.setTextViewText(R.id.txt_conn_time, time);
        contentView.setTextViewText(R.id.txt_conn_kok, kok);
        mBuilder.setContent(contentView);
        mNm.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());
        //startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());
    }

    /**
     * 현재 충전되어 남은시간과 적립된 콕정보를 요청
     */
    public void setRemainTimer(String mac) {
        RequestBody formBody = new FormBody.Builder()
                .add("mac", mac)
                .build();
        try {
            OkHttpUtils.post(getApplicationContext(), "http://pc.wifi.gamekok.co.kr", "/wifi/remainTime.do", formBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //    alertMessage(getActivity(), getResources().getString(R.string.error_etc));
                    Log.i(TAG, "OkHTTP onFailure : " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i(TAG, "OkHTTP Response code : " + response.code());
                    final String results = response.body().string();
                    Log.i(TAG, "OkHTTP Response Results : " + results);

                    String result = JSONParserUtil.getString(results, "result");
                    if ("SUCCESS".equals(result)) {
                        Message msg = serverTimeHandler.obtainMessage();
                        msg.obj = results;
                        serverTimeHandler.sendMessage(msg);
                    } else {

                    }
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "OkHTTP Exception : " + e.toString());

        }
    }

    /**
     * 서버로 부터 전달받은 남은시간을 카운트하는 재귀핸들러 콜
     */
    @SuppressLint("HandlerLeak")
    public Handler serverTimeHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i(TAG, msg.obj.toString());
            switch (msg.what) {
                case -1:
                    // 에러 알림 메세지
                    Log.i(TAG, "Server Response error ");
                    break;

                case 0:
                    String result = JSONParserUtil.getString(msg.obj, "result");
                    if (("SUCCESS").equals(result)) {
                        int dd = JSONParserUtil.getInt(msg.obj, "days1");
                        int hh = JSONParserUtil.getInt(msg.obj, "days2");
                        int mm = JSONParserUtil.getInt(msg.obj, "days3");
                        int ss = JSONParserUtil.getInt(msg.obj, "days4");
                        String mac = JSONParserUtil.getString(msg.obj, "mac");
                        int totalKokCount = JSONParserUtil.getInt(msg.obj, "totalKokCount");
                        kok =  Integer.toString(totalKokCount);
                        if (dd < 0 || (dd == 0 && hh == 0 && mm < 1)) {
                            stopSelf();
                        } else {
                            Log.d(TAG, "mac ==>" + mac);
                            Log.d(TAG, "days[0]" + dd);
                            Log.d(TAG, "days[1]" + hh);
                            Log.d(TAG, "days[2]" + mm);
                            Log.d(TAG, "days[3]" + ss);

                            dayArray[0] = dd;
                            dayArray[1] = hh;
                            dayArray[2] = mm;
                            dayArray[3] = ss;
                            mInCountHandler.sendEmptyMessage(0);
                        }

                    } else {
                        Log.i(TAG, "시간을 가져오는중에 문제가 발생하였습니다.");
                    }
                    break;
            }
        }
    };

    /**
     * 시간카운트 핸들러 - 메모리 누수방지 적용
     */
    private InnerHandler mInCountHandler = new InnerHandler(this);
    static class InnerHandler extends Handler {
        WeakReference<ForegroundService> m_HandlerObj;

        InnerHandler(ForegroundService handlerobj) {
            m_HandlerObj = new WeakReference<ForegroundService>(handlerobj);
        }

        @Override
        public void handleMessage(Message message) {
            ForegroundService handlerobj = m_HandlerObj.get();
            if (handlerobj != null) {
                handlerobj.handleMessage(message);
            }
        }
    }

    /**
     * 1초씩 카운트하며 Notification 갱신
     */
    private void handleMessage(Message msg) {
        try {
            int[] days = dayArray;

            int dd = days[0];
            int hh = days[1];
            int mm = days[2];
            int ss = days[3];

            if (dd < 0 || (dd == 0 && hh == 0 && mm < 1)) {
                stopSelf();
            } else {

                ss = ss - 1;
                if (ss < 0) {
                    ss = 59;
                    mm = mm - 1;
                }
                if (mm < 0) {
                    mm = 59;
                    hh = hh - 1;
                }
                if (hh < 0) {
                    hh = 23;
                    dd = dd - 1;
                }

                days[0] = dd;
                days[1] = hh;
                days[2] = mm;
                days[3] = ss;


                if (dd > 999) {
                    day = "999+일";
                } else if (dd > 99) {
                    day =CommonUtil.addZero(dd, 3) + "일";
                } else {
                    day = CommonUtil.addZero(dd, 2) + "일";
                }
                time = CommonUtil.appendValue(CommonUtil.addZero(hh, 2), " ", 1) + " : " + CommonUtil.appendValue(CommonUtil.addZero(mm, 2), " ", 1) + " : " + CommonUtil.appendValue(CommonUtil.addZero(ss, 2), " ", 1);
                Log.d(TAG, "time :" + time);
                updateNotification();
                dayArray = days;
                mInCountHandler.sendEmptyMessageDelayed(0, 1000);
            }
        } catch (Exception e) {
            Log.d(TAG, "aplList.Exception:" + e.toString());
        }

    }



}
