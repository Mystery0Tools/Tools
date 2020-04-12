package vip.mystery0.tools.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


fun <T : View> T.changeLayoutParams(action: (ViewGroup.LayoutParams) -> Unit) {
	val params = layoutParams
	action(params)
	layoutParams = params
}

/**
 * @see [ViewUtils.java](https://github.com/zhanghai/MaterialFiles/blob/master/app/src/main/java/me/zhanghai/android/files/util/ViewUtils.java)
 * @author Hai Zhang
 */
fun View.fadeOut(duration: Long,
				 gone: Boolean = true,
				 next: (() -> Unit)? = null) {
	if (visibility != View.VISIBLE || alpha == 0F) {
		// Cancel any starting animation.
		animate().alpha(0F)
				.setDuration(0)
				.start()
		visibility = if (gone) View.GONE else View.INVISIBLE
		next?.invoke()
	} else {
		animate().alpha(0F)
				.setDuration(duration)
				.setInterpolator(FastOutLinearInInterpolator())
				.setListener(object : AnimatorListenerAdapter() {
					var mCanceled = false

					override fun onAnimationCancel(animation: Animator?) {
						mCanceled = true
					}

					override fun onAnimationEnd(animation: Animator?) {
						if (!mCanceled) {
							visibility = if (gone) View.GONE else View.INVISIBLE
							next?.invoke()
						}
					}
				})
				.start()
	}
}

/**
 * @see [ViewUtils.java](https://github.com/zhanghai/MaterialFiles/blob/master/app/src/main/java/me/zhanghai/android/files/util/ViewUtils.java)
 * @author Hai Zhang
 */
fun View.fadeIn(duration: Int) {
	if (visibility == View.VISIBLE && alpha == 1f) {
		// Cancel any starting animation.
		animate().alpha(1f)
				.setDuration(0)
				.start()
		return
	}
	alpha = if (visibility == View.VISIBLE) alpha else 0F
	visibility = View.VISIBLE
	animate().alpha(1f)
			.setDuration(duration.toLong())
			.setInterpolator(FastOutSlowInInterpolator()) // NOTE: We need to remove any previously set listener or Android will reuse it.
			.setListener(null)
			.start()
}