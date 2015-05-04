package com.alejandrolai.sfpark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by Alejandro on 5/1/15.
 */
public class AlertDialogs {

    private static AlertDialogs instance = new AlertDialogs();
    private AlertDialogs(){}
    public static AlertDialogs getInstance(){
        return instance;
    }



    /**
     * Prompts user to change location settings.
     */
    public void showLocationSettingsAlert(final Context context) {

        // Creates a new Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Sets the Dialog Title
        alertDialog.setTitle(context.getString(R.string.location_services_not_activated));

        // Sets the Dialog Message
        alertDialog.setMessage(context.getString(R.string.activate_location_services));

        // On pressing Settings button, sends user to the location settings
        alertDialog.setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // On pressing Cancel button, cancels
        alertDialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }



    /**
     * Shows a Dialog to prompt the user to change internet settings.
     */
    public void showInternetAlert(final Context context) {

        // Creates a new Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Sets the Dialog Title
        alertDialog.setTitle(context.getString(R.string.no_connection_available));

        // Sets the Dialog Message
        alertDialog.setMessage(context.getString(R.string.turn_on_wifi_or_data_services));

        //On pressing Settings button, ?
        alertDialog.setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //On pressing cancel button, ?
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
