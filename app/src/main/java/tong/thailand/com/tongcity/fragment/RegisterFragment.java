package tong.thailand.com.tongcity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tong.thailand.com.tongcity.MainActivity;
import tong.thailand.com.tongcity.R;

public class RegisterFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create Toolbar
        createToolbar();

    } // Method

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toobarRegister);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please Fill in Blank");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
