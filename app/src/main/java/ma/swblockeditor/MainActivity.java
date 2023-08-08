package ma.swblockeditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ma.swblockeditor.core.DesignDataManager;
import ma.swblockeditor.core.LogicEditorActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DesignDataManager.initialize(getApplicationContext(), "");

        var intent = new Intent();
        intent.putExtra("id", "onCreate");
        intent.putExtra("event", "initializeLogic");
        intent.putExtra("event_text", "On activity create");
        intent.putExtra("filename", "idk");
        intent.setClass(this, LogicEditorActivity.class);
        startActivity(intent);
        finish();
    }
}
