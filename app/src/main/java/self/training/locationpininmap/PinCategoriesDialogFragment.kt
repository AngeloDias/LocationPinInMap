package self.training.locationpininmap

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PinCategoriesDialogFragment(checkedArray: BooleanArray) : DialogFragment() {
    private val arrayChecked: BooleanArray = checkedArray

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.map_filter_pin)
                .setMultiChoiceItems(
                    R.array.pin_categories,
                    arrayChecked
                ) { _, which,
                    isChecked ->

                    arrayChecked[which] = isChecked
                }

            builder.setPositiveButton(getString(R.string.map_filter_pin)) { _, _ ->
                val activity = activity as MapsActivity

                activity.showPinInSelectedCategories(arrayChecked)
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}