package com.jrdev9.sample.retrofit.main;

import android.text.TextUtils;
import android.widget.Toast;

import com.jrdev9.sample.retrofit.adapter.UsersAdapter;
import com.jrdev9.sample.retrofit.model.User;
import com.jrdev9.sample.retrofit.service.GitHubService;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter implements MainView.OnMainViewActions {

    private final WeakReference<MainActivity> mainActivityWeakReference;
    private final MainView mainView;
    private final RestAdapter restAdapter;
    private UsersAdapter usersAdapter;

    public MainPresenter(MainActivity mainActivity) {
        mainActivityWeakReference = new WeakReference<>(mainActivity);
        mainView = new MainView(mainActivityWeakReference, this);
        restAdapter = getRestAdapter();
    }

    private void showInfoUser(final String nick) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", "Retrofit-Sample-App");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        GitHubService service = restAdapter.create(GitHubService.class);
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
                if (error.getResponse() != null && error.getResponse().getStatus() == 404) {
                    showMessageToast("User not found");
                } else {
                    showMessageToast("Connection failed");
                }
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
        GitHubService service = getRestAdapter().create(GitHubService.class);
        service.getFollowersByUser(nick)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        //Do not nothing
                    }

                    @Override
                    public void onError(Throwable error) {
                        showMessageToast(error.getMessage());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        if (usersAdapter == null) {
                            usersAdapter = new UsersAdapter(users);
                        } else {
                            usersAdapter.showUsers(users);
                        }
                        mainView.showUsers(usersAdapter);
                    }
                });
    }

    @Override
    public void onShowFollowings(String nick) {
        GitHubService service = getRestAdapter().create(GitHubService.class);
        service.getFollowingsByUser(nick)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        //Do not nothing
                    }

                    @Override
                    public void onError(Throwable error) {
                        showMessageToast(error.getMessage());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        if (usersAdapter == null) {
                            usersAdapter = new UsersAdapter(users);
                        } else {
                            usersAdapter.showUsers(users);
                        }
                        mainView.showUsers(usersAdapter);
                    }
                });
    }

    private void showMessageToast(String text) {
        Toast.makeText(mainActivityWeakReference.get().getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    private RestAdapter getRestAdapter() {
        return restAdapter != null ? restAdapter :
                new RestAdapter.Builder()
                        .setEndpoint("https://api.github.com")
                        .setClient(new OkClient(new OkHttpClient()))
                        .setLogLevel(RestAdapter.LogLevel.FULL).build();
    }
}
