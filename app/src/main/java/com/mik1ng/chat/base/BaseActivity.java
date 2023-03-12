package com.mik1ng.chat.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    public T viewBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        if (useEvent()) {
            EventBus.getDefault().register(this);
        }

        viewBind = getViewBind();
        setContentView(viewBind.getRoot());

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean useEvent() {
        return false;
    }

    private void initAdapter(RecyclerView.Adapter adapter, RecyclerView recyclerView) {

    }


    public abstract T getViewBind();

    public abstract void initView();
    public abstract void initData();
}
