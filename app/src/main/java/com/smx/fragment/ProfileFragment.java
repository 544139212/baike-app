package com.smx.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smx.BillActivity;
import com.smx.R;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    TextView tvPhone;
    TextView tvBill;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        SharedPreferences preferences =  this.getActivity().getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
        String oPhone = preferences.getString("O_PHONE", "");
        tvPhone.setText(oPhone);

        tvBill = (TextView) view.findViewById(R.id.tv_bill);
        tvBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BillActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
