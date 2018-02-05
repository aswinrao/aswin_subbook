package ca.aswinualberta.subbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aswin Rao on 05-02-2018.
 */

class DummyViewHolder<T extends View> extends RecyclerView.ViewHolder{
    public T view;

    public DummyViewHolder(T view) {
        super(view);
        this.view = view;
    }

    public T getView(){
        return view;
    }
}
