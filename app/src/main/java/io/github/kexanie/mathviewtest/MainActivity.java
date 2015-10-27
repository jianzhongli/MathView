package io.github.kexanie.mathviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.kexanie.library.MathView;

public class MainActivity extends AppCompatActivity {
    MathView math_one;
    MathView math_two;
    String tex = "This come from string. You can insert inline formula: \\(\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}\\) or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        math_one = (MathView) findViewById(R.id.formula_one);
        math_two = (MathView) findViewById(R.id.formula_two);

        // This toast will print raw TeX string
        Toast.makeText(this, math_one.getText(), Toast.LENGTH_LONG).show();
        math_two.setText(tex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
