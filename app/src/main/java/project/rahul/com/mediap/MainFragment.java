package project.rahul.com.mediap;


import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    String pln;
    int cl;
    ProgressDialog pBar;
    ImageButton b1, b2, b3;
    SeekBar s1;
    MediaPlayer mp ;
    MyAdapter mad;
    ArrayList<String> als,path1;
    ArrayList<Song> Myfiles, myList;
    View iteming;
    ListView slv;
    int current=0,pre=0;
    ArrayAdapter<String> adapter;
    ArrayList<Song> playl;
    Handler handler = new Handler();
    LinearLayout ll;
    LinearLayout mfl;
    int curpos;
    int duration;
    TextView t1,t2,t3;
    MyReciver myReceiver;
    AlertDialog.Builder dio;
    AlertDialog alertDialog ;
    MyDatabase myDatabase;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainv = inflater.inflate(R.layout.fragment_main, container, false);

        ll= (LinearLayout) mainv.findViewById(R.id.ll1);
        setHasOptionsMenu(true);
        myReceiver = new MyReciver();
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        slv = (ListView) mainv.findViewById(R.id.lv1);
        slv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //  slv.setSelector(android.R.color.darker_gray);

        als = new ArrayList<String>();
        Myfiles = new ArrayList<Song>();
        path1 = new ArrayList<String>();
        myList=new ArrayList<Song>();
        playl= new ArrayList<Song>();

        mfl=(LinearLayout) mainv.findViewById(R.id.mfl);

        pBar= new ProgressDialog(getContext());

        t3= (TextView) mainv.findViewById(R.id.tvc);
        t3.setSelected(true);

        //  myList=GetFiles(new File("/storage/sdcard1"));
        myDatabase = new MyDatabase(getContext());
        new MainFragment.MyasincTask().execute();

        // adapter = new ArrayAdapter<String>(this,
        //         android.R.layout.simple_list_item_1, android.R.id.text1, myList);
        // mad= new MyAdapter(myList,this);
        // slv.setAdapter(mad);



        b1 = (ImageButton) mainv.findViewById(R.id.ib1c);
        b2= (ImageButton) mainv.findViewById(R.id.ib2c);


        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();

                    b1.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mp.start();
                    b1.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (current < myList.size()-1)
                {
                    pre=current;
                    current++;
                }
                else
                {   pre=myList.size()-1;
                    current=0;
                }
                try
                {
                    mp.reset();
                    mp.setDataSource(myList.get(current).getPath());
                    mp.prepare();
                    mp.start();
                    //  s1.setMax(mp.getDuration());
                }
                catch (Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
                b1.setImageResource(android.R.drawable.ic_media_pause);
                slv.setSelection(current);
                myList.get(pre).setAart(R.drawable.m1);
                myList.get(current).setAart(R.drawable.m2);
                slv.invalidateViews();

            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if(current<path1.size()) {
                    try {
                        mp.reset();
                        mp.setDataSource(myList.get(current + 1).getPath());
                        mp.prepare();

                        mp.start();
                        // s1.setMax(mp.getDuration());
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                    b1.setImageResource(android.R.drawable.ic_media_pause);
                    current++;

                }


            }

        });


        slv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pre=current;
                current=position;


                try
                {
                    mp.reset();
                    mp.setDataSource(myList.get(position).getPath());
                    mp.prepare();

                    mp.start();
                    // s1.setMax(mp.getDuration());
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show();
                }
                b1.setImageResource(android.R.drawable.ic_media_pause);

                String s=""+current;
                Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
                myList.get(pre).setAart(R.drawable.m1);
                myList.get(current).setAart(R.drawable.m2);
                slv.invalidateViews();
                setTitle();


            }

        });


        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongFrag frag=new SongFrag();
                Bundle data = new Bundle();
               // data.putOArrayList("slist",Myfiles);
                data.putStringArrayList("slist",getPath1(myList));
                data.putInt("current",current);
                //   data.putInt("pre",pre);
                data.putInt("currlen",mp.getCurrentPosition());
                mp.pause();
                frag.setArguments(data);


              //  mfl.removeAllViewsInLayout();
                getFragmentManager().beginTransaction().replace(R.id.fl1,frag,"SongFrag").addToBackStack(null).commit();

            }
        });




        return mainv;
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
        if (song.getName().endsWith(".mp3") && song.length()/1000000>=4) {
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

    public class MyasincTask extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            pBar.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            pBar.setMessage("Loading Library");
            pBar.show();
        }

        @Override
        protected void onPostExecute(String s) {

            mad= new MyAdapter(myList,getContext());
            slv.setAdapter(mad);
            pBar.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
             if(myDatabase.isEmpty()) {
                // Log.d("Path",Environment.getRootDirectory().getAbsolutePath().toString());
                // Log.d("Path",Environment.get);
                 myList = GetFiles(new File("/storage/9016-4EF8"));
                 myDatabase.insertValue(myList);
             }
             else
             {
                 myList=myDatabase.showValues();
             }

            return null;
        }
    }




    public void setTitle()
    {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(myList.get(current).getPath());
        t3.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)+" "
                +mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)+"      ");

    }

    public  ArrayList<String>  getPath1(ArrayList<Song> sl)
    {   ArrayList<String> ssl = new ArrayList<String>();
        for(int i=0;i<sl.size();i++)
        {
            ssl.add(sl.get(i).getPath());
        }

        return  ssl;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem mi1= menu.add(0,1,1,"Add to playlist");
        MenuItem mi2= menu.add(0,2,2,"Add to favorites");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case 1:

                AlertDialog.Builder dio =new AlertDialog.Builder(getContext());
                LayoutInflater inflater1= getActivity().getLayoutInflater();
                View v1= inflater1.inflate(R.layout.selenctplaylist,null);
                dio.setView(v1);
                Button b1,b2;
                ListView lv;

                b1 = (Button) v1.findViewById(R.id.cancel);
                b2 = (Button) v1.findViewById(R.id.ok);
                lv = (ListView) v1.findViewById(R.id.spllv);
                lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                playl= myDatabase.showPlaylist();
                MyAdapter adapter = new MyAdapter(playl,getContext());
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pln=playl.get(position).getTitle();

                    }

                });
                dio.setCancelable(false);
                final AlertDialog vd = dio.create();
                vd.show();
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vd.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDatabase.insertsongtoplaylist(pln,myList.get(current));
                        vd.dismiss();
                    }
                });

                Toast.makeText(getContext(),"Adding Song to Play list",Toast.LENGTH_SHORT).show();

                return true;
            case 2:


                myDatabase.insertFavorite(myList.get(current));
                Toast.makeText(getContext(),"Adding favorite",Toast.LENGTH_SHORT).show();
                return true;

            default:
                break;
        }

        return false;
    }

    public void onStop()
    {
       // mp.stop();

        super.onStop();
    }

    public void onDestroyView()
    {   mp.pause();
        Log.d("SongFrag","OnDestroyView");
        super.onDestroyView();

    }

}
