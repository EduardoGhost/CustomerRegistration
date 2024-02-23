package com.eduardo.customerregistration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.eduardo.customerregistration.databinding.FragmentFirstBinding
import com.eduardo.customerregistration.view.CadastroActivity
import com.eduardo.customerregistration.view.listClientActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var binding: FragmentFirstBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.buttonFirst.setOnClickListener {
            val intent = Intent(activity, CadastroActivity::class.java)
            startActivity(intent)
        }
        binding!!.buttonSecond.setOnClickListener {
            val intent = Intent(activity, listClientActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}