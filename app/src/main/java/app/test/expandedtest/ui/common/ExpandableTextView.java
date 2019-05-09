package app.test.expandedtest.ui.common;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import app.test.expandedtest.R;
import app.test.expandedtest.utils.OnEndAnimatorListener;
import app.test.expandedtest.utils.ViewHeightAnimationWrapper;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposables;

public class ExpandableTextView extends FrameLayout {

  /**
   * Значения по-умолчанию
   */
  private String title = "title";
  private String description = "description";

  private TextView titleView;
  private AppCompatImageView icon;
  private TextView descView;

  public boolean isExpanded = false;

  private int expandedHeight = 0;

  public AppCompatImageView getIcon() {
    return icon;
  }

  public ExpandableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initLayout(context);
    initAttrs(context, attrs);
  }

  /**
   * Метод используется для извлечения кастомых полей во вью (в данном случае текст и описание)
   */
  private void initAttrs(Context context, AttributeSet attrs) {
    //получаем атрибуты вьюшки нашей
    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);

    //Если есть значение заголовка, извлекаем его
    if (array.hasValue(R.styleable.ExpandableTextView_etv_text_title)) {
      title = array.getString(R.styleable.ExpandableTextView_etv_text_title);
    }

    //Если есть значение описания, извлекаем его
    if (array.hasValue(R.styleable.ExpandableTextView_etv_text_desc)) {
      description = array.getString(R.styleable.ExpandableTextView_etv_text_desc);
    }

    //Освобождаем объект после использования
    array.recycle();
  }

  /**
   * Метод "раздувает" макет внутри вьюшки для работы с ним
   */
  private void initLayout(Context context) {
    LayoutInflater.from(context).inflate(R.layout.expandable_text_view, this);
  }

  /**
   * Метод вызывается после того как LayoutInflater "раздул" макет
   */
  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    titleView = findViewById(R.id.title);
    icon = findViewById(R.id.icon);
    descView = findViewById(R.id.desc);

    setDataInView();

    getHeightDescAndHide();
  }

  /**
   * инициализация вьюшек значениями из атрибутов
   */
  private void setDataInView() {
    titleView.setText(title);
    descView.setText(description);
  }


  /**
   * Метод получает высоту вьюшки с содержимым а потом скрывает ее
   */
  private void getHeightDescAndHide() {
    descView.post(() -> {
      expandedHeight = descView.getHeight();
      System.out.println("height: " + expandedHeight);
      descView.getLayoutParams().height = 0;
      descView.getParent().requestLayout();
    });
  }


  /**
   * Метод раскрывает вью
   */
  public Completable expanded() {
    return rotateAnim(icon, 180f, 500)
        .mergeWith(heightAnim(descView, 0, expandedHeight, 500))
        .doOnComplete(() -> isExpanded = true);
  }

  /**
   * Метод закрывает вью
   */
  public Completable collapse() {
    return rotateAnim(icon, 0f, 500)
        .mergeWith(heightAnim(descView, expandedHeight, 0, 500))
        .doOnComplete(() -> isExpanded = false);
  }

  /**
   * Метод вернет поток, результатом которого будет завершение анимации
   */
  private Completable rotateAnim(View view, float value, long duration) {
    return Completable.create(obs -> {
      //инициализируем анимацию
      ViewPropertyAnimator viewPropertyAnimator = view.animate()
          .rotation(value)
          .setDuration(duration)
          .setListener(new OnEndAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
              if (!obs.isDisposed()) {
                obs.onComplete();
              }
            }
          });

      //запускаем анимацию
      viewPropertyAnimator.start();

      //при отписке от потока, останавливаем анимацию
      obs.setDisposable(Disposables.fromAction(viewPropertyAnimator::cancel));
    });
  }

  private Completable heightAnim(View view, int startPosition, int endPosition, long duration) {
    return Completable.create(obs -> {
      //инициализируем анимацию
      ViewHeightAnimationWrapper wrapper = new ViewHeightAnimationWrapper(view);
      ValueAnimator widthAnimator = ObjectAnimator
          .ofInt(wrapper, "height", startPosition, endPosition);

      widthAnimator.setDuration(duration);
      widthAnimator.setInterpolator(new DecelerateInterpolator());
      widthAnimator.addListener(new OnEndAnimatorListener() {

        @Override
        public void onAnimationEnd(Animator animation, boolean isReverse) {
          super.onAnimationEnd(animation, isReverse);
          if (!obs.isDisposed()) {
            obs.onComplete();
          }
        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }
      });

      //запускаем анимацию
      widthAnimator.start();

      //при отписке от потока, останавливаем анимацию
      obs.setDisposable(Disposables.fromAction(widthAnimator::cancel));
    });
  }
}
