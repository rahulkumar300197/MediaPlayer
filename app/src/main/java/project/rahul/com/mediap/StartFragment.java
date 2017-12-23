package project.rahul.com.mediap;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    LinearLayout l1,l2;
    ListView lv;
    MyAdapter adapter;
    MyDatabase myDatabase;
    MediaPlayer mp;
    ArrayList<Song> myList;
    int current=0,pre=0;
    TextView cpl;




    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_start, container, false);

        myDatabase= new MyDatabase(getContext());
        myList= new ArrayList<Song>();

        l1=(LinearLayout) v.findViewById(R.id.localsongs);
        l2=(LinearLayout) v.findViewById(R.id.favorite);

        lv = (ListView) v.findViewById(R.id.playlist);
        cpl = (TextView) v.findViewById(R.id.cpl);

        mp = new MediaPlayer();

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fl1,new MainFragment(),"mainfrag").addToBackStack(null).commit();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fl1,new FavoriteFragment(),"favoritefrag").addToBackStack(null).commit();
            }
        });

        new MyasincTask1().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayIistFragment plf= new PlayIistFragment();
                Bundle bundle = new Bundle();
                bundle.putString("pln",myList.get(position).getTitle());
                plf.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fl1,plf,"PlayListFrag").addToBackStack(null).commit();


            }
        });

        cpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dio =new AlertDialog.Builder(getContext());
                LayoutInflater inflater1= getActivity().getLayoutInflater();
                View v1= inflater1.inflate(R.layout.createplaylist,null);
                dio.setView(v1);

                Button b1,b2;
                final EditText pln;
                b1 = (Button) v1.findViewById(R.id.cancel);
                b2 = (Button) v1.findViewById(R.id.ok);
                pln = (EditText) v1.findViewById(R.id.pln);
                dio.setCancelable(false);
                final AlertDialog vd = dio.create();
                vd.getWindow().setGravity(Gravity.BOTTOM);
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
                        myDatabase.insertplaylist(new Song(pln.getText().toString().trim(),null,R.drawable.playlist3));
                        myDatabase.createPlaylistitem(pln.getText().toString().trim());
                        new MyasincTask1().execute();
                        vd.dismiss();
                    }
                });


            }
        });



        return v;
    }

    public class MyasincTask1 extends AsyncTask<String,String,String>
    {



        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            adapter= new MyAdapter(myList,getContext());
            lv.setAdapter(adapter);
        }

        @Override
        protected String doInBackground(String... params) {
            myList=myDatabase.showPlaylist();

            return null;
        }
    }

    public void onStop()
    {
       // mp.pause();
        super.onStop();
    }

    public void onDestroyView()
    {   mp.pause();
        Log.d("SongFrag","OnDestroyView");
        super.onDestroyView();

    }

}
