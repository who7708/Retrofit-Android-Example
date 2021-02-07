package com.jrdev9.sample.retrofit.main;

import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jrdev9.sample.retrofit.R;
import com.jrdev9.sample.retrofit.adapter.UsersAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainView {

    @BindView(R.id.user_edit_text)
    EditText userEditText;
    @BindView(R.id.info_button)
    Button showInfoButton;
    @BindView(R.id.followers_button)
    Button showFollowersButton;
    @BindView(R.id.followings_button)
    Button showFollowingsButton;
    @BindView(R.id.users_list)
    RecyclerView usersRecyclerList;

    private final OnMainViewActions onMainViewActions;

    public MainView(WeakReference<MainActivity> mainActivityWeakReference, OnMainViewActions onMainViewActions) {
        ButterKnife.bind(this, mainActivityWeakReference.get());
        this.onMainViewActions = onMainViewActions;
    }

    @OnClick(R.id.info_button)
    public void showInfo() {
        onMainViewActions.onShowInfo(userEditText.getText().toString());
    }

    @OnClick(R.id.followers_button)
    public void showFollowers() {
        onMainViewActions.onShowFollowers(userEditText.getText().toString());
    }

    @OnClick(R.id.followings_button)
    public void showFollowings() {
        onMainViewActions.onShowFollowings(userEditText.getText().toString());
    }

    public void showErrorOnEditText() {
        userEditText.setError("Empty field");
    }

    public void showUsers(UsersAdapter usersAdapter) {
        usersRecyclerList.setLayoutManager(new LinearLayoutManager(usersRecyclerList.getContext(), RecyclerView.VERTICAL, false));
        usersRecyclerList.setAdapter(usersAdapter);
    }

    public interface OnMainViewActions {
        void onShowInfo(String nick);

        void onShowFollowers(String nick);

        void onShowFollowings(String nick);
    }
}
