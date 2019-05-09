package app.test.expandedtest.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

public abstract class OnEndAnimatorListener implements AnimatorListener {

  @Override
  public void onAnimationStart(Animator animation, boolean isReverse) {

  }

  @Override
  public void onAnimationEnd(Animator animation, boolean isReverse) {
    System.out.println();
  }

  @Override
  public void onAnimationStart(Animator animator) {

  }

  @Override
  public void onAnimationCancel(Animator animator) {

  }

  @Override
  public void onAnimationRepeat(Animator animator) {

  }
}
