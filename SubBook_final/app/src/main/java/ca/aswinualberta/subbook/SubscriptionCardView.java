package ca.aswinualberta.subbook;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class SubscriptionCardView extends CardView{
    private TextView nameTextView;
    private TextView dateTextView;
    private TextView commentTextView;
    private TextView chargeTextView;

    private Subscription subscription;

    public SubscriptionCardView(Context context) {
        this(context, null);
    }

    public SubscriptionCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscriptionCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs == null) {
            init();
        } else {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, null, defStyle, 0);
            ta.recycle();
            init();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.activity_subscription_card_view, this);
        nameTextView = findViewById(R.id.subscriptionName);
        dateTextView = findViewById(R.id.subscriptionDate);
        commentTextView = findViewById(R.id.subscriptionComment);
        chargeTextView = findViewById(R.id.subscriptionCharge);
    }

    public void setSubscription(Subscription sub){
        this.subscription = sub;
        nameTextView.setText(sub.getName());
        dateTextView.setText(sub.getDateString());
        commentTextView.setText(sub.getComment() == null ? "" : sub.getComment());
        chargeTextView.setText(sub.getChargeString());
    }

    public Subscription getSubscription(){
        return this.subscription;
    }
}
