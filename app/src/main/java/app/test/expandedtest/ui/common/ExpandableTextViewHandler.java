package app.test.expandedtest.ui.common;

import android.view.View;
import android.view.View.OnClickListener;
import io.reactivex.Completable;
import io.reactivex.Observable;
import java.util.List;

public class ExpandableTextViewHandler {

  private List<ExpandableTextView> views;

  public ExpandableTextViewHandler(List<ExpandableTextView> views) {
    this.views = views;
  }

  public void initListener() {

    OnClickListener onClickListener = view -> {

      ExpandableTextView current = (ExpandableTextView) view.getParent().getParent().getParent();

      Observable.fromIterable(views)
          .doOnNext(item -> item.getIcon().setClickable(false))
          .filter(item -> item != current)
          .flatMapCompletable(item -> item.isExpanded ? item.collapse() : Completable.complete())
          .andThen(current.isExpanded ? current.collapse() : current.expanded())
        .subscribe(() -> {
          for (ExpandableTextView expandableTextView : views) {
            expandableTextView.getIcon().setClickable(true);
          }
        });

    };

    for (ExpandableTextView expandableTextView : views) {
      expandableTextView.getIcon().setOnClickListener(onClickListener);
    }

  }

  /*
  * lass ExpandableHelper(private val views: List<ExpandableTextView>) {

        fun initListener() {

            val onClickCardListener: (View) -> Unit = { view ->
                val current = view as ExpandableTextView

                Observable.fromIterable(views)
                        .doOnNext { it.isClickable = false }
                        .filter { it != current }
                        .flatMapCompletable { if (it.isExpanded) it.collapse() else Completable.complete() }
                        .andThen(if (current.isExpanded) current.collapse() else current.expanded())
                        .subscribe { views.forEach { it.isClickable = true } }

            }

            views.forEach { it.setOnClickListener(onClickCardListener) }

        }

    }*/



}
