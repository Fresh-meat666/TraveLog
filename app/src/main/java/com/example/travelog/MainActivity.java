package com.example.travelog;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.dailyjourney.app.activity.FaceActivity;
import com.dailyjourney.app.activity.FaceAgeActivity;
import com.dailyjourney.app.activity.FaceDetectCameraActivity;
import com.dailyjourney.app.activity.JournalActivity;
import com.dailyjourney.app.fragment.CalendarFragment;
import com.dailyjourney.app.fragment.JournalFragment;
import com.dailyjourney.app.fragment.ReportFragment;
import com.dailyjourney.app.fragment.SettingFragment;
import com.dailyjourney.app.model.Activities;
import com.dailyjourney.app.model.GlobalData;
import com.dailyjourney.app.model.Mood;
import com.dailyjourney.app.utils.DetectionTask;
import com.dailyjourney.app.utils.ImageHelper;
import com.dailyjourney.app.view.CircleMenuView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CalendarFragment.FragmentListener, View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 99;

    private final String TAG = "Maintivity";

    private AHBottomNavigation bottomNavigation;

    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<AHBottomNavigationItem>();

    private CalendarFragment calendarFragment;

    MainHandler handler = new MainHandler(Looper.myLooper());

    public int id = 0;

    Uri imageUri;

    private JournalFragment journalFragment;

    private long lastPressTime = 0L;

    private CircleMenuView mAddBtn;

    private ImageView mAgeTipsView;

    private ImageView mDiaryTipsView;

    private ImageView mFeelingTipsView;

    private View mFloatView;

    private RelativeLayout mRootView;

    private int mTipsPos = -1;

    private ToolTipsManager mToolTipsManager;

    private Uri mUriPhotoTaken;

    private ReportFragment reportFragment;

    private int selectedDay;

    private int selectedMonth;

    private int selectedYear;

    private SettingFragment settingFragment;

    private Toolbar toolbar;

    private void initActData() {
        boolean bool;
        String str = getSharedPreferences("ActList", 0).getString("act", null);
        StringBuilder stringBuilder = new StringBuilder();
        if (str == null) {
            bool = true;
        } else {
            bool = false;
        }
        Log.d("tag1", stringBuilder.append(bool).append("act   joson is null").toString());
        if (str != null) {
            Gson gson = new Gson();
            Type type = (new TypeToken<ArrayList<Activities>>() {

            }).getType();
            new ArrayList();
            ArrayList<Activities> arrayList = (ArrayList)gson.fromJson(str, type);
            GlobalData.setActivitiesArrayList(arrayList);
            for (int i = 0; i < arrayList.size(); i++)
                Log.d("tag1", ((Activities)arrayList.get(i)).getName() + ":" + ((Activities)arrayList.get(i)).getName() + ",");
        } else {
            ArrayList<Activities> arrayList = new ArrayList();
            Activities activities = new Activities();
            activities.setName("Work");
            activities.setSelected(false);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Reading");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Friends");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Date");
            activities.setSelected(false);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Movies");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Sport");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Fine Food");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Party");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Shopping");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Travel");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Hospital");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Cooking");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Relax");
            activities.setSelected(true);
            arrayList.add(activities);
            activities = new Activities();
            activities.setName("Cleaning");
            activities.setSelected(true);
            arrayList.add(activities);
            GlobalData.setActivitiesArrayList(arrayList);
        }
    }

    private void initData() {
        initMoodData();
        initActData();
    }

    private void initMoodData() {
        boolean bool;
        String str = getSharedPreferences("MoodList", 0).getString("mood", null);
        StringBuilder stringBuilder = new StringBuilder();
        if (str == null) {
            bool = true;
        } else {
            bool = false;
        }
        Log.d("tag1", stringBuilder.append(bool).append("joson is null").toString());
        if (str != null) {
            Gson gson = new Gson();
            Type type = (new TypeToken<ArrayList<Mood>>() {

            }).getType();
            new ArrayList();
            ArrayList<Mood> arrayList = (ArrayList)gson.fromJson(str, type);
            GlobalData.setMoodArrayList(arrayList);
            for (int i = 0; i < arrayList.size(); i++)
                Log.d("tag1", ((Mood)arrayList.get(i)).getName() + ":" + ((Mood)arrayList.get(i)).getName() + "," + ((Mood)arrayList.get(i)).getValue());
        } else {
            ArrayList<Mood> arrayList = new ArrayList();
            Mood mood = new Mood();
            mood.setName("Awful");
            mood.setValue(2);
            mood.setSelected(true);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Bad");
            mood.setValue(5);
            mood.setSelected(true);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Neutral");
            mood.setValue(8);
            mood.setSelected(true);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Good");
            mood.setValue(11);
            mood.setSelected(true);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Excellent");
            mood.setValue(14);
            mood.setSelected(true);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Anger");
            mood.setValue(1);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Fear");
            mood.setValue(3);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Sadness");
            mood.setValue(4);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Disgust");
            mood.setValue(6);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Contempt");
            mood.setValue(7);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Surprise");
            mood.setValue(9);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Refreshed");
            mood.setValue(10);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Happy");
            mood.setValue(12);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Great");
            mood.setValue(13);
            mood.setSelected(false);
            arrayList.add(mood);
            mood = new Mood();
            mood.setName("Fanstatic");
            mood.setValue(15);
            mood.setSelected(false);
            arrayList.add(mood);
            GlobalData.setMoodArrayList(arrayList);
        }
    }

    public void AddJournalClickEvent(View paramView) {
        this.mAddBtn.close(true);
        startActivity(new Intent((Context)this, JournalActivity.class));
    }

    public void AgeClickEvent(View paramView) {
        this.mAddBtn.close(true);
        startActivity(new Intent((Context)this, FaceAgeActivity.class));
    }

    public void FeelingClickEvent(View paramView) {
        this.mAddBtn.close(true);
        takePhoto();
    }

    public void getSelecetTime(int paramInt1, int paramInt2, int paramInt3) {
        this.selectedDay = paramInt3;
        this.selectedMonth = paramInt2;
        this.selectedYear = paramInt1;
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        switch (paramInt1) {
            default:
                return;
            case 99:
                if (paramInt2 == -1) {
                    if (paramIntent == null || paramIntent.getData() == null) {
                        this.imageUri = this.mUriPhotoTaken;
                    } else {
                        this.imageUri = paramIntent.getData();
                    }
                    paramIntent = new Intent((Context)this, FaceActivity.class);
                    paramIntent.setData(this.imageUri);
                    startActivity(paramIntent);
                    return;
                }
            case 1:
                break;
        }
        Log.d("newj", "不会被调用到这吧");
        List<LocalMedia> list = PictureSelector.obtainMultipleResult(paramIntent);
        if (list.size() != 0) {
            for (LocalMedia localMedia : list)
                Log.d("newj", "selectedList:-------photo---------->" + localMedia.getCutPath());
            Message message = new Message();
            message.what = 6;
            message.obj = ((LocalMedia)list.get(0)).getCutPath();
            GlobalData.getSettingHandler().handleMessage(message);
            return;
        }
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() - this.lastPressTime > 2000L) {
            Toast.makeText((Context)this, "Press again to exit!", 0).show();
            this.lastPressTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    public void onClick(View paramView) {
        paramView.getId();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(2130968609);
        GlobalData.setMainHandler(this.handler);
        this.mRootView = (RelativeLayout)findViewById(2131820753);
        this.mFloatView = findViewById(2131820844);
        this.mFeelingTipsView = (ImageView)findViewById(2131820851);
        this.mAgeTipsView = (ImageView)findViewById(2131820850);
        this.mDiaryTipsView = (ImageView)findViewById(2131820849);
        this.mFloatView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                if (MainActivity.this.mTipsPos == -1) {
                    MainActivity.this.mAddBtn.close(true);
                    return;
                }
                if (MainActivity.this.mTipsPos == 1) {
                    if (MainActivity.this.mToolTipsManager != null) {
                        MainActivity.this.mToolTipsManager.findAndDismiss((View)MainActivity.this.mFeelingTipsView);
                        return;
                    }
                    return;
                }
                if (MainActivity.this.mTipsPos == 2) {
                    if (MainActivity.this.mToolTipsManager != null) {
                        MainActivity.this.mToolTipsManager.findAndDismiss((View)MainActivity.this.mAgeTipsView);
                        return;
                    }
                    return;
                }
                if (MainActivity.this.mTipsPos == 3 && MainActivity.this.mToolTipsManager != null) {
                    MainActivity.this.mToolTipsManager.findAndDismiss((View)MainActivity.this.mDiaryTipsView);
                    return;
                }
            }
        });
        this.mToolTipsManager = new ToolTipsManager(new ToolTipsManager.TipListener() {
            public void onTipDismissed(View param1View, int param1Int, boolean param1Boolean) {
                if (param1Int == 2131820851) {
                    MainActivity.access$002(MainActivity.this, 2);
                    ToolTip.Builder builder = new ToolTip.Builder((Context)MainActivity.this, (View)MainActivity.this.mAgeTipsView, (ViewGroup)MainActivity.this.mFloatView, "Check out the age of your face!", 0);
                    builder.setBackgroundColor(MainActivity.this.getResources().getColor(2131689647));
                    builder.setTextColor(Color.parseColor("#666666"));
                    builder.setAlign(0);
                    MainActivity.this.mToolTipsManager.show(builder.build());
                    return;
                }
                if (param1Int == 2131820850) {
                    MainActivity.access$002(MainActivity.this, 3);
                    ToolTip.Builder builder = new ToolTip.Builder((Context)MainActivity.this, (View)MainActivity.this.mDiaryTipsView, (ViewGroup)MainActivity.this.mFloatView, "Record your day with detail", 0);
                    builder.setBackgroundColor(MainActivity.this.getResources().getColor(2131689647));
                    builder.setTextColor(Color.parseColor("#666666"));
                    builder.setAlign(1);
                    MainActivity.this.mToolTipsManager.show(builder.build());
                    return;
                }
                if (param1Int == 2131820849) {
                    MainActivity.access$002(MainActivity.this, -1);
                    return;
                }
            }
        });
        this.mAddBtn = (CircleMenuView)findViewById(2131820852);
        this.mAddBtn.setEventListener(new CircleMenuView.EventListener() {
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView param1CircleMenuView, int param1Int) {
                Log.d("D", "onButtonClickAnimationEnd| index: " + param1Int);
                if (param1Int == 0) {
                    MainActivity.this.FeelingClickEvent((View)param1CircleMenuView);
                    return;
                }
                if (param1Int == 1) {
                    MainActivity.this.AgeClickEvent((View)param1CircleMenuView);
                    return;
                }
                if (param1Int == 2) {
                    MainActivity.this.AddJournalClickEvent((View)param1CircleMenuView);
                    return;
                }
            }

            public void onButtonClickAnimationStart(@NonNull CircleMenuView param1CircleMenuView, int param1Int) {
                Log.d("D", "onButtonClickAnimationStart| index: " + param1Int);
            }

            public boolean onButtonLongClick(@NonNull CircleMenuView param1CircleMenuView, int param1Int) {
                Log.d("D", "onButtonLongClick| index: " + param1Int);
                return true;
            }

            public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView param1CircleMenuView, int param1Int) {
                Log.d("D", "onButtonLongClickAnimationEnd| index: " + param1Int);
            }

            public void onButtonLongClickAnimationStart(@NonNull CircleMenuView param1CircleMenuView, int param1Int) {
                Log.d("D", "onButtonLongClickAnimationStart| index: " + param1Int);
            }

            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView param1CircleMenuView) {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            public void onMenuCloseAnimationStart(@NonNull CircleMenuView param1CircleMenuView) {
                Log.d("D", "onMenuCloseAnimationStart");
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
                alphaAnimation.setDuration(200L);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationEnd(Animation param2Animation) {
                        MainActivity.this.mFloatView.setVisibility(8);
                        MainActivity.this.mFloatView.clearAnimation();
                    }

                    public void onAnimationRepeat(Animation param2Animation) {}

                    public void onAnimationStart(Animation param2Animation) {}
                });
                MainActivity.this.mFloatView.clearAnimation();
                MainActivity.this.mFloatView.startAnimation((Animation)alphaAnimation);
            }

            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView param1CircleMenuView) {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            public void onMenuOpenAnimationStart(@NonNull CircleMenuView param1CircleMenuView) {
                Log.d("D", "onMenuOpenAnimationStart");
                MainActivity.this.mFloatView.setVisibility(0);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
                alphaAnimation.setDuration(200L);
                alphaAnimation.setFillAfter(true);
                alphaAnimation.setInterpolator((Interpolator)new AccelerateInterpolator());
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationEnd(Animation param2Animation) {
                        MainActivity.this.mFloatView.clearAnimation();
                        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("config", 0);
                        if (!sharedPreferences.getBoolean("show_tips", false)) {
                            ToolTip.Builder builder = new ToolTip.Builder((Context)MainActivity.this, (View)MainActivity.this.mFeelingTipsView, (ViewGroup)MainActivity.this.mFloatView, "Record your emotion by a simple selfie", 0);
                            builder.setBackgroundColor(MainActivity.this.getResources().getColor(2131689647));
                            builder.setTextColor(Color.parseColor("#666666"));
                            builder.setAlign(2);
                            MainActivity.this.mToolTipsManager.show(builder.build());
                            MainActivity.access$002(MainActivity.this, 1);
                            sharedPreferences.edit().putBoolean("show_tips", true).commit();
                        }
                    }

                    public void onAnimationRepeat(Animation param2Animation) {}

                    public void onAnimationStart(Animation param2Animation) {}
                });
                MainActivity.this.mFloatView.clearAnimation();
                MainActivity.this.mFloatView.startAnimation((Animation)alphaAnimation);
            }
        });
        this.bottomNavigation = (AHBottomNavigation)findViewById(2131820843);
        AHBottomNavigationItem aHBottomNavigationItem1 = new AHBottomNavigationItem(2131362035, 2130838052, 2131689647);
        AHBottomNavigationItem aHBottomNavigationItem2 = new AHBottomNavigationItem(2131362034, 2130838051, 2131689647);
        AHBottomNavigationItem aHBottomNavigationItem3 = new AHBottomNavigationItem(2131362032, 2130838049, 2131689647);
        AHBottomNavigationItem aHBottomNavigationItem4 = new AHBottomNavigationItem(2131362033, 2130838050, 2131689647);
        AHBottomNavigationItem aHBottomNavigationItem5 = new AHBottomNavigationItem(2131362036, 2130838072, 2131689647);
        this.bottomNavigationItems.add(aHBottomNavigationItem1);
        this.bottomNavigationItems.add(aHBottomNavigationItem2);
        this.bottomNavigationItems.add(aHBottomNavigationItem5);
        this.bottomNavigationItems.add(aHBottomNavigationItem3);
        this.bottomNavigationItems.add(aHBottomNavigationItem4);
        this.bottomNavigation.addItems(this.bottomNavigationItems);
        this.bottomNavigation.setTitleTextSizeInSp(12.0F, 10.0F);
        this.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        this.bottomNavigation.setColored(false);
        this.bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            public boolean onTabSelected(int param1Int, boolean param1Boolean) {
                if (param1Int == 0) {
                    if (MainActivity.this.mAddBtn != null)
                        MainActivity.this.mAddBtn.close(true);
                    if (MainActivity.this.journalFragment == null) {
                        MainActivity.access$702(MainActivity.this, new JournalFragment());
                        MainActivity.this.journalFragment.setMainActivity(MainActivity.this);
                    }
                    FragmentTransaction fragmentTransaction = MainActivity.this.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(2131820923, (Fragment)MainActivity.this.journalFragment, "jouranal_fragment");
                    fragmentTransaction.commit();
                    return true;
                }
                if (param1Int == 3) {
                    if (MainActivity.this.mAddBtn != null)
                        MainActivity.this.mAddBtn.close(true);
                    if (MainActivity.this.calendarFragment == null)
                        MainActivity.access$802(MainActivity.this, new CalendarFragment());
                    FragmentTransaction fragmentTransaction = MainActivity.this.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(2131820923, (Fragment)MainActivity.this.calendarFragment, "calendar_fragment");
                    fragmentTransaction.commit();
                    return true;
                }
                if (param1Int == 1) {
                    if (MainActivity.this.mAddBtn != null)
                        MainActivity.this.mAddBtn.close(true);
                    if (MainActivity.this.reportFragment == null)
                        MainActivity.access$902(MainActivity.this, new ReportFragment());
                    FragmentTransaction fragmentTransaction = MainActivity.this.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(2131820923, (Fragment)MainActivity.this.reportFragment, "report_fragment");
                    fragmentTransaction.commit();
                    return true;
                }
                if (param1Int == 2) {
                    if (MainActivity.this.mAddBtn != null) {
                        MainActivity.this.mAddBtn.open(true);
                        return false;
                    }
                    return true;
                }
                if (param1Int == 4) {
                    if (MainActivity.this.mAddBtn != null)
                        MainActivity.this.mAddBtn.close(true);
                    if (MainActivity.this.settingFragment == null)
                        MainActivity.access$1002(MainActivity.this, new SettingFragment());
                    FragmentTransaction fragmentTransaction = MainActivity.this.getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(2131820923, (Fragment)MainActivity.this.settingFragment, "setting_fragment");
                    fragmentTransaction.commit();
                    return true;
                }
                return true;
            }
        });
        if (this.journalFragment == null) {
            this.journalFragment = new JournalFragment();
            this.journalFragment.setMainActivity(this);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(2131820923, (Fragment)this.journalFragment, "jouranal_fragment");
        fragmentTransaction.commit();
        initData();
    }

    public void takePhoto() {
        startActivity(new Intent((Context)this, FaceDetectCameraActivity.class));
    }

    public class MainHandler extends Handler {
        public MainHandler(Looper param1Looper) {
            super(param1Looper);
        }

        public void handleMessage(Message param1Message) {
            ByteArrayInputStream byteArrayInputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            super.handleMessage(param1Message);
            switch (param1Message.what) {
                default:
                    return;
                case 1:
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageHelper.loadSizeLimitedBitmapFromBitmap((Bitmap)((HashMap)param1Message.obj).get("image")).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    (new DetectionTask((Context)MainActivity.this, "MainActivity")).executeOnExecutor(Executors.newCachedThreadPool(), (Object[])new InputStream[] { byteArrayInputStream });
                    GlobalData.getMyJourneyHandler().sendEmptyMessage(99);
                    return;
                case 2:
                    GlobalData.getMyJourneyHandler().sendEmptyMessage(98);
                    return;
                case 3:
                    break;
            }
            Toast.makeText((Context)MainActivity.this, "Detection Failed", 1).show();
            GlobalData.getMyJourneyHandler().sendEmptyMessage(98);
        }
    }
}