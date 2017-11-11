package cuellardelgado.encrypterw;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ComponentName;
import io.paperdb.Paper;

import org.w3c.dom.Text;

import static android.content.Intent.ACTION_SEND;

/**
 * Implementation of App Widget functionality.
 */
public class EncryptWidget extends AppWidgetProvider {
    static String CHANGE_TO_DECRYPT = "CHANGETODECRYPT";
    static String CHANGE_TO_ENCRYPT = "CHANGETOENCRYPT";
    static String ENCRYPT_ACTION = "ENCRYPT";
    static String DECRYPT_ACTION = "DECRYPT";
    static String ACTION ="ACTIONENCRYPTDECRYPT";
    static String SHARE_ACTION = "SHARE";

    static String EncryptText(Context context){
        Paper.init(context);
        String sTexto = Paper.book().read("input");
        char[] aTexto = sTexto.toCharArray();
        int i = 0;
        int lenTexto = sTexto.length();
        int codigoEncrypt = 3;
        while (i < lenTexto){
            int tmp = 0;
            if (i%2 == 0){
                tmp = (int) sTexto.charAt(i);
                tmp = tmp + codigoEncrypt;
                if(tmp > 255) tmp = tmp - 255;
                aTexto[i] = (char) tmp;
            }else if(i%2 != 0){
                tmp = (int) sTexto.charAt(i);
                tmp = tmp + (codigoEncrypt*2);
                if(tmp > 255) tmp = tmp - 255;
                aTexto[i] = (char) tmp;
            }
            i = i + 1;
        }
        sTexto = String.valueOf(aTexto);
        return sTexto;

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Paper.init(context);
        String input = Paper.book().read("input");
        Intent intent = new Intent(context,EncryptWidget.class);
        intent.setAction(CHANGE_TO_DECRYPT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        Intent intent2 = new Intent(context, EncryptWidget.class);
        intent2.setAction(CHANGE_TO_ENCRYPT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,0,intent2,0);
        Intent intent3 = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context,0,intent3,0);

        Intent intentEncrypt = new Intent(context,EncryptWidget.class);
        intentEncrypt.setAction(ACTION);
        PendingIntent pendingIntentEncrypt = PendingIntent.getBroadcast(context,0,intentEncrypt,0);

        Intent intentShare = new Intent(context,EncryptWidget.class );
        intentShare.setAction(ACTION_SEND);
        PendingIntent pendingIntentShare = PendingIntent.getBroadcast(context,0,intentShare,0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.encrypt_widget);
        views.setOnClickPendingIntent(R.id.widgetEncrypt, pendingIntent);
        views.setOnClickPendingIntent(R.id.widgetDecrypt,pendingIntent2);
        views.setOnClickPendingIntent(R.id.widgetTextInput,pendingIntent3);
        views.setOnClickPendingIntent(R.id.widgetButtonGo, pendingIntentEncrypt);
        views.setOnClickPendingIntent(R.id.widgetButtonShare, pendingIntentShare);

        views.setTextViewText(R.id.widgetTextInput,input);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Paper.init(context);
        String action = Paper.book().read("action");

        if(intent.getAction().equals(CHANGE_TO_DECRYPT)){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.encrypt_widget);
            views.setViewVisibility(R.id.widgetEncrypt,View.INVISIBLE);
            views.setViewVisibility(R.id.widgetDecrypt,View.VISIBLE);
            //views.setTextViewText(R.id.widgetSwitchType,"Decrypt");
            Paper.book().write(("action"), DECRYPT_ACTION);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, EncryptWidget.class),views);
        }else if(intent.getAction().equals(CHANGE_TO_ENCRYPT)){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.encrypt_widget);
            views.setViewVisibility(R.id.widgetEncrypt,View.VISIBLE);
            views.setViewVisibility(R.id.widgetDecrypt,View.INVISIBLE);
            Paper.book().write(("action"),ENCRYPT_ACTION);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, EncryptWidget.class),views);
        }else if(intent.getAction().equals(ACTION)){
            if(action.equals(ENCRYPT_ACTION)) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.encrypt_widget);
                String messageEncrypted = EncryptText(context);
                views.setTextViewText(R.id.widgetTextOutput, messageEncrypted);
                AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, EncryptWidget.class), views);
            }else if(action.equals(DECRYPT_ACTION)){
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.encrypt_widget);
                //String messageEncrypted = EncryptText(context);
                String messageEncrypted = "DESENCRIPTAR";
                views.setTextViewText(R.id.widgetTextOutput, messageEncrypted);
                AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, EncryptWidget.class),views);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

