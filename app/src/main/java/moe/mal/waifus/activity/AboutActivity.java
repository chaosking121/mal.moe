package moe.mal.waifus.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import moe.mal.waifus.R;

public class AboutActivity extends MaterialAboutActivity {


    @Override
    protected MaterialAboutList getMaterialAboutList(Context c) {

        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.title("About");
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Mal.moe")
                .icon(R.mipmap.icon)
                .build());

        try {
            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(this,
                    getDrawable(R.drawable.ic_alert_black_24dp),
                    "Version",
                    false));
        } catch (PackageManager.NameNotFoundException e) {
            //lol
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Check for updates.")
                .icon(R.drawable.ic_update_black_24dp)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        new AppUpdater(AboutActivity.this)
                                .setUpdateFrom(UpdateFrom.JSON)
                                .setUpdateJSON("https://arshbox.xyz/dist/update-changelog.json")
                                .setDisplay(Display.SNACKBAR)
                                .showAppUpdated(true)
                                .start();
                    }
                })
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Arshad Hosein")
                .subText("Georgia Tech")
                .icon(getDrawable(R.drawable.ic_person_black_24dp))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Email Me")
                .subText("arshad@arshadhosein.com")
                .icon(getDrawable(R.drawable.ic_email_black_24dp))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("arshad@arshadhosein.com")))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("View on GitHub")
                .subText("@chaosking121")
                .icon(getDrawable(R.drawable.ic_github_circle_black_24p))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/chaosking121/mal.moe-android")))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Website")
                .subText("arshadhosein.com")
                .icon(getDrawable(R.drawable.ic_web_black_24p))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://arshadhosein.com")))
                .build());

        return new MaterialAboutList.Builder()
                .addCard(appCardBuilder.build())
                .addCard(authorCardBuilder.build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }
}
