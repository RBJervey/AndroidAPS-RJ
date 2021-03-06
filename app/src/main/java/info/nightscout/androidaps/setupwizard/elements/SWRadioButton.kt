package info.nightscout.androidaps.setupwizard.elements

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import dagger.android.HasAndroidInjector

class SWRadioButton(injector: HasAndroidInjector) : SWItem(injector, Type.RADIOBUTTON) {
    private var labelsArray = 0
    private var valuesArray = 0
    private var radioGroup: RadioGroup? = null

    fun option(labels: Int, values: Int): SWRadioButton {
        labelsArray = labels
        valuesArray = values
        return this
    }

    private fun labels(): Array<String> {
        return resourceHelper.gsa(labelsArray)
    }

    fun values(): Array<String> {
        return resourceHelper.gsa(valuesArray)
    }

    override fun generateDialog(layout: LinearLayout) {
        val context = layout.context
        val pdesc = TextView(context)
        pdesc.text = getComment()
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0, 40)
        pdesc.layoutParams = params
        layout.addView(pdesc)

        // Get if there is already value in SP
        val previousValue = sp.getString(preferenceId, "none")
        radioGroup = RadioGroup(context)
        radioGroup?.clearCheck()
        radioGroup?.orientation = LinearLayout.VERTICAL
        radioGroup?.visibility = View.VISIBLE
        for (i in labels().indices) {
            val rdbtn = RadioButton(context)
            rdbtn.id = View.generateViewId()
            rdbtn.text = labels()[i]
            if (previousValue == values()[i]) rdbtn.isChecked = true
            rdbtn.tag = i
            radioGroup!!.addView(rdbtn)
        }
        radioGroup!!.setOnCheckedChangeListener { group: RadioGroup, checkedId: Int ->
            val i = group.findViewById<View>(checkedId).tag as Int
            save(values()[i], 0)
        }
        layout.addView(radioGroup)
        super.generateDialog(layout)
    }

    fun preferenceId(preferenceId: Int): SWRadioButton {
        this.preferenceId = preferenceId
        return this
    }
}