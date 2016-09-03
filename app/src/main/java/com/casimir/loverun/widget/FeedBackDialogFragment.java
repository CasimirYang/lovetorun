package com.casimir.loverun.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casimir.loverun.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedBackDialogFragment extends DialogFragment{

    @BindView(R.id.feedBackEditText) EditText mEditText;
    @BindView(R.id.feedBackButton) Button button;

    private long mLastClickTime = 0;
    private Activity activity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static FeedBackDialogFragment newInstance() {
        return new FeedBackDialogFragment();
    }
    public FeedBackDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_back_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.feedBackButton) void sumit(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) { //to prevent double click
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
       if(mEditText.length()>10){
            //sent feedback
           activity = getActivity();
           FeedBack feedBack = new FeedBack();
           feedBack.setMessage(mEditText.getText().toString());
           feedBack.save(new SaveListener<String>() {
               @Override
               public void done(String s, BmobException e) {
                   if(e!=null){
                       Toast.makeText(activity,"无法连接",Toast.LENGTH_SHORT).show();
                   }
               }
           });
           dismiss();
        }else{
           Toast.makeText(getActivity(),"请输入至少10个字符",Toast.LENGTH_SHORT).show();
       }
    }
    public class FeedBack extends BmobObject {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
