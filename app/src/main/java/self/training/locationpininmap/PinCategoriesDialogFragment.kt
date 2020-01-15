package self.training.locationpininmap

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class PinCategoriesDialogFragment(checkedArray: BooleanArray) : DialogFragment() {
    private var arrayChecked: BooleanArray = checkedArray
    private val backupArray = booleanArrayOf(true, true, true, true, true)
    private lateinit var alertDialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arrayChecked.copyInto(backupArray)

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.map_filter_pin)
                .setMultiChoiceItems(
                    R.array.pin_categories,
                    arrayChecked
                ) { _, which,
                    isChecked ->

                    val countTrue = arrayChecked.count { itBool -> itBool }

                    if ((countTrue > 1) || (countTrue == 1 && !isChecked)) {
                        arrayChecked[which] = isChecked
                    } else if (countTrue == 0 && !isChecked) {
                        Toast.makeText(
                            activity,
                            "Selecionar pelo menos uma categoria",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                builder.setOnDismissListener { backupArray.copyInto(arrayChecked) }
            } else {
                builder.setOnCancelListener { backupArray.copyInto(arrayChecked) }
            }

            builder.setPositiveButton(getString(R.string.map_filter_pin)) { _, _ ->

                val activity = activity as MapsActivity
                val countTrue = arrayChecked.count { itBool -> itBool }

                if (countTrue > 0) {
                    activity.showPinInSelectedCategories(arrayChecked)
                }
            }

            alertDialog = builder.create()

            alertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
