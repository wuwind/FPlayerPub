package com.wuwind.ui.base;

import com.wuwind.uisdk.model.IModelDelegate;
import com.wuwind.uisdk.presenter.AbstractFragmentPresenter;
import com.wuwind.uisdk.view.IViewDelegate;

public abstract class FragmentPresenter<V extends IViewDelegate, M extends IModelDelegate> extends AbstractFragmentPresenter<V, M> {

}
