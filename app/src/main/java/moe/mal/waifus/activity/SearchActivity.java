package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import butterknife.BindView;
import moe.mal.waifus.R;

public class SearchActivity extends SidebarActivity {

    @BindView(R.id.waifuSearch) TextView waifuSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        super.setUpSidebar();
        waifuSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    displayWaifu(v.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
        waifuSearch.requestFocus();
    }

    public void searchSubmitted(View v) {
        displayWaifu(waifuSearch.getText().toString());
    }
}
