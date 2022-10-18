package com.will.kgu_math.fragments.register

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.will.kgu_math.R
import com.will.kgu_math.databinding.FragmentRegisterBinding
import com.will.kgu_math.fragments.subjects.SubjectsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private val vm: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        saveVariantsFile(uri ?: return@registerForActivityResult)
    }

    private var activeFileBinding: TextView? = null

    private val fileCoroutineScope = CoroutineScope(Dispatchers.IO)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etFirstName.setText(vm.firstName)
            etSecondName.setText(vm.secondName)
//            if (vm.agVariantsSaved) binding.btnAddAG.text = "Сохранено"
//            if (vm.maVariantsSaved) binding.btnAddMA.text = "Сохранено"

            etFirstName.addTextChangedListener { vm.firstName = it.toString() }
            etSecondName.addTextChangedListener { vm.secondName = it.toString() }
            btnAddAG.setOnClickListener { launchPdfChooseForBtn(btnAddAG) }
            btnAddMA.setOnClickListener { launchPdfChooseForBtn(btnAddMA) }

            btnConfirm.setOnClickListener {
                saveRegisterData()
                completeRegister()
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.btnEnabledStateFlow.collectLatest { state ->
                    binding.btnConfirm.isEnabled = state
                }
            }
        }
    }

    private fun launchPdfChooseForBtn(btnView: TextView) {
        activeFileBinding = btnView
        launcher.launch(arrayOf("application/pdf"))
    }

    private fun saveVariantsFile(uri: Uri) = fileCoroutineScope.launch {
        val activity = requireActivity()

        val basePath = activity.filesDir.path

        val stream = activity.contentResolver.openInputStream(uri)
        stream ?: return@launch

        if (vm.saveVariantsFile(basePath, "variants.pdf", stream)) {
            withContext(Dispatchers.Main) {
                activeFileBinding?.text = "Сохранено"
            }
        }
    }

    private fun saveRegisterData() {
        requireActivity().getSharedPreferences("LOGIN", AppCompatActivity.MODE_PRIVATE).edit {
            putString("firstName", vm.firstName)
            putString("secondName", vm.secondName)
            putString("variantsPath", vm.filePath)
            putBoolean("registered", true)
        }
    }

    private fun completeRegister() {
        parentFragmentManager.commit {
            replace(R.id.nav_host_fragment, SubjectsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activeFileBinding = null
        _binding = null
    }
}