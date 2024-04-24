package com.example.tripy.presentation.ui.main2

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripy.R
import com.example.tripy.databinding.FragmentExpenseBinding
import com.example.tripy.databinding.ToolBarBinding
import com.example.tripy.presentation.Adapters.IconAdapter
import com.example.tripy.room.Category
import com.example.tripy.room.ExpenseTable
import com.example.tripy.room.TripTable
import com.example.tripy.utils.Constants
import com.example.tripy.utils.parseDate
import com.example.tripy.utils.showDateDialog
import com.example.tripy.viewmodels.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseFragment : Fragment(), IconAdapter.OnItemClickListener {

    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!

    private var _toolbarBinding: ToolBarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private lateinit var description: String
    private var amount: Double = 0.0
    private lateinit var date: String
    private lateinit var trip: TripTable
    private var expenseTable: ExpenseTable? = null
    private var category: Category = Category(0, "")
 //   private lateinit var slider: Slider
    private val viewModel by viewModels<ExpenseViewModel>()

  //  private  lateinit var  iconAdapter:IconAdapter
private  var position: Int? = null

    private val iconList = listOf(
        Category(R.drawable.transport, "Transport"),
        Category(R.drawable.flight, "Flight"),
        Category(R.drawable.hotel, "Hotel"),
        Category(R.drawable.restaurant, "Restaurant"),
        Category(R.drawable.local_bar, "Local Bar"),
        Category(R.drawable.oil, "Oil"),
        Category(R.drawable.parking, "Parking"),
        Category(R.drawable.shopping, "Shopping"),
        Category(R.drawable.gifts, "Gifts")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        _toolbarBinding = ToolBarBinding.bind(binding.toolbar.root)

        Log.e("expense fragment", "onCreateView")
        (activity as TripActivity?)?.disableBottomNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.e("expense fragment", "onViewCreated")
        //toolbar setup
        toolbarBinding.title.text = getString(R.string.add_expense)

        //get trip info from home activity
        val tripArgument: TripTable? = arguments?.getParcelable(Constants.ARG_SELECTED_TRIP)
        if (tripArgument != null) {
            trip = tripArgument
        }
        date = trip.startDate

        // here is the initial date to show to user
        binding.date.text = trip.startDate
        //get expense from  expense recyclerview
        val argument: ExpenseTable? = arguments?.getParcelable(Constants.ARG_SELECTED_EXPENSE)



        //recycler for categories
      val iconAdapter = IconAdapter(iconList,this)
        binding.iconRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = iconAdapter

        }



        if (argument?.tripId != null) {
            expenseTable = argument

            Log.e("argument-->>", argument.toString())

            binding.description.text = expenseTable?.description?.toEditable()
            binding.amount.text = expenseTable?.amount?.toString()?.toEditable()
            binding.date.text = expenseTable?.date?.toEditable()
            date = expenseTable?.date.toString()




        }


//        // Set up Slider listener
//      binding.recyclerViewSlider.addOnChangeListener { _, value, _ ->
//            binding.iconRecycler.scrollToPosition(value.toInt())
//        }

        toolbarBinding.icon.setOnClickListener {

            requireActivity().onBackPressed()
        //   findNavController().popBackStack()

        }


        binding.calendar.setOnClickListener {
            val minDate = parseDate(trip.startDate)
            val maxDate = parseDate(trip.endDate)

            binding.calendar.isEnabled=false
            showDateDialog(
                requireActivity(), requireActivity().supportFragmentManager,
                minDate = minDate, maxDate = maxDate
            ) { selectedDate ->
                date = selectedDate
                binding.date.text = date

                binding.calendar.isEnabled=true
            }
        }
        binding.saveExpense.setOnClickListener {
            getDescriptionAndAmount()


            setupWarningVisibility(binding.description,binding.descriptionWarning)
            setupWarningVisibility(binding.amount, binding.amountWarning)
            onButtonClick()
        }





        // warning text visibility here-->>
   //     setupWarningVisibility(binding.description, binding.descriptionWarning)
   //     setupWarningVisibility(binding.amount, binding.amountWarning)
    }

    private fun validation(): Boolean {
        var isValid = true

        // Validate description
        val desc = binding.description.text.toString()
        if (desc.isEmpty()) {
            binding.descriptionWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.descriptionWarning.visibility = View.GONE
        }

        // Validate amount
        val amountText = binding.amount.text.toString()
        if (amountText.isEmpty()) {
            binding.amountWarning.visibility = View.VISIBLE
            isValid = false
        } else {
            try {
                amount = amountText.toDouble()
                binding.amountWarning.visibility = View.GONE
            } catch (e: NumberFormatException) {
                binding.amountWarning.visibility = View.VISIBLE
                isValid = false
            }
        }
        // Validate date
        if (date.isEmpty()) {
            isValid = false
        }

        // Validate icon selection
        if (category.icon == 0 || category.name!!.isEmpty()) {
            isValid = false
        }

        return isValid
    }


    private fun onButtonClick() {
        if (validation()) {
            if (expenseTable != null && expenseTable?.tripId != null) {

                updateExpense()
            } else {

                insertExpense()
            }
        } else {

            // Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertExpense() {

        Log.d("save expense btn -->>","insert operation")
        expenseTable = ExpenseTable(
            tripId = trip.id,
            description = description,
            amount = amount,
            date = date,
            category = category
        )
        viewModel.insertExpense(expenseTable!!)


        // here setting the field to emtpoy
        resetFields()

        replaceFragment()
//
//        val intent=Intent(requireActivity(),TripActivity::class.java)
//        startActivity(intent)
    }

    private fun updateExpense() {
        Log.d("save expense btn -->>","update operation")
        expenseTable = expenseTable?.let {
            ExpenseTable(
                id = it.id,
                tripId = trip.id,
                description = description,
                amount = amount,
                date = date,
                category = category
            )
        }

        viewModel.updateExpense(expenseTable!!)

        // Reset fields after successful update
        resetFields()
        replaceFragment()
    }

    private fun resetFields() {
        binding.description.text = null
        binding.amount.text = null
        date = trip.startDate
        binding.date.text = date
        category = Category(0, "")
    }


//    private fun onButtonClick() {
//        if (validation()) {
//            // Proceed with expense insertion
//            expenseTable = ExpenseTable(
//                tripId = trip.id,
//                description = description,
//                amount = amount,
//                date = date,
//                category = category
//            )
//            Log.e("expense", expenseTable.toString())
//
//            viewModel.insertExpense(expenseTable)
//
//
//            //reset to empty after sucess of insert
//            binding.description.text = null
//            binding.amount.text = null
//            date = trip.startDate
//            binding.date.text = date
//            category = Category(0, "")
//
//            replaceFragment()
//
//            // findNavController().popBackStack()
//        } else {
////            Toast.makeText(requireContext(), "fill all fields", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun replaceFragment() {

        val fragment = TripDetailFragment()

        val bundle = Bundle().apply {
            putParcelable(Constants.ARG_SELECTED_TRIP, trip)
        }

        fragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.container, fragment)
        transaction.commit()
        hideKeyboard()
        expenseTable=null
    }

    private fun getDescriptionAndAmount() {
        description = binding.description.text.toString()
        val amountText = binding.amount.text.toString()

        if (description.isNotEmpty()) {
            binding.amountWarning.visibility = View.GONE
        } else {
            binding.descriptionWarning.visibility = View.VISIBLE
        }

        if (amountText.isNotEmpty()) {
            try {
                amount = amountText.toDouble()
                binding.amountWarning.visibility = View.GONE
            } catch (e: NumberFormatException) {
                binding.amountWarning.visibility = View.VISIBLE
            }
        } else {
            binding.amountWarning.visibility = View.VISIBLE
        }
    }

    private fun setupWarningVisibility(editText: EditText, warningText: TextView) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                warningText.visibility = if (s.isNullOrBlank()) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onItemClick(category: Category) {


        this.category = category
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
        hideKeyboard()
    }


    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
