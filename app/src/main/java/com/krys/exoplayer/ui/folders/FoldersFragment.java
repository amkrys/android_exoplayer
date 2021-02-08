package com.krys.exoplayer.ui.folders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.krys.exoplayer.R;


public class FoldersFragment extends Fragment {

    private FoldersViewModel foldersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        foldersViewModel = new ViewModelProvider(this).get(FoldersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_folders, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        foldersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}