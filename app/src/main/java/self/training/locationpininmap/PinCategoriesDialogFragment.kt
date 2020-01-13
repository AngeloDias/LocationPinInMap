package self.training.locationpininmap

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PinCategoriesDialogFragment : DialogFragment() {
    private lateinit var selectedItems: Array<CharSequence>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            selectedItems = resources.getTextArray(R.array.pin_categories)
            val builder = AlertDialog.Builder(it)
            val arrayChecked = booleanArrayOf(true, true, true, true, true)

            builder.setTitle(R.string.map_filter_pin)
                .setMultiChoiceItems(R.array.pin_categories,
                    arrayChecked
                ) { _, which,
                    isChecked ->

                    arrayChecked[which] = isChecked

                    when(isChecked) {
                        false -> {
                            selectedItems[which] = ""
                        }
                    }
                }

            builder.setPositiveButton(getString(R.string.map_filter_pin)) {_, _ ->
                val activity = activity as MapsActivity

                activity.showPinInSelectedCategories(selectedItems)
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}