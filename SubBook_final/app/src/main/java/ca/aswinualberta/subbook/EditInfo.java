package ca.aswinualberta.subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import ca.aswinualberta.subbook.R;
import ca.aswinualberta.subbook.Subscription;

import java.math.BigDecimal;

public class EditInfo extends AddInfo {

    public static final int EDIT_SUBSCRIPTION_REQUEST_CODE = 1002;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.editSubscription);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);

        Subscription subscription = new Gson().fromJson(intent.getStringExtra("subscription"), Subscription.class);
        nameEditText.setText(subscription.getName());
        setDate(subscription.getDate());
        chargeEditText.setText(subscription.getCharge().setScale(2, BigDecimal.ROUND_UP).toString());
        commentEditText.setText(subscription.getComment());
    }

    @Override
    protected void onSubmit(){
        Intent intent = new Intent();
        Subscription subscription = new Subscription(getName(), getDate(), getComment(), getCharge());
        intent.putExtra("subscription",new Gson().toJson(subscription));
        intent.putExtra("index", index);
        setResult(RESULT_OK, intent);
        finish();
    }
}
