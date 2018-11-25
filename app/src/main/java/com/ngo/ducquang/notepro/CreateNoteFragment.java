package com.ngo.ducquang.notepro;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.ngo.ducquang.notepro.base.DatabaseRoom;
import com.ngo.ducquang.notepro.base.EventBusManager;
import com.ngo.ducquang.notepro.base.GlobalVariables;
import com.ngo.ducquang.notepro.base.KeyboardUtils;
import com.ngo.ducquang.notepro.base.ManagerTime;
import com.ngo.ducquang.notepro.base.baseView.BaseFragment;
import com.ngo.ducquang.notepro.base.view.ConfirmDialog;
import com.ngo.ducquang.notepro.base.view.OnConfirmDialogAction;
import com.ngo.ducquang.notepro.note.EventCreateOrUpdateNote;
import com.ngo.ducquang.notepro.note.NoteModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.LogManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ducqu on 7/28/2018.
 */

public class CreateNoteFragment extends BaseFragment implements View.OnClickListener
{
    public static final int REQUEST_CODE_SPEECH_OUTPUT = 31;

    @BindView(R.id.toolBar) Toolbar toolBar;
    @BindView(R.id.title) TextView title;

    @BindView(R.id.timeCreated) TextView timeCreated;
    @BindView(R.id.llCreateNoteMain) LinearLayout llCreateNoteMain;
    @BindView(R.id.contentNote) EditText contentNote;
    @BindView(R.id.nameNote) EditText nameNote;
    @BindView(R.id.fabDone) FloatingActionButton fabDone;
    @BindView(R.id.imgMic) ImageView imgMic;
    @BindView(R.id.tts) ImageView tts;
    @BindView(R.id.adView) AdView adView;

    private NoteModel noteModel;
    private boolean isUpdate = false;
    int position = -1;

    TextToSpeech t1;

    private InterstitialAd mInterstitialAd;

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected int getContentView() {
        return R.layout.create_note_fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View view)
    {
        title.setText(getString(R.string.createNewNote));
        toolBar.setNavigationIcon(R.drawable.icon_asset_back);
        toolBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        long time= System.currentTimeMillis();
        String timeString = ManagerTime.convertToMonthDayYearHourMinuteFormat(time);
        timeCreated.setText(timeString);

        llCreateNoteMain.setOnClickListener(this);
        fabDone.setOnClickListener(this);
        imgMic.setOnClickListener(this);

        if (isUpdate && noteModel != null)
        {
            timeCreated.setText(ManagerTime.convertToMonthDayYearHourMinuteFormat(noteModel.getCreated()));
            contentNote.setText(noteModel.getContent());
            nameNote.setText(noteModel.getName());
        }

        MobileAds.initialize(getContext(), getString(R.string.idAdmob4));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        MobileAds.initialize(getContext(), getString(R.string.idAdmob3));

//        mInterstitialAd = new InterstitialAd(getContext());
//        mInterstitialAd.setAdUnitId(getString(R.string.idAdmob3));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        t1 = new TextToSpeech(getContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.getDefault());
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }
            }
        });

        tts.setOnClickListener(v -> {
            String toSpeak = contentNote.getText().toString();
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_SPEECH_OUTPUT:
                {
                    if (data != null)
                    {
                        ArrayList<String> voiceIntent = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if(contentNote.getText().length() != 0)
                        {
                            contentNote.setText(contentNote.getText() + " " + voiceIntent.get(0));
                        }
                        else
                        {
                            contentNote.setText(voiceIntent.get(0)+"");
                        }

                        contentNote.setSelection(contentNote.getText().length());
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llCreateNoteMain:
            {
                contentNote.requestFocus();
                KeyboardUtils.showSoftInput(contentNote, getContext());
                break;
            }
            case R.id.fabDone:
            {
                KeyboardUtils.showSoftInput(contentNote, getContext());

                String name = nameNote.getText().toString();
                String content = contentNote.getText().toString();
                long created = System.currentTimeMillis();

                if (isUpdate)
                {
                    noteModel.setName(name);
                    noteModel.setContent(content);
                    noteModel.setEditFinal(created);
                }
                else
                {
                    noteModel = new NoteModel();
                    noteModel.setName(name);
                    noteModel.setContent(content);
                    noteModel.setCreated(created);
                }

                if (isUpdate)
                {
                    EventCreateOrUpdateNote eventCreateOrUpdateNote = new EventCreateOrUpdateNote(noteModel, true);
                    eventCreateOrUpdateNote.setPosition(position);
                    EventBusManager.instance().post(eventCreateOrUpdateNote);
                    DatabaseRoom.getAppDatabase(getContext()).noteDao().update(noteModel);
                    showToast(getString(R.string.editSuccess), GlobalVariables.TOAST_SUCCESS);
                }
                else
                {
                    EventBusManager.instance().post(new EventCreateOrUpdateNote(noteModel, false));
                    DatabaseRoom.getAppDatabase(getContext()).noteDao().insert(noteModel);

                    showToast(getString(R.string.createSuccess), GlobalVariables.TOAST_SUCCESS);
                }
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }

                getActivity().onBackPressed();
                break;
            }
            case R.id.imgMic:
            {
                Intent intentVoice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentVoice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intentVoice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intentVoice.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.speakNow));

                try
                {
                    startActivityForResult(intentVoice, REQUEST_CODE_SPEECH_OUTPUT);
                }
                catch (ActivityNotFoundException tim)
                {
                    // showSnackBar(R.string.doNotSupportSpeech);
                }

                break;
            }
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
