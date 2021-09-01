package cn.flandre.review.logic.extradata;

import android.media.AudioAttributes;
import android.media.SoundPool;
import cn.flandre.review.logic.callback.OnPlayFail;
import cn.flandre.review.data.bean.DetailData;

import static android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
import static android.media.AudioAttributes.USAGE_MEDIA;

public class DetailDataSpeaker implements SoundPool.OnLoadCompleteListener {
    private DetailData data = null;
    private final SoundPool pool;
    private final OnPlayFail fail;

    public DetailDataSpeaker(OnPlayFail fail) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(CONTENT_TYPE_MUSIC)
                .setUsage(USAGE_MEDIA)
                .build();
        pool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build();

        pool.setOnLoadCompleteListener(this);
        this.fail = fail;
    }

    public void setData(DetailData data) {
        this.data = data;
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        // 其实应该判断是否已经加载，如果已经就在就直接播放，但这点性能而已
        if (status == 0)
            soundPool.play(sampleId, 1, 1, 0, 0, 1);
        else fail.onPlayFail();

    }

    public void playWordAudio() {
        if (data == null) return;
        pool.load(data.getWordAudioPath(), 0);
    }

    public void playSentenceAudio() {
        if (data == null) return;
        pool.load(data.getSentenceAudioPath(), 0);
    }
}
