package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.mbijou.geounlockl.MainActivity;

public class lockScreenReeiver extends BroadcastReceiver  {
	 public static boolean wasScreenOn = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        	
        	wasScreenOn=false;
        	Intent intent2 = new Intent(context,MainActivity.class);
        	intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        	context.startActivity(intent2);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

        	wasScreenOn=true;
        	Intent intent2 = new Intent(context,MainActivity.class);
        	intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
       else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
        	Intent intent11 = new Intent(context, MainActivity.class);

        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent11);
       }

    }


}
