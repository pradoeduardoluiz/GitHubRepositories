package br.com.prado.eduardo.luiz.githubrepositories.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.addEndDrawable(@DrawableRes drawableRes: Int) {
  val drawable = ContextCompat.getDrawable(context, drawableRes)
  addEndDrawable(drawable)
}

fun TextInputEditText.addEndDrawable(drawable: Drawable?) {
  drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
  setCompoundDrawables(null, null, drawable, null)
}

fun TextInputEditText.removeDrawables() {
  setCompoundDrawables(null, null, null, null)
}

@SuppressLint("ClickableViewAccessibility")
fun TextInputEditText.onEndDrawableClicked(onClicked: (view: TextInputEditText) -> Unit) {
  setOnTouchListener { v, event ->
    var hasConsumed = false
    if (v is TextInputEditText) {
      if (event.x >= v.width - v.totalPaddingRight) {
        if (event.action == MotionEvent.ACTION_UP) {
          onClicked(this)
        }
        hasConsumed = true
      }
    }
    hasConsumed
  }
}
