package project.rahul.com.mediap;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    ProgressDialog pBar;
    int cl;
    ImageButton b1, b2, b3;
    SeekBar s1;
    MediaPlayer mp ;
    MyAdapter mad;
    ArrayList<String> als,path1;
    ArrayList<Song> Myfiles, songList;
    View iteming;
    ListView slv;
    int current=0,pre=0;
    ArrayAdapter<String> adapter;
    Handler handler = new Handler();
    LinearLayout ll;
    FrameLayout frameLayout;
    int curpos;
    int duration;
    TextView t1,t2;
    MyReciver myReceiver;
    AlertDialog.Builder dio;
    AlertDialog alertDialog ;
    int state1=2;
    MyDatabase db;
    ArrayList<Song> myList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
           // Log.i(TAG, "Permission to record denied");
            makeRequest();
        }
        db= new MyDatabase(getApplicationContext());
        pBar= new ProgressDialog(MainActivity.this);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.fl1,new StartFragment(),"StartFrag").addToBackStack(null).commit();



        }
        Myfiles = new ArrayList<Song>();
        songList = new ArrayList<Song>();
        myList= new ArrayList<Song>();



        BroadcastReceiver nbr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    int state = intent.getIntExtra("state", -1);

                         if(state==0 && state1==0)
                            Toast.makeText(context, "Earphone Unpluged", Toast.LENGTH_SHORT).show();
                          //  mp.pause();


                         else if(state==1)
                            Toast.makeText(context, "Earphone Pluged", Toast.LENGTH_SHORT).show();
                            // intent.
                         //   mp.start();
                         else if(state==0)
                             state1=0;


                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(nbr,filter);


    }


    protected void onResume()
    {   IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_CALL);
        registerReceiver(myReceiver, filter1);



        super.onResume();

    }

    protected void onRestart()
    {  super.onRestart();
        Log.d("mainact","onrestart");

    }


    protected void onDestroy()
    {
        super.onDestroy();


    }



    public ArrayList<Song> GetFiles(File s)
    {


        File list[] = s.listFiles();
        for (File file1 : list)
        {
            if (file1.isDirectory())
            {
                GetFiles(file1);
            }
            else
            {   Myfiles=addSongToList(file1,Myfiles);

            }
        }

        return Myfiles;
    }
    private ArrayList<Song> addSongToList(File song,ArrayList<Song
            > songList) {
        if (song.getName().endsWith(".mp3")) {
            String s= song.getName();
            if(s.length()>30)
            {
                s=s.substring(0,30);
            }
            Song p= new Song();
            p.setTitle(s);
            p.setAart(R.drawable.m1);
            p.setPath(song.getAbsolutePath());
            songList.add(p);



        }
        return songList;

    }
    public class MyasincTask1 extends AsyncTask<String,String,String>
    {



        @Override
        protected void onPreExecute() {
           pBar.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
           pBar.setMessage("Refreshing Library");
           pBar.show();

        }

        @Override
        protected void onPostExecute(String s) {

           pBar.dismiss();
           Log.d("Post","1111");
           getSupportFragmentManager().beginTransaction().replace(R.id.fl1,new MainFragment(),"MainFrag").addToBackStack(null).commit();


        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Song> myList= new ArrayList<Song>();
            myList = GetFiles(new File("/storage/sdcard1"));
            db.refresh(myList);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       MenuItem mi2= menu.add(0,3,3,"Refresh");


       return true;
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()>=2)
                getSupportFragmentManager().popBackStack();
                Log.d("MainAct",""+getSupportFragmentManager().getBackStackEntryCount());
                return true;
            case 3:
                new MainActivity.MyasincTask1().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
      }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(getSupportFragmentManager().getBackStackEntryCount()>=2)
                getSupportFragmentManager().popBackStack();
            Log.d("MainAct",""+getSupportFragmentManager().getBackStackEntryCount());
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                101);
    }

}
