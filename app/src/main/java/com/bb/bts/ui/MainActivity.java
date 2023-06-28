package com.bb.bts.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bb.bts.R;
import com.bb.bts.databinding.ActivityMainBinding;
import com.bb.bts.models.UIBalance;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    private int balance = 0;
    private double btcBalance = 0.00000000;
    private DecimalFormat formatter = new DecimalFormat("0.00000000");

    private Animation scaleAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this,ViewModelProvider.Factory.from(MainViewModel.initializer)
        ).get(MainViewModel.class);
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);

        viewModel.getBalance();

        setUI();
        setObservers();
    }

    private void setObservers() {
        viewModel.getLiveBalanceList().observe(this, new Observer<UIBalance>() {
            @Override
            public void onChanged(UIBalance uiBalance) {
                balance = uiBalance.balance;
                btcBalance = uiBalance.btcBalance;

                binding.tvBalance.setText(getResources().getString(R.string.balance,String.valueOf(balance)));
                binding.tvBtcBalance.setText(getResources().getString(R.string.btc_balance, formatter.format(btcBalance).replace(".",",")));
            }
        });
    }

    private int p = 0;

    private void setUI() {
        binding.tvBalance.setText(getResources().getString(R.string.balance,"0"));
        binding.tvBtcBalance.setText(getResources().getString(R.string.btc_balance,"0,00000000"));

        binding.tvPing.setText("15");
        binding.tvPing2.setText("25");
        binding.tvPing3.setText("55");
        binding.tvPing4.setText("61");

        binding.btnStart.setOnClickListener(view -> {
            view.startAnimation(scaleAnim);
            increaseBalance();
            p += 5;
            binding.pbProgress.setProgress(p,true);
        });

        binding.llBoost.setOnClickListener(view -> {
            view.startAnimation(scaleAnim);
            view.setEnabled(false);
            resetServers();
            binding.llServer1.setSelected(true);
            binding.apProgress1.setOnFinish(() -> {

                binding.llServer2.setSelected(true);
                binding.apProgress2.setOnFinish(() -> {

                    binding.llServer3.setSelected(true);
                    binding.apProgress3.setOnFinish(() -> {

                        binding.llServer4.setSelected(true);
                        binding.apProgress4.setOnFinish(() -> {
                            view.setEnabled(true);
                        });
                        binding.apProgress4.startIncreaseAnimation();
                    });
                    binding.apProgress3.startIncreaseAnimation();
                });
                binding.apProgress2.startIncreaseAnimation();
            });
            binding.apProgress1.startIncreaseAnimation();
        });

        binding.btnTake.setOnClickListener(view -> {
            view.startAnimation(scaleAnim);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.example.android"));
            startActivity(intent);
        });
    }

    private void resetServers(){
        binding.llServer1.setSelected(false);
        binding.apProgress1.setProgress(0);

        binding.llServer2.setSelected(false);
        binding.apProgress2.setProgress(0);

        binding.llServer3.setSelected(false);
        binding.apProgress3.setProgress(0);

        binding.llServer4.setSelected(false);
        binding.apProgress4.setProgress(0);
    }

    private void increaseBalance(){
        balance += 15;
        btcBalance += 0.00000015;

        binding.tvBalance.setText(getResources().getString(R.string.balance,String.valueOf(balance)));
        binding.tvBtcBalance.setText(getResources().getString(R.string.btc_balance, formatter.format(btcBalance).replace(".",",")));

        viewModel.updateBalance(new UIBalance(balance, (float) btcBalance));
    }


}