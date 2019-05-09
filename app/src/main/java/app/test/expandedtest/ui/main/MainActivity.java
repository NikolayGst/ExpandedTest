package app.test.expandedtest.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import app.test.expandedtest.R;
import app.test.expandedtest.ui.common.ExpandableTextView;
import app.test.expandedtest.ui.common.ExpandableTextViewHandler;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ExpandableTextView view1 = findViewById(R.id.view1);
    ExpandableTextView view2 = findViewById(R.id.view2);
    ExpandableTextView view3 = findViewById(R.id.view3);
    ExpandableTextView view4 = findViewById(R.id.view4);

    ExpandableTextViewHandler handler = new ExpandableTextViewHandler(Arrays.asList(view1, view2, view3, view4));
    handler.initListener();

  }
}
