package com.justsaver.yjh.loverun.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.justsaver.yjh.loverun.Constant.PreferenceString;
import com.justsaver.yjh.loverun.R;
import com.justsaver.yjh.loverun.activity.CourseActivity;
import com.justsaver.yjh.loverun.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by yjh on 16/8/24.
 */
public class WeekCardFragment extends Fragment {

    private int position;
    private int weekLevel;
    private int courseLevel;
    private static boolean forceRefresh = false;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Fragment newInstance(int position) {
        WeekCardFragment f = new WeekCardFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate %d",position);
    }

    @BindView(R.id.weekNo) AppCompatTextView weekNoView;
    @BindView(R.id.weekTip) AppCompatTextView weekTipView;
    @BindView(R.id.weekLayout) RelativeLayout weekLayout;

    @BindView(R.id.course_one_image) AppCompatImageView course_one_image;
    @BindView(R.id.course_two_image) AppCompatImageView course_two_image;
    @BindView(R.id.course_three_image) AppCompatImageView course_three_image;

    @BindView(R.id.course_one_finish_time) AppCompatTextView courseOneFinishTime;
    @BindView(R.id.course_two_finish_time) AppCompatTextView courseTwoFinishTime;
    @BindView(R.id.course_three_finish_time) AppCompatTextView courseThreeFinishTime;


    @BindView(R.id.overlay_1) RelativeLayout courseOneOverLay;
    @BindView(R.id.overlay_2) RelativeLayout courseTwoOverLay;
    @BindView(R.id.overlay_3) RelativeLayout courseThreeOverLay;

    @BindView(R.id.course_one) RelativeLayout courseOneLayout;
    @BindView(R.id.course_two) RelativeLayout courseTwoLayout;
    @BindView(R.id.course_three) RelativeLayout courseThreeLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.i("onCreateView %d",position);
        Bundle bundle = getArguments();
        if(bundle != null){
            position = bundle.getInt("position");
        }

        View view = inflater.inflate(R.layout.week_card_item,container,false);
        ButterKnife.bind(this,view);
        refreshUI();
        return view;
    }

    private void refreshUI(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PreferenceString.userInfo, Context.MODE_PRIVATE);
        weekLevel = sharedPreferences.getInt(PreferenceString.weekLevel,1);
        courseLevel = sharedPreferences.getInt(PreferenceString.courseLevel,1);
        int weekNo = position +1;
        weekNoView.setText("第"+weekNo+"周");
        weekTipView.setText(sharedPreferences.getString(weekNo+"_0_text",""));
        courseOneFinishTime.setText(sharedPreferences.getString(weekNo+"_1",""));
        courseTwoFinishTime.setText(sharedPreferences.getString(weekNo+"_2",""));
        courseThreeFinishTime.setText(sharedPreferences.getString(weekNo+"_3",""));
        Timber.i("onCreateView position:%d weekLevel:%d courseLevel:%d",position,weekLevel,courseLevel);
        if( position+1 < weekLevel){
            courseOneLayout.setClickable(false);
            courseTwoLayout.setClickable(false);
            courseThreeLayout.setClickable(false);
            courseOneOverLay.setVisibility(View.GONE);
            courseTwoOverLay.setVisibility(View.GONE);
            courseThreeOverLay.setVisibility(View.GONE);
            course_one_image.setImageResource(R.drawable.ic_star_rate_on);
            course_two_image.setImageResource(R.drawable.ic_star_rate_on);
            course_three_image.setImageResource(R.drawable.ic_star_rate_on);
        }else if( position+1 > weekLevel){
            courseOneLayout.setClickable(false);
            courseTwoLayout.setClickable(false);
            courseThreeLayout.setClickable(false);
        }else if(position+1 == weekLevel){
            switch (courseLevel){
                case 1:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseOneLayout.setClickable(true);
                    courseTwoLayout.setClickable(false);
                    courseThreeLayout.setClickable(false);
                    break;
                case 2:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseTwoOverLay.setVisibility(View.GONE);
                    courseOneLayout.setClickable(false);
                    courseOneLayout.setClickable(false);
                    courseTwoLayout.setClickable(true);
                    course_one_image.setImageResource(R.drawable.ic_star_rate_on);
                    break;
                case 3:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseTwoOverLay.setVisibility(View.GONE);
                    courseThreeOverLay.setVisibility(View.GONE);
                    courseOneLayout.setClickable(false);
                    courseTwoLayout.setClickable(false);
                    courseThreeLayout.setClickable(true);
                    course_one_image.setImageResource(R.drawable.ic_star_rate_on);
                    course_two_image.setImageResource(R.drawable.ic_star_rate_on);
                    break;
            }
        }else{
            Timber.e("UI fresh error. current position:%d weekLevel:%d courseLevel:%d",position,weekLevel,courseLevel);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Timber.i("onViewCreated %d",position);
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.course_one) void ClickCourseOne(){
        onCourseClick();
    }
    @OnClick(R.id.course_two) void ClickCourseTwo(){
        onCourseClick();
    }
    @OnClick(R.id.course_three) void ClickCourseThree(){
        onCourseClick();
    }

    private void onCourseClick(){
        Intent intent = new Intent(getActivity(),CourseActivity.class);
        intent.putExtra(PreferenceString.weekLevel,weekLevel);
        intent.putExtra(PreferenceString.courseLevel,courseLevel);
        Timber.i("%s",getActivity().toString());
        startActivityForResult(intent, MainActivity.START_RUN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        Timber.i("from fragment onActivityResult %d,%d ",requestCode,resultCode);
        if(requestCode == MainActivity.START_RUN &&  resultCode == getActivity().RESULT_OK){
            if(requestCode == MainActivity.START_RUN &&  resultCode == getActivity().RESULT_OK){
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PreferenceString.userInfo, Context.MODE_PRIVATE).edit();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getDefault());
                String currentDateAndTime = sdf.format(new Date());
                editor.putString(weekLevel+"_"+courseLevel,currentDateAndTime);
                Log.i("save level",weekLevel+"_"+courseLevel);
                if(courseLevel == 3){
                    weekLevel = weekLevel+1;
                    courseLevel = 1;
                    forceRefresh = true;
                }else{
                    courseLevel = courseLevel+1;
                }
                editor.putInt(PreferenceString.weekLevel,weekLevel);
                editor.putInt(PreferenceString.courseLevel,courseLevel);
                editor.apply();
                if(weekLevel > 13){
                    //todo  popup message: finish all course
                }
            }
            refreshUI();
        }
    }

    @Override
    public void onResume() {
        Timber.i("onResume %d",position);
        super.onResume();
    }

    @Override
    public void onStart() {

        if(forceRefresh && (weekLevel == position)){
            refreshUI();
            forceRefresh = false;
        }
        Timber.i("onStart %d,%d,%s",weekLevel,position,forceRefresh);
        super.onStart();
    }

    //todo  delete
    @Override
    public void onStop() {
        Timber.i("onStop %d",position);
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        Timber.i("onDestroyView %d",position);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Timber.i("onDestroy  %d",position);
        super.onDestroy();
    }
}
