package com.example.testetcc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testetcc.databinding.FragmentSecondBinding;

import java.sql.SQLException;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSecondnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_thirdFragment);
            }
        });
        binding.buttonSecondnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
                //valores dos campos, em ordem, só montar o query e mandar pro banco chamando a classe DAO.
                //binding.spinner2.getSelectedItem().toString();
//                binding.checkBoxComercio.isChecked();
//                binding.checkBoxIluminacao.isChecked();
//                binding.checkBoxPoliciamento.isChecked();
//                binding.checkBoxMovimentacao.isChecked();
                try {
                    DAO dao = new DAO();
                    dao.inserirOcorrencia(binding.editTextText.getText().toString(), binding.editTextText3.getText().toString(), binding.checkBoxPoliciamento.isChecked(), binding.checkBoxIluminacao.isChecked(), binding.checkBoxComercio.isChecked(), binding.checkBoxMovimentacao.isChecked());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
//        Spinner spinner = (Spinner) getView().findViewById(R.id.spinner2);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this.getContext(),
//                R.array.planets_array,
//                android.R.layout.simple_spinner_item
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}