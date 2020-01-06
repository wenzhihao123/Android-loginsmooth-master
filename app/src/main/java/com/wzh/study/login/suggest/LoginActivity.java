package com.wzh.study.login.suggest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.wzh.study.R;
import com.wzh.study.login.utils.KeyboardWatcher;
import com.wzh.study.login.utils.ScreenUtils;
import com.wzh.study.login.utils.statusbar.StatusBarUtil;
import com.wzh.study.login.widget.DrawableTextView;

/**
 * Created by WZH on 2017/3/25.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener, KeyboardWatcher.SoftKeyboardStateListener, View.OnFocusChangeListener {

    private static final int duration = 300;

    private DrawableTextView mTopImageView;

    private EditText mMobileEditText;
    private EditText mPasswordEditText;
    private ImageView mCleanPhoneImageView;
    private ImageView mCleanPasswordImageView;
    private ImageView mShowPasswordImageView;

    //view for slide animation
    private View mSlideContent;

    private int mRealScreenHeight = 0;

    //logo scaleRatio ratio
    private float scaleRatio = 0.8f;

    private KeyboardWatcher keyboardWatcher;

    //the position of mSlideContent on screen Y
    private int mSlideViewY = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();

        StatusBarUtil.transparentStatusbarAndLayoutInsert(this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        keyboardWatcher = new KeyboardWatcher(findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

    private void initView() {
        mTopImageView = findViewById(R.id.image_logo);
        mMobileEditText = findViewById(R.id.et_mobile);
        mPasswordEditText = findViewById(R.id.et_password);
        mCleanPhoneImageView = findViewById(R.id.iv_clean_phone);
        mCleanPasswordImageView = findViewById(R.id.clean_password);
        mShowPasswordImageView = findViewById(R.id.iv_show_pwd);
        mSlideContent = findViewById(R.id.slide_content);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.root).setBackgroundResource(R.drawable.fullscreen_redenve_back);

        mRealScreenHeight = ScreenUtils.getRealScreenHeight(this);
    }

    private void initListener() {
        mCleanPhoneImageView.setOnClickListener(this);
        mCleanPasswordImageView.setOnClickListener(this);
        mShowPasswordImageView.setOnClickListener(this);
        //TODO mMobileEditText and mPasswordEditText have different InputType, which will lead to the difference of KeyboardSize
        //TODO if they have same InputType, you can delete setOnFocusChangeListener
        mMobileEditText.setOnFocusChangeListener(this);
        mPasswordEditText.setOnFocusChangeListener(this);

        mMobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPhoneImageView.getVisibility() == View.GONE) {
                    mCleanPhoneImageView.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPhoneImageView.setVisibility(View.GONE);
                }
            }
        });
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPasswordImageView.getVisibility() == View.GONE) {
                    mCleanPasswordImageView.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPasswordImageView.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(LoginActivity.this, R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    mPasswordEditText.setSelection(s.length());
                }
            }
        });
    }

    /**
     * play animator when softkeyboard opened
     *
     * @param logoImage
     * @param mSlideContent
     * @param logoSlideDist
     */
    public void setViewAnimatorWhenKeyboardOpened(View logoImage, View mSlideContent, float logoSlideDist) {
        logoImage.setPivotY(logoImage.getHeight());
        logoImage.setPivotX(0);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, 1.0f, scaleRatio);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, 1.0f, scaleRatio);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(logoImage, View.TRANSLATION_Y, 0.0f, -logoSlideDist);
        ObjectAnimator mContentAnimatorTranslateY = ObjectAnimator.ofFloat(mSlideContent, View.TRANSLATION_Y, 0.0f, -logoSlideDist);

        mAnimatorSet.play(mContentAnimatorTranslateY)
                .with(mAnimatorTranslateY)
                .with(mAnimatorScaleX)
                .with(mAnimatorScaleY);

        mAnimatorSet.setDuration(duration);
        mAnimatorSet.start();
    }

    /**
     * play animator when softkeyboard closed
     *
     * @param logoImage
     * @param mSlideContent
     */
    public void setViewAnimatorWhenKeyboardClosed(View logoImage, View mSlideContent) {
        if (logoImage.getTranslationY() == 0) {
            return;
        }
        logoImage.setPivotY(logoImage.getHeight());
        logoImage.setPivotX(0);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, scaleRatio, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, scaleRatio, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(logoImage, View.TRANSLATION_Y, logoImage.getTranslationY(), 0);
        ObjectAnimator mContentAnimatorTranslateY = ObjectAnimator.ofFloat(mSlideContent, View.TRANSLATION_Y, mSlideContent.getTranslationY(), 0);

        mAnimatorSet.play(mContentAnimatorTranslateY)
                .with(mAnimatorTranslateY)
                .with(mAnimatorScaleX)
                .with(mAnimatorScaleY);

        mAnimatorSet.setDuration(duration);
        mAnimatorSet.start();

    }

    private boolean flag = false;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_clean_phone:
                mMobileEditText.setText("");
                break;
            case R.id.clean_password:
                mPasswordEditText.setText("");
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_show_pwd:
                if (flag == true) {
                    mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPasswordImageView.setImageResource(R.drawable.ic_pass_gone);
                    flag = false;
                } else {
                    mPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPasswordImageView.setImageResource(R.drawable.ic_pass_visuable);
                    flag = true;
                }
                String pwd = mPasswordEditText.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    mPasswordEditText.setSelection(pwd.length());
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        int[] location = new int[2];
        //get mSlideContent's location on screen
        mSlideContent.getLocationOnScreen(location);
        if (mSlideViewY == 0) {
            mSlideViewY = location[1];
        }
        int bottom = mRealScreenHeight - (mSlideViewY + mSlideContent.getHeight());

        if (keyboardSize > bottom) {
            int slideDist = keyboardSize - bottom;
            setViewAnimatorWhenKeyboardOpened(mTopImageView, mSlideContent, slideDist);
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        setViewAnimatorWhenKeyboardClosed(mTopImageView, mSlideContent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (keyboardWatcher.isSoftKeyboardOpened()){
                keyboardWatcher.setIsSoftKeyboardOpened(true);
            } else {
                keyboardWatcher.setIsSoftKeyboardOpened(false);
            }
        }
    }
}
