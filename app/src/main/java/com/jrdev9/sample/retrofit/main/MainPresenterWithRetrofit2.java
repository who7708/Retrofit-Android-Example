package com.jrdev9.sample.retrofit.main;

import android.text.TextUtils;
import android.widget.Toast;

import com.jrdev9.sample.retrofit.adapter.UsersAdapter;
import com.jrdev9.sample.retrofit.model.User;
import com.jrdev9.sample.retrofit.service.GitHubServiceWithRetrofit2;

import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenterWithRetrofit2 implements MainView.OnMainViewActions {

    private final WeakReference<MVPWithRetrofit2Activity> mainActivityWeakReference;
    private final MainView<MVPWithRetrofit2Activity> mainView;
    private final Retrofit restAdapter;
    private UsersAdapter usersAdapter;

    public MainPresenterWithRetrofit2(MVPWithRetrofit2Activity mainActivity) {
        mainActivityWeakReference = new WeakReference<>(mainActivity);
        mainView = new MainView<>(mainActivityWeakReference, this);
        restAdapter = getRestAdapter();
    }

    private void showInfoUser(final String nick) {
        GitHubServiceWithRetrofit2 service = getRestAdapter().create(GitHubServiceWithRetrofit2.class);
        service.getInfoUser(nick).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (usersAdapter == null) {
                    usersAdapter = new UsersAdapter(response.body());
                } else {
                    usersAdapter.showUser(response.body());
                }
                mainView.showUsers(usersAdapter);
            }

            @Override
            public void onFailure(Call<User> call, Throwable error) {
                showMessageToast(error.getMessage());
            }
        });
    }

    @Override
    public void onShowInfo(String nick) {
        if (TextUtils.isEmpty(nick)) {
            mainView.showErrorOnEditText();
        } else {
            showInfoUser(nick);
        }
    }

    /**
     * 此版本以下才可以在主线程上使用同步请求
     */
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onShowFollowers(String nick) {
        GitHubServiceWithRetrofit2 service = getRestAdapter().create(GitHubServiceWithRetrofit2.class);
        service.getFollowersByUser(nick).enqueue(getUsersCallback());
    }

    @Override
    public void onShowFollowings(String nick) {
        GitHubServiceWithRetrofit2 service = getRestAdapter().create(GitHubServiceWithRetrofit2.class);
        service.getFollowingsByUser(nick).enqueue(getUsersCallback());
    }

    private Callback<List<User>> getUsersCallback() {
        return new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (usersAdapter == null) {
                    usersAdapter = new UsersAdapter(response.body());
                } else {
                    usersAdapter.showUsers(response.body());
                }
                mainView.showUsers(usersAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable e) {
                showMessageToast(e.getMessage());
            }
        };
    }

    private void showMessageToast(String text) {
        Toast.makeText(mainActivityWeakReference.get().getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * retrofit2
     */
    private Retrofit getRestAdapter() {
        return restAdapter != null ? restAdapter :
                new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .client(new OkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
    }

}
