package tattler.pro.tattler.main.invitations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.OnItemViewClickListener;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Invitation;

public class InvitationsAdapter extends RecyclerView.Adapter<InvitationsAdapter.ViewHolder> {
    private Context context;
    private List<Invitation> invitations;
    private OnItemViewClickListener clickListener;

    private int lastPosition;

    InvitationsAdapter(Context context, List<Invitation> invitations, OnItemViewClickListener clickListener) {
        this.context = context;
        this.invitations = invitations;
        this.clickListener = clickListener;
        this.lastPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_invitation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invitation invitation = getInvitation(position);
        Chat chat = invitation.chat;
        ColorDrawable chatNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(context, chat.chatName)));

        holder.chatAvatar.setImageDrawable(chatNameColorDrawable);
        holder.chatInitials.setText(Util.extractUserInitials(chat.chatName));
        holder.chatName.setText(chat.chatName);

        if (Util.isMessageSentByMe(context, invitation.senderId)) {
            holder.contactNumber.setText(String.valueOf(invitation.receiverId));
            holder.invitationDirectionIcon.setBackgroundResource(R.drawable.ic_call_made);
        } else {
            holder.contactNumber.setText(String.valueOf(invitation.senderId));
            holder.invitationDirectionIcon.setImageResource(R.drawable.ic_call_received);
            holder.acceptChat.setVisibility(View.VISIBLE);
            holder.rejectChat.setVisibility(View.VISIBLE);
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void addInvitation(Invitation invitation) {
        invitations.add(invitation);
        notifyDataSetChanged();
    }

    public void addInvitations(List<Invitation> invitations) {
        this.invitations.addAll(invitations);
        notifyDataSetChanged();
    }

    public void clearInvitations() {
        invitations.clear();
        notifyDataSetChanged();
    }

    public void removeInvitation(int position) {
        invitations.remove(position);
        notifyItemRemoved(position);
    }

    public Invitation getInvitation(int position) {
        return invitations.get(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatAvatar)
        CircleImageView chatAvatar;

        @BindView(R.id.chatInitials)
        TextView chatInitials;

        @BindView(R.id.chatName)
        TextView chatName;

        @BindView(R.id.userNumber)
        TextView contactNumber;

        @BindView(R.id.acceptChat)
        ImageView acceptChat;

        @BindView(R.id.rejectChat)
        ImageView rejectChat;

        @BindView(R.id.invitationDirectionIcon)
        ImageView invitationDirectionIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            acceptChat.setOnClickListener(v -> clickListener.onItemViewClick(getAdapterPosition(), acceptChat));
            rejectChat.setOnClickListener(v -> clickListener.onItemViewClick(getAdapterPosition(), rejectChat));
        }
    }
}