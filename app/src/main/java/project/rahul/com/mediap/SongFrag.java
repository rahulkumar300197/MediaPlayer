package project.rahul.com.mediap;


import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongFrag extends Fragment {

    int cl;
    ImageButton b1, b2, b3,b4;
    SeekBar s1;
    MediaPlayer mp ;
    ArrayList<String> path1;
    int current,pre,curpos;
    Handler handler = new Handler();
    int duration;
    TextView t1,t2,t3;
    ImageView iv1,iv2;
    Runnable notification;
    AudioManager audioManager ;

    public SongFrag() {
        // Required empty public constructor
    }



    public void startPlayProgressUpdater() {
        cl=mp.getCurrentPosition();
        s1.setProgress(cl);

        if (mp.isPlaying()) {
            notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                    time();
                }
            };
            handler.postDelayed(notification, 1000);

        }


    }


    private void seekChange() {
        if (mp.isPlaying()) {

            mp.seekTo(s1.getProgress());
        }
        else
        {
            mp.seekTo(s1.getProgress());
        }
    }
    public void onCompletion() {
        mp.stop();
        b1.setImageResource(android.R.drawable.ic_media_play);
    }

    public void time()
    {   duration=mp.getDuration()/1000;
        int min1,sec1,min2,sec2;
        int cur=(int)mp.getCurrentPosition()/1000;
        sec1=cur%60;
        min1=cur/60;
        sec2=(duration-cur)%60;
        min2=(duration-cur)/60;
        t1.setText(min1+":"+sec1);
        t2.setText("-"+min2+":"+sec2);

    }

    public void setArt()
    {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path1.get(current));

        byte [] data = mmr.getEmbeddedPicture();

        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            iv1.setImageBitmap(bitmap);
            iv1.setAdjustViewBounds(true);

        }
        else
        {
            iv1.setImageResource(R.drawable.cover);
            iv1.setAdjustViewBounds(true);

        }

        t3.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        String s=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String s1=t3.getText().toString();
        if(s1.equals(""))
        {
            t3.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        }
        //  t3.setText("hispbqmebembkQEMB OPBMEWB[Pmeb[pmew ebmKEOWB");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_song, container, false);

        Bundle intbun= getArguments();
        path1=intbun.getStringArrayList("slist");
        current=intbun.getInt("current");
        curpos=intbun.getInt("currlen");
        AlertDialog.Builder dio =new AlertDialog.Builder(getContext());

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        iv1= (ImageView) v.findViewById(R.id.iv1);

        b1 = (ImageButton) v.findViewById(R.id.bt1);
        b2= (ImageButton) v.findViewById(R.id.bt2);
        b3= (ImageButton) v.findViewById(R.id.bt3);
        b4= (ImageButton) v.findViewById(R.id.volume);
        s1 = (SeekBar) v.findViewById(R.id.sb1);

        t1 = (TextView) v.findViewById(R.id.t1);
        t2 = (TextView) v.findViewById(R.id.t2);
        t3 = (TextView) v.findViewById(R.id.tv1);
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mp.reset();
            mp.setDataSource(path1.get(current ));
            mp.prepare();
            mp.start();
            mp.seekTo(curpos);
            s1.setMax(mp.getDuration());
            startPlayProgressUpdater();
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
        }

        setArt();




        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mp.isPlaying()) {
                    mp.pause();

                    b1.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mp.start();
                    startPlayProgressUpdater();
                    b1.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (current > 0)
                {
                        pre=current;
                        current--;
                }
                else
                {
                    pre=0;
                    current=path1.size()-1;
                }
                try {
                    mp.reset();
                    mp.setDataSource(path1.get(current));
                    mp.prepare();

                    mp.start();
                    s1.setMax(mp.getDuration());
                }
                catch (Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
                b1.setImageResource(android.R.drawable.ic_media_pause);
                startPlayProgressUpdater();
                setArt();


            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(current<path1.size()-1) {

                    pre=current;
                    current++;
                }
                else
                {
                    current=0;
                    pre=path1.size()-1;
                }
                try {
                    mp.reset();
                    mp.setDataSource(path1.get(current));
                    mp.prepare();

                    mp.start();
                    s1.setMax(mp.getDuration());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
                b1.setImageResource(android.R.drawable.ic_media_pause);
                startPlayProgressUpdater();
                setArt();

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Volume","1");
                AlertDialog.Builder dio =new AlertDialog.Builder(getContext());
                LayoutInflater inflater1= getActivity().getLayoutInflater();
                View v1= inflater1.inflate(R.layout.volemecontroler,null);
                dio.setView(v1);
                SeekBar sb=(SeekBar) v1.findViewById(R.id.volumeseek);
                try
                {   Log.d("Volume","2");
                    sb.setMax(audioManager
                            .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                    Log.d("Volume","3");
                    sb.setProgress(audioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC));
                    Log.d("Volume","4");


                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                    {
                        @Override
                        public void onStopTrackingTouch(SeekBar arg0)
                        {
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar arg0)
                        {
                        }

                        @Override
                        public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                        {
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                    progress, 0);
                        }
                    });
                }
                catch (Exception e)
                {   Log.d("Volume","5");
                    //e.printStackTrace();
                }
                Log.d("Volume","6");
                AlertDialog vd = dio.create();
                vd.getWindow().setGravity(Gravity.TOP);
                vd.show();
                Log.d("Volume","7");
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(current<path1.size()) {
                    try {
                        mp.reset();
                        mp.setDataSource(path1.get(current + 1));
                        mp.prepare();

                        mp.start();
                        s1.setMax(mp.getDuration());
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                    b1.setImageResource(android.R.drawable.ic_media_pause);
                    startPlayProgressUpdater();
                    setArt();
                    current++;

                }


            }

        });



        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekChange();


            }
        });



        return v;
    }



   /* protected void onPause() {


        super.onPause();
        Log.d("song act","onpause");
        Intent i = new Intent();

        //data.putStringArrayList("slist",path1);
        handler.removeCallbacks(notification);
        i.putExtra("current",current);
        i.putExtra("cpath",path1.get(current));
        i.putExtra("currlen",cl);
        mp.stop();
        mp.reset();
        mp.release();

        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        setResult(RESULT_OK,i);
        finish();

        // super.onDestroy();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override

    public void onStop()
    {
       // mp.pause();
      //  mp.stop();
      //  mp.release();
        Log.d("SongFrag","OnStop");
        super.onStop();

    }

    public void onDestroyView()
    {   mp.pause();
        Log.d("SongFrag","OnDestroyView");
        super.onDestroyView();

    }


}
