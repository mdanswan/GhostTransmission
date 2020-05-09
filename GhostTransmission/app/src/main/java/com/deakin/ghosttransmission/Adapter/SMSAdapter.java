package com.deakin.ghosttransmission.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deakin.ghosttransmission.Model.SMS;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;

class SMSViewHolder extends RecyclerView.ViewHolder {

    /**
     * Instance Variables
     */
    private TextView contentTV;

    /**
     * Constructor
     *
     * @param view sms view layout
     */
    public SMSViewHolder(@NonNull View view) {
        super(view);

        setContentTV((TextView) view.findViewById(R.id.sms_content));
    }

    /**
     * Getters and Setters
     */
    public TextView getContentTV() {
        return contentTV;
    }

    public void setContentTV(TextView contentTV) {
        this.contentTV = contentTV;
    }
};

public class SMSAdapter extends RecyclerView.Adapter<SMSViewHolder> {

    /**
     * Instance Variables
     */
    private ArrayList<SMS> sms = null;

    public SMSAdapter(@NonNull ArrayList<SMS> sms) {
        setSms(sms);
    }

    @NonNull
    @Override
    public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View smsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_layout, parent, false);
        SMSViewHolder smsViewHolder = new SMSViewHolder(smsView);
        return smsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SMSViewHolder holder, int position) {

        // get the current SMS model from the sms dataset
        SMS sms = getSms().get(position);

        // set the content of each view in the SMS View Layout
        TextView contentTV = holder.getContentTV();
        contentTV.setText(sms.getContent());
    }

    @Override
    public int getItemCount() {
        return getSms().size();
    }

    /**
     * Getters and Setters
     */
    public ArrayList<SMS> getSms() {
        return sms;
    }

    public void setSms(ArrayList<SMS> sms) {
        this.sms = sms;
    }
}
