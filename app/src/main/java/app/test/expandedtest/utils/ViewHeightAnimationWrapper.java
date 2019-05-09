package app.test.expandedtest.utils;

import android.view.View;

public class ViewHeightAnimationWrapper {

  private View view;

  public ViewHeightAnimationWrapper(View view) {
    this.view = view;
  }

  public int getHeight() {
    return view.getLayoutParams().height;
  }

  public void setHeight(int height) {
    view.getLayoutParams().height = height;
    view.getParent().requestLayout();
  }
}
