package com.will.kgu_math.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.will.kgu_math.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val vm: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val launcher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        vm.loadAndParseFile(uri ?: return@registerForActivityResult)
        checkButtonIsReady()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etFirstName.setText(vm.firstName)
            etSecondName.setText(vm.secondName)
            if (vm.fileLoaded)
                binding.btnAddMarksFile.text = vm.fileName

            etFirstName.addTextChangedListener { text -> vm.firstName = text.toString(); checkButtonIsReady() }
            etSecondName.addTextChangedListener { text -> vm.secondName = text.toString(); checkButtonIsReady() }

            btnAddMarksFile.setOnClickListener {
                launcher.launch(arrayOf("application/pdf"))
            }

            btnConfirm.setOnClickListener {
                requireActivity().getSharedPreferences("LOGIN", 0).edit {
                    putString("fname", vm.firstName)
                    putString("sname", vm.secondName)
                }
//                requireActivity().supportFragmentManager.beginTransaction().add(ChooseFragment)
            }
        }
    }

    private fun checkButtonIsReady() {
        if (vm.firstName.isNotEmpty() && vm.secondName.isNotEmpty() && vm.fileLoaded)
            binding.btnConfirm.isEnabled = true
    }
}