package com.datahk.callmeup.callmeup;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CallMeUp extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


//    me
    private Button mStartBtn1,mStartBtn2,mStopBtn;
    private EditText mTxtSeconds;
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_me_up);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        mStartBtn1 = (Button) findViewById(R.id.btnSetAlarm1);
        mStartBtn2 = (Button) findViewById(R.id.btnSetAlarm2);
        mStopBtn = (Button) findViewById(R.id.btnSetAlarm3);

        mTxtSeconds = (EditText) findViewById(R.id.txtSeconds);

        mStartBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int i = Integer.parseInt(mTxtSeconds.getText().toString());
                    Intent intent = new Intent(CallMeUp.this, AlarmReceiverActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(CallMeUp.this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);

                    if (mToast != null) {
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(), "Alarm for activity is set in: " + i + "seconds", Toast.LENGTH_LONG);
                    mToast.show();
                }catch (NumberFormatException e){
                    if (mToast != null){
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(CallMeUp.this, "Please enter some number in the text field and try again!", Toast.LENGTH_LONG);
                    mToast.show();
                    Log.i("CallMeUp", "Number format exception");
                }
            }
        });


        mStartBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int i = Integer.parseInt(mTxtSeconds.getText().toString());
                    Intent intent = new Intent(CallMeUp.this, RepeatingAlarmReciverActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(CallMeUp.this, 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), 15 * 1000, pendingIntent);

                    if (mToast != null) {
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(), "Repeating alarm for activity is set in: " + i + "seconds,"
                            + " and repeat every 15 seconds after that", Toast.LENGTH_LONG);
                    mToast.show();
                }catch (NumberFormatException e){
                    if (mToast != null){
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(CallMeUp.this, "Please enter some number in the text field and try again!", Toast.LENGTH_LONG);
                    mToast.show();
                    Log.i("CallMeUp", "Number format exception");
                }
            }
        });


        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    int i = Integer.parseInt(mTxtSeconds.getText().toString());
                    Intent intent = new Intent(CallMeUp.this, RepeatingAlarmReciverActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(CallMeUp.this, 3, intent, 0);

                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.cancel(pendingIntent);

                    if (mToast != null) {
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(getApplicationContext(), "Repeating alarm has been cancelled!", Toast.LENGTH_LONG);
                    mToast.show();

            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.call_me_up, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_call_me_up, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CallMeUp) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
