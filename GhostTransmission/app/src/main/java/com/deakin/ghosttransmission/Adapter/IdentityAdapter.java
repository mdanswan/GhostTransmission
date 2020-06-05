package com.deakin.ghosttransmission.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deakin.ghosttransmission.Listener.ViewListener;
import com.deakin.ghosttransmission.R;

import java.util.ArrayList;

class IdentityViewHolder extends RecyclerView.ViewHolder {

    /**
     * Instance Variables
     */
    private Button identityButton = null;

    /**
     * Constructor
     *
     * @param view identity view layout
     */
    public IdentityViewHolder(@NonNull View view) {
        super(view);
        setIdentityButton((Button) view.findViewById(R.id.identity_button));
    }

    /**
     * Getters and Setters
     */
    public Button getIdentityButton() {
        return identityButton;
    }

    public void setIdentityButton(Button identityButton) {
        this.identityButton = identityButton;
    }
};

public class IdentityAdapter extends RecyclerView.Adapter<IdentityViewHolder> {

    /**
     * Instance Variables
     */
    // IDENTITY LIST
    private ArrayList<String> identityList = null;

    // LISTENERS
    private ViewListener viewListener = null;

    /**
     * Constructor
     *
     * @param identityList List of identities
     * @param viewListener view listener for requesting changes in the view
     */
    public IdentityAdapter(@NonNull ArrayList<String> identityList, ViewListener viewListener) {
        setIdentityList(identityList);
        setViewListener(viewListener);
    }

    /**
     * Creates an Identity View Holder instance which represents an instance of a view with the template 'identity layout'
     *
     * @param parent   parent view group
     * @param viewType type of view
     * @return new identity view holder instance
     */
    @NonNull
    @Override
    public IdentityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View identityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.identity_layout, parent, false);
        final IdentityViewHolder identityViewHolder = new IdentityViewHolder(identityView);
        identityViewHolder.getIdentityButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onRequestOpenConversationView(identityViewHolder.getIdentityButton().getText().toString());
            }
        });
        return identityViewHolder;
    }

    /**
     * Binds the data at the given position, to one of the previously created Identity View Holders
     *
     * @param holder   identity view holder to map
     * @param position position in dataset
     */
    @Override
    public void onBindViewHolder(@NonNull IdentityViewHolder holder, int position) {

        // get the current identity from the dataset
        String identity = getIdentityList().get(position);

        // set the content of the identity button
        Button identityButton = holder.getIdentityButton();
        identityButton.setText(identity);
    }

    /**
     * Gets the item count in the Identity List
     *
     * @return item count
     */
    @Override
    public int getItemCount() {
        return getIdentityList().size();
    }

    /**
     * Getters and Setters
     */
    public ArrayList<String> getIdentityList() {
        return identityList;
    }

    public void setIdentityList(ArrayList<String> identityList) {
        this.identityList = identityList;
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }
}
