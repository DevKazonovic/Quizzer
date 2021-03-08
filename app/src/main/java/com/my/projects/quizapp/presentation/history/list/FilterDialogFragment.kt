package com.my.projects.quizapp.presentation.history.list

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.my.projects.quizapp.QuizApplication
import com.my.projects.quizapp.R
import com.my.projects.quizapp.databinding.FragmentDialogFilterBinding
import com.my.projects.quizapp.presentation.ViewModelProviderFactory
import com.my.projects.quizapp.presentation.common.adapter.MaterialSpinnerAdapter
import com.my.projects.quizapp.util.Const.Companion.cats
import com.my.projects.quizapp.util.converters.Converters
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.show
import timber.log.Timber
import javax.inject.Inject


class FilterDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private val viewModel: HistoryViewModel by navGraphViewModels(R.id.graph_history_list) {
        viewModelFactory
    }

    private lateinit var binding: FragmentDialogFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QuizApplication).component.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initInputFields()

        initDatePicker()

        initClearFiltersListener()

        observeData()

        binding.btnClose.setOnClickListener {
            this.dismiss()
        }

    }

    private fun observeData() {
        viewModel.filterByDate.observe(viewLifecycleOwner, { date ->
            if (date == null) {
                binding.datePicker.text = getString(R.string.dialogfilter_dateinput_hint)
            } else {
                Timber.d("Show Clear filterByDate $date")
                binding.txtviewDialogfilterSavedateclear.show()
            }
        })

        viewModel.filterByCat.observe(viewLifecycleOwner, { id ->
            if (id == null) {
                binding.categoryInputEt.text = Editable.Factory.getInstance().newEditable("All")
            } else {
                Timber.d("Show Clear filterByCat $id")
                binding.txtviewDialogfilterCategoryinputclear.show()
            }
        })
    }

    private fun initClearFiltersListener() {
        binding.txtviewDialogfilterSavedateclear.setOnClickListener {
            Timber.d("Clear Date")
            viewModel.onFilterByDate(null)
            binding.txtviewDialogfilterSavedateclear.hide()
        }
        binding.txtviewDialogfilterCategoryinputclear.setOnClickListener {
            Timber.d("Clear Cat")
            viewModel.onFilterByCat(null)
            binding.txtviewDialogfilterCategoryinputclear.hide()
        }
    }

    private fun initInputFields() {
        val catsAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cats.map { item -> item.name }.toTypedArray()
        )

        binding.categoryInputEt.threshold = Integer.MAX_VALUE


        binding.categoryInputEt.setAdapter(catsAdapter)
        cats.find { item -> item.id == viewModel.currentCatID() }?.let {
            binding.categoryInputEt.text = Editable.Factory().newEditable(it.name)
        }


        binding.categoryInputEt.setOnItemClickListener { parent, view, position, id ->
            viewModel.onFilterByCat(cats.find { item -> item.name == binding.categoryInputEt.text.toString() }?.id)
        }

    }

    private fun initDatePicker() {
        //Date Picker
        viewModel.currentDate()?.let {
            binding.datePicker.text =
                Editable.Factory.getInstance().newEditable(Converters.noTimeDateToString(it.time))
        }
        val builder = MaterialDatePicker.Builder.datePicker()


        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            Timber.d("Date String = ${picker.headerText}:: Date epoch value = $it")
            binding.datePicker.text =
                Editable.Factory.getInstance().newEditable(Converters.noTimeDateToString(it))
            viewModel.onFilterByDate(it)

        }

        binding.datePicker.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }

    }

}