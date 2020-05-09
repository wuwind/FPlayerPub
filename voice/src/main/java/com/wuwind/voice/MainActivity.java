package com.wuwind.voice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.wuwind.voice.params.CommonRecogParams;
import com.wuwind.voice.params.OfflineRecogParams;
import com.wuwind.voice.util.MyLogger;

import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends ActivityCommon {

    EventManager asr;
    /*
     * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
     */
    private CommonRecogParams apiParams;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    protected void startNow() {
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = fetchParams();
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        loge("设置的start输入参数：" + params);
        // 复制此段可以自动检测常规错误
//        (new AutoCheck(getApplicationContext(), new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n");
//                        ; // 可以用下面一行替代，在logcat中查看代码
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//        }, enableOffline)).checkAsr(params);
//
//        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
//        // DEMO集成步骤2.2 开始识别
        start(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asr = EventManagerFactory.create(this, "asr");
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNow();
            }
        });

        apiParams = new OfflineRecogParams();
        // 基于DEMO集成第1.1, 1.2, 1.3 步骤 初始化EventManager类并注册自定义输出事件
        // DEMO集成步骤 1.2 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
        init();

    }

    /**
     * @param params
     */
    public void start(Map<String, Object> params) {
        // SDK集成步骤 拼接识别参数
        String json = new JSONObject(params).toString();
        MyLogger.info(".Debug", "识别参数（反馈请带上此行日志）" + json);
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    private void init() {
//        EventManager asr = EventManagerFactory.create(this, "asr");
        EventListener yourListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                    loge("引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了");
                }
                if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
                    loge("识别结束");
                }
// ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
            }
        };
        asr.registerListener(yourListener);
        //        基于DEMO集成1.4 加载离线资源步骤(离线时使用)。offlineParams是固定值，复制到您的代码里即可
        Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
        String json = new JSONObject(offlineParams).toString();
        MyLogger.info(".Debug", "离线命令词初始化参数（反馈请带上此行日志）:" + json);
        // SDK集成步骤（可选）加载离线命令词(离线时使用)
//        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        // 没有ASR_KWS_LOAD_ENGINE这个回调表试失败，如缺少第一次联网时下载的正式授权文件。
    }

    private void loge(String s) {
        Log.e("main", s);
    }

    protected Map<String, Object> fetchParams() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        //  集成时不需要上面的代码，只需要params参数。
        return params;
    }

    /**
     * 离线命令词，在线不需要调用
     *
     * @param params 离线命令词加载参数，见文档“ASR_KWS_LOAD_ENGINE 输入事件参数”
     */
    public void loadOfflineEngine(Map<String, Object> params) {

    }


}
