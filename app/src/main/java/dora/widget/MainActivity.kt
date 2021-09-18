package dora.widget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val radioGroup = findViewById<DoraRadioGroup>(R.id.radioGroup)
        radioGroup.check(R.id.rb_default_checked)
        radioGroup.setOnCheckedChangeListener(object : DoraRadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: DoraRadioGroup, checkedId: Int) {
                Log.e("MainActivity", "checkedId=$checkedId")
            }
        })
    }
}