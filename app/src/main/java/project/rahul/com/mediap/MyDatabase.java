package project.rahul.com.mediap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rahul on 4/4/2017.
 */

public class MyDatabase extends SQLiteOpenHelper{

    public static final int dbv=1;
    public static String dbname="my_db";
    Context ct;

    MyDatabase(Context c)
    {
        super(c,dbname,null,dbv);
            ct=c;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String s="create table musicplayer(sname text ,spath text ,img int)";
        String s1="create table playlist(sname text,img int)";
        String s2="create table favorite(sname text ,spath text ,img int)";
        db.execSQL(s);
        db.execSQL(s1);
        db.execSQL(s2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertValue(ArrayList<Song> slv)
    {
        SQLiteDatabase sl= getWritableDatabase();
        for(int i=0;i<slv.size();i++)
        {   ContentValues cv= new ContentValues();
            cv.put("sname",slv.get(i).getTitle());
            cv.put("spath",slv.get(i).getPath());
            cv.put("img",slv.get(i).getAart());
            sl.insert("musicplayer",null,cv);

        }

    }

    public ArrayList<Song> showValues()
    {
        SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from musicplayer";
        Cursor c = sr.rawQuery(s1,null);
        ArrayList<Song> al = new ArrayList<Song>();

        while(c.moveToNext())
        {
            String a= c.getString(0);
            String b= c.getString(1);
            int d= c.getInt(2);
            Song p= new Song();
            p.setTitle(a);
            p.setPath(b);
            p.setAart(d);
            al.add(p);
        }
        c.close();
        return  al;
    }

    public boolean isEmpty()
    {
        SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from musicplayer";
        Cursor c = sr.rawQuery(s1,null);
        if(c.getCount()==0) {
            c.close();
            return true;
        }
        else {
            c.close();
            return false;
        }

    }

    public void refresh(ArrayList<Song> slv1)
    {    SQLiteDatabase sl= getWritableDatabase();
         String s= "delete from musicplayer ;";
        // sl.delete("musicplayer",null,null);
        // String s="create table musicplayer(sname text ,spath text ,img int)";
         sl.execSQL(s);
         insertValue(slv1);

    }
    public  void insertplaylist(Song s)
    {
        SQLiteDatabase sl= getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("sname",s.getTitle());
        cv.put("img",s.getAart());
        sl.insert("playlist",null,cv);
       // createPlaylistitem(s.getTitle());

    }

    public ArrayList<Song> showPlaylist()
    {
        SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from playlist";
        Cursor c = sr.rawQuery(s1,null);
        ArrayList<Song> al = new ArrayList<Song>();

        while(c.moveToNext())
        {
            String a= c.getString(0);
            int d= c.getInt(1);
            Song p= new Song();
            p.setTitle(a);
            p.setPath(null);
            p.setAart(d);
            al.add(p);
        }
        c.close();
        return  al;
    }

    public void createPlaylistitem(String s)
    {  SQLiteDatabase sr= getWritableDatabase();
       try {
           String s1 = "create table " + s + "(sname text ,spath text ,img int)";
           sr.execSQL(s1);
       }
       catch (Exception e){
           Log.d("Table",e.getMessage()+s);
       }
    }
    public  void insertsongtoplaylist(String s,Song song)
    {   if(!isExistInPlaylist(s,song.getTitle())) {
         SQLiteDatabase sl = getWritableDatabase();
         ContentValues cv = new ContentValues();
         song.setAart(R.drawable.m1);
         cv.put("sname", song.getTitle());
         cv.put("spath", song.getPath());
         cv.put("img", song.getAart());
         sl.insert(s, null, cv);
       }
       else
       {
           Toast.makeText(ct,"Already added",Toast.LENGTH_SHORT).show();
       }
    }

    public boolean isExistInPlaylist(String s, String s2)
    {  SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from "+s+" where sname='"+s2+"'";
        Cursor c = sr.rawQuery(s1,null);
        if(c.getCount()==0) {
            c.close();
            return false;
        }
        else {
            c.close();
            return true;
        }


    }

    public void deletesongPlaylist(String s, String s2)
    {   SQLiteDatabase sr= getWritableDatabase();
        String s1= "delete from "+s+" where sname='"+s2+"'";
        sr.execSQL(s1);
    }

    public ArrayList<Song> showPlaylistsong(String s)
    {
        SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from "+s;
        Cursor c = sr.rawQuery(s1,null);
        ArrayList<Song> al = new ArrayList<Song>();

        while(c.moveToNext())
        {
            String a= c.getString(0);
            String b= c.getString(1);
            int d= c.getInt(2);
            Song p= new Song();
            p.setTitle(a);
            p.setPath(b);
            p.setAart(d);
            al.add(p);
        }
        c.close();
        return  al;
    }

    public void insertFavorite(Song slv)
    {  if(!isExistInPlaylist("favorite",slv.getTitle())) {
        SQLiteDatabase sl = getWritableDatabase();
        ContentValues cv = new ContentValues();
        slv.setAart(R.drawable.m1);
        cv.put("sname", slv.getTitle());
        cv.put("spath", slv.getPath());
        cv.put("img", slv.getAart());
        sl.insert("favorite", null, cv);
       }
       else
       {
           Toast.makeText(ct,"Already added",Toast.LENGTH_SHORT).show();
       }
    }

    public ArrayList<Song> showFavorite()
    {
        SQLiteDatabase sr= getReadableDatabase();
        String s1= "select * from favorite";
        Cursor c = sr.rawQuery(s1,null);
        ArrayList<Song> al = new ArrayList<Song>();

        while(c.moveToNext())
        {
            String a= c.getString(0);
            String b= c.getString(1);
            int d= c.getInt(2);
            Song p= new Song();
            p.setTitle(a);
            p.setPath(b);
            p.setAart(d);
            al.add(p);
        }
        c.close();
        return  al;
    }
}
