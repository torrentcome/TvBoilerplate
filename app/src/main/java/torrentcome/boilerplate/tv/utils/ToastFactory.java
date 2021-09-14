package torrentcome.boilerplate.tv.utils;


import android.content.Context;
import android.widget.Toast;

import torrentcome.boilerplate.tv.R;

public class ToastFactory {
    public static Toast createWifiErrorToast(Context context) {
        return Toast.makeText(context, context.getString(R.string.error_message_network_needed), Toast.LENGTH_SHORT);
    }
}