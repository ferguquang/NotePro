package com.ngo.ducquang.notepro.base.baseView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ngo.ducquang.notepro.R;
import com.ngo.ducquang.notepro.base.LogManager;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by ducqu on 9/17/2018.
 */

public abstract class BaseActivity extends AppCompatActivity
{
    @BindView(R.id.toolbar)
    protected Toolbar toolBar;
    @BindView(R.id.title)
    protected TextView title;

    public MenuItem menuSearch;
    protected MenuItem menu_settings;
    public SearchView searchView;

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
            toolBar.setNavigationIcon(R.drawable.icon_asset_back);
            toolBar.setNavigationOnClickListener(v -> finish());
        }

        initView();
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
                onBackPressed();
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

    protected void addFragment(@NonNull BaseFragment fragment, @Nullable Bundle bundle, boolean addToBackStack)
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

    protected void replaceFragment(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean addToBackStack)
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

    public void searchOnQueryTextChange(String text)
    {
        // search
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

    protected void showToast(String text)
    {
        Toasty.info(getApplicationContext(), text, Toast.LENGTH_LONG, true).show();
    }

    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initMenu(Menu menu);
}
