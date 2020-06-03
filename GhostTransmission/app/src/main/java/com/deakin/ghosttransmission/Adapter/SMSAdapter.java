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
    private TextView phonenoTV;
    private TextView timestampTV;
    private TextView bodyTV;
    private TextView readTV;

    /**
     * Constructor
     *
     * @param view sms view layout
     */
    public SMSViewHolder(@NonNull View view) {
        super(view);

        setPhonenoTV((TextView) view.findViewById(R.id.phoneno_textview));
        setTimestampTV((TextView) view.findViewById(R.id.timestamp_textview));
        setBodyTV((TextView) view.findViewById(R.id.body_textview));
        setReadTV((TextView) view.findViewById(R.id.read_textview));
    }

    /**
     * Getters and Setters
     */
    public TextView getPhonenoTV() {
        return phonenoTV;
    }

    public void setPhonenoTV(TextView phonenoTV) {
        this.phonenoTV = phonenoTV;
    }

    public TextView getTimestampTV() {
        return timestampTV;
    }

    public void setTimestampTV(TextView timestampTV) {
        this.timestampTV = timestampTV;
    }

    public TextView getBodyTV() {
        return bodyTV;
    }

    public void setBodyTV(TextView bodyTV) {
        this.bodyTV = bodyTV;
    }

    public TextView getReadTV() {
        return readTV;
    }

    public void setReadTV(TextView readTV) {
        this.readTV = readTV;
    }
};

public class SMSAdapter extends RecyclerView.Adapter<SMSViewHolder> {

    /**
     * Instance Variables
     */
    private ArrayList<SMS> smsList = null;

    public SMSAdapter(@NonNull ArrayList<SMS> smsList) {
        setSmsList(smsList);
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
        SMS sms = getSmsList().get(position);

        // set the content of each view in the SMS View Layout
        TextView phonenoTV = holder.getPhonenoTV();
        TextView timestampTV = holder.getTimestampTV();
        TextView bodyTV = holder.getBodyTV();
        TextView readTV = holder.getReadTV();

        phonenoTV.setText(sms.getPhoneno());
        timestampTV.setText(sms.getTimestamp().toString());
        bodyTV.setText(sms.getBody());
        readTV.setText(sms.getRead());
    }

    @Override
    public int getItemCount() {
        return getSmsList().size();
    }

    /**
     * Getters and Setters
     */
    public ArrayList<SMS> getSmsList() {
        return smsList;
    }

    public void setSmsList(ArrayList<SMS> smsList) {
        this.smsList = smsList;
    }
}
