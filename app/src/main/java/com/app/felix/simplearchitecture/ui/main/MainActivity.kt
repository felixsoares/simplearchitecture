package com.app.felix.simplearchitecture.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.app.felix.simplearchitecture.R
import com.app.felix.simplearchitecture.data.model.ChuckNorrisFact
import com.app.felix.simplearchitecture.data.state.ViewState
import com.app.felix.simplearchitecture.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserver()
        setupActions()
    }

    private fun setupActions() {
        binding.btnFact.setOnClickListener {
            viewModel.getFact()
        }

        binding.chipGroupCategories.setOnCheckedChangeListener { group, checkedId ->
            viewModel.setCategory("")
            group.children.forEach { view ->
                if (view.id == checkedId) {
                    val selectChip = (view as Chip)
                    selectChip.isChecked = true
                    viewModel.setCategory(selectChip.text.toString())
                } else {
                    (view as Chip).isChecked = false
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.factViewState.observe(this, { state ->
            when (state) {
                is ViewState.Loading -> setupLoading()
                is ViewState.ContentLoaded -> setupContent(state.data)
                is ViewState.ContentFailure -> setupError()
            }
        })

        viewModel.categoriesViewState.observe(this, { state ->
            when (state) {
                is ViewState.ContentLoaded -> createCategories(state.data)
                else -> setupCategoriesVisibility(false)
            }
        })
    }

    private fun createCategories(categories: List<String>) {
        for (category in categories) {
            val chip = layoutInflater.inflate(
                R.layout.layout_chip_choice,
                binding.chipGroupCategories,
                false
            ) as Chip
            chip.text = category
            binding.chipGroupCategories.addView(chip)
        }
        setupCategoriesVisibility(true)
    }

    private fun setupError() {
        setupFactVisibility(
            progressVisibility = false,
            textVisibility = true,
            buttonVisibility = true
        )
        setupCategoriesVisibility(false)

        binding.txtFact.text = getString(R.string.error)
        binding.btnFact.text = getString(R.string.try_again)
    }

    private fun setupContent(data: ChuckNorrisFact) {
        setupFactVisibility(
            progressVisibility = false,
            textVisibility = true,
            buttonVisibility = true
        )

        binding.txtFact.text = data.value
        binding.btnFact.text = getString(R.string.try_another)
    }

    private fun setupLoading() {
        setupFactVisibility(
            progressVisibility = true,
            textVisibility = false,
            buttonVisibility = false
        )
    }

    private fun setupFactVisibility(
        progressVisibility: Boolean,
        textVisibility: Boolean,
        buttonVisibility: Boolean
    ) {
        binding.progressBar.isVisible = progressVisibility
        binding.txtFact.isVisible = textVisibility
        binding.btnFact.isVisible = buttonVisibility
    }

    private fun setupCategoriesVisibility(categoriesVisibility: Boolean) {
        binding.txtChoseCategory.isVisible = categoriesVisibility
        binding.chipGroupCategories.isVisible = categoriesVisibility
    }
}