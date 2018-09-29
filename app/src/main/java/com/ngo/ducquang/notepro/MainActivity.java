package com.ngo.ducquang.notepro;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.ngo.ducquang.notepro.base.DatabaseRoom;
import com.ngo.ducquang.notepro.base.EventBusManager;
import com.ngo.ducquang.notepro.base.baseView.BaseActivity;
import com.ngo.ducquang.notepro.note.EventCreateOrUpdateNote;
import com.ngo.ducquang.notepro.note.NoteAdapter;
import com.ngo.ducquang.notepro.note.NoteModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.recyclerViewListNote) RecyclerView recyclerViewListNote;
    @BindView(R.id.fab) FloatingActionButton fab;

    private NoteAdapter adapter;
    private List<NoteModel> dataList = new ArrayList<>();

    private DatabaseRoom databaseRoom;

    private InterstitialAd mInterstitialAd;

    private SubcriberMainActivity subcriberMainActivity = new SubcriberMainActivity()
    {
        @Override
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onCreateOrUpdateNote(EventCreateOrUpdateNote event) {
            handleCreateOrUpdateNote(event);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        title.setText("GHI CHÚ");
        databaseRoom = DatabaseRoom.getAppDatabase(getApplicationContext());

        fab.setOnClickListener(this);

        dataList = databaseRoom.noteDao().getAll();
        initData();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.idAdmob));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void initMenu(Menu menu) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusManager.instance().register(subcriberMainActivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusManager.instance().unregister(subcriberMainActivity);
    }

    private void initData()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        adapter = new NoteAdapter(getApplicationContext(), dataList, getSupportFragmentManager(), databaseRoom);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewListNote.setLayoutManager(linearLayoutManager);
        recyclerViewListNote.setHasFixedSize(true);
        recyclerViewListNote.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fab:
            {
                CreateNoteFragment createNewCouponFragment = new CreateNoteFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                fragmentTransaction.add(android.R.id.content, createNewCouponFragment, "fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
        }
    }


    private void handleCreateOrUpdateNote(EventCreateOrUpdateNote event) {
        if (event.isAdd())
        {

        }
        else
        {

        }
    }
}
