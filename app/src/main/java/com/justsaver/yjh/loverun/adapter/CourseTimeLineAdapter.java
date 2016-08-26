package com.justsaver.yjh.loverun.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justsaver.yjh.loverun.R;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Created by yjh on 16/8/14.
 */
public class CourseTimeLineAdapter extends RecyclerView.Adapter<CourseTimeLineAdapter.ViewHolder>{

    private final int REST_TIME = 0;
    private final int RUN_TIME = 1;
    private int currentProgress = -1;
    private long remainTime = -2L;
    private Context context;


    private List<Long> timeList;

    public CourseTimeLineAdapter(@NonNull  List<Long> timeList,@NonNull Context context) {
        this.timeList = timeList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if((position+2)%2>0){//奇数, odd item
            return REST_TIME;
        }else{ //偶数,  even item
            return RUN_TIME;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resoureId;
        if(REST_TIME == viewType){
            resoureId = R.layout.course_timeline_rest_item;
        }else {
            resoureId = R.layout.course_timeline_run_item;
        }
       View view = LayoutInflater.from(parent.getContext()).inflate(resoureId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Long mills = timeList.get(position);
        Integer minutes = Integer.parseInt(Long.toString(mills/1000/60));
        String textValue;
        if(getItemViewType(position) == REST_TIME){
            textValue = context.getString(R.string.rest_timeFormat,minutes);
        }else{
            textValue = context.getString(R.string.run_timeFormat,minutes);
        }
        holder.timeDisplay.setText(textValue);

        Log.i("onBindViewHolder","before cal position:"+position); 
        Log.i("onBindViewHolder","before cal remainTime:"+remainTime); 
        Log.i("onBindViewHolder","before cal currentProgress:"+currentProgress);  
        CircleProgressView circleProgressView = holder.progressBar;
        long expiredTime = 0L;
        if(remainTime != -2L){ //resume moment
            expiredTime = remainTime;
            long temp =0;
            for(int i=timeList.size()-1; i>=0; i--){
                temp = temp + timeList.get(i);
                if(remainTime < temp){
                    currentProgress = i;
                    expiredTime = temp-remainTime;
                    break;
                }
            }
        }
        Log.i("onBindViewHolder","after cal currentProgress:"+currentProgress); 
        Log.i("onBindViewHolder","after cal expiredTime:"+expiredTime);
        if(currentProgress > position){
            circleProgressView.setValue(100f);
        }else if(currentProgress == position){
            long animationDuration = timeList.get(position)-expiredTime;
            expiredTime = (expiredTime*100)/(timeList.get(position));
            Log.i("onBindViewHolder","animationDuration:"+animationDuration);
            Log.i("onBindViewHolder","expiredTime:"+expiredTime);
            circleProgressView.setValueAnimated(Float.parseFloat(Long.toString(expiredTime)),100f,animationDuration);
        }else{
            circleProgressView.setValue(0f);
        }

    }


    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleProgressView progressBar;
        private AppCompatTextView timeDisplay;
        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = (CircleProgressView) itemView.findViewById(R.id.course_progress);
            timeDisplay = (AppCompatTextView) itemView.findViewById(R.id.timeDisplay);
        }
    }

}
