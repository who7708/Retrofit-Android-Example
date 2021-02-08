package com.jrdev9.sample.retrofit.main;

import android.text.TextUtils;
import android.widget.Toast;

import com.jrdev9.sample.retrofit.adapter.UsersAdapter;
import com.jrdev9.sample.retrofit.model.User;
import com.jrdev9.sample.retrofit.service.GitHubServiceWithRxjava;
import com.squareup.okhttp.OkHttpClient;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenterWithRxjava implements MainView.OnMainViewActions {

    private final WeakReference<MVPWithRxjavaActivity> mainActivityWeakReference;
    private final MainView<MVPWithRxjavaActivity> mainView;
    private final RestAdapter restAdapter;
    private UsersAdapter usersAdapter;

    public MainPresenterWithRxjava(MVPWithRxjavaActivity mainActivity) {
        mainActivityWeakReference = new WeakReference<>(mainActivity);
        mainView = new MainView<>(mainActivityWeakReference, this);
        restAdapter = getRestAdapter();
    }

    private void showInfoUser(final String nick) {
        GitHubServiceWithRxjava service = getRestAdapter().create(GitHubServiceWithRxjava.class);
        service.getInfoUser(nick)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        //Do not nothing
                    }

                    @Override
                    public void onError(Throwable error) {
                        showMessageToast(error.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        if (usersAdapter == null) {
                            usersAdapter = new UsersAdapter(user);
                        } else {
                            usersAdapter.showUser(user);
                        }
                        mainView.showUsers(usersAdapter);
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
        GitHubServiceWithRxjava service = getRestAdapter().create(GitHubServiceWithRxjava.class);
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
        GitHubServiceWithRxjava service = getRestAdapter().create(GitHubServiceWithRxjava.class);
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
        if (restAdapter != null) {
            return restAdapter;
        }
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttpClient
        //初始化okhttp
        final okhttp3.OkHttpClient client = new okhttp3.OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //添加拦截器
                .addInterceptor(httpLoggingInterceptor)
                .build();
        return new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("User-Agent", "Retrofit-Sample-App");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL).build();
    }
}
