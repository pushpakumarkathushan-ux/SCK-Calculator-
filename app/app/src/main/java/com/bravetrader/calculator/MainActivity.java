
package com.bravetrader.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    TextView display;
    String current = "0";
    double first = 0;
    String operator = "";
    boolean newNumber = true;

    int green = Color.rgb(0, 190, 130);
    int dark = Color.rgb(20, 30, 45);

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(16, 35, 16, 16);
        root.setBackgroundColor(dark);

        TextView title = new TextView(this);
        title.setText("BRAVE CALCULATOR");
        title.setTextColor(green);
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        display = new TextView(this);
        display.setText("0");
        display.setTextColor(Color.WHITE);
        display.setTextSize(42);
        display.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        display.setPadding(12, 25, 12, 25);
        root.addView(display,
            new LinearLayout.LayoutParams(-1, 130));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);

        String[] keys = {
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "0", ".", "=", " "
        };

        for (String key : keys) {
            Button btn = new Button(this);
            btn.setText(key);
            btn.setTextSize(23);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                    "+−×÷=".contains(key) || key.equals("C")
                    ? green : Color.rgb(52, 68, 90)
                )
            );

            GridLayout.LayoutParams p =
                new GridLayout.LayoutParams();
            p.width = 0;
            p.height = 95;
            p.columnSpec = GridLayout.spec(
                GridLayout.UNDEFINED, 1f);
            p.setMargins(4, 4, 4, 4);
            grid.addView(btn, p);

            btn.setOnClickListener(v -> press(key));
        }

        root.addView(grid);
        setContentView(root);
    }

    void press(String key) {
        if (key.equals(" ")) return;

        if (key.equals("C")) {
            current = "0";
            first = 0;
            operator = "";
            newNumber = true;
        }
        else if (key.equals("⌫")) {
            if (current.length() > 1)
                current = current.substring(0,
                    current.length() - 1);
            else current = "0";
        }
        else if (key.equals("+") || key.equals("−")
                || key.equals("×") || key.equals("÷")) {
            first = Double.parseDouble(current);
            operator = key;
            newNumber = true;
        }
        else if (key.equals("=")) {
            if (!operator.isEmpty()) {
                double second = Double.parseDouble(current);
                double result = 0;

                switch (operator) {
                    case "+": result = first + second; break;
                    case "−": result = first - second; break;
                    case "×": result = first * second; break;
                    case "÷":
                        if (second == 0) {
                            current = "Error";
                            display.setText(current);
                            operator = "";
                            return;
                        }
                        result = first / second;
                        break;
                }

                current = new DecimalFormat("0.##########")
                    .format(result);
                operator = "";
                newNumber = true;
            }
        }
        else if (key.equals("%")) {
            current = String.valueOf(
                Double.parseDouble(current) / 100);
        }
        else {
            if (newNumber || current.equals("0")
                    || current.equals("Error")) {
                current = key.equals(".") ? "0." : key;
                newNumber = false;
            } else if (key.equals(".")) {
                if (!current.contains("."))
                    current += ".";
            } else {
                current += key;
            }
        }

        display.setText(current);
    }
}
