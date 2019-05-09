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

    //создаем слушатель для каждой вьюшки
    OnClickListener onClickListener = view -> {

      ExpandableTextView current = (ExpandableTextView) view.getParent().getParent().getParent();

      //При нажатии на любую из вью, перебираем список добавленных вьюшек
      Observable.fromIterable(views)
           //блокируем для всех нажатия, чтобы не сломать анимацию
          .doOnNext(item -> item.getIcon().setClickable(false))
          //фильтрируем чтобы среди этого списка не было текущей вьюшки
          .filter(item -> item != current)
          //если вьюшка открыта, закрываем, иначе просто пропускаем ее
          .flatMapCompletable(item -> item.isExpanded ? item.collapse() : Completable.complete())
          .andThen(current.isExpanded ? current.collapse() : current.expanded())
          //по окончанию работы, возвращаем обратно нажатия
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
