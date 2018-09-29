package com.ngo.ducquang.notepro.note;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ngo.ducquang.notepro.CreateNoteFragment;
import com.ngo.ducquang.notepro.R;
import com.ngo.ducquang.notepro.base.DatabaseRoom;
import com.ngo.ducquang.notepro.base.ManagerTime;
import com.ngo.ducquang.notepro.base.view.ConfirmDialog;
import com.ngo.ducquang.notepro.base.view.OnConfirmDialogAction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ducqu on 5/13/2018.
 */

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final int TYPE_EMPTY = 0;
    private final int TYPE_ITEM = 1;

    private Context context;
    private List<NoteModel> dataList;
    private FragmentManager fragmentManager;
    private DatabaseRoom databaseRoom;

    public NoteAdapter(Context context, List<NoteModel> dataList, FragmentManager fragmentManager, DatabaseRoom databaseRoom) {
        this.context = context;
        this.dataList = dataList;
        this.fragmentManager = fragmentManager;
        this.databaseRoom = databaseRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == TYPE_EMPTY)
        {
            view = inflater.inflate(R.layout.layout_empty, parent, false);
            return new EmptyViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.layout_note_item, parent,false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder)
        {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.binding(dataList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() == 0 ? 1 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.size() == 0)
        {
            return TYPE_EMPTY;
        }

        return TYPE_ITEM;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        @BindView(R.id.rlNoteItem) RelativeLayout rlNoteItem;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.cbFavorite) CheckBox cbFavorite;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            rlNoteItem.setOnLongClickListener(this);
            rlNoteItem.setOnClickListener(this);

            cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                NoteModel noteModel = dataList.get(getAdapterPosition());
                noteModel.setFavorite(isChecked);
                databaseRoom.noteDao().update(noteModel);
            });
        }

        public void binding(NoteModel model)
        {
            name.setText(model.getContent());
            content.setText(model.getContent());
            String timeString = ManagerTime.convertToMonthDayYearHourMinuteFormat(model.getCreated());
            date.setText(timeString);
            cbFavorite.setChecked(model.isFavorite());
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId())
            {
                case R.id.rlNoteItem:
                {
                    String name = dataList.get(getAdapterPosition()).getName();
                    final int id = dataList.get(getAdapterPosition()).getId();
                    ConfirmDialog.initialize("Bạn có muốn xóa note " + name + " này không?", new OnConfirmDialogAction() {
                        @Override
                        public void onCancel() {}

                        @Override
                        public void onAccept()
                        {
                            databaseRoom.noteDao().deleteByIDNote(id);
                        }
                    }).show(fragmentManager, "");
                    break;
                }
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            CreateNoteFragment createNoteFragment = new CreateNoteFragment();
            createNoteFragment.setPosition(getAdapterPosition());
            createNoteFragment.setNoteModel(dataList.get(getAdapterPosition()));
            createNoteFragment.setUpdate(true);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
            fragmentTransaction.add(android.R.id.content, createNoteFragment, "fragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void addStore(NoteModel userStore)
    {
        dataList.add(userStore);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    public void updateItem(NoteModel userStore, int position)
    {
        dataList.set(position, userStore);
        notifyItemChanged(position);
    }
}
