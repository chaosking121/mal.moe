package moe.mal.waifus.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.AuthLevel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrapeActivity extends GenericActivity {

    @BindView(R.id.urlField) TextView urlField;
    @BindView(R.id.waifuSpinner) Spinner waifuSpinner;
    @BindView(R.id.scrapeButton) Button scrapeButton;
    @BindView(R.id.newWaifuButton) Button newWaifuButton;

    private String waifu;
    private String url;
    private List<String> waifus = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrape);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (Ougi.getInstance().getUser().getAuthLevel() < AuthLevel.ADMIN.getValue()) {
            newWaifuButton.setVisibility(View.INVISIBLE);
        } else {
            newWaifuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showScreen(NewWaifuActivity.class);
                }
            });
        }

        if (!intent.hasExtra("url")) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (clipboard.hasPrimaryClip()) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                if (item.getText() != null) {
                    urlField.setText(item.getText());
                }
            }
        } else {
            url = ((String) intent.getExtras().get("url"));
            urlField.setText(url);
            urlField.setFocusable(false);
        }

        scrapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = urlField.getText().toString();
                waifu = (String) waifuSpinner.getSelectedItem();
                if (!verifyGenericInput(url)) {
                    showToast("Invalid URL Entered");
                    return;
                }
                attemptToScrape(waifu, url);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent.hasExtra("waifu")) {
            waifu = ((String) intent.getExtras().get("waifu"));
            waifus.add(waifu);
            adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, waifus);
            waifuSpinner.setAdapter(adapter);

        } else {

            Call<List<String>> call = Ougi.getInstance().getWaifuAPI().getAllWaifuNames(Ougi.getInstance().getUser().getAuth());

            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> newWaifus = response.body();
                    Collections.sort(newWaifus);
                    waifus.clear();
                    waifus.addAll(newWaifus);
                    adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, waifus);
                    waifuSpinner.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    showToast("Failed to load waifus.");
                    finish();
                }
            });
        }
    }

    private void handleScrapeResponse(Response<ResponseBody> response) {
        try {
            if ((response == null) || (response.code() != 200) || response.body().string().isEmpty()) {
                showToast(response.errorBody().string());
            } else {
                // TODO: WHY THE FUCK IS THIS NECESSARY?
                // Slightly less angry - showing a toast with response.body().string()
                // just shows a blank toast. Dunno why.
                showToast(String.format("Scraping to %s", waifu));
            }
        } catch (IOException e) {
            showToast("Scraping failed.");
        }
        finally {
            finish();
        }
    }

    private void attemptToScrape(String waifu, String url) {

        Call<ResponseBody> call = Ougi.getInstance().getWaifuAPI()
                .scrape(waifu, url,
                        Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleScrapeResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleScrapeResponse(null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveApp(SadActivity.class);
    }
}
