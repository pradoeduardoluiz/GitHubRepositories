package br.com.prado.eduardo.luiz.githubrepositories.extensions

import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout

var ShimmerFrameLayout.isShimmering: Boolean
  get() = isShimmerVisible
  set(value) {
    isVisible = value
    if (value) showShimmer(true) else hideShimmer()
  }
