package ca.aswinualberta.subbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ca.aswinualberta.subbook.R;
import ca.aswinualberta.subbook.Subscription;
import ca.aswinualberta.subbook.SubscriptionCardView;
import ca.aswinualberta.subbook.Units;
import ca.aswinualberta.subbook.DummyAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class ListView extends AppCompatActivity{

    public static final String SUBSCRIPTIONS_FILE_NAME = "Subscriptions";

    private RecyclerView recyclerView;
    private Button addSubButton;
    private TextView totalTextView;

    private ArrayList<Subscription> subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        subscriptions = new ArrayList<>();

        this.totalTextView = findViewById(R.id.listSubscriptionTotal);


        this.recyclerView = findViewById(R.id.listSubscriptionRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(new DummyAdapter<Subscription, SubscriptionCardView>(subscriptions) {
            @Override
            public SubscriptionCardView createView(int viewType) {
                Log.d("ListSubscription", "Create SubscriptionCardView");
                final SubscriptionCardView view = new SubscriptionCardView(ListView.this);
                LinearLayout.LayoutParams marginPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = Units.dpToPX(10, ListView.this);
                marginPar.setMargins(margin, margin, margin, margin);
                view.setLayoutParams(marginPar);
                view.setOnClickListener(new View.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                        Intent intent = new Intent(ListView.this, EditInfo.class);
                        intent.putExtra("subscription", new Gson().toJson(view.getSubscription()));
                        intent.putExtra("index", subscriptions.indexOf(view.getSubscription()));
                        startActivityForResult(intent, EditInfo.EDIT_SUBSCRIPTION_REQUEST_CODE);
                    }
                });
                return view;
             }

            @Override
            public void onBindView(SubscriptionCardView view, Subscription sub) {
                Log.d("ListSubscription", "Set SubscriptionCardView");
                view.setSubscription(sub);
            }

            @Override
            public void onReachingLastItem(int position) {

            }
        });

        //Swipe to Remove
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Subscription subscription = subscriptions.get(viewHolder.getAdapterPosition());
                new AlertDialog.Builder(ListView.this)
                        .setTitle(R.string.confirmRemoveSubscriptionTitle)
                        .setMessage(getResources().getString(R.string.confirmRemoveSubscriptionMessage).replace("{name}", subscription.getName()))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeSubscription(subscription);
                                Snackbar.make(recyclerView, R.string.subscriptionRemoved, Snackbar.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // To reset swiped item
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        })
                        .show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        this.addSubButton = findViewById(R.id.addSubButton);
        this.addSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListView.this, AddInfo.class);
                ListView.this.startActivityForResult(intent,AddInfo.ADD_SUBSCRIPTION_REQUEST_CODE);
            }
        });

        loadSubscriptions();
    }

    /*
        Add a subscription and update charges
     */
    public void addSubscription(Subscription subscription){
        subscriptions.add(subscription);
        recyclerView.getAdapter().notifyDataSetChanged();
        updateTotal();
    }

    /*
        Remove a subscription and update charges
     */
    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
        recyclerView.getAdapter().notifyDataSetChanged();
        updateTotal();
    }

    public void updateTotal(){
        this.totalTextView.setText(getTotalChargeString());
    }

    public String getTotalChargeString(){
        return "$" + getTotalCharge().setScale(2, RoundingMode.HALF_UP).toString();
    }

    public BigDecimal getTotalCharge(){
        BigDecimal total = new BigDecimal(0);
        for(Subscription sub : subscriptions){
            total = total.add(sub.getCharge());
        }
        return total;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == AddInfo.ADD_SUBSCRIPTION_REQUEST_CODE){
                Subscription subscription = new Gson().fromJson(data.getStringExtra("subscription"), Subscription.class);
                addSubscription(subscription);
            }else if(requestCode == EditInfo.EDIT_SUBSCRIPTION_REQUEST_CODE){
                Subscription subscription = new Gson().fromJson(data.getStringExtra("subscription"), Subscription.class);
                int index = data.getIntExtra("index", -1);
                subscriptions.remove(index);
                subscriptions.add(index, subscription);
                recyclerView.getAdapter().notifyDataSetChanged();
                updateTotal();
            }
        }
    }

    public void loadSubscriptions(){
        try {
            File file = new File(getFilesDir(), SUBSCRIPTIONS_FILE_NAME);
            if(!file.exists()){
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append("\n" + line);
                line = reader.readLine();
            }
            reader.close();
            ArrayList<Subscription> subscriptions = new Gson().fromJson(builder.toString(), new TypeToken<List<Subscription>>(){}.getType());
            this.subscriptions.clear();
            this.subscriptions.addAll(subscriptions);
            this.recyclerView.getAdapter().notifyDataSetChanged();
            updateTotal();
        }
        catch (IOException e) {
            Log.e("ListSubscription", e.getMessage(), e);
        }
        catch (Throwable t){
            Log.e("ListSubscription", t.getMessage(), t);
        }
    }

    public void saveSubscriptions(){
        try {
            File file = new File(getFilesDir(), SUBSCRIPTIONS_FILE_NAME);
            if(!file.exists()){
                file.createNewFile();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(new Gson().toJson(subscriptions));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("ListSubscription", e.getMessage(), e);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveSubscriptions();
    }
}
