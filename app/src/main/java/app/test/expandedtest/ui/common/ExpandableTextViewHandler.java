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

    //инициализируем для каждой вью слушатель выше
    for (ExpandableTextView expandableTextView : views) {
      expandableTextView.getIcon().setOnClickListener(onClickListener);
    }

  }
}
