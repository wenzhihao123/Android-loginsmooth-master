package com.wzh.study.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.wzh.study.R;
import com.wzh.study.login.deprecated.OtherLoginAct;
import com.wzh.study.login.suggest.LoadFragmentActivity;
import com.wzh.study.login.suggest.LoginActivity;
import com.wzh.study.login.suggest.LoginFragment;
import com.wzh.study.login.utils.statusbar.StatusBarUtil;

/**
 * Created by wenzhihao on 2017/8/18.
 */

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparentStatusbarAndLayoutInsert(this, true);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        int id = view.getId() ;
        if (id == R.id.old_version){
            startActivity(new Intent(this, OtherLoginAct.class));
        }else if (id == R.id.latest_version){
            startActivity(new Intent(this, LoginActivity.class));
        }else if (id == R.id.latest_fragment_version){
            LoadFragmentActivity.lunchFragment(this, LoginFragment.class,null);
        }
    }
}
