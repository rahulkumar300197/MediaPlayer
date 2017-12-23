package project.rahul.com.mediap;


import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class PlayIistFragment extends Fragment {

    String pln;
    int cl;
    ProgressBar pBar;
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
    String playlistname;


    public PlayIistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View plv=inflater.inflate(R.layout.fragment_playlist, container, false);
        ll= (LinearLayout) plv.findViewById(R.id.ll1);
        setHasOptionsMenu(true);
        Bundle intbun= getArguments();
        playlistname = intbun.getString("pln");
        myReceiver = new MyReciver();
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        slv = (ListView) plv.findViewById(R.id.lv1);
        registerForContextMenu(slv);
        slv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        //  slv.setSelector(android.R.color.darker_gray);

        als = new ArrayList<String>();
        Myfiles = new ArrayList<Song>();
        path1 = new ArrayList<String>();
        myList=new ArrayList<Song>();
        playl= new ArrayList<Song>();

        mfl=(LinearLayout) plv.findViewById(R.id.mfl);


        t3= (TextView) plv.findViewById(R.id.tvc);
        t3.setSelected(true);

        //  myList=GetFiles(new File("/storage/sdcard1"));
        myDatabase = new MyDatabase(getContext());

        // adapter = new ArrayAdapter<String>(this,
        //         android.R.layout.simple_list_item_1, android.R.id.text1, myList);
        // mad= new MyAdapter(myList,this);
        // slv.setAdapter(mad);

        new MyasincTaskpl().execute();



        b1 = (ImageButton) plv.findViewById(R.id.ib1c);
        b2= (ImageButton) plv.findViewById(R.id.ib2c);


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
                {  pre=current;
                   current++;
                }
                else
                {  pre=myList.size()-1;
                   current=0;
                }
                try {
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




        return plv;
    }


    public class MyasincTaskpl extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            mad= new MyAdapter(myList,getContext());
            slv.setAdapter(mad);
            Toast.makeText(getActivity(),"Async1" ,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            myList=myDatabase.showPlaylistsong(playlistname);
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




    public void onStop()
    {
        //mp.stop();

        super.onStop();
    }

    public void onDestroyView()
    {   mp.pause();
        Log.d("SongFrag","OnDestroyView");
        super.onDestroyView();

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem m1 = menu.add(0,1,0,"Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                myDatabase.deletesongPlaylist(playlistname,myList.get(current).getTitle());
                getFragmentManager().popBackStack();
                PlayIistFragment plf= new PlayIistFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pln",playlistname);
                plf.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fl1,plf,"PlayListFrag").addToBackStack(null).commit();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}
