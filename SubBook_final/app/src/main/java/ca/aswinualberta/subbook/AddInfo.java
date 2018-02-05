package ca.aswinualberta.subbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.Gson;

public class AddInfo extends AppCompatActivity{

    public static final int ADD_SUBSCRIPTION_REQUEST_CODE = 1001;

    protected TextView dateDisplay;
    protected EditText nameEditText;
    protected EditText chargeEditText;
    protected EditText commentEditText;
    protected Button submitButton;

    protected Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        this.dateDisplay = findViewById(R.id.addSubscriptionDateDisplay);
        this.nameEditText = findViewById(R.id.addSubscriptionName);
        this.chargeEditText = findViewById(R.id.addSubscriptionCharge);
        this.commentEditText = findViewById(R.id.addSubscriptionComment);
        this.submitButton = findViewById(R.id.addSubButton);

        final Calendar currentDate = Calendar.getInstance();
        setDate(currentDate);
        this.dateDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(AddInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        setDate(calendar);
                    }
                }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSubmit();
            }
        };
        nameEditText.addTextChangedListener(textWatcher);
        chargeEditText.addTextChangedListener(textWatcher);
        //commentEditText.addTextChangedListener(textWatcher);


    }

    protected void onSubmit(){
        Intent intent = new Intent();
        Subscription subscription = new Subscription(getName(), getDate(), getComment(), getCharge());
        intent.putExtra("subscription",new Gson().toJson(subscription));
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void checkSubmit(){
        if(canSubmit()) {
            submitButton.setEnabled(true);
        }else{
            submitButton.setEnabled(false);
        }
    }

    public boolean canSubmit(){
        return !getName().isEmpty() && getCharge().signum() >= 0 ;
    }

    public String getName(){
        return String.valueOf(this.nameEditText.getText());
    }
    public String getComment(){
        return String.valueOf(this.commentEditText.getText());
    }

    public BigDecimal getCharge(){
        String chargeString = String.valueOf(this.chargeEditText.getText());
        if(chargeString.isEmpty()){
            chargeString = "-1";
        }
        Log.d("AddSubscription", new BigDecimal(chargeString).toString());
        return new BigDecimal(chargeString);
    }

    public Calendar getDate(){
        return this.selectedDate;
    }

    protected void setDate(Calendar date){
        this.selectedDate = date;
        this.dateDisplay.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
    }
}

