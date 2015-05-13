package com.alejandrolai.sfpark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by Alejandro on 5/1/15.
 */

/**
 * Helper class which contains methods to create AlertDialogs
 */
public class AlertDialogs {

    private static AlertDialogs instance = new AlertDialogs();

    private AlertDialogs() {
    }

    public static AlertDialogs getInstance() {
        return instance;
    }


    /**
     * Shows an alert dialog asking the user to turn location services
     * @param context Context of calling activity
     */
    public void showLocationSettingsAlert(final Context context) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.location_services_not_activated));

        //Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.activate_location_services));

        //On Pressing Setting button
        alertDialog.setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    /**
     * Shows an alert dialog asking the user to turn on wifi or data services
     * @param context Context of calling activity
     */
    public void showInternetAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.no_connection_available));

        //Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.turn_on_wifi_or_data_services));

        //On Pressing Setting button
        alertDialog.setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    /**
     * Shows a dialog telling the user to long tap on the map to find more locations
     * @param context
     */
    public void showNoLocationsFoundDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle("No locations around you?");

        //Setting Dialog Message
        alertDialog.setMessage("Try long tapping on the map on different locations");

        //On Pressing Setting button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //On pressing cancel button
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}