package com.practice.android.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public int quantity=2;
    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        display(quantity);
        displayMessage();
    }

    public void decrement(View view) {
        if(quantity>0) quantity--;
        display(quantity);
    }

    public void increment(View view) {
        quantity++;
        display(quantity);
    }

    private void display(int number){
        displayQty(number);
        displayPrice(number);
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQty(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.qty);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        number*=5;
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    private void displayMessage(){
        TextView priceTextView = (TextView) findViewById(R.id.thanks);
        if(quantity>1)
            priceTextView.setText("You order " + quantity + " cups of coffee\nThank you for coming!\n");
        else if(quantity==1)
            priceTextView.setText("You order a cup of coffee\nThank you for coming!\n");
        else
            priceTextView.setText("You order no cup of coffee\nThank you for coming!\n");
    }
}