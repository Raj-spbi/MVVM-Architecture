package com.example.grocerysystem.showProducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.ShowItemDetails
import com.example.grocerysystem.addProducts.AddProductsActivity
import com.example.grocerysystem.databinding.ActivityAdminDashboardBinding
import com.example.grocerysystem.model.BannerItem
import com.example.grocerysystem.model.Items
import com.example.grocerysystem.mycart.MyCartActivity
import com.example.grocerysystem.splash.SplashScreen
import com.example.grocerysystem.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {
    private var _binding: ActivityAdminDashboardBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!
    private val showProductsViewModel: ShowProductsViewModel by viewModels<ShowProductsViewModel>()

    lateinit var adapterRecycler: AdapterRecycler
    private var isSeller: Boolean = false
    lateinit var userManager: UserManager
    var modelList = arrayListOf<BannerItem>()
    lateinit var adapter: BannerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)
        _context = this
        userManager = UserManager(context)
        checkUserOrSeller()
        loadData()
//        showProductsViewModel =
//            ViewModelProvider(
//                this,
//                ShowProductsVMFactory(ShowProductsRepository())
//            ).get(
//                ShowProductsViewModel::class.java
//            )

        binding.itemViewModel = showProductsViewModel
        binding.showProductsRecycler.layoutManager = GridLayoutManager(this, 2)
        binding.lifecycleOwner = this
        binding.mContext = this

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddProductsActivity::class.java)
            startActivity(intent)
        }
        showProductsViewModel.readAllUsers()
        bindingObservables()


        binding.btnLogout.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                userManager.removeUser()
                gotoSplash()
            }
        }

        binding.cartBtn.setOnClickListener {
            startActivity(Intent(context,MyCartActivity::class.java))
        }
    }

    private fun loadData() {
        modelList.add(
            BannerItem(
                "Name 1",
                "22-05-2019",
                "https://fastly.picsum.photos/id/76/4912/3264.jpg?hmac=VkFcSa2Rbv0R0ndYnz_FAmw02ON1pPVjuF_iVKbiiV8"
            )
        )
        modelList.add(
            BannerItem(
                "Name 2",
                "20-05-2019",
                "https://fastly.picsum.photos/id/90/3000/1992.jpg?hmac=v_xO0GFiGn3zpcKzWIsZ3WoSoxJuAEXukrYJUdo2S6g"
            )
        )
        modelList.add(
            BannerItem(
                "Name 3",
                "15-02-2020",
                "https://fastly.picsum.photos/id/111/4400/2656.jpg?hmac=leq8lj40D6cqFq5M_NLXkMYtV-30TtOOnzklhjPaAAQ"
            )
        )
        modelList.add(
            BannerItem(
                "Name 4",
                "08-05-2019",
                "https://fastly.picsum.photos/id/28/4928/3264.jpg?hmac=GnYF-RnBUg44PFfU5pcw_Qs0ReOyStdnZ8MtQWJqTfA"
            )
        )
        modelList.add(
            BannerItem(
                "Name 5",
                "22-05-2018",
                "https://fastly.picsum.photos/id/42/3456/2304.jpg?hmac=dhQvd1Qp19zg26MEwYMnfz34eLnGv8meGk_lFNAJR3g"
            )
        )

        setupCarousel()

        adapter = BannerAdapter(this, modelList)
        binding.viewPager.adapter = adapter

    }

    private fun setupCarousel() {

        binding.viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
//            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
//            // If you want a fading effect uncomment the next line:
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }
        binding.viewPager.setPageTransformer(pageTransformer)
//
//// The ItemDecoration gives the current (centered) item horizontal margin so that
//// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager.addItemDecoration(itemDecoration)


    }

    private fun gotoSplash() {
        val intent = Intent(context, SplashScreen::class.java)
        startActivity(intent)
        finishAffinity()
    }


    private fun checkUserOrSeller() {
        val isUserSeller = intent.getStringExtra(Const.isSeller)
        isSeller = isUserSeller.equals(Const.SELLER, true)

        Constants.isSellerGlobal = isSeller
        if (!Constants.isSellerGlobal) {// user
            binding.btnLogout.visibility = View.VISIBLE
            binding.btnAdd.visibility = View.GONE
            userManager.userNameFlow.asLiveData().observe(this) {

                if (it.toString().isNotBlank()) {
                    binding.greetingsTxt.text = "Hello ${it.toString()}"

                } else {
                    binding.greetingsTxt.text = "Hello User"
                }
            }
        } else {
            binding.btnLogout.visibility = View.GONE
            binding.btnAdd.visibility = View.VISIBLE
            binding.greetingsTxt.text = "Hello Admin"


        }
    }

    private fun bindingObservables() {
        showProductsViewModel.readLiveData.observe(this, androidx.lifecycle.Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    adapterRecycler = AdapterRecycler(it.data as ArrayList<Items>, ::onItemClick)
                    binding.showProductsRecycler.adapter = adapterRecycler
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

    private fun onItemClick(item: Items) {
        val intent = Intent(context, ShowItemDetails::class.java)
        intent.putExtra("itemDetails", item)
        startActivity(intent)

    }


}