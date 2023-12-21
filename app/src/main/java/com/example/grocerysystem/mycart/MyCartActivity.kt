package com.example.grocerysystem.mycart

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ActivityMyCartBinding
import com.example.grocerysystem.db.AppDBViewModel
import com.example.grocerysystem.util.Helper
import com.example.grocerysystem.util.UserManager
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class MyCartActivity : AppCompatActivity(), PaymentResultListener {

    private var _binding: ActivityMyCartBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!

    lateinit var userManager: UserManager

    private val cartViewModel: AppDBViewModel by viewModels<AppDBViewModel>()
    private var userId = ""
    private var amountToPay = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_cart)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cart)
        _context = this
        userManager = UserManager(context)
        userManager.userIdFlow.asLiveData().observe(this) {
            userId = it.toString()
            cartViewModel.getAllCartItems(userId)
        }

//        Checkout Razor pay initiation
        Checkout.preload(applicationContext)
        /*   val co = Checkout()
           co.setKeyID("rzp_live_XXXXXXXXXXXXXX")
   */

        bindObservables()
        binding.btnProceed.setOnClickListener {
            startPayment()
        }
    }

    private fun bindObservables() {
        cartViewModel.cartListLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Log.i("asdfghjkl", it.data.toString() + " Activity")
                    val asd: StringBuilder = java.lang.StringBuilder()
                    var amt = 0
                    if (it.data != null && it.data.isNotEmpty()) {
                        for (num in it.data) {
                            asd.append(num.title + " " + " Rs." + num.price + " x " + num.count + " \n")
                            amt += (num.price!!.toInt() * num.count.toInt())
                        }
                        binding.tvCartItems.text = asd
                        amountToPay = amt
                        binding.amtToPay.text = "Amount to Pay: Rs. ${amt.toString()}"

                    } else {
                        binding.tvCartItems.text = "No record found in your cart"

                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }

        })
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        co.setKeyID("rzp_test_nfryhLkcjNSpDF")
        try {
            val options = JSONObject()
            var amtn = 5000 * 100
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount", amtn.toString())//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "gaurav.kumar@example.com")
            prefill.put("contact", "9876543210")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.wtf("payStatus", p0)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.wtf("payStatus", p1)

    }
}