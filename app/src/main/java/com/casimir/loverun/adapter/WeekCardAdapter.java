package com.casimir.loverun.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.casimir.loverun.R;
import com.casimir.loverun.data.WeekCard;

import java.util.List;

/**
 * Created by yjh on 16/8/12.
 */
public class WeekCardAdapter extends RecyclerView.Adapter<WeekCardAdapter.ViewHolder>  {

    private List<WeekCard> dataList;
    private int resource;
    private int weekLevel;
    private int courseLevel;
    private Activity activity;

    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener{
        void OnItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void LevelUp(int weekLevel,int courseLevel){
        this.weekLevel = weekLevel;
        this.courseLevel = courseLevel;
    }

    public WeekCardAdapter(Activity activity,@NonNull List<WeekCard> list,@NonNull int layout,int weekLevel,int courseLevel) {
        this.activity = activity;
        this.dataList = list;
        this.resource = layout;
        this.weekLevel = weekLevel;
        this.courseLevel = courseLevel;
    }

    @Override
    public WeekCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeekCardAdapter.ViewHolder holder, int position) {
        Log.i("onBindViewHolder",String.valueOf(position));
        Log.i("weekLevel",String.valueOf(weekLevel));
        Log.i("courseLevel",String.valueOf(courseLevel));
        WeekCard weekCard = dataList.get(position);
        holder.weekNoView.setText(weekCard.getWeekNo());
        holder.weekTipView.setText(weekCard.getWeekTip());
        if( position+1 > weekLevel){
            holder.courseOneOverLay.setVisibility(View.VISIBLE);
            holder.courseTwoOverLay.setVisibility(View.VISIBLE);
            holder.courseThreeOverLay.setVisibility(View.VISIBLE);
            holder.courseOneLayout.setClickable(false);
            holder.courseTwoLayout.setClickable(false);
            holder.courseThreeLayout.setClickable(false);

            holder.course_one_image.setBackgroundResource(R.drawable.ic_star_rate_off);
            holder.course_two_image.setBackgroundResource(R.drawable.ic_star_rate_off);
            holder.course_three_image.setBackgroundResource(R.drawable.ic_star_rate_off);
        }else if((position+1) == weekLevel){
            switch (courseLevel){
                case 1:
                    holder.courseOneOverLay.setVisibility(View.GONE);
                    holder.courseTwoOverLay.setVisibility(View.VISIBLE);
                    holder.courseThreeOverLay.setVisibility(View.VISIBLE);
                    holder.courseOneLayout.setClickable(true);
                    holder.courseTwoLayout.setClickable(false);
                    holder.courseThreeLayout.setClickable(false);

                    holder.course_one_image.setBackgroundResource(R.drawable.ic_star_rate_off);
                    holder.course_two_image.setBackgroundResource(R.drawable.ic_star_rate_off);
                    holder.course_three_image.setBackgroundResource(R.drawable.ic_star_rate_off);

                    break;
                case 2:
                    holder.courseOneOverLay.setVisibility(View.GONE);
                    holder.courseTwoOverLay.setVisibility(View.GONE);
                    holder.courseThreeOverLay.setVisibility(View.VISIBLE);
                    holder.courseOneLayout.setClickable(false);
                    holder.courseTwoLayout.setClickable(true);
                    holder.courseThreeLayout.setClickable(false);

                    holder.course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    holder.course_two_image.setBackgroundResource(R.drawable.ic_star_rate_off);
                    holder.course_three_image.setBackgroundResource(R.drawable.ic_star_rate_off);
                    break;
                case 3:
                    holder.courseOneOverLay.setVisibility(View.GONE);
                    holder.courseTwoOverLay.setVisibility(View.GONE);
                    holder.courseThreeOverLay.setVisibility(View.GONE);
                    holder.courseOneLayout.setClickable(false);
                    holder.courseTwoLayout.setClickable(false);
                    holder.courseThreeLayout.setClickable(true);

                    holder.course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    holder.course_two_image.setBackgroundResource(R.drawable.ic_star_rate_on);
                    holder.course_three_image.setBackgroundResource(R.drawable.ic_star_rate_off);
                    break;
            }
        }else {
            holder.courseOneOverLay.setVisibility(View.GONE);
            holder.courseTwoOverLay.setVisibility(View.GONE);
            holder.courseThreeOverLay.setVisibility(View.GONE);
            holder.courseOneLayout.setClickable(false);
            holder.courseTwoLayout.setClickable(false);
            holder.courseThreeLayout.setClickable(false);

            holder.course_one_image.setBackgroundResource(R.drawable.ic_star_rate_on);
            holder.course_two_image.setBackgroundResource(R.drawable.ic_star_rate_on);
            holder.course_three_image.setBackgroundResource(R.drawable.ic_star_rate_on);
        }
        holder.courseOneFinishTime.setText(weekCard.getCourseOneFinishTime());
        holder.courseTwoFinishTime.setText(weekCard.getCourseTwoFinishTime());
        holder.courseThreeFinishTime.setText(weekCard.getCourseThreeFinishTime());
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatTextView weekNoView;
        private AppCompatTextView weekTipView;
        private RelativeLayout weekLayout;
        private RelativeLayout courseOneOverLay;
        private RelativeLayout courseTwoOverLay;
        private RelativeLayout courseThreeOverLay;
        private AppCompatImageView course_one_image;
        private AppCompatImageView course_two_image;
        private AppCompatImageView course_three_image;
        private RelativeLayout courseOneLayout;
        private RelativeLayout courseTwoLayout;
        private RelativeLayout courseThreeLayout;
        private AppCompatTextView courseOneFinishTime;
        private AppCompatTextView courseTwoFinishTime;
        private AppCompatTextView courseThreeFinishTime;


        public ViewHolder(View itemView) {
            super(itemView);
            weekNoView = (AppCompatTextView) itemView.findViewById(R.id.weekNo);
            weekTipView = (AppCompatTextView) itemView.findViewById(R.id.weekTip);
            weekLayout = (RelativeLayout) itemView.findViewById(R.id.weekLayout);

            course_one_image = (AppCompatImageView) itemView.findViewById(R.id.course_one_image);
            course_two_image = (AppCompatImageView) itemView.findViewById(R.id.course_two_image);
            course_three_image = (AppCompatImageView) itemView.findViewById(R.id.course_three_image);

            courseOneFinishTime = (AppCompatTextView) itemView.findViewById(R.id.course_one_finish_time);
            courseTwoFinishTime = (AppCompatTextView) itemView.findViewById(R.id.course_two_finish_time);
            courseThreeFinishTime = (AppCompatTextView) itemView.findViewById(R.id.course_three_finish_time);

            courseOneOverLay = (RelativeLayout) itemView.findViewById(R.id.overlay_1);
            courseTwoOverLay = (RelativeLayout) itemView.findViewById(R.id.overlay_2);
            courseThreeOverLay = (RelativeLayout) itemView.findViewById(R.id.overlay_3);

            courseOneLayout = (RelativeLayout) itemView.findViewById(R.id.course_one);
            courseTwoLayout = (RelativeLayout) itemView.findViewById(R.id.course_two);
            courseThreeLayout = (RelativeLayout) itemView.findViewById(R.id.course_three);
            courseOneLayout.setOnClickListener(this);
            courseTwoLayout.setOnClickListener(this);
            courseThreeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.OnItemClick(v,getLayoutPosition());
            }
        }
    }


}
