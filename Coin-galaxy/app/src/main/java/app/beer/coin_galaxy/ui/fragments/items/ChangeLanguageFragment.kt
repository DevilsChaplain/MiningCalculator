package app.beer.coin_galaxy.ui.fragments.items

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.beer.coin_galaxy.R
import app.beer.coin_galaxy.utils.Constants
import app.beer.coin_galaxy.utils.restartActivity

class ChangeLanguageFragment : DialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences("Coin_galaxy_sh", Context.MODE_PRIVATE)

        val languages = resources.getStringArray(R.array.languags)

        val listView: ListView = view.findViewById(R.id.languages)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_single_choice,
            languages
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, view, position, _ ->
            val selectedLanguage = languages[position]
            sharedPreferences.edit().let {
                it.putString(Constants.CURRENT_LANGUAGE_KEY, selectedLanguage)
                it.apply()
            }
            restartActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog!!.window!!.setLayout(width, height)
    }

    companion object {
        fun newInstance(
            currentSelectedLanguage: String,
            fm: FragmentManager
        ): ChangeLanguageFragment = ChangeLanguageFragment().apply {
            arguments = Bundle().apply {
                putString(currentSelectedLanguage, null)
            }
            show(fm, "change_language_dialog")
        }
    }

}