package com.belicoffee.Dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.belicoffee.R;
import com.bumptech.glide.Glide;

@SuppressLint("ValidFragment")
public class ShippingDialog extends DialogFragment implements View.OnClickListener, TextWatcher {
    ImageView ivImage;
    TextView tvName;
    TextView tvPrice;
    EditText etQuantity;
    TextView tvTotal;
    ImageButton ibAdd;
    ImageButton ibSub;
    Button btnOrder;
    LinearLayout llOption;
    ImageButton ibMore;
    String name;
    double price;
    String image;

    @SuppressLint("ValidFragment")
    public ShippingDialog(String name, double price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_shipping, container, false);
        ivImage = view.findViewById(R.id.iv_image_dialog_shipping);
        tvName = view.findViewById(R.id.tv_name_dialog_shipping);
        tvPrice = view.findViewById(R.id.tv_price_dialog_shipping);
        etQuantity = view.findViewById(R.id.et_quantity_dialog_shipping);
        tvTotal = view.findViewById(R.id.tv_total_dialog_shipping);
        ibAdd = view.findViewById(R.id.ib_add_dialog_shipping);
        ibSub = view.findViewById(R.id.ib_sub_dialog_shipping);
        btnOrder = view.findViewById(R.id.btn_order_dialog_shipping);
        llOption = view.findViewById(R.id.ll_option_dialog_shipping);
        ibMore = view.findViewById(R.id.ib_more_dialog_shipping);

        tvName.setText(name);
        tvPrice.setText(formatMoney(price));
        Glide.with(getContext()).load(image).override(200, 200).centerCrop().into(ivImage);
        llOption.setVisibility(View.GONE);

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ibAdd.setOnClickListener(this);
        ibSub.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        etQuantity.addTextChangedListener(this);

        return view;
    }

    private double caculateMoney(double price, double quantity) {
        double total = price * quantity;
        return total;
    }

    public static String formatMoney(double money) {
        String formatedMoney = null;
        if (money < 1000)
            formatedMoney = String.format("%.0f", money) + " VNĐ";
        else if (money > 1000 && money < 1000000)
            formatedMoney = String.valueOf(money / 1000) + "00 VNĐ";
        else if (money > 1000000)
            formatedMoney = String.format("%.3f",money / 1000000) + ".000 VNĐ";
        return formatedMoney;
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add_dialog_shipping:
                if (etQuantity.getText().toString().equals(""))
                    etQuantity.setText("0");
                etQuantity.setText(String.valueOf(Integer.parseInt(etQuantity.getText().toString()) + 1));
                break;
            case R.id.ib_sub_dialog_shipping:
                if (!etQuantity.getText().toString().equals("")) {
                    if (Integer.parseInt(etQuantity.getText().toString()) > 0) {
                        if (Integer.parseInt(etQuantity.getText().toString()) == 1) {
                            etQuantity.setText("");
                        } else {
                            etQuantity.setText(String.valueOf(Integer.parseInt(etQuantity.getText().toString()) - 1));
                        }
                    }
                }
                break;
            case R.id.ib_more_dialog_shipping:
                if (llOption.getVisibility() == View.GONE) {
                    llOption.setVisibility(View.VISIBLE);
                    ibMore.setImageResource(R.drawable.ic_expand_less_dark_cyan_48dp);
                } else {
                    llOption.setVisibility(View.GONE);
                    ibMore.setImageResource(R.drawable.ic_expand_more_dark_cyan_48dp);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etQuantity.getText().toString().equals("")) {
            tvTotal.setText(formatMoney(caculateMoney(price, 0)));
        } else {
            tvTotal.setText(formatMoney(caculateMoney(price, Integer.parseInt(etQuantity.getText().toString()))));
        }
    }
}
