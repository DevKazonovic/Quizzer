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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInputFields()

        initDatePicker()

        initClearFiltersListener()

        observeData()

        binding.imageViewClose.setOnClickListener {
            this.dismiss()
        }

    }

    private fun observeData() {
        viewModel.filterByDate.observe(viewLifecycleOwner, { date ->
            if (date == null) {
                binding.viewDatePicker.text = getString(R.string.dialogfilter_dateinput_hint)
            } else {
                binding.viewDateInputClear.show()
            }
        })
        viewModel.filterByCat.observe(viewLifecycleOwner, { id ->
            if (id == null) {
                binding.editTextCategoryInput.text =
                    Editable.Factory.getInstance().newEditable("All")
            } else {
                binding.viewCategoryInputClear.show()
            }
        })
    }

    private fun initClearFiltersListener() {
        binding.viewDateInputClear.setOnClickListener {
            viewModel.onFilterByDate(null)
            binding.viewDateInputClear.hide()
        }
        binding.viewCategoryInputClear.setOnClickListener {
            viewModel.onFilterByCat(null)
            binding.viewCategoryInputClear.hide()
        }
    }

    private fun initInputFields() {
        val catsAdapter = MaterialSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cats.map { item -> item.name }.toTypedArray()
        )

        binding.editTextCategoryInput.threshold = Integer.MAX_VALUE


        binding.editTextCategoryInput.setAdapter(catsAdapter)
        cats.find { item -> item.id == viewModel.currentCatID() }?.let {
            binding.editTextCategoryInput.text = Editable.Factory().newEditable(it.name)
        }


        binding.editTextCategoryInput.setOnItemClickListener { parent, view, position, id ->
            viewModel.onFilterByCat(cats.find { item ->
                item.name == binding.editTextCategoryInput.text.toString()
            }?.id)
        }

    }

    private fun initDatePicker() {
        //Date Picker
        viewModel.currentDate()?.let {
            binding.viewDatePicker.text =
                Editable.Factory.getInstance().newEditable(Converters.noTimeDateToString(it.time))
        }
        val builder = MaterialDatePicker.Builder.datePicker()


        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            Timber.d("Date String = ${picker.headerText}:: Date epoch value = $it")
            binding.viewDatePicker.text =
                Editable.Factory.getInstance().newEditable(Converters.noTimeDateToString(it))
            viewModel.onFilterByDate(it)

        }

        binding.viewDatePicker.setOnClickListener {
            picker.show(requireActivity().supportFragmentManager, picker.toString())
        }

    }

}