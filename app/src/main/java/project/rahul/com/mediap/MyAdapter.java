package project.rahul.com.mediap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rahul on 2/7/2017.
 */

public class MyAdapter extends BaseAdapter
{
    ArrayList<Song> sl1;
    ImageView iv ;
    TextView tv1;

    Context ct;

    MyAdapter(ArrayList<Song> al,Context c)
    {
        sl1=al;
        ct=c;
    }
    @Override
    public int getCount() {
        return sl1.size();
    }

    @Override
    public Object getItem(int position) {
        return sl1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppCompatActivity at=(AppCompatActivity) ct;
        LayoutInflater lf=at.getLayoutInflater();

        View v1= lf.inflate(R.layout.songlist,null);

        Song p=(Song) getItem(position);

        iv =(ImageView) v1.findViewById(R.id.iv1);
        tv1= (TextView) v1.findViewById(R.id.tv1);
        //TextView tv2= (TextView) v1.findViewById(R.id.tv2);

        iv.setImageResource(p.getAart());
        tv1.setText(p.getTitle());


        return v1;

    }
}
