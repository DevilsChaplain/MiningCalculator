package app.beer.coin_galaxy.utils

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.beer.coin_galaxy.MainActivity
import app.beer.coin_galaxy.R
import com.bumptech.glide.Glide

/**
 * загрузка фото из интернета
 * */
fun ImageView.loadAndSetImage(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

/**
 * смена фрагмента из другого врагмента
 * */
fun Fragment.replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    replaceFragmentWithActivity(activity as MainActivity?, fragment, addToBackStack)
}

/**
 * смена фрагмента из Activity
 * */
fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false,
    id: String? = null
) {
    replaceFragmentWithActivity(this, fragment, addToBackStack, id)
}

private fun replaceFragmentWithActivity(
    activity: AppCompatActivity?,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    id: String? = null
) {
    activity?.apply {
        if (addToBackStack) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.data_container, fragment, id)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.data_container, fragment)
                .commit()
        }
    }
}

/**
 * Показ всплывающего сообшения
 * */
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

/**
 * Перезапуск MainActivity
 * */
fun Fragment.restartActivity() {
    val intent = Intent(context, MainActivity::class.java)
    activity?.finish()
    activity?.startActivity(intent)
}
