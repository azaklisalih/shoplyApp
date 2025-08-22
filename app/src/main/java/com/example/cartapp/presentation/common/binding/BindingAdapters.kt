package com.example.cartapp.presentation.common.binding
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        } else {
            view.setImageDrawable(null)
        }
    }

    @JvmStatic
    @BindingAdapter("formattedPrice")
    fun formatPrice(view: TextView, price: Double?) {
        view.text = if (price != null) {
            formatCurrency(price, view.context.resources.configuration.locales[0])
        } else {
            ""
        }
    }

    @JvmStatic
    @BindingAdapter("formattedDiscountPrice")
    fun formatDiscountPrice(view: TextView, amount: String?) {
        view.text = if (!amount.isNullOrEmpty()) {
            try {
                val price = amount.toDouble()
                formatCurrency(price, view.context.resources.configuration.locales[0])
            } catch (e: NumberFormatException) {
                amount
            }
        } else {
            ""
        }
    }

    private fun formatCurrency(amount: Double, locale: Locale): String {
        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        return numberFormat.format(amount)
    }

    @BindingAdapter("strikeThrough")
    @JvmStatic
    fun strikeThrough(textView: TextView, strike: Boolean) {
        textView.paintFlags = if (strike)
            textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

}
