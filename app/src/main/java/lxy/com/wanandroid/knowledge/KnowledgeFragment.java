package lxy.com.wanandroid.knowledge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.sdk.screen.FragmentListener;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.FragmentInterface;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * @author lxy
 * date: 2019/1/26
 */

public class KnowledgeFragment extends Fragment implements FragmentInterface{

    private RecyclerView recyclerView;
    private KnowledgeAdapter articleAdapter;
    private List<KnowledgeModel.DataBean> knowledgeModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_knowledge,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        FragmentListener.getInstance().registerFragment(this);
        initView(view);
        initListener();
        getArticleByServer();
    }

    private void initListener() {

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.knowledge_recycle);
        knowledgeModels = new ArrayList<>();
        articleAdapter = new KnowledgeAdapter(R.layout.item_knowledge,knowledgeModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(articleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KnowledgeModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KnowledgeModel knowledgeModel) {
                        if (knowledgeModel.getErrorCode() == 0){
                            knowledgeModels.clear();
                            knowledgeModels.addAll(knowledgeModel.getData());
                            articleAdapter.notifyDataSetChanged();
                        }else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void smoothToTop() {
        recyclerView.scrollToPosition(0);
    }
}
