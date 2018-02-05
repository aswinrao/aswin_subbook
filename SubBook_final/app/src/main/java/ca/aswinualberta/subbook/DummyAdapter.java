package ca.aswinualberta.subbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
public abstract class DummyAdapter<T, V extends View> extends RecyclerView.Adapter<DummyViewHolder<V>> {

        private List<T> list;

        public DummyAdapter(List<T> list){
            this.list = list;
        }

        @Override
        public DummyViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
            DummyViewHolder<V> viewHolder = new DummyViewHolder<>(createView(viewType));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DummyViewHolder<V> holder, int position) {
            onBindView(holder.view, list.get(position));
            if(position >= list.size() - 1){
                onReachingLastItem(position);
            }
        }

        public abstract V createView(int viewType);

        public abstract void onBindView(V view, T item);

        public abstract void onReachingLastItem(int position);

        @Override
        public int getItemViewType(int position){
            return getItemViewType(list.get(position));
        }

        public int getItemViewType(T item){
            return 0;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


