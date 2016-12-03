package moe.mal.waifus;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

public class SearchActivity extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextView waifuSearch = (TextView) findViewById(R.id.waifuSearch);
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
        TextView waifuSearch = (TextView) findViewById(R.id.waifuSearch);
        displayWaifu(waifuSearch.getText().toString());
    }
}
