package project.rahul.com.mediap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Rahul on 2/19/2017.
 */

public class MyReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Toast.makeText(context, "Earphone Unpluged", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "Earphone Pluged", Toast.LENGTH_SHORT).show();
                   // intent.
                    break;
            }
        }
        else if (intent.getAction().equals(Intent.ACTION_CALL)) {

             Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();

        }

    }

}

