package com.example.grocerysystem

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.lang.Exception

class Helper {
    companion object {

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun hideKeyboard(view: View){
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }catch (e: Exception){

            }
        }


        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        var pDialog: ProgressDialog? = null

        fun showLoadingDialog(mContext: Context?) {
            pDialog = ProgressDialog(mContext)
            pDialog!!.setCancelable(false)
            pDialog!!.setMessage("Please wait...")
            pDialog!!.show()
//        pDialog.setContentView(R.layout.progress_layout);
//        pDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        fun hideLoadingDialog() {
            if (pDialog != null && pDialog!!.isShowing) {
                pDialog!!.dismiss()
            }
        }
    }



}