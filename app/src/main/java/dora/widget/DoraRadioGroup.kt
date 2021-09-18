package dora.widget

import android.content.Context
import kotlin.jvm.JvmOverloads
import android.widget.LinearLayout
import android.widget.CompoundButton
import android.view.ViewGroup
import android.widget.RadioButton
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import java.util.ArrayList

class DoraRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var checkedRadioButtonId = NO_ID
    private var childOnCheckedChangeListener = CheckedStateTracker()
    private var protectFromCheckedChange = false
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        setOnHierarchyChangeListener(PassThroughHierarchyChangeListener())
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        val buttons = getAllRadioButton(child)
        if (buttons != null && buttons.isNotEmpty()) {
            for (button in buttons) {
                if (button.id == NO_ID) {
                    button.id = generateViewId()
                }
                if (button.isChecked) {
                    protectFromCheckedChange = true
                    setCheckedStateForView(checkedRadioButtonId, false)
                    protectFromCheckedChange = false
                    setCheckedId(button.id)
                }
            }
        }
        super.addView(child, index, params)
    }

    private fun getAllRadioButton(child: View): List<RadioButton> {
        val buttons: MutableList<RadioButton> = ArrayList()
        if (child is RadioButton) {
            buttons.add(child)
        } else if (child is ViewGroup) {
            val counts = child.childCount
            for (i in 0 until counts) {
                buttons.addAll(getAllRadioButton(child.getChildAt(i)))
            }
        }
        return buttons
    }

    fun check(id: Int) {
        if (id != NO_ID && id == checkedRadioButtonId) {
            return
        }
        if (checkedRadioButtonId != NO_ID) {
            setCheckedStateForView(checkedRadioButtonId, false)
        }
        if (id != NO_ID) {
            setCheckedStateForView(id, true)
        }
        setCheckedId(id)
    }

    private fun setCheckedId(id: Int) {
        checkedRadioButtonId = id
        onCheckedChangeListener?.onCheckedChanged(this, checkedRadioButtonId)
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById<View>(viewId)
        if (checkedView != null && checkedView is RadioButton) {
            checkedView.isChecked = checked
        }
    }

    fun clearCheck() {
        check(NO_ID)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun generateDefaultLayoutParams(): LinearLayout.LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = DoraRadioGroup::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = DoraRadioGroup::class.java.name
    }

    class LayoutParams : LinearLayout.LayoutParams {

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        constructor(w: Int, h: Int) : super(w, h)

        constructor(w: Int, h: Int, initWeight: Float) : super(w, h, initWeight)

        constructor(p: ViewGroup.LayoutParams) : super(p)

        constructor(source: MarginLayoutParams) : super(source)

        override fun setBaseAttributes(
            a: TypedArray,
            widthAttr: Int, heightAttr: Int
        ) {
            width = if (a.hasValue(widthAttr)) {
                a.getLayoutDimension(widthAttr, "layout_width")
            } else {
                WRAP_CONTENT
            }
            height = if (a.hasValue(heightAttr)) {
                a.getLayoutDimension(heightAttr, "layout_height")
            } else {
                WRAP_CONTENT
            }
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(group: DoraRadioGroup, checkedId: Int)
    }

    private inner class CheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            if (protectFromCheckedChange) {
                return
            }
            protectFromCheckedChange = true
            if (checkedRadioButtonId != NO_ID) {
                setCheckedStateForView(checkedRadioButtonId, false)
            }
            protectFromCheckedChange = false
            val id = buttonView.id
            setCheckedId(id)
        }
    }

    inner class PassThroughHierarchyChangeListener : OnHierarchyChangeListener {

        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@DoraRadioGroup) {
                val btns = getAllRadioButton(child)
                if (btns != null && btns.isNotEmpty()) {
                    for (btn in btns) {
                        btn.setOnCheckedChangeListener(
                            childOnCheckedChangeListener
                        )
                    }
                }
            }
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@DoraRadioGroup) {
                val buttons = getAllRadioButton(child)
                if (buttons != null && buttons.isNotEmpty()) {
                    for (button in buttons) {
                        button.setOnCheckedChangeListener(null)
                    }
                }
            }
        }
    }
}