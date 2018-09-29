package com.ngo.ducquang.notepro.base.baseView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ngo.ducquang.notepro.R;
import com.ngo.ducquang.notepro.base.GlobalVariables;
import com.ngo.ducquang.notepro.base.LogManager;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by ducqu on 9/21/2018.
 */

public abstract class BaseActivity extends AppCompatActivity
{
    @Nullable
    @BindView(R.id.toolBar)
    protected Toolbar toolBar;
    @Nullable
    @BindView(R.id.title)
    protected TextView title;
    @Nullable
    @BindView(R.id.appBarLayout)
    protected RelativeLayout appBarLayout;

    public MenuItem menuSearch;
    protected MenuItem menu_settings;
    public SearchView searchView;

    private ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        if (toolBar != null)
        {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        progressBarHandler = new ProgressBarHandler(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (menuSearch != null) {
            menuSearch.collapseActionView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_common, menu);
        menuSearch = menu.findItem(R.id.menuSearch);
        menu_settings = menu.findItem(R.id.menu_settings);

        searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);

        AutoCompleteTextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try
        {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.color_cursor);
        }
        catch (NoSuchFieldException e) {
            LogManager.tagDefault().error(e.toString());
        }
        catch (IllegalAccessException e) {
            LogManager.tagDefault().error(e.toString());
        }

        MenuItemCompat.setOnActionExpandListener(menuSearch, new MenuItemCompat.OnActionExpandListener()
        {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item)
            {
                title.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item)
            {
                title.setVisibility(View.VISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchOnQueryTextChange(newText);
                return true;
            }
        });

        hideMenu();
        initMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
                break;
            }
            case R.id.menu_settings:
            {
                eventClickMenuSettings();
                break;
            }
        }

        return true;
    }

//    public void enableServiceNotification()
//    {
//        ServiceManager.startService(getBaseContext());
//    }
//
//    public void disableServiceNotification()
//    {
//        ServiceManager.stopService(getBaseContext());
//    }

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

    public void addFragment(@NonNull BaseFragment fragment, @Nullable Bundle bundle, boolean addToBackStack)
    {
        if (bundle != null)
        {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.add(android.R.id.content, fragment);
        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void replaceFragment(@NonNull BaseFragment fragment, @Nullable Bundle bundle, boolean addToBackStack)
    {
        if (bundle != null)
        {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.replace(android.R.id.content, fragment);
        if (addToBackStack)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void eventClickMenuSettings()
    {
        // event click listener in click menu settings
    }

    public void hideToolBar()
    {
        if (appBarLayout != null)
        {
            appBarLayout.setVisibility(View.GONE);
        }
    }

    public void showIconBack()
    {
        if (toolBar != null)
        {
            toolBar.setNavigationIcon(R.drawable.icon_asset_back);
            toolBar.setNavigationOnClickListener(v -> finish());
        }
    }

    public void searchOnQueryTextChange(String text)
    {
        // search
    }

    public void hideMenu()
    {
        if (menuSearch != null)
        {
            menuSearch.setVisible(false);
        }
        if (menu_settings != null)
        {
            menu_settings.setVisible(false);
        }
    }

    public void hideMenuSearch()
    {
        if (menuSearch != null)
        {
            menuSearch.setVisible(false);
        }
    }

    public void showMenuSearch()
    {
        if (menuSearch != null)
        {
            menuSearch.setVisible(true);
        }
    }

    public void hideMenuSetting()
    {
        if (menu_settings != null)
        {
            menu_settings.setVisible(false);
        }
    }

    protected void showMenuSetting()
    {
        if (menu_settings != null)
        {
            menu_settings.setVisible(true);
        }
    }

    public void setTitle(String string)
    {
        title.setText(string);
    }

    public void setTitle(int id)
    {
        title.setText(id);
    }

    public void startActivity(Class<?> activity, Bundle bundle, boolean clearTask)
    {
        Intent intent = new Intent(getBaseContext(), activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (clearTask)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        startActivity(intent);
    }

    protected void showToast(String text, int type)
    {
        switch (type)
        {
            case GlobalVariables.TOAST_INFO:
                Toasty.info(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
                break;
            case GlobalVariables.TOAST_NORMAL:
                Toasty.normal(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                break;
            case GlobalVariables.TOAST_SUCCESS:
                Toasty.success(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
                break;
            case GlobalVariables.TOAST_WARNING:
                Toasty.warning(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
                break;
            case GlobalVariables.TOAST_ERRO:
                Toasty.error(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
                break;
            default:
                Toasty.info(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
                break;
        }
    }

    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initMenu(Menu menu);
}
