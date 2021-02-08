package com.jrdev9.sample.retrofit.main;

import android.text.TextUtils;
import android.widget.Toast;

import com.jrdev9.sample.retrofit.adapter.UsersAdapter;
import com.jrdev9.sample.retrofit.model.User;
import com.jrdev9.sample.retrofit.service.GitHubServiceWithRetrofit1;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * retrofit1 实现
 */
public class MainPresenterWithRetrofit1 implements MainView.OnMainViewActions {

    private final WeakReference<MVPWithRetrofit1Activity> mainActivityWeakReference;
    private final MainView<MVPWithRetrofit1Activity> mainView;
    private final RestAdapter restAdapter;
    private UsersAdapter usersAdapter;

    public MainPresenterWithRetrofit1(MVPWithRetrofit1Activity mainActivity) {
        mainActivityWeakReference = new WeakReference<>(mainActivity);
        mainView = new MainView<>(mainActivityWeakReference, this);
        restAdapter = getRestAdapter();
    }

    private void showInfoUser(final String nick) {
        GitHubServiceWithRetrofit1 service = getRestAdapter().create(GitHubServiceWithRetrofit1.class);
        service.getInfoUser(nick, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (usersAdapter == null) {
                    usersAdapter = new UsersAdapter(user);
                } else {
                    usersAdapter.showUser(user);
                }
                mainView.showUsers(usersAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
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
        GitHubServiceWithRetrofit1 service = getRestAdapter().create(GitHubServiceWithRetrofit1.class);
        service.getFollowersByUser(nick, getUsersCallback());
    }

    @Override
    public void onShowFollowings(String nick) {
        GitHubServiceWithRetrofit1 service = getRestAdapter().create(GitHubServiceWithRetrofit1.class);
        service.getFollowingsByUser(nick, getUsersCallback());
    }

    private Callback<List<User>> getUsersCallback() {
        return new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                if (usersAdapter == null) {
                    usersAdapter = new UsersAdapter(users);
                } else {
                    usersAdapter.showUsers(users);
                }
                mainView.showUsers(usersAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                showMessageToast(error.getMessage());
            }
        };
    }

    private void showMessageToast(String text) {
        Toast.makeText(mainActivityWeakReference.get().getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * retrofit1.x
     */
    private RestAdapter getRestAdapter() {
        return restAdapter != null ? restAdapter :
                new RestAdapter.Builder()
                        .setEndpoint("https://api.github.com")
                        .setClient(new OkClient(new OkHttpClient()))
                        .setRequestInterceptor(new RequestInterceptor() {
                            @Override
                            public void intercept(RequestFacade request) {
                                request.addHeader("User-Agent", "Retrofit-Sample-App");
                            }
                        })
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
    }
}
