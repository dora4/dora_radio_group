# DoraRadioGroup

描述：一个支持多行的RadioGroup，修复官方RadioGroup的BUG

复杂度：★★☆☆☆

分组：【系统控件优化】

关系：DoraButton

技术要点：ViewGroup添加View过程、LayoutParams解析过程

### 照片

![avatar](https://github.com/dora4/dora_radio_group/blob/main/art/dora_radio_group.jpg)

### 动图

![avatar](https://github.com/dora4/dora_radio_group/blob/main/art/dora_radio_group.gif)

### 软件包

https://github.com/dora4/dora_radio_group/blob/main/art/dora_radio_group.apk

### 用法

```kotlin
val radioGroup = findViewById<DoraRadioGroup>(R.id.radioGroup)
        radioGroup.check(R.id.rb_default_checked)
        radioGroup.setOnCheckedChangeListener(object : DoraRadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: DoraRadioGroup, checkedId: Int) {
                Log.e("MainActivity", "checkedId=$checkedId")
            }
        })
```
