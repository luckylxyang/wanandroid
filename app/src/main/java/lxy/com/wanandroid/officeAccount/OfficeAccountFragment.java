package lxy.com.wanandroid.officeAccount;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.HomeAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Creator : lxy
 * date: 2019/3/18
 */

public class OfficeAccountFragment extends Fragment {

    private RecyclerView rv;
    private HomeAdapter adapter;
    private List<ArticleModel> list = new ArrayList<>();
    private int ocId;
    private int page = 0;
    private int totalPages = 0;

    public static OfficeAccountFragment newInstance(int ocId) {

        Bundle args = new Bundle();
        args.putInt("officeAccountId",ocId);
        OfficeAccountFragment fragment = new OfficeAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_office_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            ocId = getArguments().getInt("officeAccountId");
        }
        initView(view);
        getDataByServer();
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.office_account_rv);
        adapter = new HomeAdapter(R.layout.item_home_article,list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        
    }

    private void getDataByServer(){
//        WaitDialog.show(getContext());
        NetworkManager.getManager().getServer().getOfficeAccountArticle(ocId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel model) {
                        if (model.getErrorCode() != 0){
                            ToastUtils.show(model.getErrorMsg());
                            return;
                        }
                        if (page == 0){
                            list.clear();
                        }
                        list.addAll(model.getData().getDatas());
                        totalPages = model.getData().getPageCount();
                        adapter.notifyDataSetChanged();
                        page++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("officeAccount",e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        WaitDialog.close();
                    }
                });
    }
}
