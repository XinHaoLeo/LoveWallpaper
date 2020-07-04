/*
 * Copyright 2020 Leo
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xin.lovewallpaper.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.xin.lovewallpaper.R;
import com.xin.lovewallpaper.ui.activity.DisclaimerActivity;
import com.xin.lovewallpaper.ui.activity.PrivacyActivity;

import org.jetbrains.annotations.NotNull;

/**
 * █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 *
 * @author : Leo
 * @date : 2020/7/3 16:08
 * @desc : 协议对话框
 * @since : lightingxin@qq.com
 */
public class AgreementDialog extends AlertDialog implements View.OnClickListener {

    private static final String TAG = AgreementDialog.class.getSimpleName();
    private Context mContext;
    private String title;
    private String message;
    private OnDialogClickListener dialogListener;

    private AgreementDialog(Builder builder) {
        super(builder.mContext, R.style.AgreementDialog);
        this.mContext = builder.mContext;
        this.title = builder.title;
        this.message = builder.message;
        this.dialogListener = builder.listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_agreement);
        initView();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private void initView() {
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        if (dialogTitle != null) {
            dialogTitle.setText(title);
        }

        TextView dialogMessage = findViewById(R.id.dialogMessage);
        if (dialogMessage != null) {
            dialogMessage.setText(message);
        }

        TextView subMessage = findViewById(R.id.subMessage);
        if (subMessage != null) {
            String content = mContext.getString(R.string.agreement_dialog_message);
            SpannableString spanText = new SpannableString(content);
            //免责声明
            int disclaimerIndex = content.indexOf(mContext.getString(R.string.disclaimer));
            String disclaimer = mContext.getString(R.string.disclaimer);
            Log.d(TAG, "initView: disclaimerIndex=" + disclaimerIndex);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    mContext.startActivity(new Intent(mContext, DisclaimerActivity.class));
                }

                @Override
                public void updateDrawState(@NotNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.RED);
                    ds.setUnderlineText(true);
                }
            }, disclaimerIndex, disclaimerIndex + disclaimer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            subMessage.setMovementMethod(LinkMovementMethod.getInstance());
            subMessage.setText(spanText);

            //隐私政策
            int privacyIndex = content.indexOf(mContext.getString(R.string.privacy_policy));
            String privacy = mContext.getString(R.string.privacy_policy);
            Log.d(TAG, "initView: privacyIndex=" + privacyIndex);
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    mContext.startActivity(new Intent(mContext, PrivacyActivity.class));
                }

                @Override
                public void updateDrawState(@NotNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.RED);
                    ds.setUnderlineText(true);
                }
            }, privacyIndex, privacyIndex + privacy.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            subMessage.setMovementMethod(LinkMovementMethod.getInstance());
            subMessage.setText(spanText);
        }

        Button confirmButton = findViewById(R.id.confirmButton);
        if (confirmButton != null) {
            confirmButton.setOnClickListener(this);
        }
        TextView cancelView = findViewById(R.id.cancelView);
        if (cancelView != null) {
            cancelView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.confirmButton) {
            if (dialogListener != null) {
                dialogListener.onConfirmClick();
            }
        } else if (i == R.id.cancelView) {
            if (dialogListener != null) {
                dialogListener.onCancelClick();
            }
        }
        dismiss();
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private OnDialogClickListener listener;

        public Builder setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder setDialogTitle(String s) {
            this.title = s;
            return this;
        }

        public Builder setDialogMessage(String s) {
            this.message = s;
            return this;
        }

        public Builder setOnDialogClickListener(OnDialogClickListener listener) {
            this.listener = listener;
            return this;
        }

        public AgreementDialog build() {
            return new AgreementDialog(this);
        }
    }

    public interface OnDialogClickListener {
        void onConfirmClick();

        void onCancelClick();
    }
}
