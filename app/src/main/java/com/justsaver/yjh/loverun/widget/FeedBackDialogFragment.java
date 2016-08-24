package com.justsaver.yjh.loverun.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.justsaver.yjh.loverun.R;
import com.maxleap.MLDataManager;
import com.maxleap.MLObject;
import com.maxleap.SaveCallback;
import com.maxleap.exception.MLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class FeedBackDialogFragment extends DialogFragment{

    @BindView(R.id.feedBackEditText) EditText mEditText;
    @BindView(R.id.feedBackButton) Button button;

    private long mLastClickTime = 0;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Activity activity;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static FeedBackDialogFragment newInstance(String param1, String param2) {
        FeedBackDialogFragment fragment = new FeedBackDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FeedBackDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
           MLObject myComment = new MLObject("feedback");
           myComment.put("content",mEditText.getText().toString());
           MLDataManager.saveInBackground(myComment,new SaveCallback(){

               @Override
               public void done(MLException e) {
                   if(e != null){
                       Toast.makeText(activity,"无法连接",Toast.LENGTH_SHORT).show();
                   }
               }
           });
           dismiss();
         //  onCloseListener.closeDialog(this);
        }else{
           Toast.makeText(getActivity(),"请输入至少10个字符",Toast.LENGTH_SHORT).show();
       }
    }
}
