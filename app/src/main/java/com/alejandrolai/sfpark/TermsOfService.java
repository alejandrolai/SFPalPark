package com.alejandrolai.sfpark;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.SharedPreferences;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.preference.PreferenceManager;

public class TermsOfService {

    private String TOS_PREFIX = "tos_"; //private String EULA_PREFIX = "eula_";
    private Activity mActivity;

    public TermsOfService(Activity context) {
        mActivity = context;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the tosKey changes every time you increment the version number in the AndroidManifest.xml
        final String tosKey = TOS_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean hasBeenShown = prefs.getBoolean(tosKey, false);
        if(hasBeenShown == false){

            // Show the tos
            String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;

            //Includes the updates as well so users know what changed.
            String message = mActivity.getString(R.string.updates) + "\n\n" + mActivity.getString(R.string.tos);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(tosKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the TOS
                            mActivity.finish();
                        }

                    });
            builder.create().show();
        }
    }

}
