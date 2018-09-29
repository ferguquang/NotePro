package com.ngo.ducquang.notepro.base.baseView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngo.ducquang.notepro.R;
import com.ngo.ducquang.notepro.base.LogManager;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ducqu on 9/21/2018.
 */

public abstract class BaseFragment extends Fragment {
    private WeakReference<Activity> mWeakRef;
//    private LoadingDialog mLoadingDialog;

    private ProgressBarHandler progressBarHandler;

    @Nullable
    @BindView(R.id.toolBar)
    protected Toolbar toolBar;
    @Nullable
    @BindView(R.id.title)
    protected TextView title;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mWeakRef = new WeakReference<Activity>((Activity) context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);
        progressBarHandler = new ProgressBarHandler(getContext());
        initView(view);
        return view;
    }

    protected abstract int getContentView();

    protected abstract void initView(View view);

    @Override
    public void onDetach() {
        super.onDetach();
        hideLoadingDialog();
    }

    public void showBackPressToolBar()
    {
        toolBar.setNavigationIcon(R.drawable.icon_asset_back);
        toolBar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    //    public void runOnUiThread(Runnable runnable) {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(runnable);
//        }
//    }

    public WeakReference<Activity> getmWeakRef() {
        return mWeakRef;
    }

    public Activity getActivityReference() {
        return mWeakRef != null ? mWeakRef.get() : null;
    }

    public void addFragment(BaseFragment fragment, Bundle bundle, boolean addToBackStack)
    {
//        ((BaseActivity) getActivity()).addFragment(fragment, bundle, addToBackStack);
        if (bundle != null)
        {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.add(android.R.id.content, fragment);
        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void replaceFragment(BaseFragment fragment, Bundle bundle, boolean addToBackStack)
    {
//        ((BaseActivity) getActivity()).replaceFragment(fragment, bundle, addToBackStack);
        if (bundle != null)
        {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void showDialogFragment(DialogFragment fragment)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
        {
            ft.remove(prev);
        }

        fragment.show(ft, "dialog");
    }

    public void dismissDialog()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
        {
            ft.remove(prev);
        }

        ft.commit();
    }

    public void showLoadingDialog()
    {
        try
        {
            progressBarHandler.show();
        }
        catch (Exception e)
        {
            LogManager.tagDefault().error(e.toString());
        }
    }

    public void hideLoadingDialog()
    {
        try
        {
            progressBarHandler.hide();
        }
        catch (Exception e) {
            LogManager.tagDefault().error(e.toString());
        }
    }

    protected void showToast(String text, int type)
    {
        ((BaseActivity) getActivity()).showToast(text, type);
    }

    public void startActivity(Class<?> activity, Bundle bundle, boolean clearTask)
    {
        if (getActivity() instanceof BaseActivity)
        {
            ((BaseActivity) getActivity()).startActivity(activity, bundle, clearTask);
        }
    }
}
