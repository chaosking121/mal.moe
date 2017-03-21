package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;
import moe.mal.waifus.model.WaifuAuthLevel;
import moe.mal.waifus.model.WaifuType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewWaifuActivity extends GenericActivity {

    @BindView(R.id.nameField) EditText nameField;
    @BindView(R.id.seriesField) EditText seriesField;
    @BindView(R.id.messageField) EditText messageField;
    @BindView(R.id.waifuTypeSpinner) Spinner waifuTypeSpinner;
    @BindView(R.id.waifuAuthLevelSpinner) Spinner waifuAuthLevelSpinner;
    @BindView(R.id.submitButton) Button submitButton;

    private String name;
    private String series;
    private String message;
    private WaifuType waifuType;
    private WaifuAuthLevel waifuAuthLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_waifu);
        ButterKnife.bind(this);

        ArrayAdapter<WaifuType> waifuTypeArrayAdapter = new ArrayAdapter<>(c,
                android.R.layout.simple_spinner_dropdown_item, WaifuType.values());
        waifuTypeSpinner.setAdapter(waifuTypeArrayAdapter);
        waifuTypeSpinner.setSelection(1);

        ArrayAdapter<WaifuAuthLevel> waifuAuthLevelArrayAdapter = new ArrayAdapter<>(c,
                android.R.layout.simple_spinner_dropdown_item, WaifuAuthLevel.values());
        waifuAuthLevelSpinner.setAdapter(waifuAuthLevelArrayAdapter);
        waifuAuthLevelSpinner.setSelection(1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPressed();
            }
        });
    }

    private void handleServerResponse(Response<Waifu> response) {
        progress.dismiss();

        if ((response == null) || (response.code() != 200)) {
            try {
                showToast(response.errorBody().string());
            } catch (IOException e) {
                showToast("Unable to add that waifu.");
            }
            return;
        }

        Waifu waifu = response.body();
        showToast(String.format("Successfully created waifu %s", waifu.getName()));
        Ougi.getInstance().setAllWaifusList(null);
        finish();
    }

    private boolean validate() {
        boolean valid = true;

        if (!verifyGenericInput(series)) {
            seriesField.setError("enter a series");
            seriesField.requestFocus();
            valid = false;
        } else {
            seriesField.setError(null);
        }

        if (!(verifyGenericInput(name) && verifyInputWithRegex(name, Waifu.WAIFU_NAME_REGEX))) {
            nameField.setError("enter a valid waifu name");
            nameField.requestFocus();
            valid = false;
        } else {
            nameField.setError(null);
        }

        return valid;

    }

    private void submitPressed() {
        name = nameField.getText().toString();
        series = seriesField.getText().toString();
        message = messageField.getText().toString();
        waifuType = (WaifuType) waifuTypeSpinner.getSelectedItem();
        waifuAuthLevel = (WaifuAuthLevel) waifuAuthLevelSpinner.getSelectedItem();

        if (!validate()) {
            return;
        }

        showProgress("Trying to sign you up.");

        Call<Waifu> call = Ougi.getInstance().getWaifuAPI()
                .createNewWaifu(name, series, message, waifuType.getValue(),
                        waifuAuthLevel.getValue(), Ougi.getInstance().getUser().getAuth());

        call.enqueue(new Callback<Waifu>() {
            @Override
            public void onResponse(Call<Waifu> call, Response<Waifu> response) {
                handleServerResponse(response);
            }

            @Override
            public void onFailure(Call<Waifu> call, Throwable t) {
                handleServerResponse(null);
            }
        });
    }
}
