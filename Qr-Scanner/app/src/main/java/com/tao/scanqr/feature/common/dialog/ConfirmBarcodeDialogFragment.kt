package com.tao.scanqr.feature.common.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tao.scanqr.R
import com.tao.scanqr.extension.toStringId
import com.tao.scanqr.model.Barcode

class ConfirmBarcodeDialogFragment : DialogFragment() {

    interface Listener {
        fun onBarcodeConfirmed(barcode: Barcode)
        fun onBarcodeDeclined()
    }

    companion object {
        private const val BARCODE_KEY = "BARCODE_FORMAT_MESSAGE_ID_KEY"

        fun newInstance(barcode: Barcode): ConfirmBarcodeDialogFragment {
            return ConfirmBarcodeDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BARCODE_KEY, barcode)
                }
                isCancelable = false
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = parentFragment as? Listener
        val barcode = arguments?.getSerializable(BARCODE_KEY) as? Barcode
                ?: throw IllegalArgumentException("No barcode passed")
        val messageId = barcode.format.toStringId()

        val dialog = AlertDialog.Builder(requireActivity(), R.style.DialogTheme)
                .setTitle(R.string.fragment_scan_barcode_from_camera_confirm_barcode_dialog_title)
                .setMessage(messageId)
                .setCancelable(false)
                .setPositiveButton(R.string.fragment_scan_barcode_from_camera_confirm_barcode_dialog_positive_button) { _, _ ->
                    listener?.onBarcodeConfirmed(barcode)
                }
                .setNegativeButton(R.string.fragment_scan_barcode_from_camera_confirm_barcode_dialog_negative_button) { _, _ ->
                    listener?.onBarcodeDeclined()
                }
                .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.blue))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.red))
        }

        return dialog
    }
}