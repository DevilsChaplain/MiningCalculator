package app.beer.coin_galaxy.ui.fragments.gpu_stat

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import app.beer.coin_galaxy.MainActivity
import app.beer.coin_galaxy.R

class GpuStatFragment : Fragment() {

    private lateinit var vm: GpuViewModel

    private val gpuItems = mutableListOf<Gpu>()

    private lateinit var gpusListSpinner: AppCompatSpinner
    private lateinit var resultTable: TableLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gpu_stat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(activity as MainActivity)[GpuViewModel::class.java]

        gpusListSpinner = view.findViewById(R.id.gpu_items)
        resultTable = view.findViewById(R.id.result_table)
        progressBar = view.findViewById(R.id.progress_bar)

        // ставим слущатель клика на каждый элемент
        gpusListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val gpuItem = gpuItems[position]
                addViewToTable(gpuItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // наблюдаем за изменениями и добавляем элементы в список
        vm.gpusLiveData.observe(viewLifecycleOwner, { state ->
            progressBar.isVisible = state.isLoading
            if (!state.isLoading) {
                state.data?.let { data ->
                    gpuItems.clear()
                    gpuItems.addAll(data)
                    gpusListSpinner.adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        gpuItems.map { it.name })
                }
            }
        })
    }

    /**
     * добавляем строку в таблицу
     * */
    private fun addViewToTable(gpu: Gpu) {
        resultTable.isVisible = true
        TableRow(context).apply {
            orientation = TableRow.VERTICAL
            val nameTV = getTextView(gpu.name)
            val priceTV = getTextView(gpu.price)
            val ethTV = getTextView(gpu.miningData["eth"] ?: "0.0 Mh/s")
            val etcTV = getTextView(gpu.miningData["etc"] ?: "0.0 Mh/s")
            val expTV = getTextView(gpu.miningData["exp"] ?: "0.0 Mh/s")
            val ubqTV = getTextView(gpu.miningData["ubq"] ?: "0.0 Mh/s")
            val rvnTV = getTextView(gpu.miningData["rvn"] ?: "0.0 Mh/s")
            val beanTV = getTextView(gpu.miningData["bean"] ?: "0.0 Mh/s")
            val montlyIncome = getTextView(gpu.monthIncome)
            val paybackTV = getTextView(gpu.payback)
            addView(nameTV)
            addView(priceTV)
            addView(ethTV)
            addView(etcTV)
            addView(expTV)
            addView(ubqTV)
            addView(rvnTV)
            addView(beanTV)
            addView(montlyIncome)
            addView(paybackTV)
            resultTable.addView(this)
        }
    }

    private fun getTextView(textS: String): TextView {
        return TextView(context).apply {
            text = textS
            setPadding(24, 24, 64, 24)
            setTextColor(resources.getColor(R.color.white))
            textSize = 14f
            layoutParams = TableRow.LayoutParams(0)
            gravity = Gravity.CENTER
            minWidth = 60
        }
    }

}