package com.example.testbottomnavigationbar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbottomnavigationbar.R;
import com.example.testbottomnavigationbar.fragments.AccountFragment;
import com.example.testbottomnavigationbar.remote_db.Account;

import java.util.List;

public class FriendOverviewAdapter extends RecyclerView.Adapter<FriendOverviewAdapter.FriendOverviewViewHolder> {
    private final int type;
    private final int accountId;
    private final List<Account> friendsList;

    public FriendOverviewAdapter(int type, int accountId, List<Account> friendsList) {
        this.type = type;
        this.accountId = accountId;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public FriendOverviewAdapter.FriendOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_overview, viewGroup, false);

        return new FriendOverviewAdapter.FriendOverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendOverviewAdapter.FriendOverviewViewHolder viewHolder, int i) {
        viewHolder.bind(friendsList.get(i));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    static final class FriendOverviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView friendName;
        private final View parentView;

        public FriendOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_overview_name);
            parentView = itemView;
        }

        private void bind(@NonNull Account friendAccount) {
            friendName.setText(friendAccount.getFirstName() + " " + friendAccount.getSecondName());

            parentView.setOnClickListener(v -> {
                Fragment fragment = new AccountFragment(1, friendAccount.getAccountId());
                FragmentTransaction fTrans = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.fragment_cv, fragment, null).commit();
                fTrans.addToBackStack(null);
            });
        }
    }
}
