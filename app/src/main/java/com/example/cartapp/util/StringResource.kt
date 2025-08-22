package com.example.cartapp.util

import android.content.Context
import java.text.NumberFormat
import java.util.Locale
import com.example.cartapp.R

/**
 * String resource utility class for XML layouts
 * Provides easy access to string resources with proper formatting
 */
object StringResource {
    
    fun getString(context: Context, resId: Int): String {
        return context.getString(resId)
    }
    
    fun getString(context: Context, resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
    
    // Product Detail
    fun productDetailAddToCart(context: Context) = getString(context, R.string.product_detail_add_to_cart)
    
    fun productDetailAddedToCart(context: Context) = getString(context, R.string.product_detail_added_to_cart)
    
    fun productDetailAddToFavorites(context: Context) = getString(context, R.string.product_detail_add_to_favorites)
    
    fun productDetailRemoveFromFavorites(context: Context) = getString(context, R.string.product_detail_remove_from_favorites)
    
    fun productDetailDiscount(context: Context, percentage: Int) = getString(context, R.string.product_detail_discount, percentage)
    
    fun productDetailPriceFormat(price: Double) = formatCurrency(price)
    
    fun productDetailOriginalPriceFormat(price: Double) = formatCurrency(price)
    
    // Cart Screen
    fun cartTitle(context: Context) = getString(context, R.string.cart_title)
    
    fun cartPriceLabel(context: Context) = getString(context, R.string.cart_price_label)
    
    fun cartDiscountLabel(context: Context) = getString(context, R.string.cart_discount_label)
    
    fun cartTotalLabel(context: Context) = getString(context, R.string.cart_total_label)
    
    fun cartCheckout(context: Context) = getString(context, R.string.cart_checkout)
    
    fun cartRemoveItem(context: Context) = getString(context, R.string.cart_remove_item)
    
    fun cartPriceFormat(price: Double) = formatCurrency(price)
    
    fun cartDiscountPriceFormat(price: Double) = formatCurrency(price)
    
    // Favorite Screen
    fun favoriteTitle(context: Context) = getString(context, R.string.favorite_title)
    
    fun favoritePriceFormat(price: Double) = formatCurrency(price)
    
    // Checkout Screen
    fun checkoutTitle(context: Context) = getString(context, R.string.checkout_title)
    
    fun checkoutName(context: Context) = getString(context, R.string.checkout_name)
    
    fun checkoutEmail(context: Context) = getString(context, R.string.checkout_email)
    
    fun checkoutPhone(context: Context) = getString(context, R.string.checkout_phone)
    
    fun checkoutPhoneRequired(context: Context) = getString(context, R.string.checkout_phone_required)
    
    fun checkoutNameRequired(context: Context) = getString(context, R.string.checkout_name_required)
    
    fun checkoutEmailRequired(context: Context) = getString(context, R.string.checkout_email_required)
    
    fun checkoutEmailInvalid(context: Context) = getString(context, R.string.checkout_email_invalid)
    
    fun checkoutPhoneInvalid(context: Context) = getString(context, R.string.checkout_phone_invalid)
    
    fun checkoutTotalAmount(context: Context, amount: Double) = getString(context, R.string.checkout_total_amount, amount)
    
    fun checkoutConfirmOrder(context: Context) = getString(context, R.string.checkout_confirm_order)
    
    fun checkoutConfirmOrderButton(context: Context) = getString(context, R.string.checkout_confirm_order_button)
    
    fun checkoutSuccess(context: Context) = getString(context, R.string.checkout_success)
    
    fun checkoutError(context: Context) = getString(context, R.string.checkout_error)
    
    // Order Success Screen
    fun orderSuccessTitle(context: Context) = getString(context, R.string.order_success_title)
    
    fun orderSuccessMessage(context: Context, orderNumber: String) = getString(context, R.string.order_success_message, orderNumber)
    
    fun orderSuccessThankYou(context: Context) = getString(context, R.string.order_success_thank_you)
    
    fun orderSuccessContinueShopping(context: Context) = getString(context, R.string.order_success_continue_shopping)
    
    fun orderSuccessGoHome(context: Context) = getString(context, R.string.order_success_go_home)
    
    fun orderSuccessOrderDetails(context: Context) = getString(context, R.string.order_success_order_details)
    
    fun orderSuccessEstimatedDelivery(context: Context) = getString(context, R.string.order_success_estimated_delivery)
    
    fun orderSuccessTrackingInfo(context: Context) = getString(context, R.string.order_success_tracking_info)
    
    // Common
    fun commonLoading(context: Context) = getString(context, R.string.common_loading)
    
    // Home Screen
    fun homeProducts(context: Context) = getString(context, R.string.home_products)
    
    fun homeClearFilters(context: Context) = getString(context, R.string.home_clear_filters)
    
    fun homeFilter(context: Context) = getString(context, R.string.home_filter)
    
    fun homeSort(context: Context) = getString(context, R.string.home_sort)
    
    fun homeSearchHint(context: Context) = getString(context, R.string.home_search_hint)
    
    fun homeTotalProducts(context: Context, count: Int) = getString(context, R.string.home_total_products, count)
    
    fun homeTotalProductsFormat(context: Context, count: Int) = getString(context, R.string.home_total_products_format, count)
    
    fun homeNoProducts(context: Context) = getString(context, R.string.home_no_products)
    
    fun homeErrorMessage(context: Context) = getString(context, R.string.home_error_message)
    
    // Filter Screen
    fun filterTitle(context: Context) = getString(context, R.string.filter_title)
    
    fun filterClear(context: Context) = getString(context, R.string.filter_clear)
    
    fun filterApply(context: Context) = getString(context, R.string.filter_apply)
    
    fun filterAllCategories(context: Context) = getString(context, R.string.filter_all_categories)
    
    // Sort Screen
    fun sortTitle(context: Context) = getString(context, R.string.sort_title)
    
    fun sortDefault(context: Context) = getString(context, R.string.sort_default)
    
    fun sortPriceLowToHigh(context: Context) = getString(context, R.string.sort_price_low_to_high)
    
    fun sortPriceHighToLow(context: Context) = getString(context, R.string.sort_price_high_to_low)
    
    fun sortRatingAsc(context: Context) = getString(context, R.string.sort_rating_asc)
    
    fun sortRatingDesc(context: Context) = getString(context, R.string.sort_rating_desc)
    
    fun sortDiscountDesc(context: Context) = getString(context, R.string.sort_discount_desc)
    
    fun sortStockAsc(context: Context) = getString(context, R.string.sort_stock_asc)
    
    fun sortStockDesc(context: Context) = getString(context, R.string.sort_stock_desc)
    
    fun sortNameAToZ(context: Context) = getString(context, R.string.sort_name_a_to_z)
    
    fun sortNameZToA(context: Context) = getString(context, R.string.sort_name_z_to_a)
    
    fun sortRating(context: Context) = getString(context, R.string.sort_rating)
    
    // Navigation
    fun navHome(context: Context) = getString(context, R.string.nav_home)
    
    fun navFavorites(context: Context) = getString(context, R.string.nav_favorites)
    
    fun navCart(context: Context) = getString(context, R.string.nav_cart)
    
    // Utility function for currency formatting
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
        return formatter.format(amount)
    }
} 