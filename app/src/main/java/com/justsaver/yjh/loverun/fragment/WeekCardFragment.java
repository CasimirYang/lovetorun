package com.justsaver.yjh.loverun.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.justsaver.yjh.loverun.Constant.PreferenceString;
import com.justsaver.yjh.loverun.R;
import com.justsaver.yjh.loverun.activity.CourseActivity;
import com.justsaver.yjh.loverun.activity.MainActivity;

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
        Bundle bundle = getArguments();
        if(bundle != null){
            position = bundle.getInt("position");
        }

        View view = inflater.inflate(R.layout.week_card_item,container,false);
        ButterKnife.bind(this,view);

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
            course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
            course_two_image.setBackgroundResource(R.drawable.ic_star_rate_on);
            course_three_image.setBackgroundResource(R.drawable.ic_star_rate_on);
        }else if( position+1 > weekLevel){
            courseOneLayout.setClickable(false);
            courseTwoLayout.setClickable(false);
            courseThreeLayout.setClickable(false);
        }else if(position+1 == weekLevel){
            switch (courseLevel){
                case 1:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseTwoLayout.setClickable(false);
                    courseThreeLayout.setClickable(false);
                    break;
                case 2:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseTwoOverLay.setVisibility(View.GONE);
                    courseOneLayout.setClickable(false);
                    courseThreeLayout.setClickable(false);
                    course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    break;
                case 3:
                    courseOneOverLay.setVisibility(View.GONE);
                    courseTwoOverLay.setVisibility(View.GONE);
                    courseThreeOverLay.setVisibility(View.GONE);
                    courseOneLayout.setClickable(false);
                    courseTwoLayout.setClickable(false);
                    course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    course_two_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    break;
            }
        }else{
            Timber.e("UI fresh error. current position:%d weekLevel:%d courseLevel:%d",position,weekLevel,courseLevel);
        }

        return view;
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
        startActivityForResult(intent, MainActivity.START_RUN);
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
