package com.wzh.study.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.wzh.study.R;
import com.wzh.study.login.suggest.LoadFragmentActivity;
import com.wzh.study.login.suggest.LoginFragment;
import com.wzh.study.login.suggest.LoginAct;

/**
 * Created by wenzhihao on 2017/8/18.
 */

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        int id = view.getId() ;
        if (id == R.id.old_version){
            startActivity(new Intent(this,OtherLoginAct.class));
        }else if (id == R.id.latest_version){
            startActivity(new Intent(this,LoginAct.class));
        }else if (id == R.id.latest_fragment_version){
            LoadFragmentActivity.lunchFragment(this, LoginFragment.class,null);
        }
    }
}
