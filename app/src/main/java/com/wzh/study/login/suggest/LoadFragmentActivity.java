package com.wzh.study.login.suggest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wzh.study.R;
import com.wzh.study.login.utils.statusbar.StatusBarUtil;

import java.util.List;

/**
 * Created by wenzhihao on 2017/8/18.
 * open a frg
 */

public class LoadFragmentActivity extends FragmentActivity {
    //fragment class
    private static Class<? extends Fragment> mLoanFragmentClass ;
    private Fragment mCurrentFragment ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_fragment);
        if (mLoanFragmentClass == null) {
            return;
        }
        StatusBarUtil.transparentStatusbarAndLayoutInsert(this, false);
        // add fragment to activity
        loadFragment(getSupportFragmentManager(), mLoanFragmentClass, R.id.contentPanel, getIntent().getExtras());
    }

    /**
     * open Fragment
     * @param context
     * @param target
     * @param bundle
     */
    public static void lunchFragment(Context context, Class<? extends Fragment> target, Bundle bundle){
        mLoanFragmentClass = target;
        Intent intent = new Intent(context, LoadFragmentActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * load fragment
     * @param manager
     * @param clz Fragment's Class
     * @param containerId id
     * @param args args
     */
    protected void loadFragment(FragmentManager manager, @NonNull Class<? extends Fragment> clz, int containerId, @Nullable Bundle args) {
        String tag = clz.getName();
        mCurrentFragment = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = manager.beginTransaction();
        if (mCurrentFragment == null) {
            try {
                mCurrentFragment = clz.newInstance();
                transaction.add(containerId, mCurrentFragment, tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mCurrentFragment.isAdded()) {
                if (mCurrentFragment.isHidden()) {
                    transaction.show(mCurrentFragment);
                }
            } else {
                transaction.add(containerId, mCurrentFragment, tag);
            }
        }

        if (mCurrentFragment != null) {
            mCurrentFragment.setArguments(args);
            hideOtherFragment(manager, transaction, mCurrentFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * hide all fragment except current fragment
     *
     * @param manager
     * @param transaction
     * @param currentFragment
     */
    private void hideOtherFragment(FragmentManager manager, FragmentTransaction transaction, Fragment currentFragment) {
        if (manager != null && transaction != null){
            List<Fragment> fragments = manager.getFragments();
            if (fragments != null && fragments.size() > 0){
                for (Fragment fragment : fragments) {
                    if (fragment != currentFragment && !fragment.isHidden()) {
                        transaction.hide(fragment);
                    }
                }
            }
        }
    }

}
