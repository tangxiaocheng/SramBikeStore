package app.sram.bikestore.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import app.sram.bikestore.di.ui.FragmentScope;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import kotlin.Unit;
@FragmentScope
public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private final PublishSubject<Unit> publishSubject = PublishSubject.create();
    private final LinearLayoutManager layoutManager;
    public static final int VISIBLE_THRESHOLD = 5;

    @Inject
    public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    public Observable<Unit> loadingMoreSignal() {
        return publishSubject.hide();
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = getFirstVisibleItem();
        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            publishSubject.onNext(Unit.INSTANCE);
        }
    }

    private int getFirstVisibleItem() {
        return layoutManager.findFirstVisibleItemPosition();
    }

    public void markCompleted() {
        publishSubject.onComplete();
    }
}