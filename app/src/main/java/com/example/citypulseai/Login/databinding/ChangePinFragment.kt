package com.example.citypulseai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.citypulseai.databinding.FragmentChangePinBinding

class ChangePinFragment : Fragment() {
    private var _binding: FragmentChangePinBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ChangePinFragment = ChangePinFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitPin.setOnClickListener {
            val newPin = binding.etNewPin.text.toString()
            val confirmPin = binding.etConfirmPin.text.toString()

            if (newPin.isEmpty() || confirmPin.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (newPin != confirmPin) {
                Toast.makeText(context, "PINs do not match", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "PIN changed successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}