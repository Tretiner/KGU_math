package com.will.kgu_math.fragments.settings;


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.will.kgu_math.databinding.FragmentSettingsBinding
import com.will.kgu_math.preferences.SettingsPreferences
import com.will.kgu_math.app.LocaleManager

class SettingsFragment : Fragment() {

    private val vm: SettingsViewModel by viewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            themeText.apply {
                text = SettingsPreferences.uiModeName
                setOnClickListener {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            SettingsPreferences.uiMode = MODE_NIGHT_NO
                            SettingsPreferences.uiModeName = LocaleManager.getStringByName("mode_night_no")
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            SettingsPreferences.uiMode = MODE_NIGHT_YES
                            SettingsPreferences.uiModeName = LocaleManager.getStringByName("mode_night_yes")
                        }
                        else -> {
                        }
                    }

                    setDefaultNightMode(SettingsPreferences.uiMode)
                }
            }

            reset.setOnClickListener {
                SettingsPreferences.uiMode = MODE_NIGHT_FOLLOW_SYSTEM
                SettingsPreferences.uiModeName = LocaleManager.getStringByName("mode_night_system")
                setDefaultNightMode(SettingsPreferences.uiMode)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}